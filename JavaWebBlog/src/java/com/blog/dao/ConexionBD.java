package com.blog.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * ConexionBD - Clase Singleton para gestionar la conexión a la base de datos
 * con pool de conexiones manual y reconexión automática
 * 
 * Principio SOLID aplicado: S - Single Responsibility Principle
 * (Responsabilidad Única).
 * Ver Sección 1.1 en PRINCIPIOS_Y_PATRONES.tex
 * 
 * Patrón de Diseño: Singleton + Object Pool.
 * Ver Sección 3.1 en PRINCIPIOS_Y_PATRONES.tex
 * 
 * Esta clase tiene una única misión en la vida: darnos conexiones a la base de
 * datos de forma confiable y eficiente.
 * Implementa un pool de conexiones simple sin dependencias externas.
 */
public class ConexionBD {

    private static final Properties properties = new Properties();
    private static volatile ConexionBD instancia;

    // Pool de conexiones
    private final List<PooledConnection> connectionPool;
    private final int maxConnections;
    private final int minConnections;
    
    // Configuración de reintentos
    private final int maxRetries;
    private final long initialRetryDelayMs;
    
    // Configuración de timeouts
    private final int connectionTimeoutSeconds;
    private final int validationTimeoutSeconds;
    
    // Configuración de validación
    private final String validationQuery;
    private final boolean testOnBorrow;
    private final long maxIdleTimeMs;

    /**
     * Clase interna para representar una conexión del pool
     */
    private static class PooledConnection {
        private Connection connection;
        private long lastUsedTime;
        private boolean inUse;

        public PooledConnection(Connection connection) {
            this.connection = connection;
            this.lastUsedTime = System.currentTimeMillis();
            this.inUse = false;
        }

        public synchronized boolean isInUse() {
            return inUse;
        }

        public synchronized void setInUse(boolean inUse) {
            this.inUse = inUse;
            if (!inUse) {
                this.lastUsedTime = System.currentTimeMillis();
            }
        }

        public long getIdleTime() {
            return System.currentTimeMillis() - lastUsedTime;
        }
    }

    // Constructor privado para Singleton
    private ConexionBD() {
        try {
            System.out.println("[ConexionBD] Inicializando sistema de conexión a base de datos...");
            
            // Cargar configuración
            loadProperties();
            
            // Cargar parámetros del pool
            maxConnections = Integer.parseInt(properties.getProperty("pool.maxConnections", "10"));
            minConnections = Integer.parseInt(properties.getProperty("pool.minConnections", "2"));
            
            // Cargar parámetros de reintentos
            maxRetries = Integer.parseInt(properties.getProperty("retry.maxAttempts", "3"));
            initialRetryDelayMs = Long.parseLong(properties.getProperty("retry.initialDelayMs", "1000"));
            
            // Cargar parámetros de timeout
            connectionTimeoutSeconds = Integer.parseInt(properties.getProperty("connection.timeoutSeconds", "10"));
            validationTimeoutSeconds = Integer.parseInt(properties.getProperty("validation.timeoutSeconds", "5"));
            
            // Cargar parámetros de validación
            validationQuery = properties.getProperty("validation.query", "SELECT 1");
            testOnBorrow = Boolean.parseBoolean(properties.getProperty("pool.testOnBorrow", "true"));
            long maxIdleMinutes = Long.parseLong(properties.getProperty("connection.maxIdleMinutes", "30"));
            maxIdleTimeMs = TimeUnit.MINUTES.toMillis(maxIdleMinutes);

            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[ConexionBD] Driver MySQL cargado.");

            // Inicializar el pool de conexiones
            connectionPool = new ArrayList<>();
            initializePool();

            System.out.println("[ConexionBD] Sistema de conexión inicializado correctamente.");
            System.out.println("[ConexionBD] Pool: min=" + minConnections + ", max=" + maxConnections);

        } catch (Exception e) {
            System.err.println("[ConexionBD] Error al inicializar conexión: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la configuración de la base de datos o el driver", e);
        }
    }

    /**
     * Carga el archivo de propiedades db.properties
     * 
     * @throws Exception si no se puede cargar el archivo
     */
    private void loadProperties() throws Exception {
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
    }

