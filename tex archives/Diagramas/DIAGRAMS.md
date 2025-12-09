# Diagramas del Proyecto Odally | Blog

Este documento contiene los diagramas UML y de arquitectura del sistema de gestión de contenidos Odally | Blog, desarrollado con JavaWeb (Servlets/JSP) y MySQL.

## 1. Diagrama de Clases Completo

```mermaid
classDiagram
    %% Paquete Model
    namespace com_blog_model {
        class Articulo {
            -int id
            -String titulo
            -String contenido
            -LocalDateTime fechaPublicacion
            -int autorId
            -String autorNombre
            +Articulo()
            +Articulo(int, String, String, LocalDateTime, int)
            +getId() int
            +setId(int)
            +getTitulo() String
            +setTitulo(String)
            +getContenido() String
            +setContenido(String)
            +getFechaPublicacion() LocalDateTime
            +setFechaPublicacion(LocalDateTime)
            +getAutorId() int
            +setAutorId(int)
            +getAutorNombre() String
            +setAutorNombre(String)
            +getFechaPublicacionFormateada() String
        }
        
        class Usuario {
            -int id
            -String nombre
            -String email
            -String username
            -String password
            -String rol
            +Usuario()
            +Usuario(int, String, String, String, String, String)
            +getId() int
            +setId(int)
            +getNombre() String
            +setNombre(String)
            +getEmail() String
            +setEmail(String)
            +getUsername() String
            +setUsername(String)
            +getPassword() String
            +setPassword(String)
            +getRol() String
            +setRol(String)
        }
    }

    %% Paquete DAO Interfaces
    namespace com_blog_dao {
        class IArticuloDAO {
            <<interface>>
            +listarTodos() List~Articulo~
            +obtenerPorId(int) Articulo
            +crear(Articulo) boolean
            +actualizar(Articulo) boolean
            +eliminar(int) boolean
            +contarTotal() int
        }
        
        class IUsuarioDAO {
            <<interface>>
            +buscarPorUsername(String) Usuario
            +obtenerPorId(int) Usuario
            +crear(Usuario) boolean
            +listarTodos() List~Usuario~
            +actualizarRol(int, String) boolean
            +eliminar(int) boolean
            +eliminarTodosExceptoAdmins() int
        }
        
        class MySQLArticuloDAO {
            -ConexionBD conexionBD
            -int MAX_OPERATION_RETRIES
            +MySQLArticuloDAO()
            +listarTodos() List~Articulo~
            +obtenerPorId(int) Articulo
            +crear(Articulo) boolean
            +actualizar(Articulo) boolean
            +eliminar(int) boolean
            +contarTotal() int
            -executeWithRetry(DatabaseOperation, String) T
        }
        
        class MySQLUsuarioDAO {
            -ConexionBD conexionBD
            -int MAX_OPERATION_RETRIES
            +MySQLUsuarioDAO()
            +buscarPorUsername(String) Usuario
            +obtenerPorId(int) Usuario
            +crear(Usuario) boolean
            +listarTodos() List~Usuario~
            +actualizarRol(int, String) boolean
            +eliminar(int) boolean
            +eliminarTodosExceptoAdmins() int
            -executeWithRetry(DatabaseOperation, String) T
        }
        
        class ConexionBD {
            <<Singleton>>
            -static volatile ConexionBD instancia
            -List~PooledConnection~ connectionPool
            -int maxConnections
            -int minConnections
            -int maxRetries
            -long initialRetryDelayMs
            -ConexionBD()
            +static getInstancia() ConexionBD
            +getConexion() Connection
            +cerrarConexion(Connection)
            +shutdown()
            +getPoolStats() String
            +static verificarConexion() boolean
            +static verificarConexionConParametros(String, String, String) boolean
            -initializePool()
            -createNewConnection() Connection
            -getConnectionFromPool() Connection
            -isConnectionValid(Connection) boolean
            -cleanupIdleConnections()
        }
        
        class DatabaseHealthCheck {
            -ConexionBD conexionBD
            +DatabaseHealthCheck()
            +checkDatabaseAvailability() HealthCheckResult
            +checkRequiredTables() HealthCheckResult
            +checkTableStructure(String, String[]) HealthCheckResult
            +performCompleteHealthCheck() HealthCheckResult
            -analyzeConnectionError(SQLException) String
            -tableExists(DatabaseMetaData, String) boolean
        }
        
        class HealthCheckResult {
            -boolean healthy
            -String message
            -List~String~ issues
            +HealthCheckResult(boolean, String)
            +isHealthy() boolean
            +getMessage() String
            +getIssues() List~String~
            +addIssue(String)
        }
    }

    %% Paquete Controller
    namespace com_blog_controller {
        class ArticuloServlet {
            -IArticuloDAO articuloDAO
            +init()
            +doGet(HttpServletRequest, HttpServletResponse)
            +doPost(HttpServletRequest, HttpServletResponse)
            -listarArticulos(HttpServletRequest, HttpServletResponse)
            -verArticulo(HttpServletRequest, HttpServletResponse)
        }
        
        class AdminArticuloServlet {
            -IArticuloDAO articuloDAO
            -IUsuarioDAO usuarioDAO
            +init()
            +doGet(HttpServletRequest, HttpServletResponse)
            +doPost(HttpServletRequest, HttpServletResponse)
            -mostrarDashboard(HttpServletRequest, HttpServletResponse)
            -listarArticulos(HttpServletRequest, HttpServletResponse)
            -mostrarFormularioCrear(HttpServletRequest, HttpServletResponse)
            -mostrarFormularioEditar(HttpServletRequest, HttpServletResponse)
            -crearArticulo(HttpServletRequest, HttpServletResponse)
            -actualizarArticulo(HttpServletRequest, HttpServletResponse)
            -eliminarArticulo(HttpServletRequest, HttpServletResponse)
        }
        
        class AdminUsuariosServlet {
            -IUsuarioDAO usuarioDAO
            +init()
            +doGet(HttpServletRequest, HttpServletResponse)
            +doPost(HttpServletRequest, HttpServletResponse)
            -listarUsuarios(HttpServletRequest, HttpServletResponse)
            -cambiarRol(HttpServletRequest, HttpServletResponse)
            -eliminarUsuario(HttpServletRequest, HttpServletResponse)
            -eliminarTodosAutores(HttpServletRequest, HttpServletResponse)
        }
        
        class LoginServlet {
            -IUsuarioDAO usuarioDAO
            +init()
            +doGet(HttpServletRequest, HttpServletResponse)
            +doPost(HttpServletRequest, HttpServletResponse)
        }
        
        class LogoutServlet {
            +doGet(HttpServletRequest, HttpServletResponse)
            +doPost(HttpServletRequest, HttpServletResponse)
        }
        
        class RegisterServlet {
            -IUsuarioDAO usuarioDAO
            +init()
            +doGet(HttpServletRequest, HttpServletResponse)
            +doPost(HttpServletRequest, HttpServletResponse)
        }
        
        class SetupServlet {
            +doGet(HttpServletRequest, HttpServletResponse)
            +doPost(HttpServletRequest, HttpServletResponse)
            -probarConexion(HttpServletRequest, HttpServletResponse)
            -guardarConfiguracion(HttpServletRequest, HttpServletResponse)
        }
    }

    %% Paquete Filter
    namespace com_blog_filter {
        class AuthFilter {
            +init(FilterConfig)
            +doFilter(ServletRequest, ServletResponse, FilterChain)
            +destroy()
        }
        
        class CharacterEncodingFilter {
            -String encoding
            +init(FilterConfig)
            +doFilter(ServletRequest, ServletResponse, FilterChain)
            +destroy()
        }
        
        class DatabaseCheckFilter {
            -static String SETUP_PATH
            -static Boolean databaseAvailable
            -static long lastCheck
            -static long CHECK_INTERVAL_MS
            +init(FilterConfig)
            +doFilter(ServletRequest, ServletResponse, FilterChain)
            +destroy()
            -shouldSkipCheck(String) boolean
            -isDatabaseAvailable() boolean
            +static invalidateCache()
        }
    }

    %% Paquete Util
    namespace com_blog_util {
        class PasswordUtil {
            <<utility>>
            +static hashPassword(String) String
            +static verificarPassword(String, String) boolean
        }
    }

    %% Relaciones entre clases
    MySQLArticuloDAO ..|> IArticuloDAO : implements
    MySQLUsuarioDAO ..|> IUsuarioDAO : implements
    
    MySQLArticuloDAO --> ConexionBD : uses
    MySQLUsuarioDAO --> ConexionBD : uses
    DatabaseHealthCheck --> ConexionBD : uses
    
    IArticuloDAO --> Articulo : uses
    IUsuarioDAO --> Usuario : uses
    
    ArticuloServlet --> IArticuloDAO : uses
    AdminArticuloServlet --> IArticuloDAO : uses
    AdminArticuloServlet --> IUsuarioDAO : uses
    AdminUsuariosServlet --> IUsuarioDAO : uses
    LoginServlet --> IUsuarioDAO : uses
    LoginServlet --> PasswordUtil : uses
    RegisterServlet --> IUsuarioDAO : uses
    RegisterServlet --> PasswordUtil : uses
    
    DatabaseCheckFilter --> ConexionBD : uses
    
    DatabaseHealthCheck ..> HealthCheckResult : creates
```

