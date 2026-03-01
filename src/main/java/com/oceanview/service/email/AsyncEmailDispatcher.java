package com.oceanview.service.email;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsyncEmailDispatcher {
    private static final Logger LOGGER = Logger.getLogger(AsyncEmailDispatcher.class.getName());
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "ovr-email-dispatcher");
        thread.setDaemon(true);
        return thread;
    });

    private final EmailService emailService;

    public AsyncEmailDispatcher(EmailService emailService) {
        this.emailService = emailService;
    }

    public void send(EmailMessage message) {
        if (message == null) {
            return;
        }
        EXECUTOR.submit(() -> {
            try {
                emailService.send(message);
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Email dispatch failed", ex);
            }
        });
    }
}
