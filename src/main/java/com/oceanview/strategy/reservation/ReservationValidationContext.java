package com.oceanview.strategy.reservation;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;

import java.util.Objects;

public class ReservationValidationContext {
    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;
    private Room room;

    public ReservationValidationContext(ReservationDAO reservationDAO, RoomDAO roomDAO) {
        this.reservationDAO = Objects.requireNonNull(reservationDAO, "reservationDAO");
        this.roomDAO = roomDAO;
    }

    public ReservationDAO getReservationDAO() {
        return reservationDAO;
    }

    public RoomDAO getRoomDAO() {
        return roomDAO;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
