package com.busreservation.service;

import java.util.List;

import com.busreservation.Dto.Reservation;
import com.busreservation.entity.Bus;
import com.busreservation.exception.ResourceNotFoundException;

public interface BusService {
	Bus addBus(Bus bus) ;
	List<Bus> fetchAllBus();
	Bus findBusById(Long id) throws ResourceNotFoundException;
	Bus updateBus(Long id, Bus bus) throws ResourceNotFoundException;
	void deleteBus(Long id) throws ResourceNotFoundException;
	List<Reservation> getReservations();

}
