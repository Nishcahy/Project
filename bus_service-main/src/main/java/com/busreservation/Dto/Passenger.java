package com.busreservation.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Passenger {

	private Long pid;

	@NotBlank(message = "name is required")
	private String name;
	@NotBlank(message = "gender is required")
	private String gender;

	@NotNull(message = "age is required")
	private int age;
}
