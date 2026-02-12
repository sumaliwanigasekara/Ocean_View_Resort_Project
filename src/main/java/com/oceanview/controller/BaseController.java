package com.oceanview.controller;

import com.oceanview.dto.ApiResponse;
import com.oceanview.util.JsonUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController extends HttpServlet {

    protected <T> T readJson(HttpServletRequest request, Class<T> type) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return JsonUtil.GSON.fromJson(sb.toString(), type);
    }

    protected void writeJson(HttpServletResponse response, Object payload, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JsonUtil.GSON.toJson(payload));
        }
    }

    protected void writeError(HttpServletResponse response, String message, int status) throws IOException {
        writeJson(response, new ApiResponse(false, message), status);
    }

    protected Map<String, Object> okResponse(String message, Object data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("success", true);
        payload.put("message", message);
        payload.put("data", data);
        return payload;
    }
}
