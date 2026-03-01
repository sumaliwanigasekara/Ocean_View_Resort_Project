package com.oceanview.controller;

import com.oceanview.factory.EmailServiceFactory;
import com.oceanview.dao.impl.GuestDAOImpl;
import com.oceanview.dto.GuestDTO;
import com.oceanview.mapper.GuestMapper;
import com.oceanview.model.Guest;
import com.oceanview.service.email.AsyncEmailDispatcher;
import com.oceanview.service.email.EmailMessage;
import com.oceanview.service.email.EmailTemplateBuilder;
import com.oceanview.service.GuestService;
import com.oceanview.service.impl.GuestServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "GuestController", urlPatterns = "/api/guests/*")
public class GuestController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(GuestController.class.getName());

    private final GuestService guestService;
    private final AsyncEmailDispatcher emailDispatcher;
    private final GuestMapper guestMapper;

    public GuestController() {
        this(
                new GuestServiceImpl(new GuestDAOImpl()),
                new AsyncEmailDispatcher(EmailServiceFactory.createDefault()),
                new GuestMapper()
        );
    }

    // For tests
    public GuestController(GuestService guestService) {
        this(guestService, new AsyncEmailDispatcher(EmailServiceFactory.createDefault()), new GuestMapper());
    }

    public GuestController(GuestService guestService, AsyncEmailDispatcher emailDispatcher) {
        this(guestService, emailDispatcher, new GuestMapper());
    }

    public GuestController(GuestService guestService, AsyncEmailDispatcher emailDispatcher, GuestMapper guestMapper) {
        this.guestService = guestService;
        this.emailDispatcher = emailDispatcher;
        this.guestMapper = guestMapper;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        try {
            if (path == null || "/".equals(path)) {
                String term = request.getParameter("q");
                if (term == null || term.isBlank()) {
                    writeJson(response, okResponse("ok", List.of()), HttpServletResponse.SC_OK);
                    return;
                }
                List<Guest> guests = guestService.searchGuests(term);
                writeJson(response, okResponse("ok", guestMapper.toDTOList(guests)), HttpServletResponse.SC_OK);
                return;
            }
            long guestId = Long.parseLong(path.replace("/", ""));
            Guest guest = guestService.getGuest(guestId);
            writeJson(response, okResponse("ok", guestMapper.toDTO(guest)), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            GuestDTO guestDTO = readJson(request, GuestDTO.class);
            Guest saved = guestService.createGuest(guestMapper.toEntity(guestDTO));
            String recipient = sendGuestCreatedEmail(saved);
            String message = "Guest added successfully.";
            if (recipient != null) {
                message += " Email sent to guest email address: " + recipient + ".";
            } else {
                message += " Email notification skipped (guest email unavailable).";
            }
            writeJson(response, okResponse(message, guestMapper.toDTO(saved)), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private String sendGuestCreatedEmail(Guest saved) {
        try {
            EmailMessage message = EmailTemplateBuilder.guestCreated(saved);
            if (message == null) {
                return null;
            }
            emailDispatcher.send(message);
            return message.getTo();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Guest created but email dispatch could not be queued.", ex);
            return null;
        }
    }
}
