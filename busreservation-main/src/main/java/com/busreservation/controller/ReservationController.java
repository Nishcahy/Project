package com.busreservation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busreservation.DTO.ReservationDTO;
import com.busreservation.entity.Reservation;
import com.busreservation.service.BusReservationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
	
	

	    private final BusReservationService reservationService;

	    public ReservationController(BusReservationService reservationService) {
	        this.reservationService = reservationService;
	    }

	    @PostMapping
	    public ResponseEntity<ReservationDTO> createReservation(@RequestBody @Valid Reservation reservation) {
	        return new ResponseEntity<>(reservationService.createReservation(reservation), HttpStatus.CREATED);
	    }

	    @GetMapping("/user/{userId}")
	    public List<Reservation> getReservationsByUser(@PathVariable Long userId) {
	        return reservationService.getReservationsByUser(userId);
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
	        reservationService.deleteReservation(id);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	

}
