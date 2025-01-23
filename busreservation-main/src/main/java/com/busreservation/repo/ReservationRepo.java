package com.busreservation.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busreservation.entity.Reservation;


@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {
	List<Reservation> findByUserId(Long userId);

}
