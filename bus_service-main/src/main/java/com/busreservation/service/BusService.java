package com.busreservation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.busreservation.Dto.Reservation;
import com.busreservation.entity.Bus;
import com.busreservation.repository.BusRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BusService {
	
	private BusRepo busRepo;
	private ReservationClient reservationClient;
	
	
	public Bus addBus(Bus  bus) {
		return busRepo.save(bus);
	}
	
	public List<Bus> fetchAllBus(){
		return busRepo.findAll();
	}
	
	public Bus findBusById(Long id) {
		return busRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Bus not found with id " + id));
	}
	
	public Bus updateBus(Long id,Bus bus) {
		Bus existingBus=busRepo.findById(id).orElse(null);
		
		if(existingBus==null) {
			throw new IllegalArgumentException("Bus not found with ID: " + id);
		}
		existingBus.setBusNo(bus.getBusNo());
	    existingBus.setRouteFrom(bus.getRouteFrom());
	    existingBus.setRouteTo(bus.getRouteTo());
	    existingBus.setSeats(bus.getSeats());
	    existingBus.setDepartureTime(bus.getDepartureTime());
	    existingBus.setPrice(bus.getPrice());

	    // Save the updated bus
	    return busRepo.save(existingBus);
	}
	
	public void deleteBus(Long id) {
        busRepo.deleteById(id);
    }
	
	public List<Reservation> getReservations(){
		return reservationClient.getAllReservation();
	}

}
