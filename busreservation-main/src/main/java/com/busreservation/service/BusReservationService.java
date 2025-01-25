package com.busreservation.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.busreservation.DTO.Bus;
import com.busreservation.DTO.ReservationDTO;
import com.busreservation.entity.Reservation;
import com.busreservation.exception.ResourceNotFoundException;
import com.busreservation.repo.ReservationRepo;

@Service
public class BusReservationService {
		
		Logger logger=LoggerFactory.getLogger(BusReservationService.class);
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
	        double totalAmount = (double)reservation.getNumberOfSeats() * busDetails.getPrice();
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
	    public String deleteReservation(Long id) {
	        Reservation reservation = reservationRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + id));
	        
	        LocalDate reservationDate = reservation.getDate();
	        LocalDate currentDate = LocalDate.now();
	        
	        // Debug statements to check the dates
	        logger.info("***************************************Reservation Date:{} " , reservationDate);
	        logger.info("Current Date: {}" ,currentDate);
	        logger.info("Allowed Cancellation Date:{} " , reservationDate.plusDays(7));
	        if (currentDate.isBefore(reservationDate.plusDays(7))) {
	        	throw new ResourceNotFoundException("Reservation cannot be cancelled after 7 days from the reservation date.");
	        }else {
	        	reservationRepository.deleteById(id);
	        }
	        
	        
	        return "Reservation deleted";
	    }
	    
	    public List<Reservation> getAllReservation(){
	    	return reservationRepository.findAll();
	    }
}
