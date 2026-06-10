package com.siemlite.detection;

import com.siemlite.model.Alert;
import com.siemlite.model.LogEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Regla que detecta múltiples intentos fallidos de inicio de sesión desde una misma IP.
 */
public class MultipleFailedLoginRule implements Rule {

    private static final Logger LOGGER = Logger.getLogger(MultipleFailedLoginRule.class.getName());
    private static final int FAILED_THRESHOLD = 5;

    @Override
    public List<Alert> evaluate(List<LogEvent> logs) {
        Map<String, Integer> failedCounts = new HashMap<>();
        for (LogEvent event : logs) {
            if ("LOGIN_FAILED".equalsIgnoreCase(event.getEventType())) {
                failedCounts.put(event.getIpAddress(), failedCounts.getOrDefault(event.getIpAddress(), 0) + 1);
            }
        }

        List<Alert> alerts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : failedCounts.entrySet()) {
            if (entry.getValue() >= FAILED_THRESHOLD) {
                LOGGER.info(() -> "Generando alerta MultipleFailedLoginRule para IP " + entry.getKey());
                alerts.add(new Alert(
                        eventTimestamp(),
                        "Multiple Failed Login",
                        "HIGH",
                        String.format("IP %s presentó %d intentos de LOGIN_FAILED", entry.getKey(), entry.getValue())
                ));
            }
        }
        return alerts;
    }

    private String eventTimestamp() {
        return java.time.LocalDateTime.now().toString();
    }
}
