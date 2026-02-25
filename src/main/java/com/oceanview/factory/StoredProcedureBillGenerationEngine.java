package com.oceanview.factory;

import com.oceanview.dao.BillDAO;
import com.oceanview.model.Bill;

import java.math.BigDecimal;
import java.util.Objects;

public class StoredProcedureBillGenerationEngine implements BillGenerationEngine {
    private final BillDAO billDAO;

    public StoredProcedureBillGenerationEngine(BillDAO billDAO) {
        this.billDAO = Objects.requireNonNull(billDAO, "billDAO");
    }

    @Override
    public Bill generate(long reservationId, BigDecimal serviceCharges, BigDecimal discountAmount) {
        return billDAO.generateBill(reservationId, serviceCharges, discountAmount);
    }
}
