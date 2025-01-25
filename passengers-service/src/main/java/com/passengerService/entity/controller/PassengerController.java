package com.passengerService.entity.controller;

import java.util.List;

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

import com.passengerService.entity.Passenger;
import com.passengerService.exception.PassengerNotFoundExeption;
import com.passengerService.service.PassengerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/passenger")
@AllArgsConstructor
public class PassengerController {

	private PassengerService passengerService;

	@GetMapping("/getAll")
	public ResponseEntity<List<Passenger>> getAllPassengers() {
		List<Passenger> passengers = passengerService.getAllPassengers();
		return new ResponseEntity<>(passengers, HttpStatus.OK);
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<Passenger> getPassengerById(@PathVariable Long id) {
		try {
			Passenger passenger = passengerService.getPassengerById(id);
			return new ResponseEntity<>(passenger, HttpStatus.OK);
		} catch (PassengerNotFoundExeption e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/save")
	public ResponseEntity<Passenger> savePassenger(@RequestBody Passenger passenger) {
		Passenger savedPassenger = passengerService.savePassenger(passenger);
		return new ResponseEntity<>(savedPassenger, HttpStatus.CREATED);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Passenger> updatePassenger(@PathVariable Long id, @RequestBody Passenger passenger) {

		passenger.setPassengerId(id);
		Passenger updatedPassenger = passengerService.updatePassenger(passenger);
		return new ResponseEntity<>(updatedPassenger, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Passenger> deletePassenger(@PathVariable Long id) {
		try {
			Passenger deletedPassenger = passengerService.deletePassenger(id);
			return new ResponseEntity<>(deletedPassenger, HttpStatus.OK);
		} catch (PassengerNotFoundExeption e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
