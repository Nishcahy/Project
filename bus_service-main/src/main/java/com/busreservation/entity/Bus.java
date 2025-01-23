package com.busreservation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Bus {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @NotNull
    @Pattern(regexp = "^(2[0-3]|[01]?\\d):[0-5]\\d$", message = "Departure time must be in HH:mm format (24-hour clock).")
    private String departureTime;

    @Min(value = 0, message = "Price must be a positive value.")
    private int price;
	
	

}
