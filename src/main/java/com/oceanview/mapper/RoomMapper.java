package com.oceanview.mapper;

import com.oceanview.dto.RoomDTO;
import com.oceanview.model.Room;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RoomMapper {
    public RoomDTO toDTO(Room room) {
        if (room == null) {
            return null;
        }
        RoomDTO dto = new RoomDTO();
        dto.setRoomId(room.getRoomId());
        dto.setRoomType(room.getRoomType());
        dto.setRatePerNight(room.getRatePerNight());
        dto.setMaxOccupancy(room.getMaxOccupancy());
        dto.setRoomStatus(room.getRoomStatus());
        dto.setCreated_at(room.getCreated_at());
        dto.setUpdated_at(room.getUpdated_at());
        return dto;
    }

    public Room toEntity(RoomDTO dto) {
        if (dto == null) {
            return null;
        }
        Room room = new Room();
        room.setRoomId(dto.getRoomId());
        room.setRoomType(dto.getRoomType());
        room.setRatePerNight(dto.getRatePerNight());
        room.setMaxOccupancy(dto.getMaxOccupancy());
        room.setRoomStatus(dto.getRoomStatus());
        room.setCreated_at(dto.getCreated_at());
        room.setUpdated_at(dto.getUpdated_at());
        return room;
    }

    public List<RoomDTO> toDTOList(List<Room> rooms) {
        if (rooms == null) {
            return Collections.emptyList();
        }
        return rooms.stream().filter(Objects::nonNull).map(this::toDTO).collect(Collectors.toList());
    }

    public List<Room> toEntityList(List<RoomDTO> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream().filter(Objects::nonNull).map(this::toEntity).collect(Collectors.toList());
    }
}
