package com.blog.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Usuario
 */
@DisplayName("Tests para Usuario")
class UsuarioTest {

    @Test
    @DisplayName("Constructor vacío debe crear un usuario")
    void testConstructorVacio() {
        Usuario usuario = new Usuario();
        assertNotNull(usuario);
    }

    @Test
    @DisplayName("Constructor con parámetros debe inicializar correctamente")
    void testConstructorConParametros() {
        Usuario usuario = new Usuario(1, "admin", "password123", "Administrador", "admin@email.com", "admin");

        assertEquals(1, usuario.getId());
        assertEquals("admin", usuario.getUsername());
        assertEquals("password123", usuario.getPassword());
        assertEquals("Administrador", usuario.getNombre());
    }

    @Test
    @DisplayName("Setter y getter de id deben funcionar correctamente")
    void testSetterGetterIdOld() {
        Usuario usuario = new Usuario();
        usuario.setId(42);
        assertEquals(42, usuario.getId());
    }

    @Test
    @DisplayName("Setter y getter de username deben funcionar correctamente")
    void testSetterGetterUsername() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        assertEquals("testuser", usuario.getUsername());
    }

    @Test
    @DisplayName("Setter y getter de password deben funcionar correctamente")
    void testSetterGetterPassword() {
        Usuario usuario = new Usuario();
        usuario.setPassword("securepass");
        assertEquals("securepass", usuario.getPassword());
    }

    @Test
    @DisplayName("Setter y getter de nombre deben funcionar correctamente")
    void testSetterGetterNombre() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan Pérez");
        assertEquals("Juan Pérez", usuario.getNombre());
    }

    @Test
    @DisplayName("toString debe retornar representación válida")
    void testToString() {
        Usuario usuario = new Usuario(1, "admin", "password", "Admin User", "admin@email.com", "admin");
        String resultado = usuario.toString();

        assertTrue(resultado.contains("id=1"));
        assertTrue(resultado.contains("username='admin'"));
        assertTrue(resultado.contains("nombre='Admin User'"));
        assertFalse(resultado.contains("password"), "El password no debe aparecer en toString");
    }

    @Test
    @DisplayName("Usuario con valores nulos debe manejarlos correctamente")
    void testValoresNulos() {
        Usuario usuario = new Usuario();
        usuario.setUsername(null);
        usuario.setPassword(null);
        usuario.setNombre(null);

        assertNull(usuario.getUsername());
        assertNull(usuario.getPassword());
        assertNull(usuario.getNombre());
    }

    @Test
    @DisplayName("Usuario con valores vacíos debe manejarlos correctamente")
    void testValoresVacios() {
        Usuario usuario = new Usuario();
        usuario.setUsername("");
        usuario.setPassword("");
        usuario.setNombre("");

        assertEquals("", usuario.getUsername());
        assertEquals("", usuario.getPassword());
        assertEquals("", usuario.getNombre());
    }
}
