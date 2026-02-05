package com.oceanview.resort.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private Long billId;
    private Long reservationId;
    private BigDecimal roomCharges;
    private BigDecimal serviceCharges;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BillStatus status;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // Transient fields
    private Reservation reservation;
    private List<BillItem> billItems = new ArrayList<>();

    public enum BillStatus {
        PENDING, PAID, PARTIALLY_PAID, CANCELLED, REFUNDED
    }

    public Bill() {}

    public Bill(Long billId, Long reservationId, BigDecimal totalAmount, BillStatus status) {
        this.billId = billId;
        this.reservationId = reservationId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public BigDecimal getRoomCharges() { return roomCharges; }
    public void setRoomCharges(BigDecimal roomCharges) { this.roomCharges = roomCharges; }

    public BigDecimal getServiceCharges() { return serviceCharges; }
    public void setServiceCharges(BigDecimal serviceCharges) { this.serviceCharges = serviceCharges; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getUpdated_at() { return updated_at; }
    public void setUpdated_at(LocalDateTime updated_at) { this.updated_at = updated_at; }

    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }

    public List<BillItem> getBillItems() { return billItems; }
    public void setBillItems(List<BillItem> billItems) { this.billItems = billItems; }
}
