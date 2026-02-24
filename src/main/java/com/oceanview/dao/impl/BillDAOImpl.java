package com.oceanview.dao.impl;

import com.oceanview.dao.BillDAO;
import com.oceanview.model.Bill;
import com.oceanview.util.DBConnection;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import java.util.List;
import java.util.ArrayList;

import java.time.LocalDate;

public class BillDAOImpl implements BillDAO {
    private static final String FIND_BY_ID_SQL =
            "SELECT billId, reservationId, room_charges, service_charges, tax_amount, discount_amount, total_amount, status " +
                    "FROM bills WHERE billId = ?";

    @Override
    public Bill findById(Long billId) {
        return getBill(billId, FIND_BY_ID_SQL);
    }

    @Override
    public Bill findByReservationId(long reservationId) {
        String sql = "SELECT billId, reservationId, room_charges, service_charges, tax_amount, discount_amount, total_amount, status " +
                "FROM bills WHERE reservationId = ? ORDER BY created_at DESC LIMIT 1";
        return getBill(reservationId, sql);
    }

    private Bill getBill(long reservationId, String sql) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, reservationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapBill(rs);
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Bill createBill(Bill bill) {
        String sql = "INSERT INTO bills (reservationId, room_charges, service_charges, tax_amount, discount_amount, total_amount, status, payment_method) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, bill.getReservationId());
            ps.setBigDecimal(2, bill.getRoomCharges());
            ps.setBigDecimal(3, bill.getServiceCharges());
            ps.setBigDecimal(4, bill.getTaxAmount());
            ps.setBigDecimal(5, bill.getDiscountAmount());
            ps.setBigDecimal(6, bill.getTotalAmount());
            ps.setString(7, bill.getStatus().name());
            ps.setString(8, bill.getPaymentMethod());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    bill.setBillId(rs.getLong(1));
                }
            }
            return bill;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Bill generateBill(long reservationId, BigDecimal serviceCharges, BigDecimal discountAmount) {
        String sql = "{CALL sp_generate_bill(?, ?, ?, ?)}";
        try (Connection con = DBConnection.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setLong(1, reservationId);
            cs.setBigDecimal(2, serviceCharges);
            cs.setBigDecimal(3, discountAmount);
            cs.registerOutParameter(4, Types.BIGINT);
            cs.execute();

            long billId = cs.getLong(4);
            return findById(billId);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public BigDecimal calculateRevenue(LocalDate start, LocalDate end) {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM bills " +
                "WHERE status = 'PAID' AND DATE(paid_at) >= ? AND DATE(paid_at) <= ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
            return BigDecimal.ZERO;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Bill> findBetweenDates(LocalDate start, LocalDate end) {
        String sql = "SELECT billId, reservationId, room_charges, service_charges, tax_amount, discount_amount, total_amount, status " +
                "FROM bills WHERE DATE(created_at) >= ? AND DATE(created_at) <= ?";
        List<Bill> bills = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bills.add(mapBill(rs));
            }
            return bills;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Bill> findByStatus(Bill.BillStatus status) {
        String sql = "SELECT billId, reservationId, room_charges, service_charges, tax_amount, discount_amount, total_amount, status " +
                "FROM bills WHERE status = ?";
        List<Bill> bills = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bills.add(mapBill(rs));
            }
            return bills;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Bill> findAll() {
        String sql = "SELECT billId, reservationId, room_charges, service_charges, tax_amount, discount_amount, total_amount, status, payment_method, paid_at, created_at " +
                "FROM bills ORDER BY created_at DESC";
        List<Bill> bills = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bills.add(mapBill(rs));
            }
            return bills;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean markAsPaid(long billId, String paymentMethod) {
        String billSql = "UPDATE bills SET status = 'PAID', payment_method = ?, paid_at = NOW(), updated_at = NOW() WHERE billId = ?";
        String reservationSql = "UPDATE reservations SET status = 'CHECKED_OUT', updated_at = NOW() " +
                "WHERE reservationId = (SELECT reservationId FROM bills WHERE billId = ?) " +
                "AND status IN ('CONFIRMED', 'CHECKED_IN')";
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            try (PreparedStatement billPs = con.prepareStatement(billSql);
                 PreparedStatement reservationPs = con.prepareStatement(reservationSql)) {

                con.setAutoCommit(false);

                billPs.setString(1, paymentMethod);
                billPs.setLong(2, billId);
                boolean billUpdated = billPs.executeUpdate() > 0;
                if (!billUpdated) {
                    con.rollback();
                    return false;
                }

                reservationPs.setLong(1, billId);
                reservationPs.executeUpdate();

                con.commit();
                return true;
            }
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (Exception ignored) {
                    // Ignore rollback failures while propagating the original exception.
                }
            }
            throw new RuntimeException(ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception ignored) {
                    // Ignore close failures.
                }
            }
        }
    }

    private Bill mapBill(ResultSet rs) throws Exception {
        Bill bill = new Bill();
        bill.setBillId(rs.getLong("billId"));
        bill.setReservationId(rs.getLong("reservationId"));
        bill.setRoomCharges(rs.getBigDecimal("room_charges"));
        bill.setServiceCharges(rs.getBigDecimal("service_charges"));
        bill.setTaxAmount(rs.getBigDecimal("tax_amount"));
        bill.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        bill.setTotalAmount(rs.getBigDecimal("total_amount"));
        bill.setStatus(Bill.BillStatus.valueOf(rs.getString("status")));
        try {
            bill.setPaymentMethod(rs.getString("payment_method"));
        } catch (Exception ignored) {
            // Optional column in some queries.
        }
        try {
            java.sql.Timestamp paidAt = rs.getTimestamp("paid_at");
            if (paidAt != null) {
                bill.setPaidAt(paidAt.toLocalDateTime());
            }
        } catch (Exception ignored) {
            // Optional column in some queries.
        }
        try {
            java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                bill.setCreated_at(createdAt.toLocalDateTime());
            }
        } catch (Exception ignored) {
            // Optional column in some queries.
        }
        return bill;
    }
}
