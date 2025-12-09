package com.blog.dao;

import com.blog.model.Articulo;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz para operaciones de acceso a datos de artículos.
 * 
 * <p>Define el contrato que debe cumplir cualquier implementación de persistencia
 * de artículos, permitiendo cambiar el mecanismo de almacenamiento (MySQL, PostgreSQL,
 * MongoDB, etc.) sin afectar la lógica de negocio.</p>
 * 
 * <h3>Principios SOLID aplicados:</h3>
 * <ul>
 *   <li><b>S - Single Responsibility Principle (SRP):</b> La interfaz solo define
 *   operaciones CRUD para artículos. Ver Sección 2.1.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>O - Open/Closed Principle (OCP):</b> El sistema está abierto a extensión
 *   (nuevas implementaciones) pero cerrado a modificación. Podemos agregar una
 *   implementación para Oracle sin modificar código existente. Ver Sección 2.1.2 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>L - Liskov Substitution Principle (LSP):</b> Cualquier implementación de
 *   esta interfaz puede sustituir a otra sin romper la funcionalidad. Ver Sección 2.1.3 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>I - Interface Segregation Principle (ISP):</b> Esta interfaz es específica
 *   para artículos, no fuerza a implementar métodos innecesarios. Ver Sección 2.1.4 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>D - Dependency Inversion Principle (DIP):</b> Los módulos de alto nivel
 *   (Servlets) dependen de esta abstracción, no de implementaciones concretas.
 *   Ver Sección 2.1.5 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Patrón de diseño:</h3>
 * <ul>
 *   <li><b>DAO (Data Access Object):</b> Esta interfaz es parte del patrón DAO que
 *   abstrae y encapsula todo acceso a la fuente de datos. Ver Sección 2.4.2 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * @author Dylan David Silva Orrego
 * @author Maria Alejandra Munevar Barrera
 * @version 1.0
 * @since 2025-12-09
 * @see com.blog.model.Articulo
 * @see com.blog.dao.MySQLArticuloDAO
 */
public interface IArticuloDAO {
    
    /**
     * Lista todos los artículos de la base de datos ordenados por fecha de publicación.
     * 
     * <p>Los artículos se devuelven con información del autor mediante JOIN con la tabla usuarios.</p>
     * 
     * @return Lista de todos los artículos, ordenados del más reciente al más antiguo
     * @throws SQLException Si ocurre un error de base de datos durante la consulta
     */
    List<Articulo> listarTodos() throws SQLException;
    
    /**
     * Obtiene un artículo específico por su identificador único.
     * 
     * <p>Incluye información del autor mediante JOIN con la tabla usuarios.</p>
     * 
     * @param id Identificador único del artículo
     * @return El artículo encontrado, o null si no existe ningún artículo con ese ID
     * @throws SQLException Si ocurre un error de base de datos durante la consulta
     */
    Articulo obtenerPorId(int id) throws SQLException;
    
    /**
     * Crea un nuevo artículo en la base de datos.
     * 
     * <p>La fecha de publicación se establece automáticamente al momento actual.
     * El ID del artículo se genera automáticamente por la base de datos.</p>
     * 
     * @param articulo Objeto Articulo con los datos a insertar (titulo, contenido, autorId)
     * @return true si el artículo se creó exitosamente, false en caso contrario
     * @throws SQLException Si ocurre un error de base de datos durante la inserción
     */
    boolean crear(Articulo articulo) throws SQLException;
    
    /**
     * Actualiza un artículo existente en la base de datos.
     * 
     * <p>Solo se actualizan el título y contenido. La fecha de publicación y el autor no cambian.</p>
     * 
     * @param articulo Objeto Articulo con los datos actualizados (debe incluir el ID)
     * @return true si el artículo se actualizó exitosamente, false si no existe
     * @throws SQLException Si ocurre un error de base de datos durante la actualización
     */
    boolean actualizar(Articulo articulo) throws SQLException;
    
    /**
     * Elimina un artículo de la base de datos.
     * 
     * @param id Identificador único del artículo a eliminar
     * @return true si el artículo se eliminó exitosamente, false si no existía
     * @throws SQLException Si ocurre un error de base de datos durante la eliminación
     */
    boolean eliminar(int id) throws SQLException;
    
    /**
     * Cuenta el número total de artículos en la base de datos.
     * 
     * <p>Útil para mostrar estadísticas en el dashboard administrativo.</p>
     * 
     * @return Número total de artículos
     * @throws SQLException Si ocurre un error de base de datos durante el conteo
     */
    int contarTotal() throws SQLException;
}
