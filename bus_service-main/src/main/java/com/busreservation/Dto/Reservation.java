package com.busreservation.Dto;

import java.time.LocalDate;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Reservation {
	
	private Long id;

    @NotNull
    private Long userId; // Reference to the user making the reservation

    @NotNull
    private Long busId; // Reference to the bus

    @NotNull
    private LocalDate date;

    @NotNull
    private int numberOfSeats;

    private Double totalAmount;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reservation_id")	
    private List<Passenger> passengers;

}
