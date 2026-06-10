package com.siemlite.parser;

import com.siemlite.model.LogEvent;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Convierte líneas de log de texto en objetos LogEvent.
 */
public class LogParser {

    private static final Logger LOGGER = Logger.getLogger(LogParser.class.getName());

    /**
     * Parsea una línea de log y retorna un LogEvent si el formato es válido.
     *
     * @param line línea de log a parsear.
     * @return Optional conteniendo el LogEvent o vacío si no se puede parsear.
     */
    public Optional<LogEvent> parseLine(String line) {
        if (line == null || line.isBlank()) {
            return Optional.empty();
        }

        String[] tokens = line.trim().split(" ");
        if (tokens.length < 6) {
            LOGGER.log(Level.WARNING, "Línea de log inválida: {0}", line);
            return Optional.empty();
        }

        try {
            String timestamp = tokens[0] + " " + tokens[1];
            String source = tokens[2];
            String eventType = tokens[3];
            String ipAddress = tokens[tokens.length - 1];
            StringBuilder messageBuilder = new StringBuilder();
            for (int index = 4; index < tokens.length - 1; index++) {
                messageBuilder.append(tokens[index]);
                if (index < tokens.length - 2) {
                    messageBuilder.append(' ');
                }
            }
            String message = messageBuilder.toString();
            return Optional.of(new LogEvent(timestamp, source, eventType, message, ipAddress));
        } catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Error parseando la línea de log: {0}", line);
            return Optional.empty();
        }
    }
}
