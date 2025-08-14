package com.busreservation.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.busreservation.dto.Reservation;
import com.busreservation.dto.ReservationDto;
import com.busreservation.entity.Bus;
import com.busreservation.exception.DatabaseException;
import com.busreservation.exception.ResourceNotFoundException;
import com.busreservation.repository.BusRepo;
import com.busreservation.service.client.ReservationClient;

import lombok.AllArgsConstructor;

/**
 * Service implementation for managing Bus entities. This class provides methods
 * for adding, fetching, updating, and deleting Bus objects, as well as
 * retrieving reservations.
 */
@Service
@AllArgsConstructor
public class BusServiceImpl implements BusService {

	private BusRepo busRepo;
	private ReservationClient reservationClient;

	private static final String BUS_NOT_FOUND_MESSAGE = "Bus not found with ID: ";
	private final Logger logger = LoggerFactory.getLogger(BusServiceImpl.class);

	/**
	 * Adds a new Bus.
	 *
	 * @param bus The Bus object to be added.
	 * @return The saved Bus object.
	 * @throws ResourceNotFoundException if available seats are not equal to total
	 *                                   seats.
	 */
	public Bus addBus(Bus bus) throws ResourceNotFoundException {
		if (bus.getAvailableSeats() != bus.getSeats()) {
			throw new ResourceNotFoundException("avialable seats cannot be less or more than total seats ");
		} else {
			logger.info("Adding bus: {}", bus);
			return busRepo.save(bus);

		}
	}

	/**
	 * Fetches all Buses.
	 *
	 * @return List of all Bus objects.
	 * @throws DatabaseException if fetching buses fails.
	 */
	public List<Bus> fetchAllBus() {
		try {
			logger.info("Fetching all buses");
			return busRepo.findAll();
		} catch (Exception e) {
			logger.error("Failed to fetch all buses", e);
			throw new DatabaseException("Failed to fetch all buses");
		}
	}

	/**
	 * Finds a Bus by its ID.
	 *
	 * @param id The ID of the Bus to find.
	 * @return The found Bus object.
	 * @throws ResourceNotFoundException if the Bus is not found.
	 * @throws DatabaseException         if finding the Bus fails.
	 */
	public Bus findBusById(Long id) throws ResourceNotFoundException {
		try {
			Optional<Bus> bus = busRepo.findById(id);
			if (!bus.isPresent()) {
				logger.error(BUS_NOT_FOUND_MESSAGE, id);
				throw new ResourceNotFoundException(BUS_NOT_FOUND_MESSAGE + id);
			}
			logger.info("Finding bus by ID: {}", id);
			return bus.get();
		} catch (Exception e) {
			logger.error("Failed to find bus by ID: {}", id, e);
			throw new DatabaseException("Failed to find bus by ID");
		}
	}

	/**
	 * Updates an existing Bus by its ID.
	 *
	 * @param id  The ID of the Bus to update.
	 * @param bus The updated Bus object.
	 * @return The updated Bus object.
	 * @throws ResourceNotFoundException if the Bus is not found.
	 * @throws DatabaseException         if updating the Bus fails.
	 */
	public Bus updateBus(Long id, Bus bus) throws ResourceNotFoundException {
		try {
			Bus existingBus = busRepo.findById(id).orElse(null);
			if (existingBus == null) {
				logger.error(BUS_NOT_FOUND_MESSAGE, id);
				throw new ResourceNotFoundException(BUS_NOT_FOUND_MESSAGE + id);
			}
			existingBus.setBusNo(bus.getBusNo());
			existingBus.setRouteFrom(bus.getRouteFrom());
			existingBus.setRouteTo(bus.getRouteTo());
			existingBus.setSeats(bus.getSeats());
			existingBus.setDepartureTime(bus.getDepartureTime());
			existingBus.setAvailableSeats(bus.getAvailableSeats());
			existingBus.setBookedSeatNumbers(bus.getBookedSeatNumbers());
			existingBus.setPrice(bus.getPrice());
			logger.info("Updating bus with ID: {}", id);
			return busRepo.save(existingBus);
		} catch (Exception e) {
			logger.error("Failed to update bus with ID: {}", id, e);
			throw new DatabaseException("Failed to update bus with ID");
		}
	}

	/**
	 * Deletes a Bus by its ID.
	 *
	 * @param id The ID of the Bus to delete.
	 * @throws ResourceNotFoundException if the Bus is not found or has existing
	 *                                   reservations.
	 */
	public void deleteBus(Long id) throws ResourceNotFoundException {

		Optional<Bus> bus = busRepo.findById(id);
		List<ReservationDto> res = reservationClient.findReservationByBus(id);
		for (ReservationDto r : res) {
			logger.info("Id############### {}" , r.getBus().getBusId());
			if (r.getBus().getBusId() == id) {
				throw new ResourceNotFoundException("You cannt delete bus,It has been booked by users");
			}
		}
		if (bus.isPresent()) {
			busRepo.deleteById(id);
			logger.info("Bus deleted with ID: {}", id);
		} else {
			logger.error(BUS_NOT_FOUND_MESSAGE, id);
			throw new ResourceNotFoundException(BUS_NOT_FOUND_MESSAGE + id);
		}

	}

	/**
	 * Gets all reservations.
	 *
	 * @return List of all Reservation objects.
	 * @throws DatabaseException if getting reservations fails.
	 */
	public List<Reservation> getReservations() {
		try {
			logger.info("Getting all reservations");
			return reservationClient.getAllReservation();
		} catch (Exception e) {
			logger.error("Failed to get all reservations", e);
			throw new DatabaseException("Failed to get all reservations");
		}
	}

	/**
	 * Finds Buses by Bus Number.
	 *
	 * @param busNo The Bus Number to search for.
	 * @return List of Buses with the given Bus Number.
	 */
	@Override
	public List<Bus> findByBusNo(String busNo) {

		return busRepo.findByBusNo(busNo);
	}
}