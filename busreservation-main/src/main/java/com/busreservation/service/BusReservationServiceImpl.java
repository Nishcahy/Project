package com.busreservation.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.busreservation.dto.Bus;
import com.busreservation.dto.ReservationDTO;
import com.busreservation.entity.Passenger;
import com.busreservation.entity.Reservation;
import com.busreservation.exception.DatabaseException;
import com.busreservation.exception.ResourceNotFoundException;
import com.busreservation.repo.ReservationRepo;
import com.busreservation.service.client.BusClient;

import feign.FeignException;

/**
 * Service implementation for managing Bus Reservations.
 * This class provides methods for creating, retrieving, updating and deleting Bus Reservations.
 */
@Service
public class BusReservationServiceImpl implements BusReservation {

	Logger logger = LoggerFactory.getLogger(BusReservationServiceImpl.class);

	private final ReservationRepo reservationRepository;
	private final BusClient busClient;

	public BusReservationServiceImpl(ReservationRepo reservationRepository, BusClient busClient) {
		this.reservationRepository = reservationRepository;
		this.busClient = busClient;
	}

	/**
	 * Creates a new reservation.
	 *
	 * @param reservation The Reservation object to be created.
	 * @return ReservationDTO containing the created reservation details.
	 * @throws ResourceNotFoundException if bus is not found or seats are not available.
	 * @throws IllegalArgumentException if passenger count or seat numbers are invalid.
	 * @throws DatabaseException if reservation creation fails.
	 */
	@Override
	public ReservationDTO createReservation(Reservation reservation) {
		try {
			// Get the bus details
			Bus busDetails = busClient.fetchBus(reservation.getBusId());
			LocalDate currentDate = LocalDate.now();
			if (reservation.getDate().isBefore(currentDate)) {
				throw new ResourceNotFoundException("Bus Reservation cannot be done in past days");
			}
			if (busDetails.getBookedSeatNumbers() == null) {
				busDetails.setBookedSeatNumbers(new ArrayList<>());
			}
			// Validate the number of passengers matches the number of seats
			if (reservation.getPassengers().size() != reservation.getNumberOfSeats()) {
				throw new IllegalArgumentException("Number of passengers must match the number of seats reserved");
			}

			// Validate seat numbers are within the valid range
			int totalSeats = busDetails.getSeats();
			for (Passenger passenger : reservation.getPassengers()) {
				int seatNumber = passenger.getSeatNumber();
				if (seatNumber < 1 || seatNumber > totalSeats) {
					throw new IllegalArgumentException("Seat number " + seatNumber + " is out of range. Valid seat numbers are from 1 to " + totalSeats);
				}
				if (busDetails.getBookedSeatNumbers().contains(seatNumber)) {
					throw new ResourceNotFoundException("Seat number " + seatNumber + " is already booked");
				}
			}
			Set<Integer> seatNumbers = new HashSet<>();
			for (Passenger passenger : reservation.getPassengers()) {
				int seatNumber = passenger.getSeatNumber();
				if (!seatNumbers.add(seatNumber)) {
					throw new IllegalArgumentException("Duplicate seat number " + seatNumber + " found in the reservation");
				}
			}

			// Calculate the total price dynamically
			double totalAmount = (double) reservation.getNumberOfSeats() * busDetails.getPrice();
			reservation.setTotalAmount(totalAmount);

			// Append new booked seat numbers to the existing list
			List<Integer> bookedSeatNumbers = reservation.getPassengers().stream()
					.map(Passenger::getSeatNumber)
					.collect(Collectors.toList());
			busDetails.getBookedSeatNumbers().addAll(bookedSeatNumbers);

			// Update available seats
			if (busDetails.getAvailableSeats() <= 0) {
				throw new ResourceNotFoundException("Not enough seats available");
			}
			busDetails.setAvailableSeats(busDetails.getAvailableSeats() - reservation.getNumberOfSeats());


			// Save the reservation
			Reservation res = reservationRepository.save(reservation);
			busClient.updateBus(busDetails.getBusId(), busDetails);

			return new ReservationDTO(busDetails, res);
		} catch (FeignException e) {
			if (e.status() == 404) {
				throw new ResourceNotFoundException("Bus not found with ID: " + reservation.getBusId());
			}
			// Handle other exceptions
			throw new ResourceNotFoundException("An error occurred while fetching bus details" + e.getMessage());
		} catch (Exception e) {
			logger.error("Failed to create reservation{}", e);
			throw new DatabaseException("Failed to create reservation " + e.getMessage());
		}
	}

	/**
	 * Gets reservations by user ID.
	 *
	 * @param userId The ID of the user.
	 * @return List of ReservationDTOs for the given user ID.
	 * @throws ResourceNotFoundException if no reservations found for the user.
	 * @throws DatabaseException if fetching reservations fails.
	 */
	@Override
	public List<ReservationDTO> getReservationsByUser(Long userId) throws ResourceNotFoundException, DatabaseException {

		List<Reservation> reservations = reservationRepository.findByUserId(userId);
		List<ReservationDTO> reservationDtos = new ArrayList<>();

		if (reservations.isEmpty()) {
			throw new ResourceNotFoundException("No reservations found for user ID: " + userId);
		}

		for (Reservation reservation : reservations) {
			Bus bus = busClient.fetchBus(reservation.getBusId());
			ReservationDTO reservationDto = new ReservationDTO(bus, reservation);
			reservationDtos.add(reservationDto);
		}

		return reservationDtos;
	}

