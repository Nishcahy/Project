package com.busreservation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
import com.busreservation.exception.ResourceNotFoundException;
import com.busreservation.service.BusService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/buses")
@AllArgsConstructor
public class BusController {
	
	private final BusService busService;
	
	private final Logger logger=LoggerFactory.getLogger(BusController.class);
	
	
	//addBus 
	@PostMapping  //http://localhost:8085/api/buses
	public ResponseEntity<Bus> addBus(@RequestBody Bus bus){
		logger.info("****Inside addBus method");
		return new ResponseEntity<> (busService.addBus(bus),HttpStatus.CREATED);
	}
	
	//Getting all buses
	@GetMapping("/all-bus")  //http://localhost:8085/api/buses/all-bus
	public ResponseEntity<List<Bus>> fetchAllBus(){
		logger.info("Fetching all busses");
		return new ResponseEntity<>(busService.fetchAllBus(),HttpStatus.OK);
	}
	
	//Updating bus 
	@PutMapping("update-bus/{id}")  //http://localhost:8085/api/buses/update-bus/123
	public ResponseEntity<Bus> updateBus(@PathVariable Long id, @RequestBody Bus bus) throws ResourceNotFoundException {
		return new ResponseEntity<> (busService.updateBus(id, bus),HttpStatus.OK);
	}
	
	//delete bus by id
	@DeleteMapping("/{id}")  //http://localhost:8085/api/buses/123
	public ResponseEntity<Bus> deleteBus(@PathVariable Long id) throws ResourceNotFoundException{
		busService.deleteBus(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	//get bus by id
	@GetMapping("/findBus/{id}")  //http://localhost:8085/api/buses/findBus/123
	public ResponseEntity<Bus> findByBusId(@PathVariable Long id) throws ResourceNotFoundException {
		
		return new ResponseEntity<>(busService.findBusById(id),HttpStatus.OK) ;
	}
	
	
	//getting all reservation
	@GetMapping("/getAllReservation") //http://localhost:8085/api/buses/getAllReservation
	public ResponseEntity<List<Reservation>> getAllReservation(){
		return new ResponseEntity<>(busService.getReservations(), HttpStatus.OK);
	}
	

}
