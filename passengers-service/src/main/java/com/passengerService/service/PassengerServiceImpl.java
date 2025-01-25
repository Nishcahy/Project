package com.passengerService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.passengerService.entity.Passenger;
import com.passengerService.exception.PassengerNotFoundExeption;
import com.passengerService.repo.PassengerRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {

	private PassengerRepo passengerRepo;

	@Override
	public List<Passenger> getAllPassengers() {
		return passengerRepo.findAll();
	}

	@Override
	public Passenger getPassengerById(Long id) throws PassengerNotFoundExeption {
		Optional<Passenger> passenger = passengerRepo.findById(id);
		if (passenger.isPresent()) {
			return passenger.get();
		} else {

			throw new PassengerNotFoundExeption("Passenger Not found with id" + id);
		}

	}

	@Override
	public Passenger savePassenger(Passenger passenger) {

		return passengerRepo.save(passenger);
	}

	@Override
	public Passenger updatePassenger(Passenger passenger) {

		return passengerRepo.save(passenger);
	}

	@Override
	public Passenger deletePassenger(Long id) throws PassengerNotFoundExeption {

		Optional<Passenger> passenger = passengerRepo.findById(id);
		if (passenger.isPresent()) {
			passengerRepo.deleteById(id);
			return passenger.get();
		} else {
			throw new PassengerNotFoundExeption("Passenger not found with id " + id);
		}
	}

}
