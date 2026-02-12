package com.oceanview.controller;

import com.oceanview.dao.impl.GuestDAOImpl;
import com.oceanview.model.Guest;
import com.oceanview.service.GuestService;
import com.oceanview.service.impl.GuestServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GuestController", urlPatterns = "/api/guests/*")
public class GuestController extends BaseController {
    private final GuestService guestService =
            new GuestServiceImpl(new GuestDAOImpl());

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
            writeJson(response, okResponse("Guest saved", saved), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
