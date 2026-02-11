package com.oceanview.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportResult {
    private String reportType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<ReportMetric> metrics = new ArrayList<>();

    public ReportResult() {}

    public ReportResult(String reportType, LocalDate fromDate, LocalDate toDate) {
        this.reportType = reportType;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public LocalDate getFromDate() { return fromDate; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

    public LocalDate getToDate() { return toDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }

    public List<ReportMetric> getMetrics() { return metrics; }
    public void setMetrics(List<ReportMetric> metrics) { this.metrics = metrics; }
}