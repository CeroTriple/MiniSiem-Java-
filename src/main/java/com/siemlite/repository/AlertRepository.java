package com.siemlite.repository;

import com.siemlite.database.DatabaseManager;
import com.siemlite.model.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repositorio para operación de persistencia de alertas.
 */
public class AlertRepository {

    private static final Logger LOGGER = Logger.getLogger(AlertRepository.class.getName());

    /**
     * Guarda una alerta en la base de datos.
     *
     * @param alert alerta a guardar.
     */
    public void save(Alert alert) {
        String query = "INSERT INTO alerts(timestamp, alert_type, severity, description) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, alert.getTimestamp());
            preparedStatement.setString(2, alert.getAlertType());
            preparedStatement.setString(3, alert.getSeverity());
            preparedStatement.setString(4, alert.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Error guardando la alerta", exception);
        }
    }

    /**
     * Recupera todas las alertas almacenadas.
     *
     * @return lista de alertas.
     */
    public List<Alert> findAll() {
        List<Alert> alerts = new ArrayList<>();
        String query = "SELECT timestamp, alert_type, severity, description FROM alerts";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                alerts.add(new Alert(
                        resultSet.getString("timestamp"),
                        resultSet.getString("alert_type"),
                        resultSet.getString("severity"),
                        resultSet.getString("description")
                ));
            }
        } catch (SQLException exception) {
            LOGGER.log(Level.SEVERE, "Error recuperando alertas", exception);
        }
        return alerts;
    }
}
