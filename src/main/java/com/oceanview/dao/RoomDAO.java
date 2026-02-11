package com.oceanview.dao;

import com.oceanview.model.Room;

import java.util.List;

public interface RoomDAO {
    Room findById(Long roomId);

    Room save(Room room);

    List<Room> list(String status);

    void updateStatus(long roomId, Room.RoomStatus status);

    long count();

    int countByStatus(Room.RoomStatus status);

    java.util.List<Room> findByRoomType(Room.RoomType type);
}
