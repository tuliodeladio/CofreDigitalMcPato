package factory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    // URL FIAP: jdbc:oracle:thin:@//oracle.fiap.com.br:1521/ORCL
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASS");

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("Erro na conexão: " + e.getMessage());
        }
    }
}