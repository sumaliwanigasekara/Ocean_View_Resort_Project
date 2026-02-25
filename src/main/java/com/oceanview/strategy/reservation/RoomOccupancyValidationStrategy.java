package com.oceanview.strategy.reservation;

import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;

public class RoomOccupancyValidationStrategy implements ReservationValidationStrategy {
    @Override
    public void validate(Reservation reservation, ReservationValidationContext context) {
        RoomDAO roomDAO = context.getRoomDAO();
        if (roomDAO == null) {
            return;
        }

        Room room = roomDAO.findById(reservation.getRoomId());
        if (room == null) {
            throw new IllegalArgumentException("Room not found.");
        }
        context.setRoom(room);

        if (reservation.getNumberOfGuests() > room.getMaxOccupancy()) {
            throw new IllegalArgumentException("Number of guests exceeds room max occupancy.");
        }
    }
}
