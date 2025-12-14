package brasilburgerjava.example.config.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseImpl implements Database {
    
    private Connection connection;
    
    private static final String JDBC_URL = "jdbc:postgresql://ep-bold-queen-ag70hh39-pooler.c-2.eu-central-1.aws.neon.tech/neondb?sslmode=require";
    private static final String USERNAME = "neondb_owner";
    private static final String PASSWORD = "npg_0O3jyUDlZPkG"; // Remplacez par votre vrai mot de passe
    
    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            props.setProperty("user", USERNAME);
            props.setProperty("password", PASSWORD);
            props.setProperty("ssl", "true");
            
            connection = DriverManager.getConnection(JDBC_URL, props);
            System.out.println("\n");
        }
        return connection;
    }
    
    @Override
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("\n");
        }
    }
}