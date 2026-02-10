package com.oceanview.dto;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BillItemDTOTest {
    @Test
    public void gettersSetters_work() {
        BillItemDTO dto = new BillItemDTO();
        dto.setId(1L);
        dto.setDescription("Room");
        dto.setQuantity(2);

        assertEquals(Long.valueOf(1L), dto.getId());
        assertEquals("Room", dto.getDescription());
        assertEquals(2, dto.getQuantity());
    }
}
