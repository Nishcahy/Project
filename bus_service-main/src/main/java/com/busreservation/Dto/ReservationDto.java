package com.busreservation.dto;



import com.busreservation.entity.Bus;

import lombok.Data;

@Data
public class ReservationDto {
	
	private Bus bus;
	private Reservation reservation;
	
	
	public ReservationDto(Bus bus,Reservation reservation) {
		this.bus=bus;
		this.reservation=reservation;
	}
	

}

