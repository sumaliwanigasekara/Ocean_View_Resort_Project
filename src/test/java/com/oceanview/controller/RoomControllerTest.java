package com.oceanview.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoomControllerTest {

    @Test
    public void ping_returnsOk() {
        RoomController controller = new RoomController();

        assertEquals("ok", controller.ping());
    }
}
