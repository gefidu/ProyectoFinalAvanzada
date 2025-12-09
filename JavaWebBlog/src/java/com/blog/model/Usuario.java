package com.blog.model;

import java.io.Serializable;

/**
 * Clase de modelo que representa un usuario del sistema.
 * 
 * <p>Esta clase es un Plain Old Java Object (POJO) que encapsula los datos de un usuario,
 * incluyendo información personal, credenciales de acceso y rol en el sistema.
 * Implementa {@link Serializable} para permitir su almacenamiento en sesiones HTTP.</p>
 * 
 * <h3>Roles de usuario:</h3>
 * <ul>
 *   <li><b>admin:</b> Administrador con acceso completo al sistema, incluyendo gestión de usuarios</li>
 *   <li><b>autor:</b> Usuario que puede crear, editar y eliminar sus propios artículos</li>
 * </ul>
 * 
 * <h3>Principios SOLID aplicados:</h3>
 * <ul>
 *   <li><b>S - Single Responsibility Principle (SRP):</b> Esta clase tiene una única
 *   responsabilidad: representar los datos de un usuario. No contiene lógica de autenticación,
 *   validación o persistencia. Ver Sección 2.1.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Otros principios de diseño:</h3>
 * <ul>
 *   <li><b>KISS (Keep It Simple, Stupid):</b> Clase simple de datos sin complejidad innecesaria.
 *   Ver Sección 2.3.2 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>SoC (Separation of Concerns):</b> Separa los datos del modelo de la lógica de
 *   autenticación y presentación, cumpliendo con el patrón MVC. Ver Sección 2.3.4 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Patrón de diseño:</h3>
 * <ul>
 *   <li><b>MVC (Model):</b> Esta clase representa el Modelo en la arquitectura MVC.
 *   Ver Sección 2.4.3 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Nota de seguridad:</h3>
 * <p>La contraseña almacenada en esta clase está hasheada con SHA-256 mediante
 * {@link com.blog.util.PasswordUtil}. Nunca se almacena en texto plano.</p>
 * 
 * @author Dylan David Silva Orrego
 * @author Maria Alejandra Munevar Barrera
 * @version 1.0
 * @since 2025-12-09
 * @see com.blog.dao.IUsuarioDAO
 * @see com.blog.dao.MySQLUsuarioDAO
 * @see com.blog.util.PasswordUtil
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String email;
    private String username;
    private String password;
    private String rol;

    /**
     * Constructor sin argumentos requerido para la compatibilidad con JavaBeans.
     */
    public Usuario() {
    }

    /**
     * Constructor con todos los parámetros para crear un usuario completo.
     * 
     * @param id Identificador único del usuario
     * @param nombre Nombre completo del usuario
     * @param email Correo electrónico del usuario
     * @param username Nombre de usuario para login
     * @param password Contraseña hasheada con SHA-256
     * @param rol Rol del usuario ("admin" o "autor")
     */
    public Usuario(int id, String nombre, String email, String username, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    /**
     * Obtiene el identificador único del usuario.
     * 
     * @return ID del usuario
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     * 
     * @param id ID del usuario
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre completo del usuario.
     * 
     * @return Nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre completo del usuario.
     * 
     * @param nombre Nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * 
     * @return Email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param email Email del usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el nombre de usuario para autenticación.
     * 
     * @return Username del usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario para autenticación.
     * 
     * @param username Username del usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña hasheada del usuario.
     * 
     * <p><b>Importante:</b> Este método devuelve la contraseña ya hasheada con SHA-256,
     * nunca la contraseña en texto plano.</p>
     * 
     * @return Contraseña hasheada con SHA-256
     * @see com.blog.util.PasswordUtil#hashPassword(String)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * <p><b>Importante:</b> La contraseña debe estar previamente hasheada con SHA-256
     * usando {@link com.blog.util.PasswordUtil#hashPassword(String)} antes de llamar este método.</p>
     * 
     * @param password Contraseña hasheada con SHA-256
     * @see com.blog.util.PasswordUtil#hashPassword(String)
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el rol del usuario en el sistema.
     * 
     * @return Rol del usuario ("admin" o "autor")
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario en el sistema.
     * 
     * @param rol Rol del usuario ("admin" o "autor")
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Devuelve una representación en String del usuario.
     * 
     * <p><b>Nota de seguridad:</b> Este método no incluye la contraseña
     * por razones de seguridad.</p>
     * 
     * @return Representación en String del usuario
     */
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
