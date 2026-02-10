package com.oceanview.dto;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ReservationDTOTest {
    @Test
    public void gettersSetters_work() {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(1L);
        dto.setGuestId(2L);
        dto.setRoomId(3L);

        assertEquals(Long.valueOf(1L), dto.getReservationId());
        assertEquals(Long.valueOf(2L), dto.getGuestId());
        assertEquals(Long.valueOf(3L), dto.getRoomId());
    }
}
