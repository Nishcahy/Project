package com.busreservation.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	private Long busId; 

	@NotNull
	private LocalDate date;

	@NotNull(message = "Minimum 1 seat is required")
	@Max(value = 5,message = "You cannot book more than 5 seats")
	private int numberOfSeats;

	private Double totalAmount;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "reservation_id")
	private List<Passenger> passengers;
	
	

}

