package com.feedback.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.feedback.dto.FeedBackDTO;
import com.feedback.entity.Feedback;
import com.feedback.entity.Reservation;
import com.feedback.exception.ResourceNotFoundException;
import com.feedback.repo.FeedBackRepo;
import com.feedback.service.client.ReservationClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

	FeedBackRepo feedBackRepo;

	ReservationClient reservationClient;

	@Override
	public Feedback addFeedBack(Feedback feedBack) {
		return feedBackRepo.save(feedBack);
	}

	@Override
	public String removeFeedBack(Long feedBackId) throws ResourceNotFoundException {
		Optional<Feedback> feedBack = feedBackRepo.findById(feedBackId);
		if (!feedBack.isPresent()) {
			throw new ResourceNotFoundException("feedback not found with specified id");
		} else {
			feedBackRepo.deleteById(feedBackId);
		}
		return "Feedback sucessfully Removed";
	}

	@Override
	public FeedBackDTO fetchFeedbackById(Long feedBackId) throws ResourceNotFoundException {
		Optional<Feedback> feedBack = feedBackRepo.findById(feedBackId);
		Reservation reservation=reservationClient.getReservation(feedBack.get().getReservationId());
		if (!feedBack.isEmpty()) {
			throw new ResourceNotFoundException("No feedback available");
		}
		FeedBackDTO feedBackDTO=new FeedBackDTO();
		feedBackDTO.setBusId(reservation.getBusId());
		feedBackDTO.setComplaintStatement(feedBack.get().getComplaintStatement());
		feedBackDTO.setDate(reservation.getDate());
		feedBackDTO.setUserId(feedBack.get().getUserId());
		feedBackDTO.setFeedBackId(feedBackId);
		feedBackDTO.setReservationId(reservation.getId());
		return feedBackDTO;
	}

	@Override
	public Feedback updateFeedback(Long feedBackId, Feedback feedBack) throws ResourceNotFoundException {
		Optional<Feedback> feedBack1 = feedBackRepo.findById(feedBackId);
		if (!feedBack1.isPresent()) {
			throw new ResourceNotFoundException("No feedback available to update");
		}
		feedBack1.get().setComplaintStatement(feedBack.getComplaintStatement());
		return feedBackRepo.save(feedBack1.get());
	}
	
	 public Reservation getReservationForFeedback(Long feedbackId) {
	        FeedBackDTO feedback = fetchFeedbackById(feedbackId);
	        if (feedback != null) {
	            return reservationClient.getReservation(feedback.getReservationId());
	        }
	        return null;
	    }

}
