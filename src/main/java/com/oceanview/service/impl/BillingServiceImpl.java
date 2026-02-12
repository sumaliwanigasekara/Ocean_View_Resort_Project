package com.oceanview.service.impl;

import com.oceanview.dao.BillDAO;
import com.oceanview.model.Bill;
import com.oceanview.service.BillingService;
import com.oceanview.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.Objects;

public class BillingServiceImpl implements BillingService {
    private final BillDAO billDAO;

    public BillingServiceImpl(BillDAO billDAO) {
        this.billDAO = Objects.requireNonNull(billDAO, "billDAO");
    }

    public BillingServiceImpl() {
        this.billDAO = null;
    }

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

    @Override
    public Bill generateBill(long reservationId, BigDecimal serviceCharges, BigDecimal discountAmount) {
        ValidationUtil.requirePositive(reservationId, "Reservation id");
        ValidationUtil.requireNonNull(serviceCharges, "Service charges");
        ValidationUtil.requireNonNull(discountAmount, "Discount amount");
        return billDAO.generateBill(reservationId, serviceCharges, discountAmount);
    }

    @Override
    public Bill findBillByReservation(long reservationId) {
        ValidationUtil.requirePositive(reservationId, "Reservation id");
        return billDAO.findByReservationId(reservationId);
    }
}