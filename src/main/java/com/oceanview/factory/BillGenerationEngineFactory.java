package com.oceanview.factory;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;

public final class BillGenerationEngineFactory {
    private BillGenerationEngineFactory() {}

    public enum Mode { SP, DIRECT, AUTO }

    public static Mode resolveMode() {
        String val = System.getProperty("billing.engine", "DIRECT").trim().toUpperCase();
        try {
            return Mode.valueOf(val);
        } catch (Exception e) {
            return Mode.DIRECT;
        }
    }

    public static BillGenerationEngine create(Mode mode, BillDAO billDAO, ReservationDAO reservationDAO, RoomDAO roomDAO) {
        BillGenerationEngine direct = new DirectSqlBillGenerationEngine(billDAO, reservationDAO, roomDAO);

        if (mode == Mode.SP || mode == Mode.AUTO || mode == Mode.DIRECT) {
            return direct;
        }
        return direct;
    }
}
