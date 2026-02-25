package com.oceanview.model;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;

public class RoomTest {
    @Test
    public void gettersSetters_work() {
        Room room = new Room();
        room.setRoomId(10L);
        room.setRoomType(Room.RoomType.SINGLE);
        room.setRatePerNight(new BigDecimal("120.00"));
        room.setMaxOccupancy(2);
        room.setRoomStatus(Room.RoomStatus.AVAILABLE);

        assertEquals(Long.valueOf(10L), room.getRoomId());
        assertEquals(Room.RoomType.SINGLE, room.getRoomType());
        assertEquals(new BigDecimal("120.00"), room.getRatePerNight());
        assertEquals(2, room.getMaxOccupancy());
        assertEquals(Room.RoomStatus.AVAILABLE, room.getRoomStatus());
    }
}
