package com.oceanview.controller;

import com.oceanview.dao.impl.BillDAOImpl;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.service.ReportService;
import com.oceanview.service.impl.ReportServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "ReportController", urlPatterns = "/api/reports/*")
public class ReportController extends BaseController {
    private final ReportService reportService =
            new ReportServiceImpl(new ReservationDAOImpl(), new RoomDAOImpl(), new BillDAOImpl());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String type = request.getParameter("type");
        String from = request.getParameter("from");
        String to = request.getParameter("to");

        if (type == null || from == null || to == null) {
            writeError(response, "Report type and date range are required.", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!"dashboard".equalsIgnoreCase(type) && !requireManagerRole(request, response)) {
            return;
        }

        try {
            LocalDate start = LocalDate.parse(from);
            LocalDate end = LocalDate.parse(to);

            switch (type) {
                case "dashboard":
                    writeJson(response, okResponse("ok", reportService.getDashboardStats()), HttpServletResponse.SC_OK);
                    break;
                case "booking":
                    writeJson(response, okResponse("ok", reportService.getBookingReport(start, end)), HttpServletResponse.SC_OK);
                    break;
                case "revenue":
                    writeJson(response, okResponse("ok", reportService.getRevenueReport(start, end)), HttpServletResponse.SC_OK);
                    break;
                case "occupancy":
                    writeJson(response, okResponse("ok", reportService.getOccupancyReport(start, end)), HttpServletResponse.SC_OK);
                    break;
                default:
                    writeError(response, "Unsupported report type.", HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
