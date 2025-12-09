package com.blog.dao;

import com.blog.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Aplicación del principio de Sustitución de Liskov: puede usarse en lugar de
 * la interfaz
 */
public class MySQLUsuarioDAO implements IUsuarioDAO {

    private final ConexionBD conexionBD;

    public MySQLUsuarioDAO() {
        this.conexionBD = ConexionBD.getInstancia();
    }

    @Override
    public Usuario buscarPorUsername(String username) throws SQLException {
        Usuario usuario = null;
        String sql = "SELECT id, username, password, nombre, email, rol FROM usuarios WHERE username = ?";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

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
    }

    @Override
    public Usuario obtenerPorId(int id) throws SQLException {
        Usuario usuario = null;
        String sql = "SELECT id, username, password, nombre, email, rol FROM usuarios WHERE id = ?";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

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
    }

    @Override
    public boolean crear(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (username, password, nombre, email, rol, activo, fecha_creacion) VALUES (?, ?, ?, ?, 'autor', 1, NOW())";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getNombre());
            stmt.setString(4, usuario.getEmail());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    @Override
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, username, password, nombre, email, rol FROM usuarios ORDER BY nombre";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql);
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
    }

    @Override
    public boolean actualizarRol(int id, String nuevoRol) throws SQLException {
        String sql = "UPDATE usuarios SET rol = ? WHERE id = ?";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoRol);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        }
    }
}
