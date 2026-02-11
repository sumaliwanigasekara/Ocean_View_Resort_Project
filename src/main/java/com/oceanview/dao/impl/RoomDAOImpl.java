package com.oceanview.dao.impl;

import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;
import com.oceanview.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {
    private static final String FIND_BY_ID_SQL =
            "SELECT roomId, roomType, ratePerNight, max_occupancy, roomStatus " +
                    "FROM rooms WHERE roomId = ?";

    @Override
    public Room findById(Long roomId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setLong(1, roomId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRoom(rs);
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Room save(Room room) {
        String sql = "INSERT INTO rooms (roomType, ratePerNight, max_occupancy, roomStatus) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, room.getRoomType().name());
            ps.setBigDecimal(2, room.getRatePerNight());
            ps.setInt(3, room.getMaxOccupancy());
            ps.setString(4, room.getRoomStatus().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    room.setRoomId(rs.getLong(1));
                }
            }
            return room;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Room> list(String status) {
        String sql = "SELECT roomId, roomType, ratePerNight, max_occupancy, roomStatus " +
                "FROM rooms " +
                (status == null || status.isBlank() ? "" : "WHERE roomStatus = ? ") +
                "ORDER BY roomId";
        List<Room> rooms = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (status != null && !status.isBlank()) {
                ps.setString(1, status);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
            return rooms;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateStatus(long roomId, Room.RoomStatus status) {
        String sql = "UPDATE rooms SET roomStatus = ? WHERE roomId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setLong(2, roomId);
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM rooms";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public int countByStatus(Room.RoomStatus status) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE roomStatus = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Room> findByRoomType(Room.RoomType type) {
        String sql = "SELECT roomId, roomType, ratePerNight, max_occupancy, roomStatus " +
                "FROM rooms WHERE roomType = ?";
        List<Room> rooms = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, type.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
            return rooms;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Room mapRoom(ResultSet rs) throws Exception {
        Room room = new Room();
        room.setRoomId(rs.getLong("roomId"));
        room.setRoomType(Room.RoomType.valueOf(rs.getString("roomType")));
        room.setRatePerNight(rs.getBigDecimal("ratePerNight"));
        room.setMaxOccupancy(rs.getInt("max_occupancy"));
        room.setRoomStatus(Room.RoomStatus.valueOf(rs.getString("roomStatus")));
        return room;
    }
}
