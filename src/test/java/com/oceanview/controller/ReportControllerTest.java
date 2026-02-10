package com.oceanview.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReportControllerTest {

    @Test
    public void ping_returnsOk() {
        ReportController controller = new ReportController();

        assertEquals("ok", controller.ping());
    }
}
