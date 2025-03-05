package com.busreservation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.busreservation.dto.Bus;
import com.busreservation.entity.Passenger;
import com.busreservation.entity.Reservation;
import com.busreservation.exception.ResourceNotFoundException;
import com.busreservation.repo.PassengerRepo;
import com.busreservation.repo.ReservationRepo;
import com.busreservation.service.client.BusClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {

	private PassengerRepo passengerRepo;
	private BusReservation busReservation;
	private ReservationRepo reservationRepo;
	private BusClient busClient;

	@Override
	public Passenger addPassenger(Passenger passenger) {
		return passengerRepo.save(passenger);
	}

	@Override
	public List<Passenger> findAllPassenger() {

		return passengerRepo.findAll();
	}

	@Override
	public String deletePassenger(Long id) {
		Optional<Passenger> passenger = passengerRepo.findById(id);
		if (!passenger.isPresent()) {
			throw new ResourceNotFoundException("passenger Id not found for delete");
		}

		passengerRepo.deleteById(id);
		return "deleted passenger";
	}

	@Override
	public Passenger updatePassenger(Passenger passenger) {
		Optional<Passenger> passenger1 = passengerRepo.findById(passenger.getPid());
		if (!passenger1.isPresent()) {
			throw new ResourceNotFoundException("passenger Id not found for update");
		}

		return passengerRepo.save(passenger);
	}

	@Override
	public Passenger findById(Long id) {
		Optional<Passenger> passenger = passengerRepo.findById(id);
		if (!passenger.isPresent()) {
			throw new ResourceNotFoundException("passenger Id not found");
		}

		return passenger.get();
	}

	public String deletePassengerFromReservation(Long reservationId, Long passengerId) {
	    Optional<Reservation> reservationOpt = reservationRepo.findById(reservationId);
	    if (reservationOpt.isPresent()) {
	        Reservation reservation = reservationOpt.get();
	        Optional<Passenger> passengerOpt = passengerRepo.findById(passengerId);
	 
	        if (passengerOpt.isPresent()) {
	            Passenger passenger = passengerOpt.get();
	            int seatNumber = passenger.getSeatNumber();
	 
	            // Remove the passenger from the reservation
	            reservation.getPassengers().removeIf(p -> p.getPid().equals(passengerId));
	            reservation.setNumberOfSeats(reservation.getNumberOfSeats() - 1);
	            reservation.setTotalAmount(
	                    reservation.getTotalAmount() - busClient.fetchBus(reservation.getBusId()).getPrice());
	 
	            // Check if all passengers have been removed
	            if (reservation.getPassengers().isEmpty()) {
	                // Delete the reservation
	                reservationRepo.delete(reservation);
	                return "Reservation deleted as all passengers have been removed";
	            } else {
	                // Save the updated reservation
	                reservationRepo.save(reservation);
	 
	                // Update the bus details
	                Bus bus = busClient.fetchBus(reservation.getBusId());
	                bus.setAvailableSeats(bus.getAvailableSeats() + 1);
	                bus.getBookedSeatNumbers().remove(Integer.valueOf(seatNumber)); // Remove the seat number from booked seats
	 
	                // Update the bus in the bus service
	                busClient.updateBus(bus.getBusId(), bus);
	 
	                return "Passenger deleted from Reservation";
	            }
	        } else {
	            throw new ResourceNotFoundException("Passenger not found");
	        }
	    } else {
	        throw new ResourceNotFoundException("Reservation not found");
	    }
	}
}
