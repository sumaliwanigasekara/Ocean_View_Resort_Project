package com.oceanview.dto;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GuestDTOTest {
    @Test
    public void gettersSetters_work() {
        GuestDTO dto = new GuestDTO();
        dto.setGuestId(1L);
        dto.setGuestName("Test");
        dto.setGuestEmail("g@test.com");

        assertEquals(Long.valueOf(1L), dto.getGuestId());
        assertEquals("Test", dto.getGuestName());
        assertEquals("g@test.com", dto.getGuestEmail());
    }
}