    /**
     * Inicializa el pool con el número mínimo de conexiones
     */
    private void initializePool() {
        System.out.println("[ConexionBD] Inicializando pool con " + minConnections + " conexiones...");
        synchronized (connectionPool) {
            for (int i = 0; i < minConnections; i++) {
                try {
                    Connection conn = createNewConnection();
                    connectionPool.add(new PooledConnection(conn));
                } catch (SQLException e) {
                    System.err.println("[ConexionBD] Advertencia: No se pudo crear conexión inicial #" + (i + 1) 
                        + ": " + e.getMessage());
                    // No lanzar excepción, el pool puede funcionar con menos conexiones
                }
            }
        }
        System.out.println("[ConexionBD] Pool inicializado con " + connectionPool.size() + " conexiones.");
    }

    /**
     * Crea una nueva conexión física a la base de datos
     * 
     * @return Nueva conexión
     * @throws SQLException Error al crear la conexión
     */
    private Connection createNewConnection() throws SQLException {
        DriverManager.setLoginTimeout(connectionTimeoutSeconds);
        Connection conn = DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password"));
        
        // Configurar la conexión
        conn.setAutoCommit(true);
        
        return conn;
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
     * Obtiene una conexión a la base de datos del pool con reintentos automáticos
     * 
     * @return Conexión activa y validada
     * @throws SQLException Error al conectar después de todos los reintentos
     */
    public Connection getConexion() throws SQLException {
        return getConexionWithRetry(maxRetries);
    }

    /**
     * Obtiene una conexión con lógica de reintento
     * 
     * @param retriesLeft Número de reintentos restantes
     * @return Conexión activa
     * @throws SQLException Error al conectar
     */
    private Connection getConexionWithRetry(int retriesLeft) throws SQLException {
        try {
            // Limpiar conexiones inactivas
            cleanupIdleConnections();
            
            // Intentar obtener una conexión del pool
            Connection conn = getConnectionFromPool();
            
            if (conn != null) {
                return conn;
            }
            
            // Si no hay conexión disponible, intentar crear una nueva con control de concurrencia
            synchronized (connectionPool) {
                // Re-verificar después de obtener el lock por si otra thread creó una conexión
                conn = getConnectionFromPool();
                if (conn != null) {
                    return conn;
                }
                
                // Verificar si el pool no está lleno
                if (connectionPool.size() < maxConnections) {
                    try {
                        Connection newConn = createNewConnection();
                        PooledConnection pooledConn = new PooledConnection(newConn);
                        pooledConn.setInUse(true);
                        connectionPool.add(pooledConn);
                        System.out.println("[ConexionBD] Nueva conexión creada. Pool size: " + connectionPool.size());
                        return newConn;
                    } catch (SQLException sqlEx) {
                        // La creación de conexión falló, lanzar para que el retry logic lo maneje
                        throw sqlEx;
                    }
                }
            }
            
            // Pool lleno, esperar un poco y reintentar
            throw new SQLException("Pool de conexiones lleno. No hay conexiones disponibles.");
            
        } catch (SQLException e) {
            if (retriesLeft > 0) {
                long delay = initialRetryDelayMs * (long) Math.pow(2, maxRetries - retriesLeft);
                System.err.println("[ConexionBD] Error al obtener conexión. Reintentando en " + delay 
                    + "ms... (Reintentos restantes: " + retriesLeft + ")");
                System.err.println("[ConexionBD] Error: " + e.getMessage());
                
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Reintento interrumpido", ie);
                }
                
                return getConexionWithRetry(retriesLeft - 1);
            } else {
                System.err.println("[ConexionBD] No se pudo obtener conexión después de " 
                    + maxRetries + " reintentos.");
                throw new SQLException("No se pudo conectar a la base de datos después de " 
                    + maxRetries + " reintentos. Error: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Obtiene una conexión del pool
     * 
     * @return Conexión disponible o null si no hay ninguna
     * @throws SQLException Error al validar la conexión
     */
    private Connection getConnectionFromPool() throws SQLException {
        for (PooledConnection pooledConn : connectionPool) {
            synchronized (pooledConn) {
                if (!pooledConn.isInUse()) {
                    // Validar la conexión si está configurado
                    if (testOnBorrow && !isConnectionValid(pooledConn.connection)) {
                        System.out.println("[ConexionBD] Conexión inválida detectada, recreando...");
                        try {
                            pooledConn.connection.close();
                        } catch (SQLException e) {
                            // Ignorar errores al cerrar
                        }
                        pooledConn.connection = createNewConnection();
                    }
                    
                    pooledConn.setInUse(true);
                    return pooledConn.connection;
                }
            }
        }
        return null;
    }

    /**
     * Valida que una conexión esté activa
     * 
     * @param conn Conexión a validar
     * @return true si la conexión es válida
     */
    private boolean isConnectionValid(Connection conn) {
        if (conn == null) {
            return false;
        }
        
        try {
            if (conn.isClosed()) {
                return false;
            }
            
            // Ejecutar consulta de validación
            try (Statement stmt = conn.createStatement()) {
                stmt.setQueryTimeout(validationTimeoutSeconds);
                try (ResultSet rs = stmt.executeQuery(validationQuery)) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            System.err.println("[ConexionBD] Error al validar conexión: " + e.getMessage());
            return false;
        }
    }

    /**
     * Limpia conexiones que han estado inactivas por mucho tiempo
     */
    private void cleanupIdleConnections() {
        synchronized (connectionPool) {
            List<PooledConnection> toRemove = new ArrayList<>();
            
            for (PooledConnection pooledConn : connectionPool) {
                synchronized (pooledConn) {
                    if (!pooledConn.isInUse() && pooledConn.getIdleTime() > maxIdleTimeMs) {
                        // Mantener al menos el mínimo de conexiones
                        if (connectionPool.size() - toRemove.size() > minConnections) {
                            toRemove.add(pooledConn);
                            try {
                                pooledConn.connection.close();
                                System.out.println("[ConexionBD] Conexión inactiva cerrada. Tiempo inactivo: " 
                                    + TimeUnit.MILLISECONDS.toMinutes(pooledConn.getIdleTime()) + " minutos");
                            } catch (SQLException e) {
                                System.err.println("[ConexionBD] Error al cerrar conexión inactiva: " + e.getMessage());
                            }
                        }
                    }
                }
            }
            
            connectionPool.removeAll(toRemove);
        }
    }

    /**
     * Devuelve una conexión al pool
     * 
     * @param conn Conexión a devolver
     */
    public void cerrarConexion(Connection conn) {
        if (conn == null) {
            return;
        }

        // Buscar la conexión en el pool y marcarla como disponible
        for (PooledConnection pooledConn : connectionPool) {
            if (pooledConn.connection == conn) {
                synchronized (pooledConn) {
                    pooledConn.setInUse(false);
                }
                return;
            }
        }

        // Si no está en el pool, cerrarla directamente
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("[ConexionBD] Error al cerrar conexión: " + e.getMessage());
        }
    }

    /**
     * Cierra todas las conexiones del pool (para shutdown)
     */
    public void shutdown() {
        System.out.println("[ConexionBD] Cerrando todas las conexiones del pool...");
        synchronized (connectionPool) {
            for (PooledConnection pooledConn : connectionPool) {
                try {
                    pooledConn.connection.close();
                } catch (SQLException e) {
                    System.err.println("[ConexionBD] Error al cerrar conexión durante shutdown: " + e.getMessage());
                }
            }
            connectionPool.clear();
        }
        System.out.println("[ConexionBD] Pool cerrado.");
    }

    /**
     * Obtiene estadísticas del pool de conexiones
     * 
     * @return String con estadísticas
     */
    public String getPoolStats() {
        synchronized (connectionPool) {
            int total = connectionPool.size();
            int inUse = 0;
            for (PooledConnection pooledConn : connectionPool) {
                if (pooledConn.isInUse()) {
                    inUse++;
                }
            }
            return String.format("Pool Stats - Total: %d, En uso: %d, Disponibles: %d, Máximo: %d",
                    total, inUse, total - inUse, maxConnections);
        }
    }
}

