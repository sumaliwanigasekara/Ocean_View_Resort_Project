package com.oceanview.dto;

import com.oceanview.model.BillItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillItemDTO {
    private Long id;
    private Long billId;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private BillItem.ItemCategory category;
    private LocalDateTime created_at;

    public BillItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BillItem.ItemCategory getCategory() {
        return category;
    }

    public void setCategory(BillItem.ItemCategory category) {
        this.category = category;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
