package com.busreservation.DTO;

import com.busreservation.entity.Bus;
import com.busreservation.entity.Reservation;

import lombok.Data;


@Data
public class ReservationDTO {
	
	private Bus bus;
	private Reservation reservation;
	
	public ReservationDTO(Bus bus,Reservation reservation) {
		this.bus=bus;
		this.reservation=reservation;
	}
	

}
