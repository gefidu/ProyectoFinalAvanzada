package com.blog.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para PasswordUtil
 */
@DisplayName("Tests para PasswordUtil")
class PasswordUtilTest {
    
    @Test
    @DisplayName("hashPassword debe generar un hash SHA-256 válido")
    void testHashPassword() {
        String password = "mySecurePassword123";
        String hash = PasswordUtil.hashPassword(password);
        
        assertNotNull(hash);
        assertEquals(64, hash.length(), "SHA-256 debe producir un hash de 64 caracteres hexadecimales");
        assertTrue(hash.matches("[0-9a-f]{64}"), "El hash debe contener solo caracteres hexadecimales");
    }
    
    @Test
    @DisplayName("hashPassword debe generar el mismo hash para la misma contraseña")
    void testHashPasswordConsistencia() {
        String password = "testPassword";
        String hash1 = PasswordUtil.hashPassword(password);
        String hash2 = PasswordUtil.hashPassword(password);
        
        assertEquals(hash1, hash2, "El mismo password debe producir el mismo hash");
    }
    
    @Test
    @DisplayName("hashPassword debe generar hashes diferentes para contraseñas diferentes")
    void testHashPasswordDiferentes() {
        String password1 = "password1";
        String password2 = "password2";
        String hash1 = PasswordUtil.hashPassword(password1);
        String hash2 = PasswordUtil.hashPassword(password2);
        
        assertNotEquals(hash1, hash2, "Passwords diferentes deben producir hashes diferentes");
    }
    
    @Test
    @DisplayName("verificarPassword debe retornar true para contraseña correcta")
    void testVerificarPasswordCorrecto() {
        String password = "myPassword123";
        String hash = PasswordUtil.hashPassword(password);
        
        assertTrue(PasswordUtil.verificarPassword(password, hash));
    }
    
    @Test
    @DisplayName("verificarPassword debe retornar false para contraseña incorrecta")
    void testVerificarPasswordIncorrecto() {
        String password = "correctPassword";
        String wrongPassword = "wrongPassword";
        String hash = PasswordUtil.hashPassword(password);
        
        assertFalse(PasswordUtil.verificarPassword(wrongPassword, hash));
    }
    
    @Test
    @DisplayName("hashPassword debe manejar contraseñas vacías")
    void testHashPasswordVacio() {
        String hash = PasswordUtil.hashPassword("");
        
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }
    
    @Test
    @DisplayName("hashPassword debe manejar contraseñas con caracteres especiales")
    void testHashPasswordCaracteresEspeciales() {
        String password = "p@$$w0rd!#%&/()=?";
        String hash = PasswordUtil.hashPassword(password);
        
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }
    
    @Test
    @DisplayName("hashPassword debe manejar contraseñas con caracteres Unicode")
    void testHashPasswordUnicode() {
        String password = "contraseña123áéíóú";
        String hash = PasswordUtil.hashPassword(password);
        
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }
    
    @Test
    @DisplayName("hashPassword debe ser sensible a mayúsculas/minúsculas")
    void testHashPasswordCaseSensitive() {
        String password1 = "Password";
        String password2 = "password";
        String hash1 = PasswordUtil.hashPassword(password1);
        String hash2 = PasswordUtil.hashPassword(password2);
        
        assertNotEquals(hash1, hash2, "Los hashes deben ser diferentes para mayúsculas/minúsculas diferentes");
    }
    
    @Test
    @DisplayName("hashPassword debe manejar contraseñas muy largas")
    void testHashPasswordLargo() {
        String password = "a".repeat(1000);
        String hash = PasswordUtil.hashPassword(password);
        
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }
    
    @Test
    @DisplayName("verificarPassword debe ser case-sensitive")
    void testVerificarPasswordCaseSensitive() {
        String password = "MyPassword";
        String hash = PasswordUtil.hashPassword(password);
        
        assertTrue(PasswordUtil.verificarPassword("MyPassword", hash));
        assertFalse(PasswordUtil.verificarPassword("mypassword", hash));
        assertFalse(PasswordUtil.verificarPassword("MYPASSWORD", hash));
    }
}
