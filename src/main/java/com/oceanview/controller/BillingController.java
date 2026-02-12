package com.oceanview.controller;

import com.oceanview.dao.impl.BillDAOImpl;
import com.oceanview.model.Bill;
import com.oceanview.service.BillingService;
import com.oceanview.service.impl.BillingServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "BillingController", urlPatterns = "/api/bills/*")
public class BillingController extends BaseController {

    private final BillingService billingService;

    public BillingController() {
        this.billingService = new BillingServiceImpl(new BillDAOImpl());
    }

    // For tests
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }
    
    public java.math.BigDecimal calculateTotal(com.oceanview.model.Bill bill) {
        return billingService.calculateTotal(bill);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        try {
            if (path != null && path.length() > 1) {
                long billId = Long.parseLong(path.replace("/", ""));
                Bill bill = new BillDAOImpl().findById(billId);
                writeJson(response, okResponse("ok", bill), HttpServletResponse.SC_OK);
                return;
            }
            String reservationId = request.getParameter("reservationId");
            if (reservationId == null) {
                writeJson(response, okResponse("ok", null), HttpServletResponse.SC_OK);
                return;
            }
            Bill bill = billingService.findBillByReservation(Long.parseLong(reservationId));
            writeJson(response, okResponse("ok", bill), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Bill payload = readJson(request, Bill.class);
            if (payload == null) {
                writeError(response, "Bill payload is required.", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            BigDecimal serviceCharges = payload.getServiceCharges() == null ? BigDecimal.ZERO : payload.getServiceCharges();
            BigDecimal discount = payload.getDiscountAmount() == null ? BigDecimal.ZERO : payload.getDiscountAmount();
            Bill bill = billingService.generateBill(payload.getReservationId(), serviceCharges, discount);
            writeJson(response, okResponse("Bill generated", bill), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
