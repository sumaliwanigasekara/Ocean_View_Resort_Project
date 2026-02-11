package com.oceanview.util;

import java.io.InputStream;
import java.util.Properties;

public final class EmailConfig {
    private static final String FILE_NAME = "mail.properties";

    private EmailConfig() {
    }

    public static Properties getProperties() {
        Properties props = new Properties();
        try (InputStream input = EmailConfig.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (input != null) {
                props.load(input);
            }
        } catch (Exception ignored) {
        }
        return props;
    }
}
