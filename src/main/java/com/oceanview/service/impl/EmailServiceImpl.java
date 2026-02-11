
package com.oceanview.service.impl;

import com.oceanview.model.Guest;
import com.oceanview.model.Reservation;
import com.oceanview.service.EmailService;
import com.oceanview.util.EmailConfig;

import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailServiceImpl implements EmailService {

    @Override
    public void sendReservationAlert(Reservation reservation) {
        if (reservation == null) {
            return;
        }

        Properties props = EmailConfig.getProperties();
        if (props.getProperty("mail.smtp.host") == null) {
            return;
        }

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        props.getProperty("mail.smtp.username"),
                        props.getProperty("mail.smtp.password")
                );
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.from")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(props.getProperty("mail.alert.to")));
            message.setSubject("New reservation received");
            message.setText(
                    "Reservation " + reservation.getReservationId() +
                            " for guest " + reservation.getGuestId() +
                            " room " + reservation.getRoomId() +
                            " from " + reservation.getCheckInDate() +
                            " to " + reservation.getCheckOutDate() +
                            " has been created."
            );
            Transport.send(message);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void sendGuestRegisteredAlert(Guest guest) {
        if (guest == null) {
            return;
        }

        Properties props = EmailConfig.getProperties();
        if (props.getProperty("mail.smtp.host") == null) {
            return;
        }

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        props.getProperty("mail.smtp.username"),
                        props.getProperty("mail.smtp.password")
                );
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("mail.from")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(props.getProperty("mail.alert.to")));
            message.setSubject("New guest registered");
            message.setText(
                    "Guest " + guest.getGuestName() +
                            " (" + guest.getGuestEmail() + ")" +
                            " has been registered."
            );
            Transport.send(message);
        } catch (Exception ignored) {
        }
    }
}