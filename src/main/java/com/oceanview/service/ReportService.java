package com.oceanview.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface ReportService {
    Map<String, Object> getDashboardStats();

    Map<String, Object> getBookingReport(LocalDate startDate, LocalDate endDate);

    Map<String, Object> getRevenueReport(LocalDate startDate, LocalDate endDate);

    Map<String, Object> getOccupancyReport(LocalDate startDate, LocalDate endDate);

    BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate);

    double getOccupancyRate(LocalDate startDate, LocalDate endDate);

    Map<String, Integer> getRoomTypeDistribution();

    Map<String, BigDecimal> getMonthlyRevenueTrend(int months);
}
