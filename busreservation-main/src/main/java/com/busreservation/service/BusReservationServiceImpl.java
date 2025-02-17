package com.busreservation.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

@Service
public class BusReservationServiceImpl implements BusReservation {

	Logger logger = LoggerFactory.getLogger(BusReservationServiceImpl.class);

	private final ReservationRepo reservationRepository;
	private final BusClient busClient;

	public BusReservationServiceImpl(ReservationRepo reservationRepository, BusClient busClient) {
		this.reservationRepository = reservationRepository;
		this.busClient = busClient;
	}

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

	        // Check for already booked seats
	        for (Passenger passenger : reservation.getPassengers()) {
	            if (busDetails.getBookedSeatNumbers().contains(passenger.getSeatNumber())) {
	                throw new ResourceNotFoundException("Seat number " + passenger.getSeatNumber() + " is already booked");
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
	        if(busDetails.getAvailableSeats()<=0) {
	        	throw new ResourceNotFoundException("Not enough seats available");
	        }
	        busDetails.setAvailableSeats(busDetails.getAvailableSeats() - reservation.getNumberOfSeats());
	        
	        busClient.updateBus(busDetails.getBusId(), busDetails);

	        // Save the reservation
	        Reservation res = reservationRepository.save(reservation);

	        return new ReservationDTO(busDetails, res);
	    } catch (FeignException e) {
	        if (e.status() == 404) {
	            throw new ResourceNotFoundException("Bus not found with ID: " + reservation.getBusId());
	        }
	        // Handle other exceptions
	        throw new ResourceNotFoundException("An error occurred while fetching bus details"+e.getMessage());
	    } catch (Exception e) {
	        logger.error("Failed to create reservation", e.getStackTrace());
	        throw new DatabaseException("Failed to create reservation "+e.getMessage());
	    }
	}

	@Override
	public List<Reservation> getReservationsByUser(Long userId) {
		try {
			return reservationRepository.findByUserId(userId);
		} catch (Exception e) {
			logger.error("Failed to fetch reservations by user ID: {}", userId, e);
			throw new DatabaseException("Failed to fetch reservations by user ID");
		}
	}

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

	@Override
	public String deleteReservation(Long id) {
		try {
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

				bus.setAvailableSeats(bus.getAvailableSeats() + reservation.getNumberOfSeats());
				reservationRepository.deleteById(id);

			}

			return "Reservation deleted";
		} catch (Exception e) {
			logger.error("Failed to delete reservation with ID: {}", id, e);
			throw new DatabaseException("Failed to delete reservation with ID: " + id);
		}
	}

	@Override
	public List<Reservation> getAllReservation() {
		try {
			return reservationRepository.findAll();
		} catch (Exception e) {
			logger.error("Failed to fetch all reservations", e);
			throw new DatabaseException("Failed to fetch all reservations");
		}
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
}