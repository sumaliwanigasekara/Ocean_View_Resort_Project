package com.oceanview.dto;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginResponseTest {

    @Test
    public void constructor_and_setters_work() {
        LoginResponse res = new LoginResponse(true, "ok", 1L, "Asha", "MANAGER");
        assertTrue(res.isSuccess());
        assertEquals("ok", res.getMessage());
        assertEquals(Long.valueOf(1L), res.getUserId());
        assertEquals("Asha", res.getUserName());
        assertEquals("MANAGER", res.getRole());

        res.setSuccess(false);
        res.setMessage("fail");
        res.setUserId(2L);
        res.setUserName("Nimal");
        res.setRole("RECEPTIONIST");

        assertFalse(res.isSuccess());
        assertEquals("fail", res.getMessage());
        assertEquals(Long.valueOf(2L), res.getUserId());
        assertEquals("Nimal", res.getUserName());
        assertEquals("RECEPTIONIST", res.getRole());
    }
}