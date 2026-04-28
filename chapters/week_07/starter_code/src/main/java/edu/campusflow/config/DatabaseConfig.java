package edu.campusflow.config;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    public static final String DB_URL = "jdbc:sqlite:campusflow.db";

    private DatabaseConfig() {
    }

    public static Connection getConnection() throws SQLException {
        // 待办：返回 DriverManager.getConnection(DB_URL)。
        throw new UnsupportedOperationException("待办：实现 getConnection");
    }
}
