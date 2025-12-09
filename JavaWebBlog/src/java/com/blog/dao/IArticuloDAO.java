package com.blog.dao;

import com.blog.model.Articulo;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface IArticuloDAO - Contrato para operaciones de Articulo
 * Aplicación del principio Open/Closed: abierto a extensión, cerrado a modificación
 */
public interface IArticuloDAO {
    
    /**
     * Lista todos los artículos
     * @return Lista de artículos
     * @throws SQLException Error de base de datos
     */
    List<Articulo> listarTodos() throws SQLException;
    
    /**
     * Obtiene un artículo por su ID
     * @param id ID del artículo
     * @return Artículo encontrado o null
     * @throws SQLException Error de base de datos
     */
    Articulo obtenerPorId(int id) throws SQLException;
    
    /**
     * Crea un nuevo artículo
     * @param articulo Artículo a crear
     * @return true si se creó exitosamente
     * @throws SQLException Error de base de datos
     */
    boolean crear(Articulo articulo) throws SQLException;
    
    /**
     * Actualiza un artículo existente
     * @param articulo Artículo a actualizar
     * @return true si se actualizó exitosamente
     * @throws SQLException Error de base de datos
     */
    boolean actualizar(Articulo articulo) throws SQLException;
    
    /**
     * Elimina un artículo
     * @param id ID del artículo a eliminar
     * @return true si se eliminó exitosamente
     * @throws SQLException Error de base de datos
     */
    boolean eliminar(int id) throws SQLException;
    
    /**
     * Cuenta el total de artículos
     * @return Número total de artículos
     * @throws SQLException Error de base de datos
     */
    int contarTotal() throws SQLException;
}
