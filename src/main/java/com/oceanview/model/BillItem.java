package com.oceanview.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillItem {
    private Long id;
    private Long billId;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private ItemCategory category;
    private LocalDateTime created_at;

    public enum ItemCategory {
        ROOM_CHARGE, FOOD_BEVERAGE, LAUNDRY, SPA, MINIBAR, TELEPHONE, OTHER
    }

    public BillItem() {}

    public BillItem(Long id, Long billId, String description, int quantity,
                   BigDecimal unitPrice, ItemCategory category) {
        this.id = id;
        this.billId = billId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.category = category;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotal();
    }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotal();
    }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public ItemCategory getCategory() { return category; }
    public void setCategory(ItemCategory category) { this.category = category; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime createdAt) { this.created_at = createdAt; }

    private void calculateTotal() {
        if (unitPrice != null && quantity > 0) {
            this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
