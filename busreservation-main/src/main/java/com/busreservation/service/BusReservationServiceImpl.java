package com.busreservation.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busreservation.dto.Bus;
import com.busreservation.dto.ReservationDTO;
import com.busreservation.entity.Reservation;
import com.busreservation.exception.ResourceNotFoundException;
import com.busreservation.repo.ReservationRepo;
import com.busreservation.service.client.BusClient;

import feign.FeignException;


@Service
public class BusReservationServiceImpl implements BusReservation{
		
		Logger logger=LoggerFactory.getLogger(BusReservationServiceImpl.class);
		
	    private final ReservationRepo reservationRepository;
		
	    private final BusClient busClient;
	    
	    public BusReservationServiceImpl(ReservationRepo reservationRepository,BusClient busClient) {
	        this.reservationRepository = reservationRepository;
	        this.busClient=busClient;
	       
	    }
	    @Override
	    public ReservationDTO createReservation(Reservation reservation) {
	        try {
	            // Get the bus price
	            Bus busDetails = busClient.fetchBus(reservation.getBusId());
                LocalDate currentDate=LocalDate.now();
	            if(reservation.getDate().isBefore(currentDate)) {
	            	throw new ResourceNotFoundException("Bus Reservation cannot be done in past days");
	            }
	            // Calculate the total price dynamically
	            double totalAmount = (double) reservation.getNumberOfSeats() * busDetails.getPrice();
	            reservation.setTotalAmount(totalAmount);
                Reservation res = reservationRepository.save(reservation);

	            return new ReservationDTO(busDetails, res);
	        } catch (FeignException e) {
	            if (e.status() == 404) {
	                throw new ResourceNotFoundException("Bus not found with ID: " + reservation.getBusId());
	            }
	            // Handle other exceptions
	            throw new ResourceNotFoundException("An error occurred while fetching bus details");
	        }
	    }
	    
	    @Override
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
	    	if(requiredBuses.isEmpty()) {
	    		throw new ResourceNotFoundException("No bus in specified location");
	    	}
	    	return requiredBuses;
	    }
	    
	    @Override
	    public String deleteReservation(Long id) {
	        Reservation reservation = reservationRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + id));
	        
	        LocalDate reservationDate = reservation.getDate();
	        LocalDate currentDate = LocalDate.now();
	        
	        // Debug statements to check the dates
	        logger.info("***************************************Reservation Date:{} " , reservationDate);
	        logger.info("Current Date: {}" ,currentDate);
	        logger.info("Allowed Cancellation Date:{} " , reservationDate.plusDays(7));
	        if (currentDate.isAfter(reservationDate.minusDays(7))) {
	            throw new ResourceNotFoundException("Reservation cannot be cancelled within 7 days from the reservation date.");
	        } else {
	            reservationRepository.deleteById(id);
	        }
	        
	        
	        return "Reservation deleted";
	    }
	    
	    @Override
	    public List<Reservation> getAllReservation(){
	    	return reservationRepository.findAll();
	    }
		@Override
		public Reservation findById(Long reservationId) {
			Optional<Reservation> reservation=reservationRepository.findById(reservationId);
			if(!reservation.isPresent()) {
			  throw new ResourceNotFoundException("No reservation with id");
			}
			
			return reservation.get();
		}
	    
	    
}
