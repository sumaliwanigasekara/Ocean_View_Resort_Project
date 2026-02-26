package com.oceanview.controller;

import com.oceanview.dao.impl.GuestDAOImpl;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.factory.EmailServiceFactory;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.service.email.AsyncEmailDispatcher;
import com.oceanview.service.email.EmailTemplateBuilder;
import com.oceanview.service.ReservationService;
import com.oceanview.service.impl.ReservationServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ReservationController", urlPatterns = "/api/reservations/*")
public class ReservationController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(ReservationController.class.getName());

    private final ReservationService reservationService;
    private final GuestDAOImpl guestDAO;
    private final RoomDAOImpl roomDAO;
    private final AsyncEmailDispatcher emailDispatcher;

    // Only ONE default constructor
    public ReservationController() {
        this(
                new ReservationServiceImpl(new ReservationDAOImpl(), new RoomDAOImpl()),
                new GuestDAOImpl(),
                new RoomDAOImpl(),
                new AsyncEmailDispatcher(EmailServiceFactory.createDefault())
        );
    }

    // Keep test constructor
    public ReservationController(ReservationService reservationService) {
        this(
                reservationService,
                new GuestDAOImpl(),
                new RoomDAOImpl(),
                new AsyncEmailDispatcher(EmailServiceFactory.createDefault())
        );
    }

    public ReservationController(ReservationService reservationService, GuestDAOImpl guestDAO, RoomDAOImpl roomDAO,
                                 AsyncEmailDispatcher emailDispatcher) {
        this.reservationService = reservationService;
        this.guestDAO = guestDAO;
        this.roomDAO = roomDAO;
        this.emailDispatcher = emailDispatcher;
    }

    public Reservation addReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation is required.");
        }
        return reservationService.addReservation(reservation);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        try {
            if (path != null && path.length() > 1) {
                long reservationId = Long.parseLong(path.replace("/", ""));
                Reservation reservation = reservationService.getReservationDetails(reservationId);
                writeJson(response, okResponse("ok", reservation), HttpServletResponse.SC_OK);
                return;
            }

            String from = request.getParameter("from");
            String to = request.getParameter("to");
            if (from == null || to == null) {
                writeJson(response, okResponse("ok", List.of()), HttpServletResponse.SC_OK);
                return;
            }

            List<Reservation> reservations = reservationService.listReservations(
                    LocalDate.parse(from), LocalDate.parse(to));
            writeJson(response, okResponse("ok", reservations), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Reservation reservation = readJson(request, Reservation.class);
            Reservation saved = reservationService.addReservation(reservation);
            sendReservationCreatedEmail(saved);
            writeJson(response, okResponse("Reservation saved", saved), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void sendReservationCreatedEmail(Reservation saved) {
        if (saved == null || saved.getGuestId() == null) {
            return;
        }

        try {
            Guest guest = guestDAO.findById(saved.getGuestId());
            Room room = saved.getRoomId() == null ? null : roomDAO.findById(saved.getRoomId());
            emailDispatcher.send(EmailTemplateBuilder.reservationCreated(saved, guest, room));
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Reservation created but email dispatch could not be queued.", ex);
        }
    }
}
