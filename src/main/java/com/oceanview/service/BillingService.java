package com.oceanview.service;

import com.oceanview.model.Bill;

import java.math.BigDecimal;

public interface BillingService {
    BigDecimal calculateTotal(Bill bill);
    
    default Bill generateBill(long reservationId, BigDecimal serviceCharges, BigDecimal discountAmount) {
        return null;
    }

    default Bill findBillByReservation(long reservationId) {
        return null;
    }
}
