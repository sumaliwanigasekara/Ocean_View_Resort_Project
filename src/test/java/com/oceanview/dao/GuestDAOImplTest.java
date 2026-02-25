package com.oceanview.dao;

import com.oceanview.dao.impl.GuestDAOImpl;
import com.oceanview.model.Guest;
import com.oceanview.util.DBConnection;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.*;

public class GuestDAOImplTest {


    @Test
    public void findById_existingGuest_returnsGuest() throws Exception {
        long guestId = insertGuest();
        assertTrue(guestId > 0);

        GuestDAO dao = new GuestDAOImpl();
        Guest guest = dao.findById(guestId);

        assertNotNull(guest);
        assertEquals(Long.valueOf(guestId), guest.getGuestId());
    }

    private long insertGuest() throws Exception {
        String uniqueId = String.valueOf(System.currentTimeMillis());
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO guests (guestPassport, guestName, guestEmail, guestAddress, guestContact, guestNIC) " +
                             "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "P" + uniqueId);
            ps.setString(2, "Test Guest");
            ps.setString(3, "guest" + uniqueId + "@test.com");
            ps.setString(4, "Galle");
            ps.setString(5, "0770000000");
            ps.setString(6, "NIC" + uniqueId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }
}