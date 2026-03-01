package com.oceanview.service.email;

import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;

public final class EmailTemplateBuilder {
    private EmailTemplateBuilder() {
    }

    public static EmailMessage guestCreated(Guest guest) {
        if (guest == null || isBlank(guest.getGuestEmail())) {
            return null;
        }

        String subject = "Welcome to Ocean View Resort";
        String body = String.format(
                "Hello %s,%n%nYour guest profile has been registered successfully.%nGuest ID: %s%n%nThank you,%nOcean View Resort",
                safe(guest.getGuestName()),
                safe(guest.getGuestId()));
        return new EmailMessage(guest.getGuestEmail(), subject, body);
    }

    public static EmailMessage reservationCreated(Reservation reservation, Guest guest, Room room) {
        if (guest == null || isBlank(guest.getGuestEmail()) || reservation == null) {
            return null;
        }

        String subject = "Reservation Confirmation - Ocean View Resort";
        String body = String.format(
                "Hello %s,%n%nYour reservation is confirmed.%nReservation ID: %s%nRoom: %s%nCheck-in: %s%nCheck-out: %s%nStatus: %s%n%nThank you,%nOcean View Resort",
                safe(guest.getGuestName()),
                safe(reservation.getReservationId()),
                room == null ? "-" : safe(room.getRoomType()),
                safe(reservation.getCheckInDate()),
                safe(reservation.getCheckOutDate()),
                safe(reservation.getStatus()));

        return new EmailMessage(guest.getGuestEmail(), subject, body);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static String safe(Object value) {
        return value == null ? "-" : value.toString();
    }
}
