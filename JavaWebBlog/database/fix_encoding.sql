-- Script para corregir el encoding de los artículos de ejemplo
-- Se asume que son los IDs 1 al 5

SET NAMES utf8mb4;

UPDATE articulos SET 
    titulo = 'Bienvenido al Blog Management System',
    contenido = 'Este es el primer artículo de nuestro sistema de gestión de contenidos desarrollado con JavaWeb. El proyecto implementa el patrón MVC (Modelo-Vista-Controlador) y utiliza tecnologías modernas como Jakarta EE, JSP, JSTL y Bootstrap.\n\nEl sistema permite a los administradores gestionar artículos de forma completa (CRUD) mientras que los visitantes pueden leer los artículos publicados sin necesidad de autenticarse.\n\nCaracterísticas principales:\n- Arquitectura MVC bien definida\n- Patrón DAO para acceso a datos\n- Principios SOLID aplicados\n- Interfaz responsive con Bootstrap\n- Autenticación y autorización de usuarios'
WHERE id = 1;

UPDATE articulos SET 
    titulo = 'Arquitectura MVC en JavaWeb',
    contenido = 'El patrón Modelo-Vista-Controlador (MVC) es fundamental en el desarrollo de aplicaciones web robustas. En este proyecto, hemos implementado una clara separación de responsabilidades:\n\nMODELO: Contiene las entidades (POJOs) como Usuario y Articulo, así como la lógica de acceso a datos mediante DAOs (Data Access Objects). Los DAOs implementan interfaces para facilitar el cambio de implementación sin afectar el resto del sistema.\n\nVISTA: Utiliza JSP (JavaServer Pages) con JSTL para evitar scriptlets y mantener las vistas limpias. Bootstrap proporciona un diseño responsive y moderno sin necesidad de escribir CSS desde cero.\n\nCONTROLADOR: Los Servlets procesan las peticiones HTTP, interactúan con el modelo y delegan la presentación a las vistas. Cada servlet tiene una responsabilidad específica y bien definida.\n\nEsta arquitectura facilita el mantenimiento, las pruebas y la escalabilidad del sistema.'
WHERE id = 2;

UPDATE articulos SET 
    titulo = 'Principios SOLID en el Desarrollo',
    contenido = 'Los principios SOLID son fundamentales para escribir código limpio y mantenible. En este proyecto hemos aplicado:\n\nS - Single Responsibility: Cada clase tiene una única responsabilidad. Por ejemplo, ConexionBD solo se encarga de gestionar conexiones a la base de datos.\n\nO - Open/Closed: El sistema está abierto a extensión pero cerrado a modificación. Usamos interfaces para los DAOs, permitiendo cambiar de MySQL a otra base de datos sin modificar los servlets.\n\nL - Liskov Substitution: Cualquier implementación de IArticuloDAO puede sustituir a otra sin afectar el funcionamiento del sistema.\n\nI - Interface Segregation: Aunque no es tan evidente en un proyecto pequeño, nuestras interfaces están diseñadas para ser específicas a cada necesidad.\n\nD - Dependency Inversion: Los servlets dependen de abstracciones (interfaces) en lugar de implementaciones concretas.\n\nEstos principios garantizan que nuestro código sea fácil de mantener, probar y extender.'
WHERE id = 3;

UPDATE articulos SET 
    titulo = 'Seguridad en Aplicaciones Web',
    contenido = 'La seguridad es un aspecto crítico en cualquier aplicación web. En este sistema hemos implementado varias medidas de seguridad:\n\n1. Hash de Contraseñas: Las contraseñas se almacenan hasheadas con SHA-256, nunca en texto plano. Esto protege las credenciales de los usuarios incluso si la base de datos se ve comprometida.\n\n2. Autenticación basada en Sesiones: Usamos HttpSession para mantener el estado de autenticación del usuario. Cada petición a rutas protegidas verifica la existencia de una sesión válida.\n\n3. Filtros de Autorización: AuthFilter verifica que el usuario esté autenticado antes de permitir el acceso a las rutas de administración (/admin/*).\n\n4. Validación de Entrada: Todos los formularios validan que los campos requeridos estén presentes antes de procesar los datos.\n\n5. Manejo de Errores: Los errores se manejan apropiadamente sin revelar información sensible del sistema.\n\nEstas medidas, aunque básicas, proporcionan una base sólida de seguridad para la aplicación.'
WHERE id = 4;

UPDATE articulos SET 
    titulo = 'Cómo Usar el Sistema',
    contenido = 'Este sistema de blog es intuitivo y fácil de usar. Aquí te explicamos cómo sacar el máximo provecho:\n\nPARA LECTORES:\n- La página principal muestra todos los artículos publicados ordenados por fecha\n- Haz clic en "Leer más" para ver el contenido completo de un artículo\n- La navegación es simple e intuitiva\n\nPARA ADMINISTRADORES:\n1. Inicia sesión con tus credenciales (usuario: admin, contraseña: admin123)\n2. Accede al Dashboard para ver estadísticas básicas\n3. Desde "Artículos" puedes ver todos los artículos publicados\n4. Usa "Nuevo Artículo" para publicar contenido\n5. Cada artículo puede ser editado o eliminado según necesites\n6. No olvides cerrar sesión cuando termines\n\nCARACTERÍSTICAS:\n- Diseño responsive: funciona en móviles, tablets y escritorio\n- Interfaz moderna con Bootstrap\n- Operaciones CRUD completas\n- Gestión simple y efectiva\n\n¡Disfruta del sistema!'
WHERE id = 5;

-- También aseguramos que los usuarios tengan nombres correctos (tildes)
UPDATE usuarios SET nombre = 'Alejandra Munevar' WHERE username = 'alejandra';
UPDATE usuarios SET nombre = 'Sergio Moreno' WHERE username = 'sergio';
