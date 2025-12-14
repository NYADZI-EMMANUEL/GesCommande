package brasilburgerjava.example.config.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {
    Connection getConnection() throws SQLException;
    void closeConnection() throws SQLException;
}