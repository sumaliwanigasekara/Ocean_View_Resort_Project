package com.oceanview.controller;

import com.oceanview.dao.impl.UserDAOImpl;
import com.oceanview.dto.LoginRequest;
import com.oceanview.dto.LoginResponse;
import com.oceanview.service.AuthService;
import com.oceanview.service.impl.AuthServiceImpl;
import com.oceanview.util.ValidationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "AuthController", urlPatterns = "/api/auth/*")
public class AuthController extends BaseController {
    private final AuthService authService;

    public AuthController() {
        this.authService = new AuthServiceImpl(new UserDAOImpl());
    }

    // For tests
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public LoginResponse login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            return LoginResponse.failure("Email and password are required.");
        }
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return LoginResponse.failure("Invalid email format.");
        }
        return authService.login(request);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if ("/me".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session == null) {
                writeError(response, "Not authenticated.", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            writeJson(response, new LoginResponse(true, "ok",
                    (Long) session.getAttribute("userId"),
                    (String) session.getAttribute("userName"),
                    (String) session.getAttribute("userRole")), HttpServletResponse.SC_OK);
            return;
        }
        writeError(response, "Not found.", HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if ("/login".equals(path)) {
            LoginRequest loginRequest = readJson(request, LoginRequest.class);
            if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                writeError(response, "Email and password are required.", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            try {
                ValidationUtil.requireEmail(loginRequest.getEmail());
            } catch (IllegalArgumentException ex) {
                writeError(response, ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            LoginResponse loginResponse = authService.login(loginRequest);
            if (loginResponse.isSuccess()) {
                HttpSession existingSession = request.getSession(false);
                if (existingSession != null) {
                    existingSession.invalidate();
                }

                HttpSession session = request.getSession(true);
                session.setMaxInactiveInterval(30 * 60);
                session.setAttribute("userId", loginResponse.getUserId());
                session.setAttribute("userName", loginResponse.getUserName());
                session.setAttribute("userRole", loginResponse.getRole());

                setRoleCookie(response, request, loginResponse.getRole(), -1);
            }
            writeJson(response, loginResponse, HttpServletResponse.SC_OK);
            return;
        }

        if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            setRoleCookie(response, request, "", 0);
            writeJson(response, new LoginResponse(true, "Logged out", null, null, null), HttpServletResponse.SC_OK);
            return;
        }
        writeError(response, "Not found.", HttpServletResponse.SC_NOT_FOUND);
    }

    private void setRoleCookie(HttpServletResponse response, HttpServletRequest request, String value, int maxAgeSeconds) {
        String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
        String path = request.getContextPath() == null || request.getContextPath().isEmpty()
                ? "/"
                : request.getContextPath();

        StringBuilder cookie = new StringBuilder("ovr_role=")
                .append(encodedValue)
                .append("; Path=").append(path)
                .append("; SameSite=Lax");

        // Only send Max-Age when explicitly deleting/persisting.
        // For login we want a normal session cookie, so we omit Max-Age.
        if (maxAgeSeconds >= 0) {
            cookie.append("; Max-Age=").append(maxAgeSeconds);
        }

        if (request.isSecure()) {
            cookie.append("; Secure");
        }

        response.addHeader("Set-Cookie", cookie.toString());
    }

}
