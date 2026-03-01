package com.oceanview.dto;

import com.oceanview.model.Room;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RoomDTO {
    private Long roomId;
    private Room.RoomType roomType;
    private BigDecimal ratePerNight;
    private int maxOccupancy;
    private Room.RoomStatus roomStatus;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public RoomDTO() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Room.RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(Room.RoomType roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public Room.RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Room.RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
