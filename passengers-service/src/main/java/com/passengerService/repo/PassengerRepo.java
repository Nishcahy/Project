package com.passengerService.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.passengerService.entity.Passenger;

public interface PassengerRepo extends JpaRepository<Passenger, Long> {

}
