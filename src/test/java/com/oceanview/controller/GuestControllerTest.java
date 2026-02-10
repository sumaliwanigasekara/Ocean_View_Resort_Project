
package com.oceanview.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GuestControllerTest {

    @Test
    public void ping_returnsOk() {
        GuestController controller = new GuestController();

        assertEquals("ok", controller.ping());
    }
}