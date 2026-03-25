package infrastructure.database;

import java.sql.Connection;

public interface DatabaseConnectionInterface {
    Connection getConnection();
    void closeConnection();
}
