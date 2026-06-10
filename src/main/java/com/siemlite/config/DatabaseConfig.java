package com.siemlite.config;

/**
 * Provee la configuración de conexión para la base de datos SQLite.
 */
public final class DatabaseConfig {

    private DatabaseConfig() {
        // Constructor privado para evitar instanciación.
    }

    /**
     * URL JDBC para la base de datos SQLite local.
     */
    public static final String JDBC_URL = "jdbc:sqlite:siemlite.db";
}
