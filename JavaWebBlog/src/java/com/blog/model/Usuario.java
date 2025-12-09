package com.blog.model;

import java.io.Serializable;

/**
 * POJO Usuario - Representa un usuario del sistema (Admin o Autor)
 * 
 * Principio SOLID: SRP. Clase simple de datos.
 * Ver Sección 1.1 en PRINCIPIOS_Y_PATRONES.tex
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String email;
    private String username;
    private String password;
    private String rol;

    public Usuario() {
    }

    public Usuario(int id, String nombre, String email, String username, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
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
