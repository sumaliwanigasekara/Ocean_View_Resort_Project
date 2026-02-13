package com.oceanview.controller;

import com.oceanview.dao.impl.UserDAOImpl;
import com.oceanview.dto.LoginRequest;
import com.oceanview.dto.LoginResponse;
import com.oceanview.service.AuthService;
import com.oceanview.service.impl.AuthServiceImpl;
import com.oceanview.util.ValidationUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", loginResponse.getUserId());
                session.setAttribute("userName", loginResponse.getUserName());
                session.setAttribute("userRole", loginResponse.getRole());

                Cookie roleCookie = new Cookie("ovr_role", loginResponse.getRole());
                roleCookie.setHttpOnly(false);
                roleCookie.setPath("/");
                response.addCookie(roleCookie);
            }
            writeJson(response, loginResponse, HttpServletResponse.SC_OK);
            return;
        }

        if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            Cookie roleCookie = new Cookie("ovr_role", "");
            roleCookie.setMaxAge(0);
            roleCookie.setPath("/");
            response.addCookie(roleCookie);
            writeJson(response, new LoginResponse(true, "Logged out", null, null, null), HttpServletResponse.SC_OK);
            return;
        }
        writeError(response, "Not found.", HttpServletResponse.SC_NOT_FOUND);
    }


}
