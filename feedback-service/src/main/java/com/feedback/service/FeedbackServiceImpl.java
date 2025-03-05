package com.feedback.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);

	FeedBackRepo feedBackRepo;
	ReservationClient reservationClient;

	// Adds a new feedback
	@Override
    public Feedback addFeedBack(Feedback feedBack) {
        Long reservationId = feedBack.getReservationId(); // Feedback has a getReservationId method
        Reservation reservation = reservationClient.getReservation(reservationId);
 
        if (reservation != null && reservation.getReservationDate().isBefore(LocalDate.now().plusDays(1))) {
            logger.info("Adding feedback: {}", feedBack);
            Feedback savedFeedback = feedBackRepo.save(feedBack);
            logger.info("Feedback added with ID: {}", savedFeedback.getFeedBackId());
            return savedFeedback;
        } else if (reservation == null) {
            throw new ResourceNotFoundException("Reservation not found.");
        } else {
            throw new IllegalStateException("Feedback can only be added after the reservation date.");
        }
	}

	// Removes a feedback by its ID
	@Override
	public String removeFeedBack(Long feedBackId) throws ResourceNotFoundException {
		logger.info("Removing feedback with ID: {}", feedBackId);
		Optional<Feedback> feedBack = feedBackRepo.findById(feedBackId);
		if (!feedBack.isPresent()) {
			logger.error("Feedback not found with ID: {}", feedBackId);
			throw new ResourceNotFoundException("Feedback not found with specified ID");
		} else {
			feedBackRepo.deleteById(feedBackId);
			logger.info("Feedback successfully removed with ID: {}", feedBackId);
		}
		return "Feedback successfully removed";
	}

	// Fetches a feedback by its ID
	@Override
	public FeedBackDTO fetchFeedbackById(Long feedBackId) throws ResourceNotFoundException {
		logger.info("Fetching feedback with ID: {}", feedBackId);
		Optional<Feedback> feedBack = feedBackRepo.findById(feedBackId);
		if (!feedBack.isPresent()) {
			logger.error("No feedback available with ID: {}", feedBackId);
			throw new ResourceNotFoundException("No feedback available");
		}
		Reservation reservation = reservationClient.getReservation(feedBack.get().getReservationId());
		FeedBackDTO feedBackDTO = new FeedBackDTO();
		feedBackDTO.setBusId(reservation.getBusId());
		feedBackDTO.setComplaintStatement(feedBack.get().getFeedBackStatement());
		feedBackDTO.setDate(reservation.getDate());
		feedBackDTO.setUserId(feedBack.get().getUserId());
		feedBackDTO.setFeedBackId(feedBackId);
		feedBackDTO.setReservationId(reservation.getId());
		logger.info("Feedback fetched with ID: {}", feedBackId);
		return feedBackDTO;
	}

	// Updates an existing feedback by its ID
	@Override
	public Feedback updateFeedback(Long feedBackId, Feedback feedBack) throws ResourceNotFoundException {
		logger.info("Updating feedback with ID: {}", feedBackId);
		Optional<Feedback> feedBack1 = feedBackRepo.findById(feedBackId);
		if (!feedBack1.isPresent()) {
			logger.error("No feedback available to update with ID: {}", feedBackId);
			throw new ResourceNotFoundException("No feedback available to update");
		}
		feedBack1.get().setFeedBackStatement(feedBack.getFeedBackStatement());
		Feedback updatedFeedback = feedBackRepo.save(feedBack1.get());
		logger.info("Feedback updated with ID: {}", updatedFeedback.getFeedBackId());
		return updatedFeedback;
	}

	// Fetches the reservation associated with a feedback by feedback ID
	public Reservation getReservationForFeedback(Long feedbackId) {
		logger.info("Fetching reservation for feedback with ID: {}", feedbackId);
		FeedBackDTO feedback = fetchFeedbackById(feedbackId);
		if (feedback != null) {
			Reservation reservation = reservationClient.getReservation(feedback.getReservationId());
			logger.info("Reservation fetched for feedback with ID: {}", feedbackId);
			return reservation;
		}else {
			logger.error("No reservation found for feedback with ID: {}", feedbackId);
			throw new ResourceNotFoundException("No feedback Available");
			
		}
		
		
	
	}

	@Override
	public List<FeedBackDTO> findByUserId(Long userId) {
		logger.info("Fetching feedback with ID: {}", userId);
		List<Feedback> feedBacks= feedBackRepo.findByUserId(userId);
		if (feedBacks.isEmpty()) {
			logger.error("No feedback available with ID: {}", userId);
			throw new ResourceNotFoundException("No feedback available");
		}
		List<FeedBackDTO> listDto=new ArrayList<>();
		for(Feedback feedback:feedBacks) {
			Reservation reservation = reservationClient.getReservation(feedback.getReservationId());
			FeedBackDTO feedBackDTO = new FeedBackDTO();
			feedBackDTO.setBusId(reservation.getBusId());
			feedBackDTO.setComplaintStatement(feedback.getFeedBackStatement());
			feedBackDTO.setDate(reservation.getDate());
			feedBackDTO.setUserId(feedback.getUserId());
			feedBackDTO.setFeedBackId(feedback.getFeedBackId());
			feedBackDTO.setReservationId(reservation.getId());
			logger.info("Feedback fetched with ID: {}", userId);
			listDto.add(feedBackDTO);
			
		}
		
		return listDto;
	}

	@Override
	public List<FeedBackDTO> findAll() {
		List<Feedback> feedBacks=feedBackRepo.findAll();
		if (feedBacks.isEmpty()) {
			logger.error("No feedback available");
			throw new ResourceNotFoundException("No feedback available");
		}
		List<FeedBackDTO> listDto=new ArrayList<>();
		for(Feedback feedback:feedBacks) {
			Reservation reservation = reservationClient.getReservation(feedback.getReservationId());
			FeedBackDTO feedBackDTO = new FeedBackDTO();
			feedBackDTO.setBusId(reservation.getBusId());
			feedBackDTO.setComplaintStatement(feedback.getFeedBackStatement());
			feedBackDTO.setDate(reservation.getDate());
			feedBackDTO.setUserId(feedback.getUserId());
			feedBackDTO.setFeedBackId(feedback.getFeedBackId());
			feedBackDTO.setReservationId(reservation.getId());
			logger.info("Feedback fetched");
			listDto.add(feedBackDTO);
			
		}
		
		return listDto;
	}
}