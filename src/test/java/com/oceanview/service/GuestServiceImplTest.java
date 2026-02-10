
package com.oceanview.service;

import com.oceanview.service.impl.GuestServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GuestServiceImplTest {

    @Test
    public void ping_returnsOk() {
        GuestServiceImpl service = new GuestServiceImpl();

        assertEquals("ok", service.ping());
    }
}