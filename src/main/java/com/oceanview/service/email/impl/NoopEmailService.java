package com.oceanview.service.email.impl;

import com.oceanview.service.email.EmailMessage;
import com.oceanview.service.email.EmailService;

import java.util.logging.Logger;

public class NoopEmailService implements EmailService {
    private static final Logger LOGGER = Logger.getLogger(NoopEmailService.class.getName());

    @Override
    public void send(EmailMessage message) {
        if (message == null) {
            return;
        }
        LOGGER.info(() -> String.format(
                "Email disabled/noop. to=%s subject=%s",
                message.getTo(), message.getSubject()));
    }
}