## 2. Diagrama de Paquetes

```mermaid
graph TD
    subgraph Presentation["Capa de Presentación"]
        JSP[JSP Views]
    end
    
    subgraph Controller["Capa de Control"]
        Servlets[Servlets]
        Filters[Filters]
    end
    
    subgraph Business["Capa de Negocio"]
        Model[Model POJOs]
        Util[Utilities]
    end
    
    subgraph Persistence["Capa de Persistencia"]
        DAOInt[DAO Interfaces]
        DAOImpl[DAO Implementations]
        DB[ConexionBD]
    end
    
    subgraph Database["Base de Datos"]
        MySQL[(MySQL)]
    end
    
    JSP --> Servlets
    Filters --> Servlets
    Servlets --> DAOInt
    Servlets --> Model
    Servlets --> Util
    DAOInt --> Model
    DAOImpl -.implements.-> DAOInt
    DAOImpl --> DB
    DB --> MySQL
    
    style Presentation fill:#e1f5ff
    style Controller fill:#fff4e1
    style Business fill:#e8f5e9
    style Persistence fill:#f3e5f5
    style Database fill:#ffebee
```

## 3. Diagrama de Casos de Uso

```mermaid
graph LR
    subgraph Sistema["Sistema Odally | Blog"]
        UC1[Ver Lista de Artículos]
        UC2[Leer Artículo Completo]
        UC3[Registrarse]
        UC4[Iniciar Sesión]
        UC5[Cerrar Sesión]
        UC6[Crear Artículo]
        UC7[Editar Artículo]
        UC8[Eliminar Artículo]
        UC9[Ver Dashboard]
        UC10[Gestionar Usuarios]
        UC11[Cambiar Rol Usuario]
        UC12[Eliminar Usuario]
        UC13[Configurar Base de Datos]
    end
    
    Visitante([Visitante])
    Autor([Usuario Autor])
    Admin([Administrador])
    
    Visitante --> UC1
    Visitante --> UC2
    Visitante --> UC3
    Visitante --> UC4
    
    Autor --> UC1
    Autor --> UC2
    Autor --> UC4
    Autor --> UC5
    Autor --> UC6
    Autor --> UC7
    Autor --> UC8
    Autor --> UC9
    
    Admin --> UC1
    Admin --> UC2
    Admin --> UC4
    Admin --> UC5
    Admin --> UC6
    Admin --> UC7
    Admin --> UC8
    Admin --> UC9
    Admin --> UC10
    Admin --> UC11
    Admin --> UC12
    Admin --> UC13
    
    UC10 ..> UC11 : includes
    UC10 ..> UC12 : includes
```

