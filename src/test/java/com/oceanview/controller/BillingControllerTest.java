
package com.oceanview.controller;

import com.oceanview.model.Bill;
import com.oceanview.service.BillingService;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class BillingControllerTest {

    @Test(expected = IllegalArgumentException.class)
    public void calculateTotal_nullBill_throws() {
        BillingController controller = new BillingController(new FakeBillingService());

        controller.calculateTotal(null);
    }

    @Test
    public void calculateTotal_validBill_delegates() {
        BillingController controller = new BillingController(new FakeBillingService());
        Bill bill = new Bill();

        BigDecimal total = controller.calculateTotal(bill);

        assertEquals(new BigDecimal("122.00"), total);
    }

    private static class FakeBillingService implements BillingService {
        @Override
        public BigDecimal calculateTotal(Bill bill) {
            return new BigDecimal("122.00");
        }
    }
}