package com.oceanview.controller;

import com.oceanview.dao.impl.BillDAOImpl;
import com.oceanview.dao.impl.GuestDAOImpl;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.model.Bill;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.service.BillingService;
import com.oceanview.service.impl.BillingServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "BillingController", urlPatterns = "/api/bills/*")
public class BillingController extends BaseController {

    private final BillingService billingService;
    private final ReservationDAOImpl reservationDAO;
    private final GuestDAOImpl guestDAO;
    private final RoomDAOImpl roomDAO;

    public BillingController() {
        this.billingService = new BillingServiceImpl(new BillDAOImpl());
        this.reservationDAO = new ReservationDAOImpl();
        this.guestDAO = new GuestDAOImpl();
        this.roomDAO = new RoomDAOImpl();
    }

    // For tests
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
        this.reservationDAO = new ReservationDAOImpl();
        this.guestDAO = new GuestDAOImpl();
        this.roomDAO = new RoomDAOImpl();
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
                enrichBill(bill);
                writeJson(response, okResponse("ok", bill), HttpServletResponse.SC_OK);
                return;
            }
            String status = request.getParameter("status");
            if (status != null && !status.isBlank()) {
                List<Bill> bills = billingService.listBillsByStatus(Bill.BillStatus.valueOf(status));
                enrichBills(bills);
                writeJson(response, okResponse("ok", bills), HttpServletResponse.SC_OK);
                return;
            }
            String reservationId = request.getParameter("reservationId");
            if (reservationId == null || reservationId.isBlank()) {
                List<Bill> bills = billingService.listBills();
                enrichBills(bills);
                writeJson(response, okResponse("ok", bills), HttpServletResponse.SC_OK);
                return;
            }
            Bill bill = billingService.findBillByReservation(Long.parseLong(reservationId));
            enrichBill(bill);
            writeJson(response, okResponse("ok", bill), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String path = request.getPathInfo();
            if (path != null && path.matches("/\\d+/pay")) {
                String[] parts = path.split("/");
                long billId = Long.parseLong(parts[1]);
                Bill payload = readJson(request, Bill.class);
                String paymentMethod = (payload != null && payload.getPaymentMethod() != null && !payload.getPaymentMethod().isBlank())
                        ? payload.getPaymentMethod()
                        : "CASH";
                boolean updated = billingService.markBillAsPaid(billId, paymentMethod);
                if (!updated) {
                    writeError(response, "Bill not found", HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                writeJson(response, okResponse("Bill marked as paid", null), HttpServletResponse.SC_OK);
                return;
            }

            Bill payload = readJson(request, Bill.class);
            if (payload == null) {
                writeError(response, "Bill payload is required.", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            BigDecimal serviceCharges = payload.getServiceCharges() == null ? BigDecimal.ZERO : payload.getServiceCharges();
            BigDecimal discount = payload.getDiscountAmount() == null ? BigDecimal.ZERO : payload.getDiscountAmount();
            Bill bill = billingService.generateBill(payload.getReservationId(), serviceCharges, discount);
            enrichBill(bill);
            writeJson(response, okResponse("Bill generated", bill), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void enrichBills(List<Bill> bills) {
        if (bills == null) return;
        for (Bill bill : bills) {
            enrichBill(bill);
        }
    }

    private void enrichBill(Bill bill) {
        if (bill == null || bill.getReservationId() == null) return;
        Reservation reservation = reservationDAO.findById(bill.getReservationId());
        if (reservation == null) return;

        Guest guest = null;
        if (reservation.getGuestId() != null) {
            guest = guestDAO.findById(reservation.getGuestId());
        }

        Room room = null;
        if (reservation.getRoomId() != null) {
            room = roomDAO.findById(reservation.getRoomId());
        }

        reservation.setGuest(guest);
        reservation.setRoom(room);
        bill.setReservation(reservation);
    }
}
