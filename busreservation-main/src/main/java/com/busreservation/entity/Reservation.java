package com.busreservation.entity;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Reservation {
		
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @NotNull
	    private Long userId; // Reference to the user making the reservation

	    @NotNull
	    private Long busId; // Reference to the bus

	    @NotNull
	    private LocalDate date;

	    @NotNull
	    private Integer numberOfSeats;

	    private Double totalAmount;
	    
	
}
