package com.siemlite.repository;

import com.siemlite.database.DatabaseManager;
import com.siemlite.model.LogEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repositorio para operación de persistencia de eventos de log.
 */
public class LogRepository {

    private static final Logger LOGGER = Logger.getLogger(LogRepository.class.getName());

    /**
     * Guarda un evento de log en la base de datos.
     *
     * @param event evento a guardar.
     */
    public void save(LogEvent event) {
        String query = "INSERT INTO logs(timestamp, source, event_type, message, ip_address) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, event.getTimestamp());
            preparedStatement.setString(2, event.getSource());
            preparedStatement.setString(3, event.getEventType());
            preparedStatement.setString(4, event.getMessage());
            preparedStatement.setString(5, event.getIpAddress());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Error guardando el evento de log", exception);
        }
    }

    /**
     * Recupera todos los eventos de log almacenados.
     *
     * @return lista de eventos de log.
     */
    public List<LogEvent> findAll() {
        List<LogEvent> logs = new ArrayList<>();
        String query = "SELECT timestamp, source, event_type, message, ip_address FROM logs";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                logs.add(mapResultSet(resultSet));
            }
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Error recuperando todos los logs", exception);
        }
        return logs;
    }

    /**
     * Busca eventos de log por dirección IP.
     *
     * @param ip dirección IP a buscar.
     * @return lista de eventos que coinciden con la IP.
     */
    public List<LogEvent> findByIp(String ip) {
        List<LogEvent> logs = new ArrayList<>();
        String query = "SELECT timestamp, source, event_type, message, ip_address FROM logs WHERE ip_address = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, ip);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    logs.add(mapResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Error recuperando los logs por IP", exception);
        }
        return logs;
    }

    /**
     * Busca eventos de log por tipo de evento.
     *
     * @param eventType tipo de evento a buscar.
     * @return lista de eventos que coinciden con el tipo.
     */
    public List<LogEvent> findByEventType(String eventType) {
        List<LogEvent> logs = new ArrayList<>();
        String query = "SELECT timestamp, source, event_type, message, ip_address FROM logs WHERE event_type = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, eventType);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    logs.add(mapResultSet(resultSet));
                }
            }
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Error recuperando los logs por tipo de evento", exception);
        }
        return logs;
    }

    private LogEvent mapResultSet(ResultSet resultSet) throws SQLException {
        return new LogEvent(
                resultSet.getString("timestamp"),
                resultSet.getString("source"),
                resultSet.getString("event_type"),
                resultSet.getString("message"),
                resultSet.getString("ip_address")
        );
    }
}
