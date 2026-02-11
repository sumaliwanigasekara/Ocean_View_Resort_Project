package com.oceanview.service.impl;

import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;
import com.oceanview.service.RoomService;
import com.oceanview.util.ValidationUtil;

import java.util.List;
import java.util.Objects;

public class RoomServiceImpl implements RoomService {
    private final RoomDAO roomDAO;

    public RoomServiceImpl(RoomDAO roomDAO) {
        this.roomDAO = Objects.requireNonNull(roomDAO, "roomDAO");
    }

    @Override
    public Room createRoom(Room room) {
        ValidationUtil.requireNonNull(room, "Room");
        ValidationUtil.requireNonNull(room.getRoomType(), "Room type");
        ValidationUtil.requirePositive(room.getRatePerNight(), "Rate per night");
        ValidationUtil.requirePositive(room.getMaxOccupancy(), "Max occupancy");
        if (room.getRoomStatus() == null) {
            room.setRoomStatus(Room.RoomStatus.AVAILABLE);
        }
        return roomDAO.save(room);
    }

    @Override
    public Room getRoom(Long roomId) {
        ValidationUtil.requirePositive(roomId, "Room id");
        Room room = roomDAO.findById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Room not found.");
        }
        return room;
    }

    @Override
    public List<Room> listRooms(String status) {
        return roomDAO.list(status);
    }

    @Override
    public void updateStatus(Long roomId, Room.RoomStatus status) {
        ValidationUtil.requirePositive(roomId, "Room id");
        ValidationUtil.requireNonNull(status, "Room status");
        roomDAO.updateStatus(roomId, status);
    }
}
