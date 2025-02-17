package com.busreservation.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Bus {

	private Long busId;

	 @NotNull
	    @Size(min = 1, max = 20, message = "Bus number must be between 1 and 20 characters.")
	    private String busNo;

	    @NotNull
	    @Size(min = 1, max = 50, message = "Route from must be between 1 and 50 characters.")
	    private String routeFrom;

	    @NotNull
	    @Size(min = 1, max = 50, message = "Route to must be between 1 and 50 characters.")
	    private String routeTo;

	    @Min(value = 1, message = "Seats must be at least 1.")
	    private int seats;
	    
	    @Min(value = 0,message = "Available seats cannot be lessthan 0")
	    private int availableSeats;
	    
	    
	    private List<Integer> bookedSeatNumbers;

	    @NotNull
	    private String departureTime;
	    
	    @Min(value = 0, message = "Price must be a positive value.")
	    private int price;

}
