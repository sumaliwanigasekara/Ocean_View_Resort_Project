package com.oceanview.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Room {
    private Long roomId;
    private RoomType roomType;
    private BigDecimal ratePerNight;
    private int maxOccupancy;
    private RoomStatus roomStatus;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public enum RoomType {
        SINGLE, DOUBLE, TWIN, SUITE, DELUXE
    }

    public enum RoomStatus {
        AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED
    }

    public Room() {}

    public Room(Long roomId, RoomType roomType, BigDecimal ratePerNight, RoomStatus roomStatus, int maxOccupancy) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.ratePerNight = ratePerNight;
        this.roomStatus = roomStatus;
        this.maxOccupancy = maxOccupancy;
    }

    // Getters and Setters
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }

    public BigDecimal getRatePerNight() { return ratePerNight; }
    public void setRatePerNight(BigDecimal ratePerNight) { this.ratePerNight = ratePerNight; }

    public int getMaxOccupancy() { return maxOccupancy; }
    public void setMaxOccupancy(int maxOccupancy) { this.maxOccupancy = maxOccupancy; }

    public RoomStatus getRoomStatus() { return roomStatus; }
    public void setRoomStatus(RoomStatus status) { this.roomStatus = status; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getUpdated_at() { return updated_at; }
    public void setUpdated_at(LocalDateTime updated_at) { this.updated_at = updated_at; }
}
