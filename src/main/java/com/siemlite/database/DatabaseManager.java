package com.siemlite.database;

import com.siemlite.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Administra la creación y conexión de la base de datos SQLite.
 */
public final class DatabaseManager {

    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());

    private DatabaseManager() {
        // Constructor privado para evitar instanciación.
    }

    /**
     * Inicializa la base de datos y crea las tablas necesarias si no existen.
     */
    public static void initializeDatabase() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            LOGGER.log(Level.INFO, "Inicializando base de datos SQLite");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS logs ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "timestamp TEXT NOT NULL,"
                    + "source TEXT NOT NULL,"
                    + "event_type TEXT NOT NULL,"
                    + "message TEXT NOT NULL,"
                    + "ip_address TEXT NOT NULL"
                    + ")");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS alerts ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "timestamp TEXT NOT NULL,"
                    + "alert_type TEXT NOT NULL,"
                    + "severity TEXT NOT NULL,"
                    + "description TEXT NOT NULL"
                    + ")");
            LOGGER.log(Level.INFO, "Tablas de base de datos creadas o verificadas");
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Error al inicializar la base de datos", exception);
            throw new IllegalStateException("No se pudo inicializar la base de datos", exception);
        }
    }

    /**
     * Obtiene una conexión JDBC hacia la base de datos SQLite.
     *
     * @return conexión activa.
     * @throws SQLException si hay un error de conexión.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, "Controlador SQLite JDBC no encontrado", e);
        }
        return DriverManager.getConnection(DatabaseConfig.JDBC_URL);
    }
}