	/**
	 * Finds buses by route from and to destinations.
	 *
	 * @param routeFrom The starting destination.
	 * @param routeTo   The ending destination.
	 * @return List of Buses matching the route.
	 * @throws ResourceNotFoundException if no bus in specified location.
	 * @throws DatabaseException if finding buses fails.
	 */
	public List<Bus> findBusByFromAndToDestination(String routeFrom, String routeTo) {
		try {
			List<Bus> buses = busClient.fetchAllBus();
			List<Bus> requiredBuses = new ArrayList<>();
			for (Bus bus : buses) {
				if (routeFrom.equalsIgnoreCase(bus.getRouteFrom()) && routeTo.equalsIgnoreCase(bus.getRouteTo())) {
					requiredBuses.add(bus);
				}
			}
			if (requiredBuses.isEmpty()) {
				throw new ResourceNotFoundException("No bus in specified location");
			}
			return requiredBuses;
		} catch (Exception e) {
			logger.error("Failed to find buses from {} to {}", routeFrom, routeTo, e);
			throw new DatabaseException("Failed to find buses from " + routeFrom + " to " + routeTo);
		}
	}

	/**
	 * Deletes a reservation by ID.
	 *
	 * @param id The ID of the reservation to be deleted.
	 * @return A message indicating the reservation was deleted.
	 * @throws ResourceNotFoundException if reservation is not found or cancellation is not allowed.
	 */
	@Override
	public String deleteReservation(Long id) {

		Reservation reservation = reservationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + id));

		Bus bus = busClient.fetchBus(reservation.getBusId());
		LocalDate reservationDate = reservation.getDate();
		LocalDate currentDate = LocalDate.now();

		// Debug statements to check the dates
		logger.info("***************************************Reservation Date:{} ", reservationDate);
		logger.info("Current Date: {}", currentDate);
		logger.info("Allowed Cancellation Date:{} ", reservationDate.plusDays(7));
		if (currentDate.isAfter(reservationDate.minusDays(7))) {
			throw new ResourceNotFoundException(
					"Reservation cannot be cancelled within 7 days from the reservation date.");
		} else {
			// Update available seats
			bus.setAvailableSeats(bus.getAvailableSeats() + reservation.getNumberOfSeats());

			// Remove booked seat numbers
			List<Integer> bookedSeatNumbers = reservation.getPassengers().stream()
					.map(Passenger::getSeatNumber)
					.collect(Collectors.toList());
			bus.getBookedSeatNumbers().removeAll(bookedSeatNumbers);

			busClient.updateBus(bus.getBusId(), bus);
			reservationRepository.deleteById(id);
		}

		return "Reservation deleted";

	}

	/**
	 * Gets all reservations.
	 * 
	 * @return all reservationDto
	 */
	@Override
	public List<ReservationDTO> getAllReservation() {
		
			List<Reservation> allreservations= reservationRepository.findAll();
			List<ReservationDTO> resDTO=new ArrayList<>();
			for(Reservation reservation:allreservations) {
				Bus bus=busClient.fetchBus(reservation.getBusId());
				ReservationDTO res=new ReservationDTO(bus, reservation);
				resDTO.add(res);
				
			}
			return resDTO;
	}

	@Override
	public Reservation findById(Long reservationId) {
		try {
			Optional<Reservation> reservation = reservationRepository.findById(reservationId);
			if (!reservation.isPresent()) {
				throw new ResourceNotFoundException("No reservation with id");
			}
			return reservation.get();
		} catch (Exception e) {
			logger.error("Failed to find reservation with ID: {}", reservationId, e);
			throw new DatabaseException("Failed to find reservation with ID: " + reservationId);
		}
	}

	@Override
	public List<ReservationDTO> getByBusId(Long busId) {
		List<Reservation> reservations=reservationRepository.findByBusId(busId);
		List<ReservationDTO> reservationDtos = new ArrayList<>();

        for (Reservation reservation : reservations) {
            Bus bus = busClient.fetchBus(reservation.getBusId());
            if(bus==null) {
            	throw new ResourceNotFoundException("Cannot get bus");
            }
            ReservationDTO reservationDto = new ReservationDTO(bus, reservation);
            reservationDtos.add(reservationDto);
        }

        return reservationDtos;
		
	}

	@Override
	public List<ReservationDTO> getByBusNo(String busNo) {
		List<Reservation> allReservations = reservationRepository.findAll();
        List<Bus> buses = busClient.fetchAllBus().stream()
                .filter(bus -> bus.getBusNo().equals(busNo))
                .collect(Collectors.toList());

        if (buses.isEmpty()) {
            return new ArrayList<>(); // Return empty list if no buses found
        }

        List<ReservationDTO> reservationDtos = new ArrayList<>();

        for (Reservation reservation : allReservations) {
            for (Bus bus : buses) {
                if (reservation.getBusId() == bus.getBusId()) {
                    ReservationDTO reservationDto = new ReservationDTO(bus, reservation);
                    reservationDtos.add(reservationDto);
                    break; // Move to the next reservation after a match
                }
            }
        }
        return reservationDtos;
    }
	
}