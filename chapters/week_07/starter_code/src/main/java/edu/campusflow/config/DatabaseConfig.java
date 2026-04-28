package edu.campusflow.config;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    public static final String DB_URL = "jdbc:sqlite:campusflow.db";

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
        // TODO: return DriverManager.getConnection(DB_URL).
        throw new UnsupportedOperationException("TODO: implement getConnection");
    }
}
