package com.oceanview.dto;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BillDTOTest {
    @Test
    public void gettersSetters_work() {
        BillDTO dto = new BillDTO();
        dto.setBillId(1L);
        dto.setReservationId(2L);
        dto.setTotalAmount("225.00");

        assertEquals(Long.valueOf(1L), dto.getBillId());
        assertEquals(Long.valueOf(2L), dto.getReservationId());
        assertEquals("225.00", dto.getTotalAmount());
    }
}
