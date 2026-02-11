package com.oceanview.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanview.model.Guest;
import com.oceanview.service.EmailService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.InputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaGuestEmailConsumer implements Runnable {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String PROPERTIES_FILE = "kafka.properties";

    private final KafkaConsumer<String, String> consumer;
    private final EmailService emailService;
    private volatile boolean running = true;

    public KafkaGuestEmailConsumer(EmailService emailService) {
        this.emailService = emailService;

        Properties props = loadKafkaProperties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                props.getProperty("kafka.bootstrap.servers", "localhost:9092"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                props.getProperty("kafka.consumer.group", "guest-email-service"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        this.consumer = new KafkaConsumer<>(props);
        String topic = props.getProperty("kafka.topic.guest.email", "guest-registered");
        this.consumer.subscribe(Collections.singletonList(topic));
    }

    @Override
    public void run() {
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                records.forEach(record -> handleMessage(record.value()));
            }
        } catch (WakeupException ex) {
            if (running) {
                System.err.println("Kafka consumer interrupted: " + ex.getMessage());
            }
        } catch (Exception ex) {
            System.err.println("Kafka consumer error: " + ex.getMessage());
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        running = false;
        consumer.wakeup();
    }

    private void handleMessage(String payload) {
        try {
            Guest guest = MAPPER.readValue(payload, Guest.class);
            emailService.sendGuestRegisteredAlert(guest);
        } catch (Exception ex) {
            System.err.println("Failed to process guest email event: " + ex.getMessage());
        }
    }

    private static Properties loadKafkaProperties() {
        Properties props = new Properties();
        try (InputStream input = KafkaGuestEmailConsumer.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                props.load(input);
            }
        } catch (Exception ex) {
            System.err.println("Failed to load " + PROPERTIES_FILE + ": " + ex.getMessage());
        }
        return props;
    }
}