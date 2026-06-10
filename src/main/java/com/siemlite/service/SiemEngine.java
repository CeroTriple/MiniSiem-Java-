package com.siemlite.service;

import com.siemlite.alert.AlertPrinter;
import com.siemlite.database.DatabaseManager;
import com.siemlite.detection.MultipleFailedLoginRule;
import com.siemlite.detection.PortScanRule;
import com.siemlite.detection.Rule;
import com.siemlite.detection.SuspiciousIpRule;
import com.siemlite.model.Alert;
import com.siemlite.model.LogEvent;
import com.siemlite.parser.LogParser;
import com.siemlite.repository.AlertRepository;
import com.siemlite.repository.LogRepository;
import com.siemlite.util.DateTimeUtil;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Motor principal que orquesta la ingesta, el análisis y la generación de alertas.
 */
public class SiemEngine {

    private static final Logger LOGGER = Logger.getLogger(SiemEngine.class.getName());
    private static final String LOG_FOLDER = "logs";

    private final LogRepository logRepository;
    private final AlertRepository alertRepository;
    private final LogParser parser;
    private final List<Rule> rules;

    public SiemEngine() {
        this.logRepository = new LogRepository();
        this.alertRepository = new AlertRepository();
        this.parser = new LogParser();
        this.rules = List.of(
                new MultipleFailedLoginRule(),
                new PortScanRule(),
                new SuspiciousIpRule()
        );
    }

    /**
     * Ejecuta el flujo completo del motor SIEM.
     */
    public void run() {
        try {
            DatabaseManager.initializeDatabase();
            List<LogEvent> logs = loadLogs();
            saveLogs(logs);
            List<Alert> alerts = executeRules(logs);
            saveAlerts(alerts);
            showSummary(logs, alerts);
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error en la ejecución del motor SIEM", exception);
        }
    }

    private List<LogEvent> loadLogs() {
        Path folder = Paths.get(LOG_FOLDER);
        List<LogEvent> logEvents = new ArrayList<>();
        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            LOGGER.log(Level.WARNING, "No se encontró la carpeta de logs: {0}", LOG_FOLDER);
            return logEvents;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.log")) {
            for (Path filePath : stream) {
                LOGGER.log(Level.INFO, "Procesando archivo de logs: {0}", filePath);
                Files.lines(filePath)
                        .map(parser::parseLine)
                        .flatMap(Optional::stream)
                        .filter(event -> DateTimeUtil.isValidTimestamp(event.getTimestamp()))
                        .forEach(logEvents::add);
            }
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Error leyendo archivos de log", exception);
        }

        LOGGER.log(Level.INFO, "Total de eventos de log cargados: {0}", logEvents.size());
        return logEvents;
    }

    private void saveLogs(List<LogEvent> logs) {
        logs.forEach(logRepository::save);
        LOGGER.log(Level.INFO, "Eventos de log guardados en base de datos: {0}", logs.size());
    }

    private List<Alert> executeRules(List<LogEvent> logs) {
        List<Alert> alerts = new ArrayList<>();
        for (Rule rule : rules) {
            alerts.addAll(rule.evaluate(logs));
        }
        LOGGER.log(Level.INFO, "Alertas generadas por reglas: {0}", alerts.size());
        return alerts;
    }

    private void saveAlerts(List<Alert> alerts) {
        alerts.forEach(alertRepository::save);
        LOGGER.log(Level.INFO, "Alertas guardadas en base de datos: {0}", alerts.size());
    }

    private void showSummary(List<LogEvent> logs, List<Alert> alerts) {
        System.out.println("=================================");
        System.out.println("MINI SIEM LITE");
        System.out.println("=================================");
        System.out.println("Logs procesados: " + logs.size());
        System.out.println("Alertas generadas: " + alerts.size());
        System.out.println();
        if (alerts.isEmpty()) {
            System.out.println("No se generaron alertas.");
        } else {
            System.out.print(AlertPrinter.formatAlerts(alerts));
        }
        System.out.println("=================================");
    }
}
