package com.oceanview.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class UserMapperTest {
    @Test
    public void ping_returnsOk() {
        UserMapper mapper = new UserMapper();
        Assertions.assertEquals("ok", mapper.ping());
    }
}
