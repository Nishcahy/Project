package com.feedback.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackDTO {

	private Long feedBackId;
	
	private Long userId;
	
	private Long reservationId;

	private String complaintStatement;

	private LocalDate date;

	private Long busId;
}
