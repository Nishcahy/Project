package com.busreservation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.busreservation.Dto.Reservation;
import com.busreservation.entity.Bus;
import com.busreservation.exception.ResourceNotFoundException;
import com.busreservation.repository.BusRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BusServiceImpl implements BusService {

	private BusRepo busRepo;
	private ReservationClient reservationClient;

	public Bus addBus(Bus bus) {
		return busRepo.save(bus);
	}

	public List<Bus> fetchAllBus() {
		return busRepo.findAll();
	}

	public Bus findBusById(Long id) throws ResourceNotFoundException {
		Optional<Bus> bus= busRepo.findById(id);
		if(!bus.isPresent()) {
			throw new ResourceNotFoundException("Bus not found with ID: " + id);
		}
		return bus.get();
		
	}

	public Bus updateBus(Long id, Bus bus) throws ResourceNotFoundException{
		Bus existingBus = busRepo.findById(id).orElse(null);

		if (existingBus == null) {
			throw new ResourceNotFoundException("Bus not found with ID: " + id);
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

	public void deleteBus(Long id) throws ResourceNotFoundException {
		Optional<Bus> bus=busRepo.findById(id);
		if(bus.isPresent()) {
			busRepo.deleteById(id);
		}else {
			throw new ResourceNotFoundException("Bus not found with id"+id);
			
		}
		
		
		
	}

	public List<Reservation> getReservations() {
		return reservationClient.getAllReservation();
	}

}