## 4. Diagrama de Secuencia: Autenticación de Usuario

```mermaid
sequenceDiagram
    actor Usuario
    participant Browser
    participant LoginServlet
    participant IUsuarioDAO
    participant MySQLUsuarioDAO
    participant ConexionBD
    participant PasswordUtil
    participant MySQL
    
    Usuario->>Browser: Envía credenciales (username, password)
    Browser->>LoginServlet: POST /login
    LoginServlet->>IUsuarioDAO: buscarPorUsername(username)
    IUsuarioDAO->>MySQLUsuarioDAO: buscarPorUsername(username)
    MySQLUsuarioDAO->>ConexionBD: getConexion()
    ConexionBD->>ConexionBD: Obtener del pool
    ConexionBD-->>MySQLUsuarioDAO: Connection
    MySQLUsuarioDAO->>MySQL: SELECT * FROM usuarios WHERE username=?
    MySQL-->>MySQLUsuarioDAO: ResultSet
    MySQLUsuarioDAO->>MySQLUsuarioDAO: Mapear ResultSet a Usuario
    MySQLUsuarioDAO->>ConexionBD: cerrarConexion(conn)
    MySQLUsuarioDAO-->>IUsuarioDAO: Usuario
    IUsuarioDAO-->>LoginServlet: Usuario
    
    alt Usuario encontrado
        LoginServlet->>PasswordUtil: verificarPassword(inputPassword, usuario.password)
        PasswordUtil->>PasswordUtil: hashPassword(inputPassword)
        PasswordUtil->>PasswordUtil: compare(hash, storedHash)
        PasswordUtil-->>LoginServlet: boolean
        
        alt Password correcto
            LoginServlet->>LoginServlet: Crear sesión HTTP
            LoginServlet->>LoginServlet: session.setAttribute("usuario", usuario)
            LoginServlet-->>Browser: Redirect a /admin/articulos
            Browser-->>Usuario: Dashboard mostrado
        else Password incorrecto
            LoginServlet-->>Browser: Forward a login.jsp con error
            Browser-->>Usuario: "Contraseña incorrecta"
        end
    else Usuario no encontrado
        LoginServlet-->>Browser: Forward a login.jsp con error
        Browser-->>Usuario: "Usuario no encontrado"
    end
```

