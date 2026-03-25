package infrastructure.database;

import infrastructure.config.Configs;
import infrastructure.config.DataBaseConfig;
import infrastructure.utils.Loggers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.Level;


public enum DatabaseConnection implements DatabaseConnectionInterface {
    INSTANCE;

    private Connection connection;

    DatabaseConnection() {
        final DataBaseConfig confDB = Configs.getInstanceDB();
        try {
            Class.forName("org.postgresql.Driver");

            Properties props = new Properties();
            props.setProperty("user", confDB.userDB());
            props.setProperty("password", confDB.passwordDB());
            props.setProperty("ssl", "false");

            this.connection = DriverManager.getConnection(confDB.urlDB(), props);
            Loggers.CONSOLE.info("Database connection established successfully");
        } catch (ClassNotFoundException | SQLException e) {
            Loggers.CONSOLE.log(Level.ERROR, "Failed to initialize database connection", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                reconnect();
            }
        } catch (SQLException e) {
            Loggers.CONSOLE.log(Level.ERROR, "Error checking connection status", e);
            reconnect();
        }
        return connection;
    }

    private void reconnect() {
        final DataBaseConfig confDB = Configs.getInstanceDB();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            Properties props = new Properties();
            props.setProperty("user", confDB.userDB());
            props.setProperty("password", confDB.passwordDB());
            this.connection = DriverManager.getConnection(confDB.urlDB(), props);
            Loggers.CONSOLE.info("Database reconnected successfully");
        } catch (SQLException e) {
            Loggers.CONSOLE.log(Level.ERROR, "Failed to reconnect to database", e);
            throw new RuntimeException("Database reconnection failed", e);
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Loggers.CONSOLE.info("Database connection closed");
            }
        } catch (SQLException e) {
            Loggers.CONSOLE.log(Level.ERROR, "Error closing database connection", e);
        }
    }
}
