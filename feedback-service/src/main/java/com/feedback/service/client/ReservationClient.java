package com.feedback.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.feedback.entity.Reservation;

@FeignClient(name = "reservation-service")
public interface ReservationClient {
	@GetMapping("/reservations/{id}")
	Reservation getReservation(@PathVariable("id") Long id);
}
