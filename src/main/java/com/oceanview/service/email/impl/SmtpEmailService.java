package com.oceanview.service.email.impl;

import com.oceanview.service.email.EmailMessage;
import com.oceanview.service.email.EmailService;
import com.oceanview.util.MailConfig;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SmtpEmailService implements EmailService {
    private final MailConfig config;

    public SmtpEmailService(MailConfig config) {
        this.config = config;
    }

    @Override
    public void send(EmailMessage message) {
        if (message == null || isBlank(message.getTo())) {
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", config.getSmtpHost());
        props.put("mail.smtp.port", String.valueOf(config.getSmtpPort()));
        props.put("mail.smtp.auth", String.valueOf(config.isAuthEnabled()));
        props.put("mail.smtp.starttls.enable", String.valueOf(config.isStartTlsEnabled()));

        Session session;
        if (config.isAuthEnabled()) {
            session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getSmtpUsername(), config.getSmtpPassword());
                }
            });
        } else {
            session = Session.getInstance(props);
        }

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(config.getFrom()));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.getTo()));
            mimeMessage.setSubject(message.getSubject(), "UTF-8");
            mimeMessage.setText(message.getBody(), "UTF-8");
            Transport.send(mimeMessage);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to send email", ex);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
