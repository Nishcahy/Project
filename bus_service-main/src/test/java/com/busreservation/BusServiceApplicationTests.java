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

import com.busreservation.dto.Reservation;
import com.busreservation.entity.Bus;
import com.busreservation.exception.ResourceNotFoundException;
import com.busreservation.repository.BusRepo;
import com.busreservation.service.BusServiceImpl;
import com.busreservation.service.client.ReservationClient;

@SpringBootTest
class BusServiceApplicationTests {

	@Mock
    private BusRepo busRepo;

    @Mock
    private ReservationClient reservationClient;

    @InjectMocks
    private BusServiceImpl busService;

    private Bus bus;
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        bus = new Bus();
        bus.setBusId(1L);
        bus.setBusNo("1234");
        bus.setRouteFrom("CityA");
        bus.setRouteTo("CityB");
        bus.setSeats(40);
        bus.setDepartureTime("10:00");
        bus.setPrice(500);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setBusId(1L);
        reservation.setDate(LocalDate.parse("2025-02-04"));
    }

    @Test
    void testAddBus() throws ResourceNotFoundException {
        when(busRepo.save(any(Bus.class))).thenReturn(bus);

        Bus savedBus = busService.addBus(bus);

        assertNotNull(savedBus);
        assertEquals(bus.getBusId(), savedBus.getBusId());
        verify(busRepo, times(1)).save(bus);
    }

    @Test
    void testFetchAllBus() {
        List<Bus> buses = Arrays.asList(bus);
        when(busRepo.findAll()).thenReturn(buses);

        List<Bus> fetchedBuses = busService.fetchAllBus();

        assertNotNull(fetchedBuses);
        assertEquals(1, fetchedBuses.size());
        verify(busRepo, times(1)).findAll();
    }

    @Test
    void testFindBusById() throws ResourceNotFoundException {
        when(busRepo.findById(1L)).thenReturn(Optional.of(bus));

        Bus foundBus = busService.findBusById(1L);

        assertNotNull(foundBus);
        assertEquals(bus.getBusId(), foundBus.getBusId());
        verify(busRepo, times(1)).findById(1L);
    }

    @Test
    void testFindBusById_NotFound() {
        when(busRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            busService.findBusById(1L);
        });

        verify(busRepo, times(1)).findById(1L);
    }

    @Test
    void testUpdateBus() throws ResourceNotFoundException {
        when(busRepo.findById(1L)).thenReturn(Optional.of(bus));
        when(busRepo.save(any(Bus.class))).thenReturn(bus);

        Bus updatedBus = busService.updateBus(1L, bus);

        assertNotNull(updatedBus);
        assertEquals(bus.getBusId(), updatedBus.getBusId());
        verify(busRepo, times(1)).findById(1L);
        verify(busRepo, times(1)).save(bus);
    }

    @Test
    void testUpdateBus_NotFound() {
        when(busRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            busService.updateBus(1L, bus);
        });

        verify(busRepo, times(1)).findById(1L);
        verify(busRepo, times(0)).save(any(Bus.class));
    }

    @Test
    void testDeleteBus() throws ResourceNotFoundException {
        when(busRepo.findById(1L)).thenReturn(Optional.of(bus));

        busService.deleteBus(1L);

        verify(busRepo, times(1)).findById(1L);
        verify(busRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBus_NotFound() {
        when(busRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            busService.deleteBus(1L);
        });

        verify(busRepo, times(1)).findById(1L);
        verify(busRepo, times(0)).deleteById(anyLong());
    }

    @Test
    void testGetReservations() {
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationClient.getAllReservation()).thenReturn(reservations);

        List<Reservation> fetchedReservations = busService.getReservations();

        assertNotNull(fetchedReservations);
        assertEquals(1, fetchedReservations.size());
        verify(reservationClient, times(1)).getAllReservation();
    }

}
