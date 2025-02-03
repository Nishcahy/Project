package com.feedback.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.feedback.entity.Reservation;

@FeignClient("BUS-BOOKING")
public interface ReservationClient {
	@GetMapping("api/reservations/{id}")
	Reservation getReservation(@PathVariable Long id);
}
