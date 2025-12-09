package com.blog.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de utilidad para verificar el estado de salud de la base de datos.
 * 
 * <p>Esta clase proporciona métodos diagnósticos para verificar la disponibilidad,
 * estructura y funcionalidad de la base de datos MySQL. Es especialmente útil durante
 * el despliegue inicial o para troubleshooting de problemas de conectividad.</p>
 * 
 * <h3>Verificaciones disponibles:</h3>
 * <ul>
 *   <li><b>Disponibilidad:</b> Verifica que MySQL esté ejecutándose y acepte conexiones</li>
 *   <li><b>Existencia de tablas:</b> Verifica que las tablas 'usuarios' y 'articulos' existan</li>
 *   <li><b>Estructura de tablas:</b> Verifica que las tablas tengan las columnas requeridas</li>
 *   <li><b>Análisis de errores:</b> Proporciona mensajes descriptivos de errores comunes</li>
 * </ul>
 * 
 * <h3>Principios SOLID aplicados:</h3>
 * <ul>
 *   <li><b>S - Single Responsibility Principle (SRP):</b> Esta clase tiene una única
 *   responsabilidad: diagnosticar el estado de la base de datos. Ver Sección 2.1.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Otros principios de diseño:</h3>
 * <ul>
 *   <li><b>DRY (Don't Repeat Yourself):</b> Centraliza la lógica de diagnóstico en
 *   un solo lugar. Ver Sección 2.3.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>KISS (Keep It Simple, Stupid):</b> API simple con resultados claros.
 *   Ver Sección 2.3.2 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Ejemplo de uso:</h3>
 * <pre>{@code
 * DatabaseHealthCheck healthCheck = new DatabaseHealthCheck();
 * HealthCheckResult result = healthCheck.performCompleteHealthCheck();
 * 
 * if (result.isHealthy()) {
 *     System.out.println("Base de datos OK: " + result.getMessage());
 * } else {
 *     System.err.println("Problemas detectados: " + result.getMessage());
 *     for (String issue : result.getIssues()) {
 *         System.err.println("  - " + issue);
 *     }
 * }
 * }</pre>
 * 
 * @author Dylan David Silva Orrego
 * @author Maria Alejandra Munevar Barrera
 * @version 1.0
 * @since 2025-12-09
 * @see com.blog.dao.ConexionBD
 * @see com.blog.controller.SetupServlet
 */
public class DatabaseHealthCheck {

    private final ConexionBD conexionBD;

    public DatabaseHealthCheck() {
        this.conexionBD = ConexionBD.getInstancia();
    }

    /**
     * Resultado de una verificación de salud
     */
    public static class HealthCheckResult {
        private final boolean healthy;
        private final String message;
        private final List<String> issues;

        public HealthCheckResult(boolean healthy, String message) {
            this.healthy = healthy;
            this.message = message;
            this.issues = new ArrayList<>();
        }

        public boolean isHealthy() {
            return healthy;
        }

        public String getMessage() {
            return message;
        }

        public List<String> getIssues() {
            return issues;
        }

        public void addIssue(String issue) {
            this.issues.add(issue);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Estado: ").append(healthy ? "SALUDABLE" : "CON PROBLEMAS").append("\n");
            sb.append("Mensaje: ").append(message).append("\n");
            if (!issues.isEmpty()) {
                sb.append("Problemas detectados:\n");
                for (String issue : issues) {
                    sb.append("  - ").append(issue).append("\n");
                }
            }
            return sb.toString();
        }
    }

    /**
     * Verifica si la base de datos está disponible y responde correctamente
     * 
     * @return Resultado de la verificación con detalles
     */
    public HealthCheckResult checkDatabaseAvailability() {
        try (Connection conn = conexionBD.getConexion()) {
            if (conn == null || conn.isClosed()) {
                return new HealthCheckResult(false, 
                    "No se puede conectar a la base de datos. Verifique que MySQL esté ejecutándose.");
            }

            // Intentar ejecutar una consulta simple
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT 1");
                if (rs.next() && rs.getInt(1) == 1) {
                    return new HealthCheckResult(true, 
                        "La base de datos está disponible y responde correctamente.");
                }
            }

            return new HealthCheckResult(false, 
                "La base de datos está conectada pero no responde correctamente.");

        } catch (SQLException e) {
            String message = analyzeConnectionError(e);
            HealthCheckResult result = new HealthCheckResult(false, message);
            result.addIssue("Error técnico: " + e.getMessage());
            return result;
        }
    }

    /**
     * Analiza un error de conexión y proporciona un mensaje descriptivo
     * 
     * @param e Excepción SQL
     * @return Mensaje descriptivo del error
     */
    private String analyzeConnectionError(SQLException e) {
        String errorMsg = e.getMessage().toLowerCase();
        String sqlState = e.getSQLState();

        // Error de conexión rechazada
        if (errorMsg.contains("connection refused") || 
            errorMsg.contains("communications link failure")) {
            return "No se puede conectar a MySQL. Verifique que:\n" +
                   "  1. MySQL esté instalado y ejecutándose\n" +
                   "  2. El puerto 3306 esté disponible\n" +
                   "  3. No haya un firewall bloqueando la conexión";
        }

        // Error de autenticación
        if (errorMsg.contains("access denied") || 
            sqlState != null && sqlState.startsWith("28")) {
            return "Error de autenticación. Verifique que:\n" +
                   "  1. El usuario 'root' (o el especificado) exista\n" +
                   "  2. La contraseña sea correcta\n" +
                   "  3. El usuario tenga permisos sobre la base de datos";
        }

        // Base de datos no existe
        if (errorMsg.contains("unknown database") || 
            sqlState != null && sqlState.equals("42000")) {
            return "La base de datos 'blog_db' no existe. Debe:\n" +
                   "  1. Crear la base de datos ejecutando: CREATE DATABASE blog_db;\n" +
                   "  2. Ejecutar el script setup_database.sql o database/schema.sql para crear las tablas";
        }

        // Error de driver no encontrado
        if (errorMsg.contains("no suitable driver")) {
            return "Driver MySQL no encontrado. Verifique que:\n" +
                   "  1. El archivo mysql-connector-j esté en WEB-INF/lib/\n" +
                   "  2. El servidor esté reiniciado después de agregar el JAR";
        }

        // Error genérico
        return "Error al conectar con la base de datos: " + e.getMessage() + "\n" +
               "Estado SQL: " + (sqlState != null ? sqlState : "desconocido");
    }

    /**
     * Verifica si las tablas necesarias existen en la base de datos
     * 
     * @return Resultado de la verificación
     */
    public HealthCheckResult checkRequiredTables() {
        String[] requiredTables = {"usuarios", "articulos"};
        List<String> missingTables = new ArrayList<>();

        try (Connection conn = conexionBD.getConexion()) {
            if (conn == null || conn.isClosed()) {
                return new HealthCheckResult(false, 
                    "No se puede verificar las tablas: sin conexión a la base de datos.");
            }

            DatabaseMetaData metaData = conn.getMetaData();
            
            for (String tableName : requiredTables) {
                if (!tableExists(metaData, tableName)) {
                    missingTables.add(tableName);
                }
            }

            if (!missingTables.isEmpty()) {
                HealthCheckResult result = new HealthCheckResult(false, 
                    "Faltan tablas en la base de datos. Debe ejecutar el script setup_database.sql o database/schema.sql.");
                for (String tableName : missingTables) {
                    result.addIssue("La tabla '" + tableName + "' no existe");
                }
                return result;
            }

            return new HealthCheckResult(true, 
                "Todas las tablas necesarias están presentes.");

        } catch (SQLException e) {
            HealthCheckResult errorResult = new HealthCheckResult(false, 
                "Error al verificar las tablas: " + e.getMessage());
            errorResult.addIssue(e.getMessage());
            return errorResult;
        }
    }

    /**
     * Verifica si una tabla existe en la base de datos
     * 
     * @param metaData Metadata de la base de datos
     * @param tableName Nombre de la tabla a verificar
     * @return true si la tabla existe, false en caso contrario
     * @throws SQLException Error al consultar metadata
     */
    private boolean tableExists(DatabaseMetaData metaData, String tableName) throws SQLException {
        try (ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    /**
     * Verifica la estructura de una tabla específica
     * 
     * @param tableName Nombre de la tabla
     * @param requiredColumns Columnas requeridas
     * @return Resultado de la verificación
     */
    public HealthCheckResult checkTableStructure(String tableName, String[] requiredColumns) {
        HealthCheckResult result = new HealthCheckResult(true, 
            "La tabla '" + tableName + "' tiene la estructura correcta.");

        try (Connection conn = conexionBD.getConexion()) {
            if (conn == null || conn.isClosed()) {
                return new HealthCheckResult(false, 
                    "No se puede verificar la estructura: sin conexión a la base de datos.");
            }

            DatabaseMetaData metaData = conn.getMetaData();
            
            // Verificar si la tabla existe
            if (!tableExists(metaData, tableName)) {
                return new HealthCheckResult(false, 
                    "La tabla '" + tableName + "' no existe.");
            }

            // Verificar columnas
            try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
                List<String> existingColumns = new ArrayList<>();
                while (rs.next()) {
                    existingColumns.add(rs.getString("COLUMN_NAME").toLowerCase());
                }

                for (String requiredColumn : requiredColumns) {
                    if (!existingColumns.contains(requiredColumn.toLowerCase())) {
                        result.addIssue("Falta la columna '" + requiredColumn + "'");
                    }
                }
            }

            if (!result.getIssues().isEmpty()) {
                return new HealthCheckResult(false, 
                    "La tabla '" + tableName + "' no tiene la estructura correcta.");
            }

            return result;

        } catch (SQLException e) {
            HealthCheckResult errorResult = new HealthCheckResult(false, 
                "Error al verificar la estructura de '" + tableName + "': " + e.getMessage());
            errorResult.addIssue(e.getMessage());
            return errorResult;
        }
    }

    /**
     * Ejecuta una verificación completa de salud del sistema
     * 
     * @return Resultado completo de la verificación
     */
    public HealthCheckResult performCompleteHealthCheck() {
        HealthCheckResult availabilityResult = checkDatabaseAvailability();
        
        if (!availabilityResult.isHealthy()) {
            return availabilityResult;
        }

        HealthCheckResult tablesResult = checkRequiredTables();
        
        if (!tablesResult.isHealthy()) {
            return tablesResult;
        }

        return new HealthCheckResult(true, 
            "El sistema de base de datos está completamente funcional.");
    }
}
