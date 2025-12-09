-- ============================================================
-- Script de Base de Datos para Blog Management System
-- Sistema de Gestión de Contenidos (Blog) JavaWeb
-- ============================================================

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS blog_db 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE blog_db;

-- ============================================================
-- Tabla: usuarios
-- Descripción: Almacena los usuarios del sistema (administradores)
-- ============================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL COMMENT 'Hash SHA-256 de la contraseña',
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    rol VARCHAR(20) DEFAULT 'autor',
    activo TINYINT(1) DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- Tabla: articulos
-- Descripción: Almacena los artículos del blog
-- ============================================================
CREATE TABLE IF NOT EXISTS articulos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    contenido TEXT NOT NULL,
    fecha_publicacion DATETIME NOT NULL,
    autor_id INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (autor_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_fecha_publicacion (fecha_publicacion),
    INDEX idx_autor (autor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- Datos de Ejemplo
-- ============================================================

-- Usuario administrador por defecto
-- Username: admin
-- Password: admin123 (Hash SHA-256)
INSERT INTO usuarios (username, password, nombre, rol) VALUES
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Administrador del Sistema', 'admin');

-- Usuarios adicionales para pruebas
INSERT INTO usuarios (username, password, nombre) VALUES
('alejandra', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Alejandra Munevar'),
('dylan', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Dylan Silva'),
('sergio', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Sergio Moreno');

-- Artículos de ejemplo
INSERT INTO articulos (titulo, contenido, fecha_publicacion, autor_id) VALUES
('Bienvenido al Blog Management System', 
'Este es el primer artículo de nuestro sistema de gestión de contenidos desarrollado con JavaWeb. El proyecto implementa el patrón MVC (Modelo-Vista-Controlador) y utiliza tecnologías modernas como Jakarta EE, JSP, JSTL y Bootstrap.

El sistema permite a los administradores gestionar artículos de forma completa (CRUD) mientras que los visitantes pueden leer los artículos publicados sin necesidad de autenticarse.

Características principales:
- Arquitectura MVC bien definida
- Patrón DAO para acceso a datos
- Principios SOLID aplicados
- Interfaz responsive con Bootstrap
- Autenticación y autorización de usuarios', 
NOW(), 1),

('Arquitectura MVC en JavaWeb',
'El patrón Modelo-Vista-Controlador (MVC) es fundamental en el desarrollo de aplicaciones web robustas. En este proyecto, hemos implementado una clara separación de responsabilidades:

MODELO: Contiene las entidades (POJOs) como Usuario y Articulo, así como la lógica de acceso a datos mediante DAOs (Data Access Objects). Los DAOs implementan interfaces para facilitar el cambio de implementación sin afectar el resto del sistema.

VISTA: Utiliza JSP (JavaServer Pages) con JSTL para evitar scriptlets y mantener las vistas limpias. Bootstrap proporciona un diseño responsive y moderno sin necesidad de escribir CSS desde cero.

CONTROLADOR: Los Servlets procesan las peticiones HTTP, interactúan con el modelo y delegan la presentación a las vistas. Cada servlet tiene una responsabilidad específica y bien definida.

Esta arquitectura facilita el mantenimiento, las pruebas y la escalabilidad del sistema.',
NOW() - INTERVAL 2 DAY, 2),

('Principios SOLID en el Desarrollo',
'Los principios SOLID son fundamentales para escribir código limpio y mantenible. En este proyecto hemos aplicado:

S - Single Responsibility: Cada clase tiene una única responsabilidad. Por ejemplo, ConexionBD solo se encarga de gestionar conexiones a la base de datos.

O - Open/Closed: El sistema está abierto a extensión pero cerrado a modificación. Usamos interfaces para los DAOs, permitiendo cambiar de MySQL a otra base de datos sin modificar los servlets.

L - Liskov Substitution: Cualquier implementación de IArticuloDAO puede sustituir a otra sin afectar el funcionamiento del sistema.

I - Interface Segregation: Aunque no es tan evidente en un proyecto pequeño, nuestras interfaces están diseñadas para ser específicas a cada necesidad.

D - Dependency Inversion: Los servlets dependen de abstracciones (interfaces) en lugar de implementaciones concretas.

Estos principios garantizan que nuestro código sea fácil de mantener, probar y extender.',
NOW() - INTERVAL 5 DAY, 3),

('Seguridad en Aplicaciones Web',
'La seguridad es un aspecto crítico en cualquier aplicación web. En este sistema hemos implementado varias medidas de seguridad:

1. Hash de Contraseñas: Las contraseñas se almacenan hasheadas con SHA-256, nunca en texto plano. Esto protege las credenciales de los usuarios incluso si la base de datos se ve comprometida.

2. Autenticación basada en Sesiones: Usamos HttpSession para mantener el estado de autenticación del usuario. Cada petición a rutas protegidas verifica la existencia de una sesión válida.

3. Filtros de Autorización: AuthFilter verifica que el usuario esté autenticado antes de permitir el acceso a las rutas de administración (/admin/*).

4. Validación de Entrada: Todos los formularios validan que los campos requeridos estén presentes antes de procesar los datos.

5. Manejo de Errores: Los errores se manejan apropiadamente sin revelar información sensible del sistema.

Estas medidas, aunque básicas, proporcionan una base sólida de seguridad para la aplicación.',
NOW() - INTERVAL 7 DAY, 4),

('Cómo Usar el Sistema',
'Este sistema de blog es intuitivo y fácil de usar. Aquí te explicamos cómo sacar el máximo provecho:

PARA LECTORES:
- La página principal muestra todos los artículos publicados ordenados por fecha
- Haz clic en "Leer más" para ver el contenido completo de un artículo
- La navegación es simple e intuitiva

PARA ADMINISTRADORES:
1. Inicia sesión con tus credenciales (usuario: admin, contraseña: admin123)
2. Accede al Dashboard para ver estadísticas básicas
3. Desde "Artículos" puedes ver todos los artículos publicados
4. Usa "Nuevo Artículo" para publicar contenido
5. Cada artículo puede ser editado o eliminado según necesites
6. No olvides cerrar sesión cuando termines

CARACTERÍSTICAS:
- Diseño responsive: funciona en móviles, tablets y escritorio
- Interfaz moderna con Bootstrap
- Operaciones CRUD completas
- Gestión simple y efectiva

¡Disfruta del sistema!',
NOW() - INTERVAL 10 DAY, 1);

-- ============================================================
-- Verificación de la instalación
-- ============================================================

-- Ver usuarios creados
SELECT 'Usuarios creados:' as mensaje;
SELECT id, username, nombre FROM usuarios;

-- Ver artículos creados
SELECT 'Artículos creados:' as mensaje;
SELECT a.id, a.titulo, u.nombre as autor, a.fecha_publicacion 
FROM articulos a 
INNER JOIN usuarios u ON a.autor_id = u.id 
ORDER BY a.fecha_publicacion DESC;

-- Información sobre las credenciales
SELECT '
============================================================
CREDENCIALES DE ACCESO POR DEFECTO
============================================================
Usuario: admin
Contraseña: admin123

Otros usuarios de prueba (misma contraseña):
- alejandra / admin123
- dylan / admin123
- sergio / admin123
============================================================
' as INFORMACION;
