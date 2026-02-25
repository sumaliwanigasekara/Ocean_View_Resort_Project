package com.oceanview.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GuestTest {
    @Test
    public void gettersSetters_work() {
        Guest g = new Guest();
        g.setGuestId(1L);
        g.setGuestName("Test");
        g.setGuestEmail("g@test.com");
        g.setGuestContact("0770000000");

        assertEquals(Long.valueOf(1L), g.getGuestId());
        assertEquals("Test", g.getGuestName());
        assertEquals("g@test.com", g.getGuestEmail());
        assertEquals("0770000000", g.getGuestContact());
    }
}
