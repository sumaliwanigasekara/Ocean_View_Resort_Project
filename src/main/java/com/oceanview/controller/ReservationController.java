package com.oceanview.controller;

import com.oceanview.dao.impl.GuestDAOImpl;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.dto.ReservationDTO;
import com.oceanview.factory.EmailServiceFactory;
import com.oceanview.mapper.ReservationMapper;
import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;
import com.oceanview.service.email.AsyncEmailDispatcher;
import com.oceanview.service.email.EmailMessage;
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
    private final ReservationMapper reservationMapper;

    // Only ONE default constructor
    public ReservationController() {
        this(
                new ReservationServiceImpl(new ReservationDAOImpl(), new RoomDAOImpl()),
                new GuestDAOImpl(),
                new RoomDAOImpl(),
                new AsyncEmailDispatcher(EmailServiceFactory.createDefault()),
                new ReservationMapper()
        );
    }

    // Keep test constructor
    public ReservationController(ReservationService reservationService) {
        this(
                reservationService,
                new GuestDAOImpl(),
                new RoomDAOImpl(),
                new AsyncEmailDispatcher(EmailServiceFactory.createDefault()),
                new ReservationMapper()
        );
    }

    public ReservationController(ReservationService reservationService, GuestDAOImpl guestDAO, RoomDAOImpl roomDAO,
                                 AsyncEmailDispatcher emailDispatcher) {
        this(reservationService, guestDAO, roomDAO, emailDispatcher, new ReservationMapper());
    }

    public ReservationController(ReservationService reservationService, GuestDAOImpl guestDAO, RoomDAOImpl roomDAO,
                                 AsyncEmailDispatcher emailDispatcher, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.guestDAO = guestDAO;
        this.roomDAO = roomDAO;
        this.emailDispatcher = emailDispatcher;
        this.reservationMapper = reservationMapper;
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
                writeJson(response, okResponse("ok", reservationMapper.toDTO(reservation)), HttpServletResponse.SC_OK);
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
            writeJson(response, okResponse("ok", reservationMapper.toDTOList(reservations)), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ReservationDTO reservationDTO = readJson(request, ReservationDTO.class);
            Reservation saved = reservationService.addReservation(reservationMapper.toEntity(reservationDTO));
            String recipient = sendReservationCreatedEmail(saved);
            String message = "Reservation successful.";
            if (recipient != null) {
                message += " Email sent to guest email address: " + recipient + ".";
            } else {
                message += " Email notification skipped (guest email unavailable).";
            }
            writeJson(response, okResponse(message, reservationMapper.toDTO(saved)), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private String sendReservationCreatedEmail(Reservation saved) {
        if (saved == null || saved.getGuestId() == null) {
            return null;
        }

        try {
            Guest guest = guestDAO.findById(saved.getGuestId());
            Room room = saved.getRoomId() == null ? null : roomDAO.findById(saved.getRoomId());
            EmailMessage message = EmailTemplateBuilder.reservationCreated(saved, guest, room);
            if (message == null) {
                return null;
            }
            emailDispatcher.send(message);
            return message.getTo();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Reservation created but email dispatch could not be queued.", ex);
            return null;
        }
    }
}
