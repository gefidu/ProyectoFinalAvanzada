package com.blog.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para ConexionBD
 * Nota: Estas pruebas pueden fallar si no hay una base de datos MySQL
 * configurada
 */
@DisplayName("Tests para ConexionBD")
class ConexionBDTest {

    @Test
    @DisplayName("getInstancia debe retornar una instancia no nula")
    void testGetInstancia() {
        ConexionBD instancia = ConexionBD.getInstancia();
        assertNotNull(instancia, "La instancia de ConexionBD no debe ser nula");
    }

    @Test
    @DisplayName("getInstancia debe retornar la misma instancia (Singleton)")
    void testGetInstanciaSingleton() {
        ConexionBD instancia1 = ConexionBD.getInstancia();
        ConexionBD instancia2 = ConexionBD.getInstancia();

        assertSame(instancia1, instancia2, "Debe retornar la misma instancia (patrón Singleton)");
    }

    @Test
    @DisplayName("cerrarConexion debe manejar conexiones nulas sin lanzar excepciones")
    void testCerrarConexionNula() {
        ConexionBD instancia = ConexionBD.getInstancia();
        assertDoesNotThrow(() -> instancia.cerrarConexion(null));
    }

    @Test
    @DisplayName("getPoolStats debe retornar estadísticas del pool")
    void testGetPoolStats() {
        ConexionBD instancia = ConexionBD.getInstancia();
        String stats = instancia.getPoolStats();
        assertNotNull(stats, "Las estadísticas del pool no deben ser nulas");
        assertTrue(stats.contains("Pool Stats"), "Las estadísticas deben contener 'Pool Stats'");
        assertTrue(stats.contains("Total:"), "Las estadísticas deben mostrar el total de conexiones");
    }

    // Nota: No podemos probar getConexion sin una base de datos real
    // En un entorno de CI/CD, se podría usar una base de datos en memoria o
    // contenedor
}
