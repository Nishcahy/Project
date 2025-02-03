package com.feedback.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	FeedbackService feedbackService;
	
	@PostMapping("/save")
	public ResponseEntity<Feedback> save(@RequestBody Feedback feedBack){
		return new ResponseEntity<>(feedbackService.addFeedBack(feedBack),HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") Long feedBackId){
		return new ResponseEntity<>(feedbackService.removeFeedBack(feedBackId),HttpStatus.OK);
	}
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Feedback> update(@PathVariable("id") Long feedBackId,@RequestBody Feedback feedBack){
		return new ResponseEntity<>(feedbackService.updateFeedback(feedBackId, feedBack),HttpStatus.OK);
	}
	
	@GetMapping("/getById/{id}")
	public ResponseEntity<FeedBackDTO> getById(@PathVariable("id") Long feedBackId){
		return new ResponseEntity<>(feedbackService.fetchFeedbackById(feedBackId),HttpStatus.OK);
	}
	
	 @GetMapping("/{id}/reservation")
	    public Reservation getReservationForFeedback(@PathVariable Long id) {
	        return feedbackService.getReservationForFeedback(id);
	    }
	
}
