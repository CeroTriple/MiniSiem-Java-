package com.siemlite.detection;

import com.siemlite.model.Alert;
import com.siemlite.model.LogEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Regla que detecta IPs sospechosas basadas en una lista negra interna.
 */
public class SuspiciousIpRule implements Rule {

    private static final Logger LOGGER = Logger.getLogger(SuspiciousIpRule.class.getName());
    private static final Set<String> BLACKLISTED_IPS = Set.of(
            "10.10.10.10",
            "45.33.22.11",
            "185.100.87.44"
    );

    @Override
    public List<Alert> evaluate(List<LogEvent> logs) {
        Set<String> detectedIps = new HashSet<>();
        List<Alert> alerts = new ArrayList<>();
        for (LogEvent event : logs) {
            if (BLACKLISTED_IPS.contains(event.getIpAddress()) && detectedIps.add(event.getIpAddress())) {
                LOGGER.info(() -> "Generando alerta SuspiciousIpRule para IP " + event.getIpAddress());
                alerts.add(new Alert(
                        eventTimestamp(),
                        "Suspicious IP Detected",
                        "CRITICAL",
                        String.format("IP %s encontrada en lista negra interna", event.getIpAddress())
                ));
            }
        }
        return alerts;
    }

    private String eventTimestamp() {
        return java.time.LocalDateTime.now().toString();
    }
}
