package com.busreservation.service.client;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.busreservation.dto.Bus;

@FeignClient("BUS-SERVICE")
public interface BusClient {
	
	
	@GetMapping("api/buses/findBus/{id}")
	Bus fetchBus(@PathVariable Long id);
	
	@GetMapping("api/buses/all-bus")
	List<Bus> fetchAllBus();

}
