package com.feedback.entity;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
	
	 	private Long id;

	    @NotNull
	    private Long userId;

	    @NotNull
	    private Long busId; // Reference to the bus

	    @NotNull
	    private LocalDate date;

	    @NotNull
	    private int numberOfSeats;

	    private Double totalAmount;
	 
	    public LocalDate getReservationDate() {
	        return date;
	    }

}
