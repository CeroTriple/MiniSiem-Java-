package com.siemlite.alert;

import com.siemlite.model.Alert;
import java.util.List;

/**
 * Formatea alertas para impresión en consola.
 */
public final class AlertPrinter {

    private AlertPrinter() {
        // Constructor privado para evitar instanciación.
    }

    /**
     * Devuelve el texto formateado para la lista de alertas.
     *
     * @param alerts alertas a formatear.
     * @return cadena con el formato de salida.
     */
    public static String formatAlerts(List<Alert> alerts) {
        StringBuilder builder = new StringBuilder();
        for (Alert alert : alerts) {
            builder.append("[" + alert.getSeverity() + "]\n");
            builder.append(alert.getAlertType()).append("\n");
            builder.append(alert.getDescription()).append("\n\n");
        }
        return builder.toString();
    }
}
