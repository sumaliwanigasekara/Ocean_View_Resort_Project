package com.oceanview.model;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;

public class BillItemTest {
    @Test
    public void gettersSetters_work() {
        BillItem item = new BillItem();
        item.setId(1L);
        item.setDescription("Room");
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("100.00"));

        assertEquals(Long.valueOf(1L), item.getId());
        assertEquals("Room", item.getDescription());
        assertEquals(2, item.getQuantity());
        assertEquals(new BigDecimal("100.00"), item.getUnitPrice());
    }
}
