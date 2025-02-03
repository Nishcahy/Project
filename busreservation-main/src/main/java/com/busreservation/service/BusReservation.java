package com.busreservation.service;

import java.util.List;

import com.busreservation.dto.Bus;
import com.busreservation.dto.ReservationDTO;
import com.busreservation.entity.Reservation;

public interface BusReservation {

	ReservationDTO createReservation(Reservation reservation);
	List<Reservation> getReservationsByUser(Long userId);
	 List<Bus> findBusByFromAndToDestination(String routeFrom,String routeTo);
	 
	 String deleteReservation(Long id);
	 List<Reservation> getAllReservation();
	 Reservation findById(Long reservationId);
	
}
