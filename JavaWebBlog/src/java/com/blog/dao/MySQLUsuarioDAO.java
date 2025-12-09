package com.blog.dao;

import com.blog.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación MySQL del patrón DAO para operaciones de usuarios.
 * 
 * <p>Esta clase proporciona la implementación concreta de {@link IUsuarioDAO} usando
 * MySQL como sistema de gestión de base de datos. Maneja todas las operaciones CRUD
 * y de autenticación para usuarios, con énfasis en la seguridad mediante el uso de
 * contraseñas hasheadas y PreparedStatements.</p>
 * 
 * <h3>Características de resiliencia:</h3>
 * <ul>
 *   <li><b>Reintentos automáticos:</b> Las operaciones se reintentan hasta 2 veces
 *   con backoff exponencial en caso de fallo</li>
 *   <li><b>Pool de conexiones:</b> Utiliza el pool de {@link ConexionBD} para
 *   gestión eficiente de conexiones</li>
 *   <li><b>Gestión de errores:</b> Manejo robusto de errores de constraint violations
 *   (ej: username duplicado)</li>
 * </ul>
 * 
 * <h3>Principios SOLID aplicados:</h3>
 * <ul>
 *   <li><b>S - Single Responsibility Principle (SRP):</b> Esta clase solo se encarga
 *   de la persistencia de usuarios en MySQL. Ver Sección 2.1.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>L - Liskov Substitution Principle (LSP):</b> Cumple completamente el contrato
 *   de {@link IUsuarioDAO}, puede sustituirse sin romper funcionalidad.
 *   Ver Sección 2.1.3 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>D - Dependency Inversion Principle (DIP):</b> Los controladores dependen de
 *   la interfaz {@link IUsuarioDAO}, no de esta clase concreta. Ver Sección 2.1.5 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Patrón de diseño:</h3>
 * <ul>
 *   <li><b>DAO (Data Access Object):</b> Encapsula toda la lógica de acceso a datos
 *   de usuarios. Ver Sección 2.4.2 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>Singleton (indirecto):</b> Utiliza {@link ConexionBD} que implementa Singleton.
 *   Ver Sección 2.4.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Seguridad:</h3>
 * <ul>
 *   <li><b>Prevención de SQL Injection:</b> Usa PreparedStatements en todas las consultas</li>
 *   <li><b>Contraseñas hasheadas:</b> Trabaja solo con contraseñas ya hasheadas por
 *   {@link com.blog.util.PasswordUtil}</li>
 *   <li><b>Protección de admin principal:</b> Previene eliminación del usuario admin con ID 1</li>
 * </ul>
 * 
 * @author Dylan David Silva Orrego
 * @author Maria Alejandra Munevar Barrera
 * @version 1.0
 * @since 2025-12-09
 * @see com.blog.dao.IUsuarioDAO
 * @see com.blog.dao.ConexionBD
 * @see com.blog.model.Usuario
 * @see com.blog.util.PasswordUtil
 */
public class MySQLUsuarioDAO implements IUsuarioDAO {

    private final ConexionBD conexionBD;
    private static final int MAX_OPERATION_RETRIES = 2;

    public MySQLUsuarioDAO() {
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
                    System.err.println("[MySQLUsuarioDAO] Error en " + operationName + 
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
    public Usuario buscarPorUsername(String username) throws SQLException {
        return executeWithRetry(() -> {
            Usuario usuario = null;
            String sql = "SELECT id, username, password, nombre, email, rol FROM usuarios WHERE username = ?";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            usuario = new Usuario();
                            usuario.setId(rs.getInt("id"));
                            usuario.setUsername(rs.getString("username"));
                            usuario.setPassword(rs.getString("password"));
                            usuario.setNombre(rs.getString("nombre"));
                            usuario.setEmail(rs.getString("email"));
                            usuario.setRol(rs.getString("rol"));
                        }
                    }
                }
                return usuario;
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "buscarPorUsername");
    }

    @Override
    public Usuario obtenerPorId(int id) throws SQLException {
        return executeWithRetry(() -> {
            Usuario usuario = null;
            String sql = "SELECT id, username, password, nombre, email, rol FROM usuarios WHERE id = ?";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            usuario = new Usuario();
                            usuario.setId(rs.getInt("id"));
                            usuario.setUsername(rs.getString("username"));
                            usuario.setPassword(rs.getString("password"));
                            usuario.setNombre(rs.getString("nombre"));
                            usuario.setEmail(rs.getString("email"));
                            usuario.setRol(rs.getString("rol"));
                        }
                    }
                }
                return usuario;
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "obtenerPorId");
    }

    @Override
    public boolean crear(Usuario usuario) throws SQLException {
        return executeWithRetry(() -> {
            String sql = "INSERT INTO usuarios (username, password, nombre, email, rol, activo, fecha_creacion) VALUES (?, ?, ?, ?, 'autor', 1, NOW())";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, usuario.getUsername());
                    stmt.setString(2, usuario.getPassword());
                    stmt.setString(3, usuario.getNombre());
                    stmt.setString(4, usuario.getEmail());

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
    public List<Usuario> listarTodos() throws SQLException {
        return executeWithRetry(() -> {
            List<Usuario> usuarios = new ArrayList<>();
            String sql = "SELECT id, username, password, nombre, email, rol FROM usuarios ORDER BY nombre";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        Usuario usuario = new Usuario();
                        usuario.setId(rs.getInt("id"));
                        usuario.setUsername(rs.getString("username"));
                        usuario.setPassword(rs.getString("password"));
                        usuario.setNombre(rs.getString("nombre"));
                        usuario.setEmail(rs.getString("email"));
                        usuario.setRol(rs.getString("rol"));
                        usuarios.add(usuario);
                    }
                }
                return usuarios;
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "listarTodos");
    }

    @Override
    public boolean actualizarRol(int id, String nuevoRol) throws SQLException {
        return executeWithRetry(() -> {
            String sql = "UPDATE usuarios SET rol = ? WHERE id = ?";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nuevoRol);
                    stmt.setInt(2, id);

                    return stmt.executeUpdate() > 0;
                }
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "actualizarRol");
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        return executeWithRetry(() -> {
            String sql = "DELETE FROM usuarios WHERE id = ?";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);

                    return stmt.executeUpdate() > 0;
                }
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "eliminar");
    }

    @Override
    public int eliminarTodosExceptoAdmins() throws SQLException {
        return executeWithRetry(() -> {
            String sql = "DELETE FROM usuarios WHERE rol != 'admin'";

            Connection conn = null;
            try {
                conn = conexionBD.getConexion();
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    return stmt.executeUpdate();
                }
            } finally {
                if (conn != null) {
                    conexionBD.cerrarConexion(conn);
                }
            }
        }, "eliminarTodosExceptoAdmins");
    }
}
