
package com.oceanview.service.impl;

import com.oceanview.model.Bill;
import com.oceanview.service.BillingService;

import java.math.BigDecimal;

public class BillingServiceImpl implements BillingService {
    @Override
    public BigDecimal calculateTotal(Bill bill) {
        if (bill == null || bill.getRoomCharges() == null || bill.getServiceCharges() == null
                || bill.getTaxAmount() == null || bill.getDiscountAmount() == null) {
            throw new IllegalArgumentException("All bill charges are required.");
        }

        return bill.getRoomCharges()
                .add(bill.getServiceCharges())
                .add(bill.getTaxAmount())
                .subtract(bill.getDiscountAmount());
    }
}