## 5. Diagrama de Secuencia: Reconexión Automática a BD

```mermaid
sequenceDiagram
    actor Usuario
    participant Servlet
    participant DAO
    participant ConexionBD
    participant MySQL
    
    Usuario->>Servlet: Solicita listar artículos
    Servlet->>DAO: listarTodos()
    DAO->>ConexionBD: getConexion()
    
    ConexionBD->>ConexionBD: getConnectionFromPool()
    
    alt Conexión disponible en pool
        ConexionBD->>ConexionBD: Validar conexión (testOnBorrow)
        ConexionBD->>MySQL: SELECT 1
        
        alt Conexión válida
            MySQL-->>ConexionBD: OK
            ConexionBD-->>DAO: Connection
        else Conexión inválida
            ConexionBD->>ConexionBD: Cerrar conexión inválida
            ConexionBD->>MySQL: Crear nueva conexión
            MySQL-->>ConexionBD: Nueva Connection
            ConexionBD-->>DAO: Connection
        end
    else Pool vacío
        ConexionBD->>ConexionBD: Intentar crear nueva (intento 1/3)
        ConexionBD->>MySQL: DriverManager.getConnection()
        
        alt Conexión exitosa
            MySQL-->>ConexionBD: Connection
            ConexionBD-->>DAO: Connection
        else Fallo de conexión
            ConexionBD->>ConexionBD: Esperar 1000ms (backoff)
            ConexionBD->>ConexionBD: Reintentar (intento 2/3)
            ConexionBD->>MySQL: DriverManager.getConnection()
            
            alt Reintento exitoso
                MySQL-->>ConexionBD: Connection
                ConexionBD-->>DAO: Connection
            else Fallo persistente
                ConexionBD->>ConexionBD: Esperar 2000ms (backoff exponencial)
                ConexionBD->>ConexionBD: Reintentar (intento 3/3)
                ConexionBD->>MySQL: DriverManager.getConnection()
                
                alt Tercer intento exitoso
                    MySQL-->>ConexionBD: Connection
                    ConexionBD-->>DAO: Connection
                else Fallo definitivo
                    ConexionBD-->>DAO: SQLException("No se pudo conectar")
                    DAO-->>Servlet: SQLException
                    Servlet-->>Usuario: Error de conexión
                end
            end
        end
    end
```

## 6. Diagrama de Despliegue

