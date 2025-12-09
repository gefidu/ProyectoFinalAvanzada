-- =====================================================================
-- SCRIPT DE CONFIGURACIÓN RÁPIDA - JavaWebBlog
-- =====================================================================
-- 
-- INSTRUCCIONES:
-- 1. Abre MySQL Shell o MySQL Command Line Client
-- 2. Conéctate a tu servidor MySQL (usuario root o el que uses)
-- 3. Copia y pega este archivo COMPLETO
-- 4. Presiona Enter para ejecutar
--
-- NOTA: Este script crea la base de datos Y las tablas automáticamente
-- =====================================================================

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS blog_db;

-- Seleccionar la base de datos
USE blog_db;

-- =====================================================================
-- CREACIÓN DE TABLAS
-- =====================================================================

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    rol ENUM('admin', 'autor', 'lector') DEFAULT 'lector',
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de artículos
CREATE TABLE IF NOT EXISTS articulos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    contenido TEXT NOT NULL,
    fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    autor_id INT NOT NULL,
    FOREIGN KEY (autor_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================================
-- DATOS DE PRUEBA (OPCIONAL - Comentar si no los necesitas)
-- =====================================================================

-- Insertar usuario administrador de prueba
-- Username: admin, Password: admin123 (sin encriptar - cambiar en producción)
INSERT INTO usuarios (username, password, nombre, email, rol) 
VALUES ('admin', 'admin123', 'Administrador', 'admin@blog.com', 'admin')
ON DUPLICATE KEY UPDATE username=username;

-- Insertar usuario autor de prueba
-- Username: autor, Password: autor123
INSERT INTO usuarios (username, password, nombre, email, rol) 
VALUES ('autor', 'autor123', 'Autor de Prueba', 'autor@blog.com', 'autor')
ON DUPLICATE KEY UPDATE username=username;

-- Insertar algunos artículos de prueba
INSERT INTO articulos (titulo, contenido, autor_id) 
VALUES 
('Bienvenido al Blog', 'Este es el primer artículo del blog. ¡Bienvenido!', 1),
('Características del Sistema', 'Este blog tiene muchas características interesantes...', 1),
('Tutorial de Uso', 'Aprende a usar todas las funcionalidades del blog...', 2)
ON DUPLICATE KEY UPDATE titulo=titulo;

-- =====================================================================
-- VERIFICACIÓN
-- =====================================================================

-- Mostrar las tablas creadas
SHOW TABLES;

-- Mostrar estructura de las tablas
DESCRIBE usuarios;
DESCRIBE articulos;

-- Contar registros
SELECT COUNT(*) AS total_usuarios FROM usuarios;
SELECT COUNT(*) AS total_articulos FROM articulos;

-- Mensaje de confirmación
SELECT '¡Base de datos configurada correctamente!' AS Estado,
       'Puedes cerrar MySQL Shell y ejecutar la aplicación' AS Accion;

-- =====================================================================
-- FIN DEL SCRIPT
-- =====================================================================
