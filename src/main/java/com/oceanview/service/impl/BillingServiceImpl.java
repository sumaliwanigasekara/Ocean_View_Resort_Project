package com.oceanview.service.impl;

import com.oceanview.dao.BillDAO;
import com.oceanview.model.Bill;
import com.oceanview.service.BillingService;
import com.oceanview.util.ValidationUtil;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.factory.BillGenerationEngine;
import com.oceanview.factory.BillGenerationEngineFactory;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.List;

public class BillingServiceImpl implements BillingService {
    private final BillDAO billDAO;
    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;
    private final BillGenerationEngine generationEngine;

    public BillingServiceImpl(BillDAO billDAO) {
        this(billDAO, new ReservationDAOImpl(), new RoomDAOImpl());
    }

    public BillingServiceImpl(BillDAO billDAO, ReservationDAO reservationDAO, RoomDAO roomDAO) {
        this.billDAO = Objects.requireNonNull(billDAO, "billDAO");
        this.reservationDAO = Objects.requireNonNull(reservationDAO, "reservationDAO");
        this.roomDAO = Objects.requireNonNull(roomDAO, "roomDAO");
        this.generationEngine = BillGenerationEngineFactory.create(
                BillGenerationEngineFactory.resolveMode(),
                this.billDAO,
                this.reservationDAO,
                this.roomDAO
        );
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
        return generationEngine.generate(reservationId, serviceCharges, discountAmount);
    }

    @Override
    public Bill findBillByReservation(long reservationId) {
        ValidationUtil.requirePositive(reservationId, "Reservation id");
        return billDAO.findByReservationId(reservationId);
    }

    @Override
    public List<Bill> listBills() {
        return billDAO.findAll();
    }

    @Override
    public List<Bill> listBillsByStatus(Bill.BillStatus status) {
        ValidationUtil.requireNonNull(status, "Bill status");
        return billDAO.findByStatus(status);
    }

    @Override
    public boolean markBillAsPaid(long billId, String paymentMethod) {
        ValidationUtil.requirePositive(billId, "Bill id");
        ValidationUtil.requireNonBlank(paymentMethod, "Payment method");
        return billDAO.markAsPaid(billId, paymentMethod);
    }
}
