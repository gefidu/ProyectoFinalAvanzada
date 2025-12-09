package com.blog.dao;

import com.blog.model.Articulo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación MySQL del patrón DAO para operaciones de artículos.
 * 
 * <p>Esta clase proporciona la implementación concreta de {@link IArticuloDAO} usando
 * MySQL como sistema de gestión de base de datos. Maneja todas las operaciones CRUD
 * (Create, Read, Update, Delete) para artículos, utilizando JDBC y PreparedStatements
 * para prevenir inyección SQL.</p>
 * 
 * <h3>Características de resiliencia:</h3>
 * <ul>
 *   <li><b>Reintentos automáticos:</b> Las operaciones se reintentan hasta 2 veces
 *   con backoff exponencial en caso de fallo</li>
 *   <li><b>Pool de conexiones:</b> Utiliza el pool de {@link ConexionBD} para
 *   gestión eficiente de conexiones</li>
 *   <li><b>LEFT JOIN:</b> Las consultas usan LEFT JOIN para mostrar artículos aunque
 *   el autor haya sido eliminado</li>
 * </ul>
 * 
 * <h3>Principios SOLID aplicados:</h3>
 * <ul>
 *   <li><b>S - Single Responsibility Principle (SRP):</b> Esta clase solo se encarga
 *   de la persistencia de artículos en MySQL. Ver Sección 2.1.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>L - Liskov Substitution Principle (LSP):</b> Cumple completamente el contrato
 *   de {@link IArticuloDAO}, puede sustituirse por cualquier otra implementación.
 *   Ver Sección 2.1.3 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>D - Dependency Inversion Principle (DIP):</b> Los controladores dependen de
 *   la interfaz {@link IArticuloDAO}, no de esta clase concreta. Ver Sección 2.1.5 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Patrón de diseño:</h3>
 * <ul>
 *   <li><b>DAO (Data Access Object):</b> Encapsula toda la lógica de acceso a datos
 *   de artículos. Ver Sección 2.4.2 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>Singleton (indirecto):</b> Utiliza {@link ConexionBD} que implementa Singleton.
 *   Ver Sección 2.4.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Seguridad:</h3>
 * <p>Utiliza PreparedStatements en todas las operaciones SQL para prevenir inyección SQL.
 * Nunca concatena directamente datos del usuario en las consultas.</p>
 * 
 * @author Dylan David Silva Orrego
 * @author Maria Alejandra Munevar Barrera
 * @version 1.0
 * @since 2025-12-09
 * @see com.blog.dao.IArticuloDAO
 * @see com.blog.dao.ConexionBD
 * @see com.blog.model.Articulo
 */
public class MySQLArticuloDAO implements IArticuloDAO {

    private final ConexionBD conexionBD;
    private static final int MAX_OPERATION_RETRIES = 2;

    public MySQLArticuloDAO() {
        this.conexionBD = ConexionBD.getInstancia();
    }

    /**
     * Ejecuta una operación con reintentos automáticos
     * 
     * @param <T> Tipo de retorno
     * @param operation Operación a ejecutar
     * @param operationName Nombre de la operación para logging
     * @return Resultado de la operación
     * @throws SQLException Error después de todos los reintentos
     */
    private <T> T executeWithRetry(DatabaseOperation<T> operation, String operationName) throws SQLException {
        SQLException lastException = null;
        
        for (int attempt = 0; attempt <= MAX_OPERATION_RETRIES; attempt++) {
            try {
                return operation.execute();
            } catch (SQLException e) {
                lastException = e;
                if (attempt < MAX_OPERATION_RETRIES) {
                    System.err.println("[MySQLArticuloDAO] Error en " + operationName + 
                        ", reintentando... (intento " + (attempt + 1) + "/" + MAX_OPERATION_RETRIES + ")");
                    try {
                        Thread.sleep(500 * (attempt + 1)); // Backoff incremental
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SQLException("Operación interrumpida", ie);
                    }
                }
            }
        }
        
        // Si llegamos aquí, todos los reintentos fallaron
        throw new SQLException("Error en " + operationName + " después de " + 
            (MAX_OPERATION_RETRIES + 1) + " intentos: " + lastException.getMessage(), lastException);
    }

