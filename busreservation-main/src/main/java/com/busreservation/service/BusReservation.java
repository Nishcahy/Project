package com.busreservation.service;

import java.util.List;

import com.busreservation.dto.Bus;
import com.busreservation.dto.ReservationDTO;
import com.busreservation.entity.Reservation;
import com.busreservation.exception.DatabaseException;
import com.busreservation.exception.ResourceNotFoundException;

public interface BusReservation {

	ReservationDTO createReservation(Reservation reservation);

	List<ReservationDTO> getReservationsByUser(Long userId) throws ResourceNotFoundException,DatabaseException;

	List<Bus> findBusByFromAndToDestination(String routeFrom, String routeTo);

	String deleteReservation(Long id);

	List<ReservationDTO> getAllReservation();

	Reservation findById(Long reservationId);
	
	List<ReservationDTO> getByBusId(Long busId);
	
	List<ReservationDTO> getByBusNo(String busNo);
	
	

}
