# Diagramas del Proyecto

## Diagrama de Clases (Paquetes Principales)

```mermaid
classDiagram
    %% Relaciones entre paquetes
    Controller ..> ServiceInterface : usa
    Controller ..> Model : usa
    ServiceInterface ..> DAOInterface : usa
    DAOInterface ..> Model : usa
    MySQLDAO --|> DAOInterface : implementa
    MySQLDAO ..> ConexionBD : usa

    namespace com.blog.model {
        class Articulo {
            -int id
            -String titulo
            -String contenido
            -LocalDateTime fechaPublicacion
            -int autorId
            +getters()
            +setters()
        }
        class Usuario {
            -int id
            -String nombre
            -String email
            -String password
            +getters()
            +setters()
        }
    }

    namespace com.blog.dao {
        class IArticuloDAO {
            <<interface>>
            +listarTodos() List~Articulo~
            +obtenerPorId(int id) Articulo
            +crear(Articulo a) boolean
            +actualizar(Articulo a) boolean
            +eliminar(int id) boolean
        }
        class MySQLArticuloDAO {
            -ConexionBD conexion
            +listarTodos()
            +obtenerPorId()
            +crear()
            +actualizar()
            +eliminar()
        }
        class ConexionBD {
            -static ConexionBD instancia
            -Connection connection
            +getInstancia() ConexionBD
            +getConexion() Connection
            +cerrar()
        }
    }

    namespace com.blog.controller {
        class ArticuloServlet {
            -IArticuloDAO articuloDAO
            +doGet()
            +doPost()
        }
        class AdminArticuloServlet {
            -IArticuloDAO articuloDAO
            +doGet()
            +doPost()
        }
    }
```

## Diagrama de Casos de Uso

```mermaid
usecaseDiagram
    actor "Visitante (No Registrado)" as Visitante
    actor "Administrador" as Admin

    package "Blog JavaWeb" {
        usecase "Ver Lista de Artículos" as UC1
        usecase "Leer Artículo Completo" as UC2
        usecase "Iniciar Sesión" as UC3
        usecase "Gestionar Artículos (CRUD)" as UC4
        usecase "Ver Dashboard" as UC5
        usecase "Cerrar Sesión" as UC6
    }

    Visitante --> UC1
    Visitante --> UC2
    Visitante --> UC3

    Admin --> UC1
    Admin --> UC2
    Admin --> UC4
    Admin --> UC5
    Admin --> UC6
    
    %% Herencia de actores
    Admin --|> Visitante
```
