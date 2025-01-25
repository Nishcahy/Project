package com.busreservation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busreservation.Dto.Reservation;
import com.busreservation.entity.Bus;
import com.busreservation.service.BusService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/buses")
@AllArgsConstructor
public class BusController {
	
	private final BusService busService;
	
	
	@PostMapping
	public ResponseEntity<Bus> addBus(@RequestBody Bus bus){
		return new ResponseEntity<> (busService.addBus(bus),HttpStatus.CREATED);
	}
	
	@GetMapping("/all-bus")
	public List<Bus> fetchAllBus(){
		return busService.fetchAllBus();
	}
	
	@PutMapping("update-bus/{id}")
	public Bus updateBus(@PathVariable Long id, @RequestBody Bus bus) {
		return busService.updateBus(id, bus);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Bus> deleteBus(@PathVariable Long id){
		busService.deleteBus(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	@GetMapping("/findBus/{id}")
	public ResponseEntity<Bus> findByBusId(@PathVariable Long id) {
		
		return new ResponseEntity<>(busService.findBusById(id),HttpStatus.OK) ;
	}
	
	@GetMapping("/getAllReservation")
	public ResponseEntity<List<Reservation>> getAllReservation(){
		return new ResponseEntity<>(busService.getReservations(), HttpStatus.OK);
	}
	

}
