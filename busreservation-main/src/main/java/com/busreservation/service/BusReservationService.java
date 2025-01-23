package com.busreservation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.busreservation.DTO.ReservationDTO;
import com.busreservation.entity.Bus;
import com.busreservation.entity.Reservation;
import com.busreservation.repo.ReservationRepo;

@Service
public class BusReservationService {

	    private final ReservationRepo reservationRepository;
	    private final BusClient busClient;
	    public BusReservationService(ReservationRepo reservationRepository,BusClient busClient) {
	        this.reservationRepository = reservationRepository;
	        this.busClient=busClient;
	       
	    }

	    public ReservationDTO createReservation(Reservation reservation) {
	        // Get the bus price (mocked for now)
	    	Bus busDetails = busClient.fetchBus(reservation.getBusId());

	        if (busDetails == null) {
	            throw new IllegalArgumentException("Bus not found with ID: " + reservation.getBusId());
	        }

	        // Calculate the total price dynamically
	        double totalAmount = reservation.getNumberOfSeats() * busDetails.getPrice();
	        reservation.setTotalAmount(totalAmount);

	        // Save the reservation
	        Reservation res=reservationRepository.save(reservation);
	        
	        return new ReservationDTO(busDetails,res);
	    }

	    public List<Reservation> getReservationsByUser(Long userId) {
	        return reservationRepository.findByUserId(userId);
	    }
	    
	    public List<Bus> findBusByFromAndToDestination(String routeFrom,String routeTo){
	    	List<Bus> buses=busClient.fetchAllBus();
	    	
	    	List<Bus> requiredBuses=new ArrayList<>();
	    	for(Bus bus:buses) {
	    		if(routeFrom.equalsIgnoreCase(bus.getRouteFrom()) && routeTo.equalsIgnoreCase(bus.getRouteTo())) {
	    			requiredBuses.add(bus);
	    		}
	    		
	    	}
	    	return requiredBuses;
	    }
	    public void deleteReservation(Long id) {
	        reservationRepository.deleteById(id);
	    }
}
