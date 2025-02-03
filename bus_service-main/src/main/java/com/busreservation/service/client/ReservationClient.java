package com.busreservation.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.busreservation.dto.Reservation;

@FeignClient("bus-booking")
public interface ReservationClient {
	
	@GetMapping("api/reservations/getAllReservation")
	List<Reservation> getAllReservation();

}
