package com.blog.dao;

import com.blog.model.Usuario;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface IUsuarioDAO - Contrato para operaciones de Usuario
 * Aplicación del principio Open/Closed: abierto a extensión, cerrado a
 * modificación
 */
public interface IUsuarioDAO {

    /**
     * Busca un usuario por su username
     * 
     * @param username Username del usuario
     * @return Usuario encontrado o null
     * @throws SQLException Error de base de datos
     */
    Usuario buscarPorUsername(String username) throws SQLException;

    /**
     * Obtiene un usuario por su ID
     * 
     * @param id ID del usuario
     * @return Usuario encontrado o null
     * @throws SQLException Error de base de datos
     */
    Usuario obtenerPorId(int id) throws SQLException;

    /**
     * Crea un nuevo usuario
     * 
     * @param usuario Usuario a crear
     * @return true si se creó exitosamente
     * @throws SQLException Error de base de datos
     */
    boolean crear(Usuario usuario) throws SQLException;

    /**
     * Lista todos los usuarios
     * 
     * @return Lista de usuarios
     * @throws SQLException Error de base de datos
     */
    List<Usuario> listarTodos() throws SQLException;

    /**
     * Actualiza el rol de un usuario
     * 
     * @param id       ID del usuario
     * @param nuevoRol Nuevo rol ('admin' o 'autor')
     * @return true si se actualizó correctamente
     * @throws SQLException Error de base de datos
     */
    boolean actualizarRol(int id, String nuevoRol) throws SQLException;
}