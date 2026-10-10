package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/online_health_consultation";

    private static final String USER = "root";

    public static Connection getConnection() {
        String password = System.getenv("OHC_DB_PASSWORD");

        if (password == null || password.isBlank()) {
            System.err.println(
                    "Database password not configured. Set OHC_DB_PASSWORD."
            );
            return null;
        }

        try {
            Connection connection =
                    DriverManager.getConnection(URL, USER, password);

            System.out.println("Database connected successfully!");
            return connection;

        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}