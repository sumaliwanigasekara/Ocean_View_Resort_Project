package com.oceanview.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;

import java.io.InputStream;
import java.util.Properties;

public class KafkaEventProducer {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String PROPERTIES_FILE = "kafka.properties";

    private final KafkaProducer<String, String> producer;
    private final String guestTopic;

    public KafkaEventProducer() {
        Properties props = loadKafkaProperties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                props.getProperty("kafka.bootstrap.servers", "localhost:9092"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        this.producer = new KafkaProducer<>(props);
        this.guestTopic = props.getProperty("kafka.topic.guest.email", "guest-registered");
    }

    public void publishGuestRegistered(Object guestPayload) {
        try {
            String message = MAPPER.writeValueAsString(guestPayload);
            producer.send(new ProducerRecord<>(guestTopic, message));
        } catch (Exception ex) {
            System.err.println("Failed to publish guest event: " + ex.getMessage());
        }
    }

    public void close() {
        producer.close();
    }

    private static Properties loadKafkaProperties() {
        Properties props = new Properties();
        try (InputStream input = KafkaEventProducer.class.getClassLoader()
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
