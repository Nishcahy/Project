package com.feedback.service;

import com.feedback.dto.FeedBackDTO;
import com.feedback.entity.Feedback;
import com.feedback.entity.Reservation;
import com.feedback.exception.ResourceNotFoundException;

public interface FeedbackService {
	
	Feedback addFeedBack(Feedback feedBack);
	
	String removeFeedBack(Long feedBackId) throws ResourceNotFoundException;
	
	FeedBackDTO fetchFeedbackById(Long feedBackId) throws ResourceNotFoundException;
	
	Feedback updateFeedback(Long feedBackId,Feedback feedBack) throws ResourceNotFoundException;
	
	Reservation getReservationForFeedback(Long feedbackId);
}
