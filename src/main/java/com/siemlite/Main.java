package com.siemlite;

import com.siemlite.service.SiemEngine;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Punto de entrada de la aplicación Mini SIEM Lite.
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Método principal que inicializa y ejecuta el motor SIEM.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        LOGGER.log(Level.INFO, "Iniciando Mini SIEM Lite");
        SiemEngine engine = new SiemEngine();
        engine.run();
        LOGGER.log(Level.INFO, "Ejecutado Mini SIEM Lite");
    }
}
