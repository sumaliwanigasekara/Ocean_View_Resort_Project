package com.oceanview.service;

import com.oceanview.model.Room;

import java.util.List;

public interface RoomService {
    Room createRoom(Room room);

    Room getRoom(Long roomId);

    List<Room> listRooms(String status);

    void updateStatus(Long roomId, Room.RoomStatus status);
}
