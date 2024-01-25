package java12.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConfig {
    private static final String url = "jdbc:postgresql://localhost:5432/Java12-SQL";
    private static final String username = "postgres";
    private static final String password = "1234";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected Successfully");
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
