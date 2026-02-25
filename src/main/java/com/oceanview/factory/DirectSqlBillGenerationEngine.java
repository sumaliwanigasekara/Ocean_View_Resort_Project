package com.oceanview.factory;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DirectSqlBillGenerationEngine implements BillGenerationEngine {
    private final BillDAO billDAO;
    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;

    public DirectSqlBillGenerationEngine(BillDAO billDAO, ReservationDAO reservationDAO, RoomDAO roomDAO) {
        this.billDAO = Objects.requireNonNull(billDAO, "billDAO");
        this.reservationDAO = Objects.requireNonNull(reservationDAO, "reservationDAO");
        this.roomDAO = Objects.requireNonNull(roomDAO, "roomDAO");
    }

    @Override
    public Bill generate(long reservationId, BigDecimal serviceCharges, BigDecimal discountAmount) {
        Reservation reservation = reservationDAO.findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }

        Room room = roomDAO.findById(reservation.getRoomId());
        if (room == null) {
            throw new IllegalArgumentException("Room not found.");
        }

        long nights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        if (nights <= 0) {
            throw new IllegalArgumentException("Invalid reservation dates.");
        }

        BigDecimal roomCharges = room.getRatePerNight().multiply(BigDecimal.valueOf(nights));
        BigDecimal tax = roomCharges.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = roomCharges.add(serviceCharges).add(tax).subtract(discountAmount);

        Bill bill = new Bill();
        bill.setReservationId(reservationId);
        bill.setRoomCharges(roomCharges);
        bill.setServiceCharges(serviceCharges);
        bill.setTaxAmount(tax);
        bill.setDiscountAmount(discountAmount);
        bill.setTotalAmount(total);
        bill.setStatus(Bill.BillStatus.PENDING);

        return billDAO.createBill(bill);
    }
}