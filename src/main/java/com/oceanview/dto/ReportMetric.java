package com.oceanview.dto;

import java.math.BigDecimal;

public class ReportMetric {
    private String label;
    private Integer count;
    private BigDecimal amount;

    public ReportMetric() {}

    public ReportMetric(String label, Integer count, BigDecimal amount) {
        this.label = label;
        this.count = count;
        this.amount = amount;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
