package com.feedback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.feedback.dto.FeedBackDTO;
import com.feedback.entity.Feedback;
import com.feedback.entity.Reservation;
import com.feedback.exception.ResourceNotFoundException;
import com.feedback.repo.FeedBackRepo;
import com.feedback.service.FeedbackServiceImpl;
import com.feedback.service.client.ReservationClient;

@SpringBootTest
class FeedbackServiceApplicationTests {

	@Mock
	private FeedBackRepo feedBackRepo;

	@Mock
	private ReservationClient reservationClient;

	@InjectMocks
	private FeedbackServiceImpl feedbackService;

	private Feedback feedback;
	private Reservation reservation;

	@BeforeEach
	public void setUp() {
		feedback = new Feedback();
		feedback.setFeedBackId(1L);
		feedback.setFeedBackStatement("Great service!");
		feedback.setReservationId(1L);
		feedback.setUserId(1L);

		reservation = new Reservation();
		reservation.setId(1L);
		reservation.setBusId(101L);
		reservation.setDate(LocalDate.parse("2025-02-04"));
	}

	@Test
	void testAddFeedBack() {
		when(feedBackRepo.save(any(Feedback.class))).thenReturn(feedback);

		Feedback savedFeedback = feedbackService.addFeedBack(feedback);

		assertNotNull(savedFeedback);
		assertEquals(feedback.getFeedBackId(), savedFeedback.getFeedBackId());
		verify(feedBackRepo, times(1)).save(feedback);
	}

	@Test
	void testFetchFeedbackById() throws ResourceNotFoundException {
		when(feedBackRepo.findById(1L)).thenReturn(Optional.of(feedback));
		when(reservationClient.getReservation(1L)).thenReturn(reservation);

		FeedBackDTO feedbackDTO = feedbackService.fetchFeedbackById(1L);

		assertNotNull(feedbackDTO);
		assertEquals(feedback.getFeedBackId(), feedbackDTO.getFeedBackId());
		assertEquals(reservation.getBusId(), feedbackDTO.getBusId());
		verify(feedBackRepo, times(1)).findById(1L);
		verify(reservationClient, times(1)).getReservation(1L);
	}

	@Test
	void testFetchFeedbackById_NotFound() {
		when(feedBackRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			feedbackService.fetchFeedbackById(1L);
		});

		verify(feedBackRepo, times(1)).findById(1L);
		verify(reservationClient, times(0)).getReservation(anyLong());
	}

	@Test
	void testRemoveFeedBack() throws ResourceNotFoundException {
		when(feedBackRepo.findById(1L)).thenReturn(Optional.of(feedback));

		String result = feedbackService.removeFeedBack(1L);

		assertEquals("Feedback successfully removed", result);
		verify(feedBackRepo, times(1)).findById(1L);
		verify(feedBackRepo, times(1)).deleteById(1L);
	}

	@Test
	void testRemoveFeedBack_NotFound() {
		when(feedBackRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			feedbackService.removeFeedBack(1L);
		});

		verify(feedBackRepo, times(1)).findById(1L);
		verify(feedBackRepo, times(0)).deleteById(anyLong());
	}
	
	@Test
    void testUpdateFeedback() throws ResourceNotFoundException {
        when(feedBackRepo.findById(1L)).thenReturn(Optional.of(feedback));
        when(feedBackRepo.save(any(Feedback.class))).thenReturn(feedback);

        Feedback updatedFeedback = feedbackService.updateFeedback(1L, feedback);

        assertNotNull(updatedFeedback);
        assertEquals(feedback.getFeedBackId(), updatedFeedback.getFeedBackId());
        verify(feedBackRepo, times(1)).findById(1L);
        verify(feedBackRepo, times(1)).save(feedback);
    }

    @Test
    void testUpdateFeedback_NotFound() {
        when(feedBackRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            feedbackService.updateFeedback(1L, feedback);
        });

        verify(feedBackRepo, times(1)).findById(1L);
        verify(feedBackRepo, times(0)).save(any(Feedback.class));
    }

}
