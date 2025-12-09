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

Editar el archivo `src/java/com/blog/dao/ConexionBD.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/blog_db?useSSL=false&serverTimezone=UTC";
private static final String USUARIO = "root";  // Tu usuario de MySQL
private static final String PASSWORD = "";      // Tu contraseña de MySQL
```

**⚠️ IMPORTANTE - Seguridad:**
- Las credenciales están hardcodeadas solo para propósitos educativos
- En producción, usar variables de entorno o un archivo de configuración externo
- Nunca commitear credenciales reales al repositorio
- Si usas un usuario diferente a `root` o tienes contraseña, actualiza estos valores

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

Hemos preparado documentación detallada adicional para este proyecto:

- **[Manual de Usuario (LaTeX)](./tex archives/USER_MANUAL.tex):** Guía completa de uso del sistema.
  > 💡 **Cómo compilar:** Puede subir este archivo a [Overleaf](https://www.overleaf.com/) o compilarlo localmente si tiene TeX Live/MiKTeX instalado (`pdflatex "tex archives/USER_MANUAL.tex"`).

- **[Principios de Ingeniería y Patrones (LaTeX)](./tex archives/PRINCIPIOS_Y_PATRONES.tex):** Documentación exhaustiva de SOLID, DRY, KISS, SoC y Patrones de Diseño, con referencias cruzadas al código.
  > 💡 **Cómo compilar:** Igualmente, compatible con cualquier compilador LaTeX estándar.

- **[Diagramas del Proyecto (Mermaid)](./tex archives/DIAGRAMS.md):** Diagramas de Clases y Casos de Uso del sistema.
  > 💡 **Cómo ver:** GitHub renderiza estos diagramas automáticamente. También puede copiar el código a [Mermaid Live Editor](https://mermaid.live/).

## 🐛 Solución de Problemas

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
- **Solución:** Verificar que `mysql-connector-j-*.jar` esté en `web/WEB-INF/lib/`

### Error: "Cannot connect to database"
- **Solución:** Verificar que MySQL esté ejecutándose
- Verificar las credenciales en `ConexionBD.java`
- Verificar que la base de datos `blog_db` exista

### Error 404 al acceder a la aplicación
- **Solución:** Verificar que el contexto path sea correcto
- La URL debe ser: `http://localhost:8080/AdvancedFinalProject/articulos`

### Error: "JSTL tags not working"
- **Solución:** Verificar que los JARs de JSTL estén en `web/WEB-INF/lib/`

### Puerto 8080 ya en uso
- **Solución:** Cambiar el puerto de Tomcat editando `conf/server.xml`
- O detener el proceso que esté usando el puerto 8080

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
