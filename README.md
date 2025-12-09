# Odally - Sistema de Gestión de Contenidos (Blog)

![Java](https://img.shields.io/badge/Java-21+-blue.svg)
![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple.svg)

**Odally** es un sistema de gestión de contenidos (Blog) moderno y elegante desarrollado con JavaWeb (Servlets/JSP) como parte de un trabajo universitario. La aplicación sigue el patrón de arquitectura MVC (Modelo-Vista-Controlador) y utiliza el patrón DAO (Data Access Object) para la capa de persistencia, aplicando principios SOLID para garantizar código limpio y mantenible.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Uso](#uso)
- [Credenciales de Acceso](#credenciales-de-acceso)
- [Principios SOLID Aplicados](#principios-solid-aplicados)
- [Documentación Adicional](#documentación-adicional)
- [Autores](#autores)

## ✨ Características

### Área Pública
- ✅ Visualización de lista de artículos publicados
- ✅ Lectura del contenido completo de cada artículo
- ✅ Diseño responsive con Bootstrap 5
- ✅ Navegación intuitiva

### Área de Administración
- ✅ Sistema de autenticación seguro
- ✅ Dashboard con estadísticas
- ✅ CRUD completo de artículos (Crear, Leer, Actualizar, Eliminar)
- ✅ **Gestión de usuarios** (solo administradores)
  - Listar todos los usuarios
  - Promover/demover roles (admin/autor)
  - Eliminar usuarios individuales
  - Eliminar todos los usuarios no-administradores
- ✅ Gestión de contenido en tiempo real
- ✅ Protección de rutas mediante filtros

### Seguridad
- ✅ Contraseñas hasheadas con SHA-256
- ✅ Filtro de autenticación para rutas protegidas
- ✅ Gestión de sesiones HTTP
- ✅ Validación de entrada de formularios

## 🛠 Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Java** | JDK 21+ | Lenguaje de programación |
| **Jakarta EE** | 10+ | API de servlets y JSP |
| **Apache Tomcat** | 10+ | Servidor de aplicaciones |
| **MySQL** | 8.0+ | Base de datos relacional |
| **JSTL** | 3.0 | Tag library para JSP |
| **Bootstrap** | 5.3 | Framework CSS responsive |
| **Apache Ant** | - | Sistema de build |

## 📦 Requisitos Previos

Antes de instalar el proyecto, asegúrese de tener:

1. **Java Development Kit (JDK) 21 o superior**
   - Descargar: https://www.oracle.com/java/technologies/downloads/
   - Verificar instalación: `java -version`

2. **Apache Tomcat 10 o superior**
   - Descargar: https://tomcat.apache.org/download-10.cgi
   - Extraer en una carpeta (ej: `C:\tomcat` o `/opt/tomcat`)

3. **MySQL Server 8.0 o superior**
   - Descargar: https://dev.mysql.com/downloads/mysql/
   - O instalar XAMPP: https://www.apachefriends.org/

4. **Apache NetBeans IDE (Recomendado)**
   - Descargar: https://netbeans.apache.org/download/
   - Alternativamente: IntelliJ IDEA o Eclipse

5. **Dependencias JAR** (Descargar y colocar en `web/WEB-INF/lib/`)
   - **MySQL Connector/J** (8.0+): https://dev.mysql.com/downloads/connector/j/
   - **JSTL API** (3.0): https://jakarta.ee/specifications/tags/3.0/
   - **JSTL Implementation** (3.0): https://jakarta.ee/specifications/tags/3.0/

## 🚀 Instalación

### Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/slmorenog-ud/AdvancedFinalProject.git
cd AdvancedFinalProject
```

### Paso 2: Configurar la Base de Datos

1. **Iniciar MySQL Server**
   ```bash
   # Si usas XAMPP, inicia el servidor MySQL desde el panel de control
   # O si lo instalaste directamente, inicia el servicio
   ```

2. **Ejecutar el script de base de datos**
   ```bash
   # Opción 1: Desde línea de comandos
   mysql -u root -p < database/schema.sql
   
   # Opción 2: Desde phpMyAdmin o MySQL Workbench
   # - Abrir phpMyAdmin (http://localhost/phpmyadmin)
   # - Crear nueva base de datos llamada 'blog_db'
   # - Importar el archivo database/schema.sql
   ```

3. **Verificar la creación**
   ```sql
   USE blog_db;
   SHOW TABLES;
   SELECT * FROM usuarios;
   SELECT * FROM articulos;
   ```

### Paso 3: Descargar Dependencias JAR

Descargar los siguientes JARs y colocarlos en `web/WEB-INF/lib/`:

1. **MySQL Connector/J 8.0.33** (o superior)
   - URL: https://dev.mysql.com/downloads/connector/j/
   - Archivo: `mysql-connector-j-8.0.33.jar`

2. **Jakarta Standard Tag Library API 3.0.0**
   - URL: https://jakarta.ee/specifications/tags/3.0/
   - Archivo: `jakarta.servlet.jsp.jstl-api-3.0.0.jar`

3. **Jakarta Standard Tag Library Implementation 3.0.0**
   - URL: https://jakarta.ee/specifications/tags/3.0/
   - Archivo: `jakarta.servlet.jsp.jstl-3.0.0.jar`

```bash
# La estructura debe quedar así:
web/WEB-INF/lib/
├── mysql-connector-j-8.0.33.jar
├── jakarta.servlet.jsp.jstl-api-3.0.0.jar
└── jakarta.servlet.jsp.jstl-3.0.0.jar
```

### Paso 4: Configurar Apache NetBeans

1. **Abrir el proyecto**
   - File → Open Project
   - Seleccionar la carpeta del proyecto

2. **Configurar Apache Tomcat**
   - Tools → Servers
   - Add Server → Apache Tomcat 10+
   - Especificar la ruta de instalación de Tomcat

3. **Configurar el proyecto**
   - Click derecho en el proyecto → Properties
   - Run → Server: Seleccionar Apache Tomcat 10+
   - Run → Context Path: `/AdvancedFinalProject`

## ⚙️ Configuración

### Configurar la Conexión a la Base de Datos

**Odally** ofrece dos métodos para configurar la conexión a la base de datos:

#### Opción 1: Interfaz Web de Configuración (Recomendado) ⭐

1. Al iniciar la aplicación por primera vez, si la conexión a la base de datos falla, será redirigido automáticamente a la página de configuración
2. También puede acceder manualmente a: `http://localhost:8080/AdvancedFinalProject/setup`
3. Complete el formulario con los datos de su servidor MySQL:
   - **Host**: localhost (o la dirección de su servidor)
   - **Puerto**: 3306 (puerto por defecto de MySQL)
   - **Base de datos**: blog_db
   - **Usuario**: root (o su usuario de MySQL)
   - **Contraseña**: su contraseña de MySQL (dejar en blanco si no tiene)
4. Click en "Probar Conexión" para verificar que los datos sean correctos
5. Si la conexión es exitosa, click en "Guardar Configuración"
6. La aplicación guardará la configuración en `db.properties` y estará lista para usar

#### Opción 2: Edición Manual del Archivo

Editar el archivo `src/java/com/blog/dao/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/blog_db?useSSL=false&serverTimezone=UTC
db.user=root
db.password=
```

**⚠️ IMPORTANTE - Seguridad:**
- Las credenciales se almacenan en el archivo `db.properties` solo para propósitos educativos
- En producción, usar variables de entorno o un sistema de gestión de secretos
- Nunca commitear credenciales reales al repositorio

## 📁 Estructura del Proyecto

```
AdvancedFinalProject/
├── src/java/com/blog/
│   ├── model/                  # POJOs (Entidades)
│   │   ├── Usuario.java
│   │   └── Articulo.java
│   ├── dao/                    # Data Access Objects
│   │   ├── IUsuarioDAO.java           # Interface
│   │   ├── IArticuloDAO.java          # Interface
│   │   ├── MySQLUsuarioDAO.java       # Implementación MySQL
│   │   ├── MySQLArticuloDAO.java      # Implementación MySQL
│   │   └── ConexionBD.java            # Singleton de conexión
│   ├── controller/             # Servlets (Controladores)
│   │   ├── ArticuloServlet.java       # Vista pública
│   │   ├── LoginServlet.java          # Autenticación
│   │   ├── LogoutServlet.java         # Cerrar sesión
│   │   └── AdminArticuloServlet.java  # CRUD admin
│   ├── filter/                 # Filtros
│   │   └── AuthFilter.java            # Protección de rutas
│   └── util/                   # Utilidades
│       └── PasswordUtil.java          # Hash de contraseñas
├── web/
│   ├── WEB-INF/
│   │   ├── web.xml                    # Descriptor de despliegue
│   │   └── lib/                       # JARs de dependencias
│   ├── css/
│   │   └── styles.css                 # Estilos personalizados
│   ├── index.jsp                      # Página principal
│   ├── articulo.jsp                   # Detalle de artículo
│   ├── login.jsp                      # Formulario de login
│   ├── error.jsp                      # Página de error
│   └── admin/
│       ├── dashboard.jsp              # Panel de administración
│       ├── listar.jsp                 # Lista de artículos
│       ├── crear.jsp                  # Crear artículo
│       └── editar.jsp                 # Editar artículo
├── database/
│   └── schema.sql                     # Script de base de datos
├── nbproject/
│   └── project.properties             # Configuración NetBeans
├── build.xml                          # Archivo Ant
└── README.md                          # Este archivo
```

## 🎯 Uso

### Compilar el Proyecto

```bash
# Con Apache Ant (desde la raíz del proyecto)
ant clean
ant compile
ant dist

# Con NetBeans
# Click derecho en el proyecto → Clean and Build
```

### Ejecutar la Aplicación

1. **Desde NetBeans:**
   - Click derecho en el proyecto → Run
   - O presionar F6

2. **Manualmente con Tomcat:**
   ```bash
   # Copiar el WAR generado
   cp dist/AdvancedFinalProject.war /path/to/tomcat/webapps/
   
   # Iniciar Tomcat
   cd /path/to/tomcat/bin
   ./startup.sh    # Linux/Mac
   startup.bat     # Windows
   ```

3. **Acceder a la aplicación:**
   - URL: http://localhost:8080/AdvancedFinalProject/articulos
   - O simplemente: http://localhost:8080/AdvancedFinalProject/

### Navegar por la Aplicación

### Área Pública (Sin login)
- **Inicio:** http://localhost:8080/AdvancedFinalProject/articulos
- **Ver artículo:** Click en "Leer más" en cualquier artículo

#### Área de Administración (Requiere login)
1. Click en "Iniciar Sesión" en el menú
2. Ingresar credenciales (ver sección siguiente)
3. Acceso al Dashboard: http://localhost:8080/AdvancedFinalProject/admin/articulos?action=dashboard
4. Gestionar artículos: http://localhost:8080/AdvancedFinalProject/admin/articulos?action=listar
5. **Gestionar usuarios (solo admins):** http://localhost:8080/AdvancedFinalProject/admin/usuarios

## 🔐 Credenciales de Acceso

### Usuario Administrador Principal
```
Usuario: admin
Contraseña: admin123
```

### Usuarios de Prueba (Todos con la misma contraseña)
```
Usuario: alejandra    | Contraseña: admin123
Usuario: dylan        | Contraseña: admin123
Usuario: sergio       | Contraseña: admin123
```

**Nota de Seguridad:** En un entorno de producción, cambie estas contraseñas inmediatamente después de la instalación.

## 🎨 Principios SOLID Aplicados

Este proyecto implementa los principios SOLID de diseño de software:

### S - Single Responsibility Principle (Responsabilidad Única)
- Cada clase tiene una única responsabilidad
- `ConexionBD`: Solo gestiona conexiones a la base de datos
- `PasswordUtil`: Solo gestiona el hasheo de contraseñas
- `Usuario` y `Articulo`: Solo mantienen datos

### O - Open/Closed Principle (Abierto/Cerrado)
- El sistema está abierto a extensión pero cerrado a modificación
- Uso de interfaces (`IArticuloDAO`, `IUsuarioDAO`)
- Se puede cambiar de MySQL a otra BD sin modificar los servlets

### L - Liskov Substitution Principle (Sustitución de Liskov)
- Cualquier implementación de `IArticuloDAO` puede sustituir a otra
- `MySQLArticuloDAO` cumple el contrato definido por la interfaz
- Los servlets funcionan con cualquier implementación

- Los servlets funcionan con cualquier implementación

### I - Interface Segregation Principle (Segregación de Interfaces)
- Interfaces específicas para cada necesidad
- `IUsuarioDAO` e `IArticuloDAO` tienen métodos específicos a su dominio

### D - Dependency Inversion Principle (Inversión de Dependencias)
- Los módulos de alto nivel (Servlets) dependen de abstracciones (Interfaces)
- No dependen de implementaciones concretas
- Facilita las pruebas unitarias y la mantenibilidad

## 📚 Documentación Adicional

Hemos preparado documentación técnica exhaustiva para este proyecto:

### 📖 [Principios y Patrones de Diseño (LaTeX)](./tex%20archives/PRINCIPIOS_Y_PATRONES.tex)
Documento técnico completo que explica en detalle:
- **Principios SOLID** con ejemplos del código del proyecto
- **Principios de Arquitectura de Paquetes** (REP, CCP, CRP, ADP, SDP, SAP)
- **Otros principios de diseño** (DRY, KISS, YAGNI, SoC, LoD)
- **Patrones de diseño implementados** (Singleton, DAO, MVC, Object Pool, Strategy)
- **Referencias bibliográficas** (Martin, Fowler, GoF)
- **Ejemplos de código completos** con análisis detallado

> 💡 **Cómo compilar:** Puede subir este archivo a [Overleaf](https://www.overleaf.com/) o compilarlo localmente con:
> ```bash
> cd "tex archives"
> pdflatex PRINCIPIOS_Y_PATRONES.tex
> bibtex PRINCIPIOS_Y_PATRONES
> pdflatex PRINCIPIOS_Y_PATRONES.tex
> pdflatex PRINCIPIOS_Y_PATRONES.tex
> ```

### 📊 [Diagramas del Sistema (Mermaid)](./tex%20archives/Diagramas/DIAGRAMS.md)
Diagramas UML y de arquitectura del sistema:
- **Diagrama de Clases Completo** - Todas las clases con relaciones
- **Diagrama de Paquetes** - Organización y dependencias
- **Diagrama de Casos de Uso** - Funcionalidades por actor
- **Diagramas de Secuencia** - Flujos de autenticación y reconexión
- **Diagrama de Despliegue** - Arquitectura física
- **Diagrama de Componentes** - Componentes del sistema

> 💡 **Cómo ver:** GitHub renderiza estos diagramas automáticamente. También puede copiar el código a [Mermaid Live Editor](https://mermaid.live/).

### 📘 [Manual de Usuario (LaTeX)](./tex%20archives/ManualDeUsuario/main.tex)
Guía completa de uso del sistema para usuarios finales.

> 💡 **Compilar con:** `pdflatex main.tex` desde el directorio ManualDeUsuario

### 🔍 Código Fuente Documentado
Todo el código Java incluye **Javadoc completo** con:
- Descripción de cada clase y método
- Principios SOLID aplicados
- Patrones de diseño implementados
- Parámetros, valores de retorno y excepciones
- Referencias cruzadas al documento LaTeX

## 🐛 Solución de Problemas

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
- **Causa:** El driver MySQL no está en el classpath
- **Solución:** 
  1. Descargar `mysql-connector-j-8.0.33.jar` (o superior)
  2. Colocar en `web/WEB-INF/lib/`
  3. Reiniciar el servidor Tomcat

### Error: "Cannot connect to database"
- **Causa:** MySQL no está ejecutándose o la configuración es incorrecta
- **Solución:** 
  1. Verificar que MySQL esté ejecutándose:
     ```bash
     # Windows con XAMPP
     Abrir XAMPP Control Panel y verificar que MySQL esté "Running"
     
     # Linux
     sudo systemctl status mysql
     ```
  2. Usar la **página de configuración automática**: `http://localhost:8080/AdvancedFinalProject/setup`
  3. Verificar credenciales (usuario, contraseña, nombre de BD)
  4. Verificar que la base de datos `blog_db` exista:
     ```sql
     SHOW DATABASES;
     ```
  5. Si es necesario, ejecutar el script: `database/schema.sql` o `setup_database.sql`

### Puerto 8080 ya está en uso

#### Síntomas:
- Error al iniciar Tomcat: `Address already in use: bind`
- No se puede acceder a `http://localhost:8080`

#### Opción 1: Cambiar el puerto de Tomcat (Requiere permisos de administrador)

**En Windows:**
1. Navegar a: `C:\apache-tomcat-10.x\conf\`
2. Abrir `server.xml` con un editor de texto
3. Buscar la línea:
   ```xml
   <Connector port="8080" protocol="HTTP/1.1"
   ```
4. Cambiar `8080` por otro puerto (ej: `8081`, `9090`)
5. Guardar y reiniciar Tomcat
6. Acceder a: `http://localhost:8081/AdvancedFinalProject/`

**En Linux/Mac:**
1. Navegar a: `/opt/tomcat/conf/` o donde esté instalado
2. Editar `server.xml`:
   ```bash
   sudo nano /opt/tomcat/conf/server.xml
   ```
3. Cambiar el puerto como arriba
4. Reiniciar Tomcat:
   ```bash
   sudo /opt/tomcat/bin/shutdown.sh
   sudo /opt/tomcat/bin/startup.sh
   ```

#### Opción 2: Identificar y detener el proceso que usa el puerto 8080

**En Windows (requiere permisos de administrador):**
```bash
# 1. Identificar qué proceso usa el puerto 8080
netstat -ano | findstr :8080

# 2. Verás algo como: TCP  0.0.0.0:8080  0.0.0.0:0  LISTENING  1234
#    El número al final (1234) es el PID

# 3. Detener el proceso (reemplazar 1234 con el PID real)
taskkill /PID 1234 /F
```

**En Linux/Mac:**
```bash
# 1. Identificar el proceso
sudo lsof -i :8080

# 2. Detener el proceso (reemplazar 1234 con el PID real)
sudo kill -9 1234
```

#### Opción 3: Usar NetBeans para cambiar el puerto (No requiere permisos de admin)

Si estás usando NetBeans y **NO tienes permisos de administrador**:

1. Click derecho en el proyecto → **Properties**
2. En la categoría **Run**
3. En **Server**, click en el botón **...** junto al servidor
4. En la configuración del servidor, buscar **HTTP Port**
5. Cambiar a otro puerto disponible (ej: `8081`, `9090`)
6. Click **OK** y reiniciar el servidor desde NetBeans

**Nota:** Esta configuración solo afecta a la ejecución desde NetBeans, no al servidor Tomcat global.

### Puerto 8005 ya está en uso (Shutdown Port)

Si ves error sobre el puerto 8005:

1. Abrir `server.xml`
2. Buscar:
   ```xml
   <Server port="8005" shutdown="SHUTDOWN">
   ```
3. Cambiar `8005` por otro puerto (ej: `8006`)

### Error 404 al acceder a la aplicación
- **Causa:** La URL o el contexto path son incorrectos
- **Solución:** 
  - Verificar que la URL sea correcta: `http://localhost:8080/AdvancedFinalProject/articulos`
  - Si cambiaste el puerto, usar: `http://localhost:PUERTO/AdvancedFinalProject/articulos`
  - Verificar en el administrador de Tomcat que la aplicación esté desplegada

### Error: "JSTL tags not working" o `<%@ taglib ... %>` no reconocido
- **Causa:** JARs de JSTL no están en el classpath
- **Solución:** 
  1. Descargar ambos JARs de JSTL 3.0:
     - `jakarta.servlet.jsp.jstl-api-3.0.0.jar`
     - `jakarta.servlet.jsp.jstl-3.0.0.jar`
  2. Colocar en `web/WEB-INF/lib/`
  3. Reiniciar Tomcat

### Cómo usar la página de Setup (Configuración de Base de Datos)

La aplicación incluye una página de configuración web para facilitar la conexión a MySQL:

1. **Acceso automático:** Si la aplicación detecta que no puede conectarse a la BD, te redirigirá automáticamente a `/setup`

2. **Acceso manual:** Navega a `http://localhost:8080/AdvancedFinalProject/setup`

3. **Completar el formulario:**
   - **Host:** `localhost` (o la IP de tu servidor MySQL)
   - **Puerto:** `3306` (puerto por defecto de MySQL)
   - **Base de datos:** `blog_db` (o el nombre que hayas elegido)
   - **Usuario:** `root` (o tu usuario MySQL)
   - **Contraseña:** Tu contraseña de MySQL (dejar en blanco si no tiene)

4. **Probar la conexión:** Click en **"Probar Conexión"**
   - Si es exitosa, verás un mensaje verde ✓
   - Si falla, verás un mensaje de error con detalles

5. **Guardar configuración:** Click en **"Guardar Configuración"**
   - Esto guardará las credenciales en `db.properties`
   - La aplicación estará lista para usar

6. **Problemas comunes en Setup:**
   - **"Connection refused":** MySQL no está ejecutándose
   - **"Access denied":** Usuario o contraseña incorrectos
   - **"Unknown database":** La base de datos `blog_db` no existe (ejecutar script SQL primero)

### Caracteres especiales (tildes, ñ) aparecen mal

- **Causa:** Problema de codificación UTF-8
- **Solución:** 
  - Verificar que `CharacterEncodingFilter` esté configurado en `web.xml`
  - Verificar que los archivos JSP tengan: `<%@ page contentType="text/html;charset=UTF-8" %>`
  - En MySQL, verificar que las tablas usen `utf8mb4_unicode_ci`

### Error de compilación en NetBeans

- **Causa:** Dependencias faltantes o configuración incorrecta
- **Solución:**
  1. Click derecho en el proyecto → **Clean and Build**
  2. Verificar que todos los JARs estén en `web/WEB-INF/lib/`
  3. Click derecho en el proyecto → **Properties** → **Libraries** → Verificar que Tomcat esté configurado

### La aplicación funciona pero las rutas `/admin/*` muestran error 403

- **Causa:** No has iniciado sesión o la sesión expiró
- **Solución:** 
  - Navegar a `http://localhost:8080/AdvancedFinalProject/login`
  - Iniciar sesión con credenciales válidas (ver sección [Credenciales de Acceso](#credenciales-de-acceso))
  - El filtro `AuthFilter` protege automáticamente las rutas `/admin/*`

## 📝 Licencia

Este proyecto fue desarrollado con fines educativos como parte de un trabajo universitario.

## 👥 Autores

* **Alejandra Munevar** - Universidad Distrital Francisco José de Caldas
* **Dylan Silva** - Universidad Distrital Francisco José de Caldas
* **Sergio Moreno** - Universidad Distrital Francisco José de Caldas

---

**Universidad Distrital Francisco José de Caldas**  
Ingeniería de Sistemas  
Programación Avanzada - 2025

---

## ✨ Características Destacadas de Odally

### 🎨 Diseño Moderno y Elegante
- Paleta de colores cuidadosamente seleccionada con tonos violeta/índigo
- Tipografía profesional con Inter y Literata
- Cards con efectos hover y sombras suaves
- Diseño completamente responsive

### 👥 Gestión Avanzada de Usuarios
- Panel de administración exclusivo para gestionar usuarios
- Protecciones de seguridad:
  - No se puede eliminar el usuario administrador principal
  - No se puede auto-eliminar un administrador
  - Confirmación doble para eliminación masiva
- Promoción/demotion de roles de forma sencilla

### 🔒 Seguridad Robusta
- Contraseñas hasheadas con SHA-256
- Pool de conexiones a base de datos con reintentos automáticos
- Validación de entrada en todos los formularios
- Protección contra SQL injection mediante PreparedStatements
