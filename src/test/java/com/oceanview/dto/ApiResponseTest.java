package com.oceanview.dto;

import org.junit.Test;
import static org.junit.Assert.*;

public class ApiResponseTest {
    @Test
    public void gettersSetters_work() {
        ApiResponse res = new ApiResponse();
        res.setSuccess(true);
        res.setMessage("ok");

        assertTrue(res.isSuccess());
        assertEquals("ok", res.getMessage());
    }
}