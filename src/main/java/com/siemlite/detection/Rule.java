package com.siemlite.detection;

import com.siemlite.model.Alert;
import com.siemlite.model.LogEvent;
import java.util.List;

/**
 * Interfaz para las reglas de detección de seguridad.
 */
public interface Rule {

    /**
     * Evalúa los eventos de log y retorna la lista de alertas generadas.
     *
     * @param logs eventos de log a evaluar.
     * @return lista de alertas generadas por la regla.
     */
    List<Alert> evaluate(List<LogEvent> logs);
}
