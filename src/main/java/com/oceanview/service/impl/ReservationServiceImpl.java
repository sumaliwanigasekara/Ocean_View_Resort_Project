package com.oceanview.service.impl;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.service.ReservationService;
import com.oceanview.strategy.reservation.ReservationValidationContext;
import com.oceanview.strategy.reservation.ReservationValidationStrategy;
import com.oceanview.strategy.reservation.ReservationValidationStrategyFactory;
import com.oceanview.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ReservationServiceImpl implements ReservationService {
    private final ReservationDAO reservationDAO;
    private final RoomDAO roomDAO;
    private final List<ReservationValidationStrategy> validationStrategies;

    public ReservationServiceImpl(ReservationDAO reservationDAO, RoomDAO roomDAO) {
        this.reservationDAO = Objects.requireNonNull(reservationDAO, "reservationDAO");
        this.roomDAO = Objects.requireNonNull(roomDAO, "roomDAO");
        this.validationStrategies = ReservationValidationStrategyFactory.defaultStrategies(roomDAO);
    }

    public ReservationServiceImpl(ReservationDAO reservationDAO) {
        this.reservationDAO = Objects.requireNonNull(reservationDAO, "reservationDAO");
        this.roomDAO = null;
        this.validationStrategies = ReservationValidationStrategyFactory.defaultStrategies(null);
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        applyValidationStrategies(reservation);

        if (reservation.getStatus() == null) {
            reservation.setStatus(Reservation.ReservationStatus.PENDING);
        }

        Reservation saved = reservationDAO.save(reservation);

        if (roomDAO != null) {
            roomDAO.updateStatus(saved.getRoomId(), Room.RoomStatus.RESERVED);
        }

        return saved;
    }

    @Override
    public Reservation getReservationDetails(Long reservationId) {
        ValidationUtil.requirePositive(reservationId, "Reservation ID");
        Reservation reservation = reservationDAO.findById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation not found.");
        }
        return reservation;
    }

    @Override
    public List<Reservation> listReservations(LocalDate from, LocalDate to) {
        ValidationUtil.requireNonNull(from, "From date");
        ValidationUtil.requireNonNull(to, "To date");
        ValidationUtil.ensureDateRange(from, to);
        return reservationDAO.listByDateRange(from, to);
    }

    private void applyValidationStrategies(Reservation reservation) {
        ReservationValidationContext context = new ReservationValidationContext(reservationDAO, roomDAO);
        for (ReservationValidationStrategy strategy : validationStrategies) {
            strategy.validate(reservation, context);
        }
    }
}
