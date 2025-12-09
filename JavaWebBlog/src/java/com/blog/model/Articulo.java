package com.blog.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * POJO Articulo - Representa un artículo del blog
 * 
 * Principio SOLID aplicado: S - Single Responsibility Principle
 * (Responsabilidad Única).
 * Ver Sección 1.1 en PRINCIPIOS_Y_PATRONES.tex
 * 
 * Principio de Ingeniería: KISS (Keep It Simple, Stupid).
 * Ver Sección 2.2 en PRINCIPIOS_Y_PATRONES.tex
 * 
 * Esta clase es un simple contenedor de datos (también llamado POJO).
 * No sabe guardar datos, no sabe validar correos, solo sabe tener datos de un
 * artículo.
 * Mantiene la cohesión alta y el acoplamiento bajo al no mezclar lógica aquí.
 */
public class Articulo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private String contenido;
    private LocalDateTime fechaPublicacion;
    private int autorId;
    private String autorNombre; // Campo auxiliar para mostrar el nombre del autor

    // Constructor vacío
    public Articulo() {
    }

    // Constructor con parámetros
    public Articulo(int id, String titulo, String contenido, LocalDateTime fechaPublicacion, int autorId) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.autorId = autorId;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getAutorId() {
        return autorId;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public String getAutorNombre() {
        return autorNombre;
    }

    public void setAutorNombre(String autorNombre) {
        this.autorNombre = autorNombre;
    }

    // Helper method for nicer date display in JSPs
    public String getFechaPublicacionFormateada() {
        if (fechaPublicacion == null)
            return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm");
        return fechaPublicacion.format(formatter);
    }

    @Override
    public String toString() {
        return "Articulo{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", autorId=" + autorId +
                '}';
    }
}
