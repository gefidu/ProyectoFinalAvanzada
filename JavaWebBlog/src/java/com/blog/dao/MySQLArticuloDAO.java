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
 * MySQLArticuloDAO - Implementación de IArticuloDAO para MySQL
 * 
 * Principio SOLID aplicado: L - Liskov Substitution Principle (Sustitución de
 * Liskov).
 * Ver Sección 1.3 en PRINCIPIOS_Y_PATRONES.tex
 * 
 * Patrón de Diseño: DAO (Data Access Object).
 * Ver Sección 3.2 en PRINCIPIOS_Y_PATRONES.tex
 * 
 * Esta clase es una ciudadana ejemplar: cumple al pie de la letra el contrato
 * definido por IArticuloDAO.
 * Esto significa que cualquier parte del código que espere un IArticuloDAO
 * aceptará esta clase
 * sin rechistar, sin saber siquiera que por debajo hay una base de datos MySQL.
 */
public class MySQLArticuloDAO implements IArticuloDAO {

    private final ConexionBD conexionBD;

    public MySQLArticuloDAO() {
        this.conexionBD = ConexionBD.getInstancia();
    }

    @Override
    public List<Articulo> listarTodos() throws SQLException {
        List<Articulo> articulos = new ArrayList<>();
        // Changed INNER JOIN to LEFT JOIN to ensure articles are shown even if author
        // is deleted
        // Used COALESCE to provide a default value for unknown authors
        String sql = "SELECT a.id, a.titulo, a.contenido, a.fecha_publicacion, a.autor_id, " +
                "COALESCE(u.nombre, 'Usuario Desconocido') as autor_nombre " +
                "FROM articulos a " +
                "LEFT JOIN usuarios u ON a.autor_id = u.id " +
                "ORDER BY a.fecha_publicacion DESC";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql);
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
    }

    @Override
    public Articulo obtenerPorId(int id) throws SQLException {
        Articulo articulo = null;
        String sql = "SELECT a.id, a.titulo, a.contenido, a.fecha_publicacion, a.autor_id, " +
                "COALESCE(u.nombre, 'Usuario Desconocido') as autor_nombre " +
                "FROM articulos a " +
                "LEFT JOIN usuarios u ON a.autor_id = u.id " +
                "WHERE a.id = ?";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

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
    }

    @Override
    public boolean crear(Articulo articulo) throws SQLException {
        String sql = "INSERT INTO articulos (titulo, contenido, fecha_publicacion, autor_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, articulo.getTitulo());
            stmt.setString(2, articulo.getContenido());
            stmt.setTimestamp(3, Timestamp.valueOf(articulo.getFechaPublicacion()));
            stmt.setInt(4, articulo.getAutorId());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    @Override
    public boolean actualizar(Articulo articulo) throws SQLException {
        String sql = "UPDATE articulos SET titulo = ?, contenido = ?, fecha_publicacion = ? WHERE id = ?";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, articulo.getTitulo());
            stmt.setString(2, articulo.getContenido());
            stmt.setTimestamp(3, Timestamp.valueOf(articulo.getFechaPublicacion()));
            stmt.setInt(4, articulo.getId());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM articulos WHERE id = ?";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    @Override
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM articulos";

        try (Connection conn = conexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        }

        return 0;
    }
}
