package com.oceanview.service;

import com.oceanview.dao.impl.BillDAOImpl;
import com.oceanview.model.Bill;
import com.oceanview.service.impl.BillingServiceImpl;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class BillingServiceImplTest {

    @Test(expected = IllegalArgumentException.class)
    public void calculateTotal_missingCharges_throws() {
        BillingServiceImpl service = new BillingServiceImpl(new BillDAOImpl());

        Bill bill = new Bill();
        bill.setRoomCharges(new BigDecimal("100.00"));
        // Missing serviceCharges, taxAmount, discountAmount

        service.calculateTotal(bill);
    }

    @Test
    public void calculateTotal_validCharges_returnsTotal() {
        BillingServiceImpl service = new BillingServiceImpl(new BillDAOImpl());

        Bill bill = new Bill();
        bill.setRoomCharges(new BigDecimal("100.00"));
        bill.setServiceCharges(new BigDecimal("20.00"));
        bill.setTaxAmount(new BigDecimal("12.00"));
        bill.setDiscountAmount(new BigDecimal("10.00"));

        BigDecimal total = service.calculateTotal(bill);

        assertEquals(new BigDecimal("122.00"), total);
    }
}