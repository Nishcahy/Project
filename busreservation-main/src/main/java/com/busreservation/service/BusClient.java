package com.busreservation.service;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.busreservation.entity.Bus;

@FeignClient(url="http://localhost:8085/api/buses",value="Bus-Client")
public interface BusClient {
	
	
	@GetMapping("/findBus/{id}")
	Bus fetchBus(@PathVariable Long id);
	
	@GetMapping("/all-bus")
	List<Bus> fetchAllBus();

}
