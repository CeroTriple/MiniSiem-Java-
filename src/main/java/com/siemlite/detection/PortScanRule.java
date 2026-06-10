package com.siemlite.detection;

import com.siemlite.model.Alert;
import com.siemlite.model.LogEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Regla que detecta patrones de exploración de puertos por frecuencia de eventos de una IP.
 */
public class PortScanRule implements Rule {

    private static final Logger LOGGER = Logger.getLogger(PortScanRule.class.getName());
    private static final int EVENT_THRESHOLD = 50;

    @Override
    public List<Alert> evaluate(List<LogEvent> logs) {
        Map<String, Integer> eventCounts = new HashMap<>();
        for (LogEvent event : logs) {
            eventCounts.put(event.getIpAddress(), eventCounts.getOrDefault(event.getIpAddress(), 0) + 1);
        }

        List<Alert> alerts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : eventCounts.entrySet()) {
            if (entry.getValue() > EVENT_THRESHOLD) {
                LOGGER.info(() -> "Generando alerta PortScanRule para IP " + entry.getKey());
                alerts.add(new Alert(
                        eventTimestamp(),
                        "Port Scan Activity",
                        "MEDIUM",
                        String.format("IP %s generó %d eventos distintos, posible escaneo de puertos", entry.getKey(), entry.getValue())
                ));
            }
        }
        return alerts;
    }

    private String eventTimestamp() {
        return java.time.LocalDateTime.now().toString();
    }
}
