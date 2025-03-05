package com.feedback.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feedback.dto.FeedBackDTO;
import com.feedback.entity.Feedback;
import com.feedback.entity.Reservation;
import com.feedback.service.FeedbackService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/feedback")
@AllArgsConstructor
public class FeedBackController {

	private static final Logger logger = LoggerFactory.getLogger(FeedBackController.class);

	FeedbackService feedbackService;

	/**
	 * EndPoint to save a new feedback.
	 * 
	 * @param feedBack The feedback entity to be saved.
	 * @return ResponseEntity containing the saved feedback and HTTP status.
	 */
	@PostMapping("/save")
	public ResponseEntity<Feedback> save(@RequestBody Feedback feedBack) {
		logger.info("Saving feedback: {}", feedBack);
		Feedback savedFeedback = feedbackService.addFeedBack(feedBack);
		logger.info("Feedback saved with ID: {}", savedFeedback.getFeedBackId());
		return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
	}

	/**
	 * EndPoint to delete a feedback by its ID.
	 * 
	 * @param feedBackId The ID of the feedback to be deleted.
	 * @return ResponseEntity containing a success message and HTTP status.
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") Long feedBackId) {
		logger.info("Deleting feedback with ID: {}", feedBackId);
		String response = feedbackService.removeFeedBack(feedBackId);
		logger.info("Feedback deleted with ID: {}", feedBackId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * EndPoint to update an existing feedback by its ID.
	 * 
	 * @param feedBackId The ID of the feedback to be updated.
	 * @param feedBack   The updated feedback entity.
	 * @return ResponseEntity containing the updated feedback and HTTP status.
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<Feedback> update(@PathVariable("id") Long feedBackId, @RequestBody Feedback feedBack) {
		logger.info("Updating feedback with ID: {}", feedBackId);
		Feedback updatedFeedback = feedbackService.updateFeedback(feedBackId, feedBack);
		logger.info("Feedback updated with ID: {}", updatedFeedback.getFeedBackId());
		return new ResponseEntity<>(updatedFeedback, HttpStatus.OK);
	}

	/**
	 * EndPoint to fetch a feedback by its ID.
	 * 
	 * @param feedBackId The ID of the feedback to be fetched.
	 * @return ResponseEntity containing the feedback DTO and HTTP status.
	 */
	@GetMapping("/getById/{id}")
	public ResponseEntity<FeedBackDTO> getById(@PathVariable("id") Long feedBackId) {
		logger.info("Fetching feedback with ID: {}", feedBackId);
		FeedBackDTO feedbackDTO = feedbackService.fetchFeedbackById(feedBackId);
		logger.info("Feedback fetched with ID: {}", feedBackId);
		return new ResponseEntity<>(feedbackDTO, HttpStatus.OK);
	}

	/**
	 * EndPoint to fetch the reservation associated with a feedback by feedback ID.
	 * 
	 * @param id The ID of the feedback.
	 * @return The reservation associated with the feedback.
	 */
	@GetMapping("/{id}/reservation")
	public Reservation getReservationForFeedback(@PathVariable Long id) {
		logger.info("Fetching reservation for feedback with ID: {}", id);
		Reservation reservation = feedbackService.getReservationForFeedback(id);
		logger.info("Reservation fetched for feedback with ID: {}", id);
		return reservation;
	}
	
	@GetMapping("/getByUserId/{id}")
	public ResponseEntity<List<FeedBackDTO>> getByUserId(@PathVariable("id") Long userId) {
		logger.info("Fetching feedback with ID: {}", userId);
		List<FeedBackDTO> feedbackDTO = feedbackService.findByUserId(userId);
		logger.info("Feedback fetched with ID: {}", userId);
		return new ResponseEntity<>(feedbackDTO, HttpStatus.OK);
	}
	
	@GetMapping("/get-all-feedback")
	public ResponseEntity<List<FeedBackDTO>> getAllFeedbacks(){
		logger.info("Getting all feedbacks");
		List<FeedBackDTO> allFeedback=feedbackService.findAll();
		return new ResponseEntity<>(allFeedback,HttpStatus.OK);
	}
	
	
}