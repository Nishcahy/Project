package com.feedback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long feedBackId;

	private Long userId;

	private Long reservationId;
	
	private String feedBackStatement;

}
