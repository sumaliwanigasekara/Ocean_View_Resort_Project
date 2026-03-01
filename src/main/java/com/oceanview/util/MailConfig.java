package com.oceanview.util;

import java.io.InputStream;
import java.util.Properties;

public final class MailConfig {
    private static final String PROPERTIES_FILE = "mail.properties";
    private static final MailConfig INSTANCE = new MailConfig();

    private final Properties properties = new Properties();

    private MailConfig() {
        try (InputStream input = MailConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (Exception ignored) {
            // Fall back to defaults when properties are unavailable.
        }
    }

    public static MailConfig getInstance() {
        return INSTANCE;
    }

    public boolean isEnabled() {
        return Boolean.parseBoolean(get("mail.enabled", "false"));
    }

    public String getProvider() {
        return get("mail.provider", "noop");
    }

    public String getFrom() {
        return get("mail.from", "no-reply@oceanview.com");
    }

    public String getSmtpHost() {
        return get("mail.smtp.host", "");
    }

    public int getSmtpPort() {
        try {
            return Integer.parseInt(get("mail.smtp.port", "587"));
        } catch (NumberFormatException ex) {
            return 587;
        }
    }

    public String getSmtpUsername() {
        return get("mail.smtp.username", "");
    }

    public String getSmtpPassword() {
        return get("mail.smtp.password", "");
    }

    public boolean isStartTlsEnabled() {
        return Boolean.parseBoolean(get("mail.smtp.starttls", "true"));
    }

    public boolean isAuthEnabled() {
        return Boolean.parseBoolean(get("mail.smtp.auth", "true"));
    }

    public String get(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return value == null ? defaultValue : value.trim();
    }
}
