package com.busreservation.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.busreservation.Dto.Reservation;

@FeignClient(url="http://localhost:8081/api/reservations",value="bus-booking")
public interface ReservationClient {
	
	@GetMapping("/getAllReservation")
	List<Reservation> getAllReservation();

}
