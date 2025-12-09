package com.blog.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase de utilidad para el hasheo y verificación de contraseñas.
 * 
 * <p>Esta clase proporciona métodos estáticos para hashear contraseñas usando el algoritmo
 * SHA-256 y verificar si una contraseña coincide con un hash almacenado. Es fundamental
 * para la seguridad del sistema de autenticación.</p>
 * 
 * <h3>Principios SOLID aplicados:</h3>
 * <ul>
 *   <li><b>S - Single Responsibility Principle (SRP):</b> Esta clase tiene una única
 *   responsabilidad: gestionar el hasheo de contraseñas. No maneja autenticación,
 *   persistencia ni lógica de negocio. Ver Sección 2.1.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Otros principios de diseño:</h3>
 * <ul>
 *   <li><b>DRY (Don't Repeat Yourself):</b> Centraliza la lógica de hasheo en un solo lugar,
 *   evitando duplicar código en múltiples servlets. Ver Sección 2.3.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>KISS (Keep It Simple, Stupid):</b> Implementación simple y directa usando
 *   SHA-256 de la biblioteca estándar de Java. Ver Sección 2.3.2 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Algoritmo de hasheo:</h3>
 * <p>Se utiliza SHA-256 (Secure Hash Algorithm 256-bit) que genera un hash de 256 bits
 * (64 caracteres hexadecimales). Aunque SHA-256 no es el algoritmo más moderno para
 * contraseñas (se recomienda bcrypt o Argon2 en producción), es suficiente para
 * propósitos educativos y no requiere dependencias externas.</p>
 * 
 * <h3>Ejemplo de uso:</h3>
 * <pre>{@code
 * // Al registrar un usuario
 * String passwordPlano = "miContraseña123";
 * String passwordHash = PasswordUtil.hashPassword(passwordPlano);
 * usuario.setPassword(passwordHash); // Guardar el hash, no la contraseña
 * 
 * // Al hacer login
 * String inputPassword = "miContraseña123";
 * String storedHash = usuario.getPassword();
 * if (PasswordUtil.verificarPassword(inputPassword, storedHash)) {
 *     // Contraseña correcta
 * }
 * }</pre>
 * 
 * <h3>Nota de seguridad:</h3>
 * <p><b>Importante:</b> Esta implementación es para propósitos educativos. En un entorno
 * de producción se recomienda usar algoritmos diseñados específicamente para contraseñas
 * como bcrypt, scrypt o Argon2, que incluyen salt automático y son resistentes a
 * ataques de fuerza bruta mediante GPU.</p>
 * 
 * @author Dylan David Silva Orrego
 * @author Maria Alejandra Munevar Barrera
 * @version 1.0
 * @since 2025-12-09
 * @see com.blog.model.Usuario
 * @see com.blog.controller.LoginServlet
 * @see com.blog.controller.RegisterServlet
 */
public class PasswordUtil {

    /**
     * Hashea una contraseña usando el algoritmo SHA-256.
     * 
     * <p>Convierte la contraseña en texto plano a un hash de 256 bits representado
     * como una cadena hexadecimal de 64 caracteres. Este proceso es irreversible
     * (one-way hash).</p>
     * 
     * <p><b>Proceso:</b></p>
     * <ol>
     *   <li>Convierte la contraseña a bytes usando UTF-8</li>
     *   <li>Aplica el algoritmo SHA-256</li>
     *   <li>Convierte los bytes resultantes a formato hexadecimal</li>
     * </ol>
     * 
     * @param password Contraseña en texto plano a hashear
     * @return Hash SHA-256 de la contraseña como cadena hexadecimal de 64 caracteres
     * @throws RuntimeException si el algoritmo SHA-256 no está disponible (extremadamente raro)
     * @see #verificarPassword(String, String)
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
     * Verifica si una contraseña en texto plano coincide con un hash almacenado.
     * 
     * <p>Este método hashea la contraseña proporcionada y compara el resultado
     * con el hash almacenado. Es el método utilizado durante el proceso de login
     * para validar credenciales.</p>
     * 
     * <p><b>Seguridad:</b> La comparación se realiza sobre los hashes, nunca se
     * almacena ni se compara la contraseña en texto plano.</p>
     * 
     * @param password Contraseña en texto plano a verificar
     * @param hashedPassword Hash SHA-256 almacenado en la base de datos
     * @return true si la contraseña coincide con el hash, false en caso contrario
     * @see #hashPassword(String)
     */
    public static boolean verificarPassword(String password, String hashedPassword) {
        String hash = hashPassword(password);
        return hash.equals(hashedPassword);
    }
}
