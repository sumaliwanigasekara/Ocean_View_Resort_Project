package com.oceanview.factory;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.GuestDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.UserDAO;
import com.oceanview.dao.impl.BillDAOImpl;
import com.oceanview.dao.impl.GuestDAOImpl;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.dao.impl.UserDAOImpl;

public final class DaoFactory {
    private DaoFactory() {
    }

    public static UserDAO userDAO() {
        return new UserDAOImpl();
    }

    public static GuestDAO guestDAO() {
        return new GuestDAOImpl();
    }

    public static RoomDAO roomDAO() {
        return new RoomDAOImpl();
    }

    public static ReservationDAO reservationDAO() {
        return new ReservationDAOImpl();
    }

    public static BillDAO billDAO() {
        return new BillDAOImpl();
    }
}
