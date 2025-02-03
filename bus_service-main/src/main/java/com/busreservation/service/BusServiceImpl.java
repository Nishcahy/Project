package com.busreservation.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.busreservation.dto.Reservation;
import com.busreservation.entity.Bus;
import com.busreservation.exception.ResourceNotFoundException;
import com.busreservation.repository.BusRepo;
import com.busreservation.service.client.ReservationClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BusServiceImpl implements BusService {

	private BusRepo busRepo;
	private ReservationClient reservationClient;
	
	private static final String BUS_NOT_FOUND_MESSAGE = "Bus not found with ID: ";

	private final Logger logger = LoggerFactory.getLogger(BusServiceImpl.class);

	// Adds a new bus
	public Bus addBus(Bus bus) throws ResourceNotFoundException {
		logger.info("Adding bus: {}", bus);
		return busRepo.save(bus);
	}

	// Fetches all buses
	public List<Bus> fetchAllBus() {
		logger.info("Fetching all buses");
		return busRepo.findAll();
	}

	// Finds a bus by its ID
	public Bus findBusById(Long id) throws ResourceNotFoundException {
		Optional<Bus> bus = busRepo.findById(id);
		if (!bus.isPresent()) {
			logger.error(BUS_NOT_FOUND_MESSAGE, id);
			throw new ResourceNotFoundException(BUS_NOT_FOUND_MESSAGE + id);
		}
		logger.info("Finding bus by ID: {}", id);
		return bus.get();
	}

	// Updates an existing bus by its ID
	public Bus updateBus(Long id, Bus bus) throws ResourceNotFoundException {
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
		existingBus.setPrice(bus.getPrice());

		logger.info("Updating bus with ID: {}", id);
		return busRepo.save(existingBus);
	}

	// Deletes a bus by its ID
	public void deleteBus(Long id) throws ResourceNotFoundException {
		Optional<Bus> bus = busRepo.findById(id);
		if (bus.isPresent()) {
			busRepo.deleteById(id);
			logger.info("Bus deleted with ID: {}", id);
		} else {
			logger.error(BUS_NOT_FOUND_MESSAGE, id);
			throw new ResourceNotFoundException(BUS_NOT_FOUND_MESSAGE + id);
		}
	}

	// Gets all reservations
	public List<Reservation> getReservations() {
		logger.info("Getting all reservations");
		return reservationClient.getAllReservation();
	}
}