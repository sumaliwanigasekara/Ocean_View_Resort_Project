package com.oceanview.controller;

import com.oceanview.factory.EmailServiceFactory;
import com.oceanview.dao.impl.GuestDAOImpl;
import com.oceanview.model.Guest;
import com.oceanview.service.email.AsyncEmailDispatcher;
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

    public GuestController() {
        this(new GuestServiceImpl(new GuestDAOImpl()), new AsyncEmailDispatcher(EmailServiceFactory.createDefault()));
    }

    // For tests
    public GuestController(GuestService guestService) {
        this(guestService, new AsyncEmailDispatcher(EmailServiceFactory.createDefault()));
    }

    public GuestController(GuestService guestService, AsyncEmailDispatcher emailDispatcher) {
        this.guestService = guestService;
        this.emailDispatcher = emailDispatcher;
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
                writeJson(response, okResponse("ok", guests), HttpServletResponse.SC_OK);
                return;
            }
            long guestId = Long.parseLong(path.replace("/", ""));
            Guest guest = guestService.getGuest(guestId);
            writeJson(response, okResponse("ok", guest), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Guest guest = readJson(request, Guest.class);
            Guest saved = guestService.createGuest(guest);
            sendGuestCreatedEmail(saved);
            writeJson(response, okResponse("Guest saved", saved), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void sendGuestCreatedEmail(Guest saved) {
        try {
            emailDispatcher.send(EmailTemplateBuilder.guestCreated(saved));
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Guest created but email dispatch could not be queued.", ex);
        }
    }
}
