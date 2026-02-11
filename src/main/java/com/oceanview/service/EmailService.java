package com.oceanview.service;

import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;

public interface EmailService {
    void sendReservationAlert(Reservation reservation);
    void sendGuestRegisteredAlert(Guest guest);
}
