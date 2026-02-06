
package com.oceanview.dao;

import com.oceanview.model.Reservation;

public interface ReservationDAO {
    Reservation save(Reservation reservation);
    Reservation findById(Long reservationId);
}