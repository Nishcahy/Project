package com.busreservation.controller;

import java.util.ArrayList;
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

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Rest Controller for managing Reservation related operations.
 * This controller handles CRUD operations for Reservation entities and finds buses.
 */
@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationController {

	private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

	private final BusReservationServiceImpl reservationService;
	private final PassengerService passengerService;

	/**
	 * Creates a new reservation.
	 *
	 * @param reservation The Reservation object to be created.
	 * @return ResponseEntity containing the created ReservationDTO and HttpStatus CREATED.
	 */
	@PostMapping
	public ResponseEntity<ReservationDTO> createReservation(@RequestBody @Valid Reservation reservation) {
		logger.info("Creating reservation: {}", reservation);
		return new ResponseEntity<>(reservationService.createReservation(reservation), HttpStatus.CREATED);
	}

	/**
	 * Gets reservations by user ID.
	 *
	 * @param userId The ID of the user.
	 * @return List of ReservationDTOs for the given user ID.
	 */
	@GetMapping("/user/{userId}")
	public List<ReservationDTO> getReservationsByUser(@PathVariable Long userId) {
		logger.info("Fetching reservations for user ID: {}", userId);
		return reservationService.getReservationsByUser(userId);
	}

	/**
	 * Deletes a reservation by ID.
	 *
	 * @param id The ID of the reservation to be deleted.
	 * @return ResponseEntity containing a message and HttpStatus OK.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
		logger.info("Deleting reservation with ID: {}", id);
		String msg = reservationService.deleteReservation(id);
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	/**
	 * Finds buses by route from and to destinations.
	 * Uses Circuit Breaker to handle failures.
	 *
	 * @param routeFrom The starting destination.
	 * @param routeTo   The ending destination.
	 * @return ResponseEntity containing a list of Buses and HttpStatus OK.
	 */
	@GetMapping("/{routeFrom}/{routeTo}")
	@CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "failReservation")
	public ResponseEntity<List<Bus>> findBusByFromAndToDestination(@PathVariable String routeFrom,
			@PathVariable String routeTo) {
		logger.info("Finding buses from {} to {}", routeFrom, routeTo);
		return new ResponseEntity<>(reservationService.findBusByFromAndToDestination(routeFrom, routeTo),
				HttpStatus.OK);
	}

	/**
	 * Fallback method for the findBusByFromAndToDestination method.
	 * Returns a dummy Bus object when the service is unavailable.
	 *
	 * @param routeFrom The starting destination.
	 * @param routeTo   The ending destination.
	 * @param e         The exception that occurred.
	 * @return ResponseEntity containing a list with a dummy Bus object and HttpStatus OK.
	 */
	public ResponseEntity<List<Bus>> failReservation(String routeFrom,
			@PathVariable String routeTo, Exception e) {
		List<Bus> buses = new ArrayList<>();
		Bus bus = new Bus();
		bus.setBusNo("Bus Not Available");
		bus.setDepartureTime("");
		bus.setPrice(0);
		bus.setSeats(0);
		bus.setRouteFrom("");
		bus.setRouteTo("");
		bus.setAvailableSeats(0);
		bus.setBookedSeatNumbers(new ArrayList<>());
		bus.setBusId(null);
		buses.add(bus);
		return new ResponseEntity<>(buses, HttpStatus.OK);

	}

	/**
	 * Deletes a passenger from a reservation by reservation ID and passenger ID.
	 *
	 * @param reservationId The ID of the reservation.
	 * @param passengerId   The ID of the passenger.
	 * @return ResponseEntity containing a message and HttpStatus OK.
	 */
	@DeleteMapping("/deletePassenger/{reservationId}/{passengerId}")
	public ResponseEntity<String> deletePassengerById(@PathVariable Long reservationId,
			@PathVariable Long passengerId) {
		logger.info("Deleting passenger with ID: {} from reservation ID: {}", passengerId, reservationId);
		return new ResponseEntity<>(passengerService.deletePassengerFromReservation(reservationId, passengerId),
				HttpStatus.OK);
	}

	/**
	 * Gets all reservations.
	 *
	 * @return ResponseEntity containing a list of ReservationDTOs and HttpStatus OK.
	 */
	@GetMapping("/getAllReservation")
	public ResponseEntity<List<ReservationDTO>> getAllReservation() {
		logger.info("Fetching all reservations");
		return new ResponseEntity<>(reservationService.getAllReservation(), HttpStatus.OK);
	}

	/**
	 * Finds a reservation by ID.
	 *
	 * @param id The ID of the reservation.
	 * @return ResponseEntity containing the Reservation object and HttpStatus OK.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Reservation> findByReservationId(@PathVariable Long id) {
		logger.info("Fetching reservation with ID: {}", id);
		return new ResponseEntity<>(reservationService.findById(id), HttpStatus.OK);
	}

	/**
	 * Finds reservations by bus ID.
	 *
	 * @param busId The ID of the bus.
	 * @return ResponseEntity containing a list of ReservationDTOs and HttpStatus OK.
	 */
	@GetMapping("/by-bus-id/{busId}")
	public ResponseEntity<List<ReservationDTO>> findReservationByBus(@PathVariable Long busId) {
		logger.info("Fetching reservation by busId");
		return new ResponseEntity<>(reservationService.getByBusId(busId), HttpStatus.OK);

	}

	/**
	 * Finds reservations by bus number.
	 *
	 * @param busno The bus number.
	 * @return ResponseEntity containing a list of ReservationDTOs and HttpStatus OK.
	 */
	@GetMapping("/by-busno/{busno}")
	public ResponseEntity<List<ReservationDTO>> findReservationByBus(@PathVariable String busno) {
		logger.info("Fetching reservation by busNo");
		return new ResponseEntity<>(reservationService.getByBusNo(busno), HttpStatus.OK);

	}
}