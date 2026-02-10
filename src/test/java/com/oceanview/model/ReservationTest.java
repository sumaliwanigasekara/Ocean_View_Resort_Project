package com.oceanview.model;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class ReservationTest {

    @Test
    public void getNumberOfNights_returnsDaysBetweenDates() {
        Reservation reservation = new Reservation();
        reservation.setCheckInDate(LocalDate.of(2026, 2, 10));
        reservation.setCheckOutDate(LocalDate.of(2026, 2, 12));

        assertEquals(2, reservation.getNumberOfNights());
    }


    @Test
    public void getNumberOfNights_nullDates_returnsZero() {
        Reservation reservation = new Reservation();
        assertEquals(0, reservation.getNumberOfNights());
    }
}
