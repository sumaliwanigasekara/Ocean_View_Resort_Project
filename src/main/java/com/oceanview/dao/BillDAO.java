package com.oceanview.dao;

import com.oceanview.model.Bill;

import java.math.BigDecimal;

public interface BillDAO {
    Bill findById(Long billId);

    Bill findByReservationId(long reservationId);

    Bill createBill(Bill bill);

    Bill generateBill(long reservationId, BigDecimal serviceCharges, BigDecimal discountAmount);
}
