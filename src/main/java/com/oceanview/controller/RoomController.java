package com.oceanview.controller;

import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.model.Room;
import com.oceanview.service.RoomService;
import com.oceanview.service.impl.RoomServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "RoomController", urlPatterns = "/api/rooms/*")
public class RoomController extends BaseController {
    private final RoomService roomService = new RoomServiceImpl(new RoomDAOImpl());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String status = request.getParameter("status");
            List<Room> rooms = roomService.listRooms(status);
            writeJson(response, okResponse("ok", rooms), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        try {
            if (path != null && path.endsWith("/status")) {
                String[] parts = path.split("/");
                long roomId = Long.parseLong(parts[1]);
                Room payload = readJson(request, Room.class);
                if (payload == null) {
                    writeError(response, "Room status payload is required.", HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                roomService.updateStatus(roomId, payload.getRoomStatus());
                writeJson(response, okResponse("Room updated", null), HttpServletResponse.SC_OK);
                return;
            }
            Room room = readJson(request, Room.class);
            Room saved = roomService.createRoom(room);
            writeJson(response, okResponse("Room saved", saved), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}