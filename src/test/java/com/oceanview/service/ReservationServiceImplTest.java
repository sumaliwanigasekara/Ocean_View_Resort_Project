package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import com.oceanview.service.impl.ReservationServiceImpl;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ReservationServiceImplTest {

    @Test(expected = IllegalArgumentException.class)
    public void addReservation_missingDates_throws() {
        ReservationServiceImpl service = new ReservationServiceImpl(new FakeReservationDAO());
        Reservation reservation = new Reservation();
        reservation.setGuestId(1L);
        reservation.setRoomId(10L);
        reservation.setNumberOfGuests(2);

        service.addReservation(reservation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addReservation_checkoutBeforeCheckin_throws() {
        ReservationServiceImpl service = new ReservationServiceImpl(new FakeReservationDAO());
        Reservation reservation = new Reservation();
        reservation.setGuestId(1L);
        reservation.setRoomId(10L);
        reservation.setNumberOfGuests(2);
        reservation.setCheckInDate(LocalDate.of(2026, 2, 10));
        reservation.setCheckOutDate(LocalDate.of(2026, 2, 9));

        service.addReservation(reservation);
    }

    @Test
    public void addReservation_validReservation_setsPendingAndReturns() {
        ReservationServiceImpl service = new ReservationServiceImpl(new FakeReservationDAO());
        Reservation reservation = new Reservation();
        reservation.setGuestId(1L);
        reservation.setRoomId(10L);
        reservation.setNumberOfGuests(2);
        reservation.setCheckInDate(LocalDate.of(2026, 2, 10));
        reservation.setCheckOutDate(LocalDate.of(2026, 2, 12));

        Reservation saved = service.addReservation(reservation);

        assertEquals(Reservation.ReservationStatus.PENDING, saved.getStatus());
        assertEquals(Long.valueOf(100L), saved.getReservationId());
    }

    @Test
    public void getReservationDetails_existing_returnsReservation() {
        FakeReservationDAO dao = new FakeReservationDAO();
        ReservationServiceImpl service = new ReservationServiceImpl(dao);
        Reservation reservation = new Reservation();
        reservation.setReservationId(200L);
        dao.setReservation(reservation);

        Reservation result = service.getReservationDetails(200L);

        assertSame(reservation, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReservationDetails_missing_throws() {
        ReservationServiceImpl service = new ReservationServiceImpl(new FakeReservationDAO());

        service.getReservationDetails(999L);
    }

    private static class FakeReservationDAO implements ReservationDAO {
        private Reservation reservation;

        void setReservation(Reservation reservation) {
            this.reservation = reservation;
        }

        @Override
        public Reservation save(Reservation reservation) {
            reservation.setReservationId(100L);
            return reservation;
        }

        @Override
        public Reservation findById(Long reservationId) {
            if (reservation != null && reservationId.equals(reservation.getReservationId())) {
                return reservation;
            }
            return null;
        }
    }
}