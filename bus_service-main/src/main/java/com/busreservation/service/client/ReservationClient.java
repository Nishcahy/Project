package com.busreservation.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.busreservation.dto.Reservation;
import com.busreservation.dto.ReservationDto;

@FeignClient("bus-booking")
public interface ReservationClient {

	@GetMapping("api/reservations/getAllReservation")
	List<Reservation> getAllReservation();
	
	@GetMapping("api/reservations/by-bus-id/{busId}")
	List<ReservationDto> findReservationByBus(@PathVariable Long busId);
	

}
