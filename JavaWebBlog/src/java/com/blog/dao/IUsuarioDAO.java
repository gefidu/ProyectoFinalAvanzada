package com.blog.dao;

import com.blog.model.Usuario;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz para operaciones de acceso a datos de usuarios.
 * 
 * <p>Define el contrato que debe cumplir cualquier implementación de persistencia
 * de usuarios, permitiendo cambiar el mecanismo de almacenamiento sin afectar
 * la lógica de autenticación y gestión de usuarios.</p>
 * 
 * <h3>Principios SOLID aplicados:</h3>
 * <ul>
 *   <li><b>S - Single Responsibility Principle (SRP):</b> La interfaz solo define
 *   operaciones de persistencia para usuarios. Ver Sección 2.1.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>O - Open/Closed Principle (OCP):</b> Abierto a extensión mediante nuevas
 *   implementaciones, cerrado a modificación. Ver Sección 2.1.2 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>L - Liskov Substitution Principle (LSP):</b> Cualquier implementación puede
 *   sustituir a otra manteniendo el contrato. Ver Sección 2.1.3 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>I - Interface Segregation Principle (ISP):</b> Interfaz específica para
 *   usuarios, separada de IArticuloDAO. Ver Sección 2.1.4 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>D - Dependency Inversion Principle (DIP):</b> Los controladores dependen
 *   de esta abstracción, no de implementaciones concretas. Ver Sección 2.1.5 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Patrón de diseño:</h3>
 * <ul>
 *   <li><b>DAO (Data Access Object):</b> Abstrae el acceso a datos de usuarios.
 *   Ver Sección 2.4.2 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * @author Dylan David Silva Orrego
 * @author Maria Alejandra Munevar Barrera
 * @version 1.0
 * @since 2025-12-09
 * @see com.blog.model.Usuario
 * @see com.blog.dao.MySQLUsuarioDAO
 */
public interface IUsuarioDAO {

    /**
     * Busca un usuario por su nombre de usuario (username).
     * 
     * <p>Este método es fundamental para el proceso de autenticación (login).
     * La búsqueda es case-sensitive.</p>
     * 
     * @param username Nombre de usuario a buscar
     * @return Usuario encontrado, o null si no existe ningún usuario con ese username
     * @throws SQLException Si ocurre un error de base de datos durante la búsqueda
     */
    Usuario buscarPorUsername(String username) throws SQLException;

    /**
     * Obtiene un usuario por su identificador único.
     * 
     * @param id Identificador único del usuario
     * @return Usuario encontrado, o null si no existe ningún usuario con ese ID
     * @throws SQLException Si ocurre un error de base de datos durante la búsqueda
     */
    Usuario obtenerPorId(int id) throws SQLException;

    /**
     * Crea un nuevo usuario en la base de datos.
     * 
     * <p><b>Importante:</b> La contraseña del usuario debe estar previamente hasheada
     * con SHA-256 usando {@link com.blog.util.PasswordUtil#hashPassword(String)}
     * antes de llamar este método.</p>
     * 
     * @param usuario Objeto Usuario con los datos a insertar
     * @return true si el usuario se creó exitosamente, false en caso contrario
     * @throws SQLException Si ocurre un error de base de datos (ej: username duplicado)
     */
    boolean crear(Usuario usuario) throws SQLException;

    /**
     * Lista todos los usuarios registrados en el sistema.
     * 
     * <p>Este método es utilizado por el panel de administración para mostrar
     * todos los usuarios. No incluye las contraseñas en el resultado por seguridad.</p>
     * 
     * @return Lista de todos los usuarios ordenados por ID
     * @throws SQLException Si ocurre un error de base de datos durante la consulta
     */
    List<Usuario> listarTodos() throws SQLException;

    /**
     * Actualiza el rol de un usuario existente.
     * 
     * <p>Permite promover usuarios de 'autor' a 'admin' o degradarlos de 'admin' a 'autor'.
     * Solo los administradores pueden ejecutar esta operación.</p>
     * 
     * @param id Identificador único del usuario
     * @param nuevoRol Nuevo rol del usuario ('admin' o 'autor')
     * @return true si el rol se actualizó correctamente, false si el usuario no existe
     * @throws SQLException Si ocurre un error de base de datos durante la actualización
     */
    boolean actualizarRol(int id, String nuevoRol) throws SQLException;

    /**
     * Elimina un usuario de la base de datos.
     * 
     * <p><b>Restricciones de seguridad:</b></p>
     * <ul>
     *   <li>No se permite eliminar el usuario administrador principal (ID 1)</li>
     *   <li>Los administradores no pueden auto-eliminarse</li>
     * </ul>
     * 
     * @param id Identificador único del usuario a eliminar
     * @return true si el usuario se eliminó correctamente, false si no existía
     * @throws SQLException Si ocurre un error de base de datos durante la eliminación
     */
    boolean eliminar(int id) throws SQLException;

    /**
     * Elimina todos los usuarios excepto los administradores.
     * 
     * <p>Esta operación de limpieza masiva elimina todos los usuarios con rol 'autor',
     * manteniendo intactos todos los administradores. Es útil para resetear usuarios
     * de prueba.</p>
     * 
     * <p><b>Precaución:</b> Esta operación no es reversible. Se recomienda solicitar
     * confirmación del usuario antes de ejecutarla.</p>
     * 
     * @return Número de usuarios eliminados
     * @throws SQLException Si ocurre un error de base de datos durante la eliminación masiva
     */
    int eliminarTodosExceptoAdmins() throws SQLException;
}