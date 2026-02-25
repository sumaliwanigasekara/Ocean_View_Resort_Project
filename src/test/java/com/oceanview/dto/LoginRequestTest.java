package com.oceanview.dto;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LoginRequestTest {
    @Test
    public void gettersSetters_work() {
        LoginRequest req = new LoginRequest();
        req.setEmail("a@b.com");
        req.setPassword("pw");

        assertEquals("a@b.com", req.getEmail());
        assertEquals("pw", req.getPassword());
    }
}