    /**
     * Interfaz funcional para operaciones de base de datos
     */
    @FunctionalInterface
    private interface DatabaseOperation<T> {
        T execute() throws SQLException;
    }

    @Override
    public List<Articulo> listarTodos() throws SQLException {
        return executeWithRetry(() -> {
            List<Articulo> articulos = new ArrayList<>();
            // Changed INNER JOIN to LEFT JOIN to ensure articles are shown even if author
            // is deleted
            // Used COALESCE to provide a default value for unknown authors
            String sql = "SELECT a.id, a.titulo, a.contenido, a.fecha_publicacion, a.autor_id, " +
                    "COALESCE(u.nombre, 'Usuario Desconocido') as autor_nombre " +
                    "FROM articulos a " +
                    "LEFT JOIN usuarios u ON a.autor_id = u.id " +
                    "ORDER BY a.fecha_publicacion DESC";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        Articulo articulo = new Articulo();
                        articulo.setId(rs.getInt("id"));
                        articulo.setTitulo(rs.getString("titulo"));
                        articulo.setContenido(rs.getString("contenido"));
                        articulo.setFechaPublicacion(rs.getTimestamp("fecha_publicacion").toLocalDateTime());
                        articulo.setAutorId(rs.getInt("autor_id"));
                        articulo.setAutorNombre(rs.getString("autor_nombre"));
                        articulos.add(articulo);
                    }
                }
                return articulos;
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "listarTodos");
    }

    @Override
    public Articulo obtenerPorId(int id) throws SQLException {
        return executeWithRetry(() -> {
            Articulo articulo = null;
            String sql = "SELECT a.id, a.titulo, a.contenido, a.fecha_publicacion, a.autor_id, " +
                    "COALESCE(u.nombre, 'Usuario Desconocido') as autor_nombre " +
                    "FROM articulos a " +
                    "LEFT JOIN usuarios u ON a.autor_id = u.id " +
                    "WHERE a.id = ?";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            articulo = new Articulo();
                            articulo.setId(rs.getInt("id"));
                            articulo.setTitulo(rs.getString("titulo"));
                            articulo.setContenido(rs.getString("contenido"));
                            articulo.setFechaPublicacion(rs.getTimestamp("fecha_publicacion").toLocalDateTime());
                            articulo.setAutorId(rs.getInt("autor_id"));
                            articulo.setAutorNombre(rs.getString("autor_nombre"));
                        }
                    }
                }
                return articulo;
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "obtenerPorId");
    }

    @Override
    public boolean crear(Articulo articulo) throws SQLException {
        return executeWithRetry(() -> {
            String sql = "INSERT INTO articulos (titulo, contenido, fecha_publicacion, autor_id) VALUES (?, ?, ?, ?)";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, articulo.getTitulo());
                    stmt.setString(2, articulo.getContenido());
                    stmt.setTimestamp(3, Timestamp.valueOf(articulo.getFechaPublicacion()));
                    stmt.setInt(4, articulo.getAutorId());

                    int filasAfectadas = stmt.executeUpdate();
                    return filasAfectadas > 0;
                }
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "crear");
    }

    @Override
    public boolean actualizar(Articulo articulo) throws SQLException {
        return executeWithRetry(() -> {
            String sql = "UPDATE articulos SET titulo = ?, contenido = ?, fecha_publicacion = ? WHERE id = ?";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, articulo.getTitulo());
                    stmt.setString(2, articulo.getContenido());
                    stmt.setTimestamp(3, Timestamp.valueOf(articulo.getFechaPublicacion()));
                    stmt.setInt(4, articulo.getId());

                    int filasAfectadas = stmt.executeUpdate();
                    return filasAfectadas > 0;
                }
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "actualizar");
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        return executeWithRetry(() -> {
            String sql = "DELETE FROM articulos WHERE id = ?";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);

                    int filasAfectadas = stmt.executeUpdate();
                    return filasAfectadas > 0;
                }
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "eliminar");
    }

    @Override
    public int contarTotal() throws SQLException {
        return executeWithRetry(() -> {
            String sql = "SELECT COUNT(*) as total FROM articulos";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery()) {

                    if (rs.next()) {
                        return rs.getInt("total");
                    }
                }
                return 0;
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "contarTotal");
    }
}
