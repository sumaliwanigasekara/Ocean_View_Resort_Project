package com.oceanview.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class GuestMapperTest {

    @Test
    public void ping_returnsOk() {
        GuestMapper mapper = new GuestMapper();
        Assertions.assertEquals("ok", mapper.ping());
    }
}
