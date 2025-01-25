package com.passengerService.service;

import java.util.List;

import com.passengerService.entity.Passenger;
import com.passengerService.exception.PassengerNotFoundExeption;

public interface PassengerService {
	
	List<Passenger> getAllPassengers();
	Passenger getPassengerById(Long id) throws PassengerNotFoundExeption;
	Passenger savePassenger(Passenger passenger);
	Passenger updatePassenger(Passenger passenger);
	Passenger deletePassenger(Long id) throws PassengerNotFoundExeption;
	
}
