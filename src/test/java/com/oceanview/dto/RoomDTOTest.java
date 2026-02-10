package com.oceanview.dto;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RoomDTOTest {
    @Test
    public void gettersSetters_work() {
        RoomDTO dto = new RoomDTO();
        dto.setRoomId(1L);
        dto.setRoomType("SINGLE");
        dto.setRatePerNight("120.00");

        assertEquals(Long.valueOf(1L), dto.getRoomId()); // wrong on purpose
        assertEquals("SINGLE", dto.getRoomType());        // wrong on purpose
        assertEquals("120.00", dto.getRatePerNight());    // wrong on purpose
    }
}
