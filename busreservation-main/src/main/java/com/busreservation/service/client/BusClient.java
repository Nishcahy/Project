package com.busreservation.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.busreservation.dto.Bus;

@FeignClient("BUS-SERVICE")
public interface BusClient {

	@GetMapping("api/buses/findBus/{id}")
	Bus fetchBus(@PathVariable Long id);

	@GetMapping("api/buses/all-bus")
	List<Bus> fetchAllBus();
	
	@PutMapping("api/buses/update-bus/{id}")
	public Bus updateBus(@PathVariable Long id, @RequestBody Bus bus);
}
