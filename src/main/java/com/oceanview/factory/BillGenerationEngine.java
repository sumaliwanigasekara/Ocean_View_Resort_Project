package com.oceanview.factory;

import com.oceanview.model.Bill;

import java.math.BigDecimal;

public interface BillGenerationEngine {
    Bill generate(long reservationId, BigDecimal serviceCharges, BigDecimal discountAmount);
}
