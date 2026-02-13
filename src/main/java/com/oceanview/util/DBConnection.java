package com.oceanview.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {
    private static final String PROPERTIES_FILE = "db.properties";
    private static final DBConnection INSTANCE = new DBConnection();

    private final Properties properties = new Properties();

    private DBConnection() {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Missing " + PROPERTIES_FILE + " in classpath.");
            }
            properties.load(input);

            String driver = properties.getProperty("db.driver");
            if (driver != null && !driver.isBlank()) {
                Class.forName(driver);
            }
        } catch (IOException | ClassNotFoundException ex) {
            throw new ExceptionInInitializerError("Failed to initialize database connection: " + ex.getMessage());
        }
    }

    public static DBConnection getInstance() {
        return INSTANCE;
    }

    // Instance method
    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
    }

    // Compatibility method so existing DAO code doesn't break
    public static Connection getConnection() throws SQLException {
        return getInstance().openConnection();
    }

    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (Exception ignored) {
        }
    }
}