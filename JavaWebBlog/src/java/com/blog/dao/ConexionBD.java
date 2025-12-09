package com.blog.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConexionBD - Clase Singleton para gestionar la conexión a la base de datos
 * 
 * Principio SOLID aplicado: S - Single Responsibility Principle
 * (Responsabilidad Única).
 * Ver Sección 1.1 en PRINCIPIOS_Y_PATRONES.tex
 * 
 * Patrón de Diseño: Singleton.
 * Ver Sección 3.1 en PRINCIPIOS_Y_PATRONES.tex
 * 
 * Esta clase tiene una única misión en la vida: darnos conexiones a la base de
 * datos.
 * No le importa si guardamos usuarios, artículos o recetas de cocina, solo le
 * importa conectar.
 */
public class ConexionBD {

    private static final Properties properties = new Properties();
    private static volatile ConexionBD instancia;

    // Constructor privado para Singleton
    private ConexionBD() {
        try {
            System.out.println("[ConexionBD] Intentando cargar db.properties...");
            InputStream input = ConexionBD.class.getResourceAsStream("db.properties");

            if (input == null) {
                System.out.println(
                        "[ConexionBD] No se encontró con getResourceAsStream relativo. Intentando absoluto...");
                input = ConexionBD.class.getResourceAsStream("/com/blog/dao/db.properties");
            }

            if (input == null) {
                System.out.println(
                        "[ConexionBD] No se encontró con getResourceAsStream absoluto. Intentando ContextClassLoader...");
                input = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("com/blog/dao/db.properties");
            }

            if (input == null) {
                System.err.println(
                        "[ConexionBD] Error FATAL: No se pudo encontrar el archivo db.properties en el classpath.");
                throw new RuntimeException("No se pudo encontrar el archivo db.properties");
            }

            properties.load(input);
            System.out.println(
                    "[ConexionBD] db.properties cargado correctamente. URL: " + properties.getProperty("db.url"));

            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[ConexionBD] Driver MySQL cargado.");

        } catch (Exception e) {
            System.err.println("[ConexionBD] Error al inicializar conexión: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la configuración de la base de datos o el driver", e);
        }
    }

    /**
     * Obtiene la instancia única de ConexionBD
     * 
     * @return Instancia de ConexionBD
     */
    public static ConexionBD getInstancia() {
        if (instancia == null) {
            synchronized (ConexionBD.class) {
                if (instancia == null) {
                    instancia = new ConexionBD();
                }
            }
        }
        return instancia;
    }

    /**
     * Obtiene una conexión a la base de datos
     * 
     * @return Conexión activa
     * @throws SQLException Error al conectar
     */
    public Connection getConexion() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password"));
    }

    /**
     * Cierra una conexión
     * 
     * @param conn Conexión a cerrar
     */
    public void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
