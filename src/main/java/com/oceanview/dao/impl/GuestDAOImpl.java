package com.oceanview.dao.impl;

import com.oceanview.dao.GuestDAO;
import com.oceanview.model.Guest;
import com.oceanview.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GuestDAOImpl implements GuestDAO {
    private static final String FIND_BY_ID_SQL =
            "SELECT guestId, guestPassport, guestName, guestEmail, guestAddress, guestContact, guestNIC " +
                    "FROM guests WHERE guestId = ?";

    @Override
    public Guest findById(Long guestId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setLong(1, guestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapGuest(rs);
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Guest save(Guest guest) {
        String sql = "INSERT INTO guests (guestPassport, guestName, guestEmail, guestAddress, guestContact, guestNIC, userId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, guest.getGuestPassport());
            ps.setString(2, guest.getGuestName());
            ps.setString(3, guest.getGuestEmail());
            ps.setString(4, guest.getGuestAddress());
            ps.setString(5, guest.getGuestContact());
            ps.setString(6, guest.getGuestNIC());
            ps.setObject(7, null);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    guest.setGuestId(rs.getLong(1));
                }
            }
            return guest;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Guest> search(String term) {
        String sql = "SELECT guestId, guestPassport, guestName, guestEmail, guestAddress, guestContact, guestNIC " +
                "FROM guests WHERE guestName LIKE ? OR guestEmail LIKE ? OR guestPassport LIKE ? OR guestNIC LIKE ? " +
                "ORDER BY guestName";
        List<Guest> results = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String like = "%" + term + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                results.add(mapGuest(rs));
            }
            return results;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Guest mapGuest(ResultSet rs) throws Exception {
        Guest guest = new Guest();
        guest.setGuestId(rs.getLong("guestId"));
        guest.setGuestPassport(rs.getString("guestPassport"));
        guest.setGuestName(rs.getString("guestName"));
        guest.setGuestEmail(rs.getString("guestEmail"));
        guest.setGuestAddress(rs.getString("guestAddress"));
        guest.setGuestContact(rs.getString("guestContact"));
        guest.setGuestNIC(rs.getString("guestNIC"));
        return guest;
    }
}
