package com.siemlite.model;

/**
 * Representa una alerta de seguridad generada por el motor de detección.
 */
public class Alert {

    private final String timestamp;
    private final String alertType;
    private final String severity;
    private final String description;

    public Alert(String timestamp, String alertType, String severity, String description) {
        this.timestamp = timestamp;
        this.alertType = alertType;
        this.severity = severity;
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getAlertType() {
        return alertType;
    }

    public String getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "timestamp='" + timestamp + '\'' +
                ", alertType='" + alertType + '\'' +
                ", severity='" + severity + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
