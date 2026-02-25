package com.oceanview.model;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;

public class BillTest {
    @Test
    public void gettersSetters_work() {
        Bill bill = new Bill();
        bill.setBillId(5L);
        bill.setReservationId(7L);
        bill.setRoomCharges(new BigDecimal("100.00"));
        bill.setServiceCharges(new BigDecimal("20.00"));
        bill.setTaxAmount(new BigDecimal("10.00"));
        bill.setDiscountAmount(new BigDecimal("5.00"));
        bill.setTotalAmount(new BigDecimal("125.00"));
        bill.setStatus(Bill.BillStatus.PENDING);

        assertEquals(Long.valueOf(5L), bill.getBillId());
        assertEquals(Long.valueOf(7L), bill.getReservationId());
        assertEquals(new BigDecimal("100.00"), bill.getRoomCharges());
        assertEquals(new BigDecimal("20.00"), bill.getServiceCharges());
        assertEquals(new BigDecimal("10.00"), bill.getTaxAmount());
        assertEquals(new BigDecimal("5.00"), bill.getDiscountAmount());
        assertEquals(new BigDecimal("125.00"), bill.getTotalAmount());
        assertEquals(Bill.BillStatus.PENDING, bill.getStatus());
    }
}
