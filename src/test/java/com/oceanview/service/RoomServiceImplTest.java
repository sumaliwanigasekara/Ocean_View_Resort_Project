package com.oceanview.service;

import com.oceanview.service.impl.RoomServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoomServiceImplTest {

    @Test
    public void ping_returnsOk() {
        RoomServiceImpl service = new RoomServiceImpl();

        assertEquals("ok", service.ping());
    }
}
