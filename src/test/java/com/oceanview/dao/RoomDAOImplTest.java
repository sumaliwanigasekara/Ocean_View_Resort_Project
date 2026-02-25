package com.oceanview.dao;

import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.model.Room;
import com.oceanview.util.DBConnection;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RoomDAOImplTest {

    @Test
    public void findById_existingRoom_returnsRoom() throws Exception {
        long roomId = insertRoom();

        RoomDAO dao = new RoomDAOImpl();
        Room room = dao.findById(roomId);

        assertNotNull(room);
        assertEquals(Long.valueOf(roomId), room.getRoomId());
    }

    private long insertRoom() throws Exception {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO rooms (roomType, ratePerNight, max_occupancy, roomStatus) " +
                             "VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, "SINGLE");
            ps.setBigDecimal(2, new java.math.BigDecimal("100.00"));
            ps.setInt(3, 2);
            ps.setString(4, "AVAILABLE");
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }
}