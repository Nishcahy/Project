package com.busreservation.service;

import java.util.List;

import com.busreservation.entity.Passenger;
import com.busreservation.exception.ResourceNotFoundException;

public interface PassengerService {
	
	Passenger addPassenger(Passenger passenger);
	List<Passenger> findAllPassenger();
	String deletePassenger(Long id)throws ResourceNotFoundException;
	Passenger updatePassenger(Passenger passenger) throws ResourceNotFoundException;
	Passenger findById(Long id) throws ResourceNotFoundException;
	String deletePassengerFromReservation(Long reservationId, Long passengerId);
}
