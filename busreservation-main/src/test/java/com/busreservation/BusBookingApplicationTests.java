package com.busreservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.busreservation.dto.Bus;
import com.busreservation.dto.ReservationDTO;
import com.busreservation.entity.Reservation;
import com.busreservation.exception.ResourceNotFoundException;
import com.busreservation.repo.ReservationRepo;
import com.busreservation.service.BusReservationServiceImpl;
import com.busreservation.service.client.BusClient;

@SpringBootTest
class BusBookingApplicationTests {

	@Mock
	private ReservationRepo reservationRepository;

	@Mock
	private BusClient busClient;

	@InjectMocks
	private BusReservationServiceImpl busReservationService;

	private Reservation reservation;
	private Bus bus;

	@BeforeEach
	public void setUp() {
		reservation = new Reservation();
		reservation.setId(1L);
		reservation.setBusId(1L);
		reservation.setDate(LocalDate.now().plusDays(1));
		reservation.setNumberOfSeats(2);
		reservation.setTotalAmount(1000.0);
		reservation.setUserId(1L);

		bus = new Bus();
		bus.setBusId(1L);
		bus.setBusNo("1234");
		bus.setRouteFrom("CityA");
		bus.setRouteTo("CityB");
		bus.setSeats(40);
		bus.setDepartureTime("10:00 AM");
		bus.setPrice(500);
	}

	@Test
	void testCreateReservation() {
		when(busClient.fetchBus(anyLong())).thenReturn(bus);
		when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

		ReservationDTO reservationDTO = busReservationService.createReservation(reservation);

		assertNotNull(reservationDTO);
		assertEquals(bus.getBusId(), reservationDTO.getBus().getBusId());
		assertEquals(reservation.getId(), reservationDTO.getReservation().getId());
		verify(busClient, times(1)).fetchBus(anyLong());
		verify(reservationRepository, times(1)).save(any(Reservation.class));
	}

	@Test
	void testCreateReservation_BusNotFound() {
		when(busClient.fetchBus(anyLong()))
				.thenThrow(new ResourceNotFoundException("Bus not found with ID: " + reservation.getBusId()));

		assertThrows(ResourceNotFoundException.class, () -> {
			busReservationService.createReservation(reservation);
		});

		verify(busClient, times(1)).fetchBus(anyLong());
		verify(reservationRepository, times(0)).save(any(Reservation.class));
	}

	@Test
	void testGetReservationsByUser() {
		List<Reservation> reservations = Arrays.asList(reservation);
		when(reservationRepository.findByUserId(anyLong())).thenReturn(reservations);

		List<Reservation> fetchedReservations = busReservationService.getReservationsByUser(1L);

		assertNotNull(fetchedReservations);
		assertEquals(1, fetchedReservations.size());
		verify(reservationRepository, times(1)).findByUserId(anyLong());
	}

	@Test
	void testFindBusByFromAndToDestination() {
		List<Bus> buses = Arrays.asList(bus);
		when(busClient.fetchAllBus()).thenReturn(buses);

		List<Bus> foundBuses = busReservationService.findBusByFromAndToDestination("CityA", "CityB");

		assertNotNull(foundBuses);
		assertEquals(1, foundBuses.size());
		verify(busClient, times(1)).fetchAllBus();
	}

	@Test
	void testFindBusByFromAndToDestination_NotFound() {
		when(busClient.fetchAllBus()).thenReturn(Arrays.asList());

		assertThrows(ResourceNotFoundException.class, () -> {
			busReservationService.findBusByFromAndToDestination("CityA", "CityB");
		});

		verify(busClient, times(1)).fetchAllBus();
	}

	@Test
	void testDeleteReservation() {
		when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

		String result = busReservationService.deleteReservation(1L);

		assertEquals("Reservation deleted", result);
		verify(reservationRepository, times(1)).findById(anyLong());
		verify(reservationRepository, times(1)).deleteById(anyLong());
	}

	@Test
	void testDeleteReservation_NotFound() {
		when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			busReservationService.deleteReservation(1L);
		});

		verify(reservationRepository, times(1)).findById(anyLong());
		verify(reservationRepository, times(0)).deleteById(anyLong());
	}

	@Test
	void testGetAllReservation() {
		List<Reservation> reservations = Arrays.asList(reservation);
		when(reservationRepository.findAll()).thenReturn(reservations);

		List<Reservation> fetchedReservations = busReservationService.getAllReservation();

		assertNotNull(fetchedReservations);
		assertEquals(1, fetchedReservations.size());
		verify(reservationRepository, times(1)).findAll();
	}

	@Test
	void testFindById() {
		when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

		Reservation foundReservation = busReservationService.findById(1L);

		assertNotNull(foundReservation);
		assertEquals(reservation.getId(), foundReservation.getId());
		verify(reservationRepository, times(1)).findById(anyLong());
	}

	@Test
	void testFindById_NotFound() {
		when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			busReservationService.findById(1L);
		});

		verify(reservationRepository, times(1)).findById(anyLong());
	}
}
