package com.blog.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordUtil - Utilidad para hashear contraseñas con SHA-256
 * Solo responsabilidad: hashear y verificar contraseñas
 */
public class PasswordUtil {

    /**
     * Hashea una contraseña usando SHA-256
     * 
     * @param password Contraseña en texto plano
     * @return Hash SHA-256 de la contraseña
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convertir bytes a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }

    /**
     * Verifica si una contraseña coincide con un hash
     * 
     * @param password       Contraseña en texto plano
     * @param hashedPassword Hash almacenado
     * @return true si coinciden
     */
    public static boolean verificarPassword(String password, String hashedPassword) {
        String hash = hashPassword(password);
        return hash.equals(hashedPassword);
    }
}
