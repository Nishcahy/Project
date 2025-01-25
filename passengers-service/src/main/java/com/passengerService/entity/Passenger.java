package com.passengerService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
public class Passenger {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long passengerId;

	@NotBlank(message = "Passenger name is mandatory")
	private String passengerName;
	@Min(value = 0, message = "Passenger age must be greater than or equal to 0")
	@NotNull(message = "Age is required")
	private int passengerAge;
	@NotBlank(message = "Gender is mandatory")
	@Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
	private String passengerGender;


}
