
package com.oceanview.controller;

import com.oceanview.model.Bill;
import com.oceanview.service.BillingService;

import java.math.BigDecimal;
import java.util.Objects;

public class BillingController {
    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = Objects.requireNonNull(billingService, "billingService");
    }

    public BigDecimal calculateTotal(Bill bill) {
        if (bill == null) {
            throw new IllegalArgumentException("Bill is required.");
        }
        return billingService.calculateTotal(bill);
    }
}