```mermaid
graph TB
    subgraph "Cliente"
        Browser[Navegador Web]
    end
    
    subgraph "Servidor de Aplicaciones"
        subgraph "Apache Tomcat 10+"
            WebApp[JavaWebBlog.war]
            subgraph "Aplicación"
                JSPs[JSP Pages]
                Servlets[Servlets]
                Filters[Filters]
                DAOs[DAO Layer]
                ConnPool[Connection Pool]
            end
        end
    end
    
    subgraph "Servidor de Base de Datos"
        MySQL[(MySQL 8.0+)]
        BlogDB[(blog_db)]
    end
    
    Browser -->|HTTP/HTTPS| WebApp
    JSPs --> Servlets
    Servlets --> Filters
    Servlets --> DAOs
    DAOs --> ConnPool
    ConnPool -->|JDBC| MySQL
    MySQL --> BlogDB
    
    style Browser fill:#e3f2fd
    style WebApp fill:#fff3e0
    style MySQL fill:#e8f5e9
    style BlogDB fill:#c8e6c9
```

## 7. Diagrama de Componentes

```mermaid
graph TD
    subgraph "Frontend"
        UI[User Interface<br/>JSP + JSTL + Bootstrap]
    end
    
    subgraph "Middleware"
        FC[Filter Chain<br/>- CharacterEncodingFilter<br/>- DatabaseCheckFilter<br/>- AuthFilter]
        SC[Servlet Container<br/>- ArticuloServlet<br/>- AdminArticuloServlet<br/>- AdminUsuariosServlet<br/>- LoginServlet<br/>- SetupServlet]
    end
    
    subgraph "Business Logic"
        Model[Domain Model<br/>- Usuario<br/>- Articulo]
        Utils[Utilities<br/>- PasswordUtil]
    end
    
    subgraph "Data Access"
        DAOInt[DAO Interfaces<br/>- IArticuloDAO<br/>- IUsuarioDAO]
        DAOImpl[DAO Implementations<br/>- MySQLArticuloDAO<br/>- MySQLUsuarioDAO]
        CP[Connection Pool<br/>ConexionBD Singleton]
    end
    
    subgraph "External"
        DB[(MySQL Database<br/>blog_db)]
    end
    
    UI --> FC
    FC --> SC
    SC --> Model
    SC --> Utils
    SC --> DAOInt
    DAOInt -.implemented by.-> DAOImpl
    DAOImpl --> CP
    DAOImpl --> Model
    CP --> DB
    
    style UI fill:#e1f5ff
    style FC fill:#fff9c4
    style SC fill:#fff4e1
    style Model fill:#e8f5e9
    style Utils fill:#f1f8e9
    style DAOInt fill:#f3e5f5
    style DAOImpl fill:#e1bee7
    style CP fill:#ffccbc
    style DB fill:#ffebee
```

## Notas sobre los Diagramas

### Principios Aplicados

1. **Separación de Responsabilidades (SoC):** Cada capa tiene responsabilidades bien definidas
2. **Inversión de Dependencias (DIP):** Los controladores dependen de interfaces DAO, no de implementaciones
3. **Patrón MVC:** Clara separación entre Modelo, Vista y Controlador
4. **Patrón DAO:** Capa de abstracción para acceso a datos
5. **Patrón Singleton:** ConexionBD garantiza una única instancia del pool

### Flujo de Datos

1. **Petición:** Usuario → Browser → Filters → Servlet
2. **Procesamiento:** Servlet → DAO Interface → DAO Implementation → Connection Pool → MySQL
3. **Respuesta:** MySQL → DAO → Servlet → JSP → Browser → Usuario

### Seguridad

- **AuthFilter:** Protege rutas administrativas
- **DatabaseCheckFilter:** Verifica disponibilidad de BD antes de procesar peticiones
- **PreparedStatements:** Previene SQL Injection
- **PasswordUtil:** Hasheo de contraseñas con SHA-256

---

**Proyecto:** Odally | Blog  
**Equipo:** Dylan David Silva Orrego, Maria Alejandra Munevar Barrera  
**Profesora:** Lilia Marcela Espinosa Rodríguez  
**Universidad Distrital Francisco José de Caldas - 2025**

