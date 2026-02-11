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
        return bill;
    }
}
