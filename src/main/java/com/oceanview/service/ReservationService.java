
package com.oceanview.service;

import com.oceanview.model.Reservation;

public interface ReservationService {
    Reservation addReservation(Reservation reservation);
    Reservation getReservationDetails(Long reservationId);
}