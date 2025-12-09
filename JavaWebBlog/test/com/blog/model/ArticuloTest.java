package com.blog.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Articulo
 */
@DisplayName("Tests para Articulo")
class ArticuloTest {
    
    @Test
    @DisplayName("Constructor vacío debe crear un artículo")
    void testConstructorVacio() {
        Articulo articulo = new Articulo();
        assertNotNull(articulo);
    }
    
    @Test
    @DisplayName("Constructor con parámetros debe inicializar correctamente")
    void testConstructorConParametros() {
        LocalDateTime fecha = LocalDateTime.now();
        Articulo articulo = new Articulo(1, "Título Test", "Contenido test", fecha, 42);
        
        assertEquals(1, articulo.getId());
        assertEquals("Título Test", articulo.getTitulo());
        assertEquals("Contenido test", articulo.getContenido());
        assertEquals(fecha, articulo.getFechaPublicacion());
        assertEquals(42, articulo.getAutorId());
    }
    
    @Test
    @DisplayName("Setter y getter de id deben funcionar correctamente")
    void testSetterGetterId() {
        Articulo articulo = new Articulo();
        articulo.setId(99);
        assertEquals(99, articulo.getId());
    }
    
    @Test
    @DisplayName("Setter y getter de titulo deben funcionar correctamente")
    void testSetterGetterTitulo() {
        Articulo articulo = new Articulo();
        articulo.setTitulo("Mi Artículo");
        assertEquals("Mi Artículo", articulo.getTitulo());
    }
    
    @Test
    @DisplayName("Setter y getter de contenido deben funcionar correctamente")
    void testSetterGetterContenido() {
        Articulo articulo = new Articulo();
        String contenido = "Este es un contenido largo de prueba";
        articulo.setContenido(contenido);
        assertEquals(contenido, articulo.getContenido());
    }
    
    @Test
    @DisplayName("Setter y getter de fechaPublicacion deben funcionar correctamente")
    void testSetterGetterFechaPublicacion() {
        Articulo articulo = new Articulo();
        LocalDateTime fecha = LocalDateTime.of(2023, 12, 25, 10, 30);
        articulo.setFechaPublicacion(fecha);
        assertEquals(fecha, articulo.getFechaPublicacion());
    }
    
    @Test
    @DisplayName("Setter y getter de autorId deben funcionar correctamente")
    void testSetterGetterAutorId() {
        Articulo articulo = new Articulo();
        articulo.setAutorId(5);
        assertEquals(5, articulo.getAutorId());
    }
    
    @Test
    @DisplayName("Setter y getter de autorNombre deben funcionar correctamente")
    void testSetterGetterAutorNombre() {
        Articulo articulo = new Articulo();
        articulo.setAutorNombre("María García");
        assertEquals("María García", articulo.getAutorNombre());
    }
    
    @Test
    @DisplayName("toString debe retornar representación válida")
    void testToString() {
        LocalDateTime fecha = LocalDateTime.of(2023, 12, 25, 10, 30);
        Articulo articulo = new Articulo(1, "Test", "Contenido", fecha, 5);
        String resultado = articulo.toString();
        
        assertTrue(resultado.contains("id=1"));
        assertTrue(resultado.contains("titulo='Test'"));
        assertTrue(resultado.contains("autorId=5"));
    }
    
    @Test
    @DisplayName("Artículo con valores nulos debe manejarlos correctamente")
    void testValoresNulos() {
        Articulo articulo = new Articulo();
        articulo.setTitulo(null);
        articulo.setContenido(null);
        articulo.setFechaPublicacion(null);
        articulo.setAutorNombre(null);
        
        assertNull(articulo.getTitulo());
        assertNull(articulo.getContenido());
        assertNull(articulo.getFechaPublicacion());
        assertNull(articulo.getAutorNombre());
    }
    
    @Test
    @DisplayName("Artículo con contenido muy largo debe manejarlo")
    void testContenidoLargo() {
        Articulo articulo = new Articulo();
        String contenidoLargo = "a".repeat(10000);
        articulo.setContenido(contenidoLargo);
        assertEquals(10000, articulo.getContenido().length());
    }
    
    @Test
    @DisplayName("Artículo con caracteres especiales debe manejarlos")
    void testCaracteresEspeciales() {
        Articulo articulo = new Articulo();
        String titulo = "Título con áéíóú ñ Ñ ¿? ¡!";
        articulo.setTitulo(titulo);
        assertEquals(titulo, articulo.getTitulo());
    }
}
