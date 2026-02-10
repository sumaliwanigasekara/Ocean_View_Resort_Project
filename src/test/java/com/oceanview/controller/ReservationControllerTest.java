
package com.oceanview.controller;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertSame;

public class ReservationControllerTest {

    @Test(expected = IllegalArgumentException.class)
    public void addReservation_null_throws() {
        ReservationController controller = new ReservationController(new FakeReservationService());

        controller.addReservation(null);
    }

    @Test
    public void addReservation_valid_delegates() {
        ReservationController controller = new ReservationController(new FakeReservationService());
        Reservation reservation = new Reservation();
        reservation.setGuestId(1L);
        reservation.setRoomId(10L);
        reservation.setNumberOfGuests(2);
        reservation.setCheckInDate(LocalDate.of(2026, 2, 10));
        reservation.setCheckOutDate(LocalDate.of(2026, 2, 12));

        Reservation result = controller.addReservation(reservation);

        assertSame(reservation, result);
    }

    private static class FakeReservationService implements ReservationService {
        @Override
        public Reservation addReservation(Reservation reservation) {
            return reservation;
        }

        @Override
        public Reservation getReservationDetails(Long reservationId) {
            Reservation reservation = new Reservation();
            reservation.setReservationId(reservationId);
            return reservation;
        }
    }
}