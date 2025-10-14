package org.ua.cafe.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/Kav'yarnya";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver не знайдено!");
            throw new SQLException("Помилка драйвера", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}