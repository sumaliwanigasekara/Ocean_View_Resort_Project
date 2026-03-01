package com.oceanview.controller;

import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.dto.RoomDTO;
import com.oceanview.mapper.RoomMapper;
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
    private final RoomMapper roomMapper = new RoomMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String status = request.getParameter("status");
            List<Room> rooms = roomService.listRooms(status);
            writeJson(response, okResponse("ok", roomMapper.toDTOList(rooms)), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireManagerRole(request, response)) {
            return;
        }

        String path = request.getPathInfo();
        try {
            if (path != null && path.endsWith("/status")) {
                String[] parts = path.split("/");
                long roomId = Long.parseLong(parts[1]);
                RoomDTO payload = readJson(request, RoomDTO.class);
                if (payload == null) {
                    writeError(response, "Room status payload is required.", HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                roomService.updateStatus(roomId, payload.getRoomStatus());
                writeJson(response, okResponse("Room updated", null), HttpServletResponse.SC_OK);
                return;
            }
            RoomDTO roomDTO = readJson(request, RoomDTO.class);
            Room saved = roomService.createRoom(roomMapper.toEntity(roomDTO));
            writeJson(response, okResponse("Room saved", roomMapper.toDTO(saved)), HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException ex) {
            writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
