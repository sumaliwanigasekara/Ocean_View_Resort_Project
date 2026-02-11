package com.oceanview.messaging;

import com.oceanview.service.EmailService;
import com.oceanview.service.impl.EmailServiceImpl;

public class KafkaConsumerApp {
    public static void main(String[] args) {
        EmailService emailService = new EmailServiceImpl();
        KafkaGuestEmailConsumer consumer = new KafkaGuestEmailConsumer(emailService);
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::shutdown));
        new Thread(consumer).start();
    }
}
