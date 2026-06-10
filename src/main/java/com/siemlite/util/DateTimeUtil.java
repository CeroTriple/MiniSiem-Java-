package com.siemlite.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilidad para manejo de fechas y horas.
 */
public final class DateTimeUtil {

    private static final Logger LOGGER = Logger.getLogger(DateTimeUtil.class.getName());
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateTimeUtil() {
        // Constructor privado para evitar instanciación.
    }

    /**
     * Devuelve el timestamp actual en formato ISO simple.
     *
     * @return timestamp actual como cadena.
     */
    public static String currentTimestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }

    /**
     * Valida si un timestamp de log es compatible con el formato esperado.
     *
     * @param timestamp timestamp a evaluar.
     * @return true si el timestamp es válido.
     */
    public static boolean isValidTimestamp(String timestamp) {
        try {
            LocalDateTime.parse(timestamp, FORMATTER);
            return true;
        } catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Timestamp inválido: {0}", timestamp);
            return false;
        }
    }
}
