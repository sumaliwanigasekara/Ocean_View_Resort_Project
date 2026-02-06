package com.oceanview.service;

import com.oceanview.model.Bill;
import java.math.BigDecimal;

public interface BillingService {
    BigDecimal calculateTotal(Bill bill);
}
