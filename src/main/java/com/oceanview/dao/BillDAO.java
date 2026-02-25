package com.oceanview.dao;

import com.oceanview.model.Bill;

import java.math.BigDecimal;

public interface BillDAO {
    Bill findById(Long billId);

    Bill findByReservationId(long reservationId);

    Bill createBill(Bill bill);

    Bill generateBill(long reservationId, BigDecimal serviceCharges, BigDecimal discountAmount);

    java.math.BigDecimal calculateRevenue(java.time.LocalDate start, java.time.LocalDate end);

    java.util.List<Bill> findBetweenDates(java.time.LocalDate start, java.time.LocalDate end);

    java.util.List<Bill> findByStatus(Bill.BillStatus status);

    java.util.List<Bill> findAll();

    boolean markAsPaid(long billId, String paymentMethod);
}
