package com.oceanview.dao;

import com.oceanview.model.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RoomDAO {
    Room findById(Long roomId);

    Room save(Room room);

    List<Room> list(String status);

    void updateStatus(long roomId, Room.RoomStatus status);

    long count();

    int countByStatus(Room.RoomStatus status);

    java.util.List<Room> findByRoomType(Room.RoomType type);

    Map<String, Object> getRoomTypeSummary(Room.RoomType type, LocalDate date);


}
