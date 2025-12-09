package com.blog.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase de modelo que representa un artículo del blog.
 * 
 * <p>Esta clase es un Plain Old Java Object (POJO) que encapsula los datos de un artículo,
 * incluyendo título, contenido, fecha de publicación e información del autor.
 * Implementa {@link Serializable} para permitir su serialización en sesiones HTTP.</p>
 * 
 * <h3>Principios SOLID aplicados:</h3>
 * <ul>
 *   <li><b>S - Single Responsibility Principle (SRP):</b> Esta clase tiene una única
 *   responsabilidad: representar los datos de un artículo. No contiene lógica de negocio,
 *   validación de datos o persistencia. Ver Sección 2.1.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Otros principios de diseño:</h3>
 * <ul>
 *   <li><b>KISS (Keep It Simple, Stupid):</b> Clase simple de datos sin complejidad innecesaria.
 *   Ver Sección 2.3.2 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>SoC (Separation of Concerns):</b> Separa los datos del modelo de la lógica de
 *   negocio y presentación, cumpliendo con el patrón MVC. Ver Sección 2.3.4 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Patrón de diseño:</h3>
 * <ul>
 *   <li><b>MVC (Model):</b> Esta clase representa el Modelo en la arquitectura MVC.
 *   Ver Sección 2.4.3 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * @author Dylan David Silva Orrego
 * @author Maria Alejandra Munevar Barrera
 * @version 1.0
 * @since 2025-12-09
 * @see com.blog.dao.IArticuloDAO
 * @see com.blog.dao.MySQLArticuloDAO
 */
public class Articulo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private String contenido;
    private LocalDateTime fechaPublicacion;
    private int autorId;
    private String autorNombre; // Campo auxiliar para mostrar el nombre del autor

    /**
     * Constructor sin argumentos requerido para la compatibilidad con JavaBeans.
     */
    public Articulo() {
    }

    /**
     * Constructor con todos los parámetros para crear un artículo completo.
     * 
     * @param id Identificador único del artículo
     * @param titulo Título del artículo
     * @param contenido Contenido completo del artículo
     * @param fechaPublicacion Fecha y hora de publicación del artículo
     * @param autorId Identificador del usuario autor del artículo
     */
    public Articulo(int id, String titulo, String contenido, LocalDateTime fechaPublicacion, int autorId) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.autorId = autorId;
    }

    /**
     * Obtiene el identificador único del artículo.
     * 
     * @return ID del artículo
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del artículo.
     * 
     * @param id ID del artículo
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el título del artículo.
     * 
     * @return Título del artículo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del artículo.
     * 
     * @param titulo Título del artículo
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el contenido completo del artículo.
     * 
     * @return Contenido del artículo
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Establece el contenido completo del artículo.
     * 
     * @param contenido Contenido del artículo
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Obtiene la fecha y hora de publicación del artículo.
     * 
     * @return Fecha de publicación como {@link LocalDateTime}
     */
    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Establece la fecha y hora de publicación del artículo.
     * 
     * @param fechaPublicacion Fecha de publicación como {@link LocalDateTime}
     */
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * Obtiene el identificador del autor del artículo.
     * 
     * @return ID del usuario autor
     */
    public int getAutorId() {
        return autorId;
    }

    /**
     * Establece el identificador del autor del artículo.
     * 
     * @param autorId ID del usuario autor
     */
    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    /**
     * Obtiene el nombre del autor del artículo.
     * Este campo es auxiliar y se rellena mediante JOINs en las consultas SQL.
     * 
     * @return Nombre del autor o null si no se ha cargado
     */
    public String getAutorNombre() {
        return autorNombre;
    }

    /**
     * Establece el nombre del autor del artículo.
     * 
     * @param autorNombre Nombre del autor
     */
    public void setAutorNombre(String autorNombre) {
        this.autorNombre = autorNombre;
    }

    /**
     * Obtiene la fecha de publicación formateada para su visualización.
     * 
     * <p>Formatea la fecha en formato legible en español:
     * "dd de MMMM de yyyy, HH:mm" (ejemplo: "09 de diciembre de 2025, 14:30")</p>
     * 
     * <p><b>Principio aplicado:</b> DRY - Evita repetir lógica de formateo en las vistas JSP.
     * Ver Sección 2.3.1 en PRINCIPIOS_Y_PATRONES.tex</p>
     * 
     * @return Fecha formateada o cadena vacía si la fecha es null
     */
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
