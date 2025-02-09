package com.busreservation.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Reservation {

	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	private Long busId;

	@NotNull
	private LocalDate date;

	@NotNull
	private int numberOfSeats;

	private Double totalAmount;

	private List<Passenger> passengers;

}
