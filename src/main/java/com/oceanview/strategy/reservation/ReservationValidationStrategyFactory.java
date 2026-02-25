package com.oceanview.strategy.reservation;

import com.oceanview.dao.RoomDAO;

import java.util.ArrayList;
import java.util.List;

public final class ReservationValidationStrategyFactory {
    private ReservationValidationStrategyFactory() {
    }

    public static List<ReservationValidationStrategy> defaultStrategies(RoomDAO roomDAO) {
        List<ReservationValidationStrategy> strategies = new ArrayList<>();
        strategies.add(new BasicReservationValidationStrategy());
        strategies.add(new RoomAvailabilityValidationStrategy());
        if (roomDAO != null) {
            strategies.add(new RoomOccupancyValidationStrategy());
        }
        return strategies;
    }
}
