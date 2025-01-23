package com.busreservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busreservation.entity.Bus;


@Repository
public interface BusRepo extends JpaRepository<Bus, Long>{

}
