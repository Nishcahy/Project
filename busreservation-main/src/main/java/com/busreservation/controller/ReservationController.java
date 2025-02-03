package com.busreservation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busreservation.dto.Bus;
import com.busreservation.dto.ReservationDTO;
import com.busreservation.entity.Reservation;
import com.busreservation.service.BusReservationServiceImpl;
import com.busreservation.service.PassengerService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationController {

	private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

	private final BusReservationServiceImpl reservationService;
	private final PassengerService passengerService;

	// Creates a new reservation
	@PostMapping
	public ResponseEntity<ReservationDTO> createReservation(@RequestBody @Valid Reservation reservation) {
		logger.info("Creating reservation: {}", reservation);
		return new ResponseEntity<>(reservationService.createReservation(reservation), HttpStatus.CREATED);
	}

	// Gets reservations by user ID
	@GetMapping("/user/{userId}")
	public List<Reservation> getReservationsByUser(@PathVariable Long userId) {
		logger.info("Fetching reservations for user ID: {}", userId);
		return reservationService.getReservationsByUser(userId);
	}

	// Deletes a reservation by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
		logger.info("Deleting reservation with ID: {}", id);
		String msg = reservationService.deleteReservation(id);
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	// Finds buses by route from and to destinations
	@GetMapping("/{routeFrom}/{routeTo}")
	public ResponseEntity<List<Bus>> findBusByFromAndToDestination(@PathVariable String routeFrom,
			@PathVariable String routeTo) {
		logger.info("Finding buses from {} to {}", routeFrom, routeTo);
		return new ResponseEntity<>(reservationService.findBusByFromAndToDestination(routeFrom, routeTo),
				HttpStatus.OK);
	}

	// Deletes a passenger from a reservation by reservation ID and passenger ID
	@DeleteMapping("/deletePassenger/{reservationId}/{passengerId}")
	public ResponseEntity<String> deletePassengerById(@PathVariable Long reservationId,
			@PathVariable Long passengerId) {
		logger.info("Deleting passenger with ID: {} from reservation ID: {}", passengerId, reservationId);
		return new ResponseEntity<>(passengerService.deletePassengerFromReservation(reservationId, passengerId),
				HttpStatus.OK);
	}

	// Gets all reservations
	@GetMapping("/getAllReservation")
	public ResponseEntity<List<Reservation>> getAllReservation() {
		logger.info("Fetching all reservations");
		return new ResponseEntity<>(reservationService.getAllReservation(), HttpStatus.OK);
	}

	// Finds a reservation by ID
	@GetMapping("/{id}")
	public ResponseEntity<Reservation> findByReservationId(@PathVariable Long id) {
		logger.info("Fetching reservation with ID: {}", id);
		return new ResponseEntity<>(reservationService.findById(id), HttpStatus.OK);
	}
}