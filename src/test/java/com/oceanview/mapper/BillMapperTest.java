package com.oceanview.mapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BillMapperTest {
    @Test
    public void ping_returnsOk() {
        BillMapper mapper = new BillMapper();
        assertEquals("ok", mapper.ping());
    }
}
