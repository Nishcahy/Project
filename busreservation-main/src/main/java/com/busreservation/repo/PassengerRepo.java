package com.busreservation.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busreservation.entity.Passenger;

public interface PassengerRepo extends JpaRepository<Passenger, Long> {

}
