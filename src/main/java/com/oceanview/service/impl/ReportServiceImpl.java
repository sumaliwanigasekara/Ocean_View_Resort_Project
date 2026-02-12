package com.oceanview.service.impl;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.impl.BillDAOImpl;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.model.Bill.BillStatus;
import com.oceanview.model.Reservation.ReservationStatus;
import com.oceanview.model.Room.RoomStatus;
import com.oceanview.model.Room.RoomType;
import com.oceanview.service.ReportService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportServiceImpl implements ReportService {
    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;
    private final BillDAO billDAO;

    public ReportServiceImpl(ReservationDAO reservationDAO, RoomDAO roomDAO, BillDAO billDAO) {
        this.reservationDAO = reservationDAO;
        this.roomDAO = roomDAO;
        this.billDAO = billDAO;
    }

    public ReportServiceImpl() {
        this(new ReservationDAOImpl(), new RoomDAOImpl(), new BillDAOImpl());
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        int totalRooms = (int) roomDAO.count();
        int availableRooms = roomDAO.countByStatus(RoomStatus.AVAILABLE);
        int occupiedRooms = roomDAO.countByStatus(RoomStatus.OCCUPIED);

        stats.put("totalRooms", totalRooms);
        stats.put("availableRooms", availableRooms);
        stats.put("occupiedRooms", occupiedRooms);

        double occupancyRate = totalRooms > 0 ? (double) occupiedRooms / totalRooms * 100 : 0;
        stats.put("occupancyRate", Math.round(occupancyRate * 100.0) / 100.0);

        stats.put("totalReservations", reservationDAO.count());
        stats.put("pendingReservations", reservationDAO.countByStatus(ReservationStatus.PENDING));
        stats.put("confirmedReservations", reservationDAO.countByStatus(ReservationStatus.CONFIRMED));
        stats.put("checkedInReservations", reservationDAO.countByStatus(ReservationStatus.CHECKED_IN));

        stats.put("todayCheckIns", reservationDAO.findTodayCheckIns().size());
        stats.put("todayCheckOuts", reservationDAO.findTodayCheckOuts().size());

        LocalDate monthStart = LocalDate.now().withDayOfMonth(1);
        LocalDate today = LocalDate.now();
        BigDecimal monthlyRevenue = billDAO.calculateRevenue(monthStart, today);
        stats.put("monthlyRevenue", monthlyRevenue);

        stats.put("pendingBills", billDAO.findByStatus(BillStatus.PENDING).size());

        return stats;
    }

    @Override
    public Map<String, Object> getBookingReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();

        report.put("startDate", startDate.toString());
        report.put("endDate", endDate.toString());

        var reservations = reservationDAO.findBetweenDates(startDate, endDate);

        report.put("totalBookings", reservations.size());

        Map<String, Long> byStatus = new HashMap<>();
        for (ReservationStatus status : ReservationStatus.values()) {
            long count = reservations.stream()
                    .filter(r -> r.getStatus() == status)
                    .count();
            byStatus.put(status.name(), count);
        }
        report.put("byStatus", byStatus);

        long totalNights = reservations.stream()
                .mapToLong(r -> r.getNumberOfNights())
                .sum();
        report.put("totalNightsBooked", totalNights);

        double avgStay = reservations.isEmpty() ? 0 :
                (double) totalNights / reservations.size();
        report.put("averageStayLength", Math.round(avgStay * 10.0) / 10.0);

        return report;
    }

    @Override
    public Map<String, Object> getRevenueReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();

        report.put("startDate", startDate.toString());
        report.put("endDate", endDate.toString());

        var bills = billDAO.findBetweenDates(startDate, endDate);

        BigDecimal totalRevenue = billDAO.calculateRevenue(startDate, endDate);
        report.put("totalRevenue", totalRevenue);

        BigDecimal pendingRevenue = bills.stream()
                .filter(b -> b.getStatus() == BillStatus.PENDING)
                .map(b -> b.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.put("pendingRevenue", pendingRevenue);

        BigDecimal roomCharges = bills.stream()
                .filter(b -> b.getStatus() == BillStatus.PAID)
                .map(b -> b.getRoomCharges())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.put("roomCharges", roomCharges);

        BigDecimal serviceCharges = bills.stream()
                .filter(b -> b.getStatus() == BillStatus.PAID)
                .map(b -> b.getServiceCharges() != null ? b.getServiceCharges() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.put("serviceCharges", serviceCharges);

        BigDecimal taxCollected = bills.stream()
                .filter(b -> b.getStatus() == BillStatus.PAID)
                .map(b -> b.getTaxAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.put("taxCollected", taxCollected);

        report.put("totalBills", bills.size());
        report.put("paidBills", bills.stream().filter(b -> b.getStatus() == BillStatus.PAID).count());
        report.put("pendingBills", bills.stream().filter(b -> b.getStatus() == BillStatus.PENDING).count());

        return report;
    }

    @Override
    public Map<String, Object> getOccupancyReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();

        report.put("startDate", startDate.toString());
        report.put("endDate", endDate.toString());

        int totalRooms = (int) roomDAO.count();
        report.put("totalRooms", totalRooms);

        int occupiedRooms = roomDAO.countByStatus(RoomStatus.OCCUPIED);
        report.put("currentlyOccupied", occupiedRooms);

        double currentOccupancyRate = totalRooms > 0 ? (double) occupiedRooms / totalRooms * 100 : 0;
        report.put("currentOccupancyRate", Math.round(currentOccupancyRate * 100.0) / 100.0);

        Map<String, Integer> byStatus = new HashMap<>();
        for (RoomStatus status : RoomStatus.values()) {
            byStatus.put(status.name(), roomDAO.countByStatus(status));
        }
        report.put("roomsByStatus", byStatus);

        report.put("roomTypeDistribution", getRoomTypeDistribution());

        var reservations = reservationDAO.findBetweenDates(startDate, endDate);
        long roomNightsBooked = reservations.stream()
                .mapToLong(r -> r.getNumberOfNights())
                .sum();

        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        long totalRoomNights = totalRooms * daysBetween;

        report.put("roomNightsBooked", roomNightsBooked);
        report.put("totalRoomNights", totalRoomNights);

        double periodOccupancyRate = totalRoomNights > 0 ? (double) roomNightsBooked / totalRoomNights * 100 : 0;
        report.put("periodOccupancyRate", Math.round(periodOccupancyRate * 100.0) / 100.0);

        return report;
    }

    @Override
    public BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) {
        return billDAO.calculateRevenue(startDate, endDate);
    }

    @Override
    public double getOccupancyRate(LocalDate startDate, LocalDate endDate) {
        int totalRooms = (int) roomDAO.count();
        if (totalRooms == 0) return 0;

        var reservations = reservationDAO.findBetweenDates(startDate, endDate);
        long roomNightsBooked = reservations.stream()
                .mapToLong(r -> r.getNumberOfNights())
                .sum();

        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        long totalRoomNights = totalRooms * daysBetween;

        return totalRoomNights > 0 ? (double) roomNightsBooked / totalRoomNights * 100 : 0;
    }

    @Override
    public Map<String, Integer> getRoomTypeDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        for (RoomType type : RoomType.values()) {
            int count = roomDAO.findByRoomType(type).size();
            distribution.put(type.name(), count);
        }
        return distribution;
    }

    @Override
    public Map<String, BigDecimal> getMonthlyRevenueTrend(int months) {
        Map<String, BigDecimal> trend = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        for (int i = months - 1; i >= 0; i--) {
            LocalDate monthStart = today.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            if (monthEnd.isAfter(today)) {
                monthEnd = today;
            }

            BigDecimal revenue = billDAO.calculateRevenue(monthStart, monthEnd);
            String monthKey = monthStart.getMonth().toString() + " " + monthStart.getYear();
            trend.put(monthKey, revenue);
        }
        return trend;
    }
}
