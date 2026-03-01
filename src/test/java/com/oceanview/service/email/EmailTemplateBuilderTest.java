package com.oceanview.service.email;

import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EmailTemplateBuilderTest {

    @Test
    public void guestCreated_buildsMessage() {
        Guest guest = new Guest();
        guest.setGuestId(7L);
        guest.setGuestName("Asha");
        guest.setGuestEmail("asha@example.com");

        EmailMessage message = EmailTemplateBuilder.guestCreated(guest);

        assertNotNull(message);
        assertEquals("asha@example.com", message.getTo());
        assertTrue(message.getSubject().contains("Welcome"));
        assertTrue(message.getBody().contains("Guest ID: 7"));
    }

    @Test
    public void reservationCreated_buildsMessage() {
        Guest guest = new Guest();
        guest.setGuestName("Asha");
        guest.setGuestEmail("asha@example.com");

        Reservation reservation = new Reservation();
        reservation.setReservationId(99L);
        reservation.setCheckInDate(LocalDate.of(2026, 2, 20));
        reservation.setCheckOutDate(LocalDate.of(2026, 2, 22));
        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);

        Room room = new Room();
        room.setRoomType(Room.RoomType.SINGLE);

        EmailMessage message = EmailTemplateBuilder.reservationCreated(reservation, guest, room);

        assertNotNull(message);
        assertEquals("asha@example.com", message.getTo());
        assertTrue(message.getSubject().contains("Reservation Confirmation"));
        assertTrue(message.getBody().contains("Reservation ID: 99"));
        assertTrue(message.getBody().contains("Room: SINGLE"));
    }
}
