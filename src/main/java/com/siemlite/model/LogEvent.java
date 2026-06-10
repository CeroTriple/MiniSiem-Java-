package com.siemlite.model;

/**
 * Representa un evento de log ingestando en el sistema SIEM.
 */
public class LogEvent {

    private final String timestamp;
    private final String source;
    private final String eventType;
    private final String message;
    private final String ipAddress;

    public LogEvent(String timestamp, String source, String eventType, String message, String ipAddress) {
        this.timestamp = timestamp;
        this.source = source;
        this.eventType = eventType;
        this.message = message;
        this.ipAddress = ipAddress;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public String getEventType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String toString() {
        return "LogEvent{" +
                "timestamp='" + timestamp + '\'' +
                ", source='" + source + '\'' +
                ", eventType='" + eventType + '\'' +
                ", message='" + message + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
