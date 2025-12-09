<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Usuarios - Odally</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Literata:opsz,wght@7..72,300;7..72,400;7..72,600;7..72,700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value='/css/styles.css'/>" rel="stylesheet">
</head>

<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="<c:url value='/articulos'/>">Odally</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/admin/articulos?action=dashboard'/>">Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/admin/articulos?action=listar'/>">Artículos</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="<c:url value='/admin/usuarios'/>">Usuarios</a>
                    </li>
                    <li class="nav-item">
                        <span class="nav-link">Hola, ${sessionScope.usuarioNombre}</span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<c:url value='/logout'/>">Cerrar Sesión</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Content -->
    <div class="container mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1>Gestión de Usuarios</h1>
            <a href="<c:url value='/admin/usuarios?action=eliminarTodos'/>" 
               class="btn btn-danger"
               onclick="return confirm('⚠️ ADVERTENCIA: Esto eliminará TODOS los usuarios excepto administradores.\\n\\n¿Está ABSOLUTAMENTE seguro?') && confirm('Esta acción NO se puede deshacer. ¿Continuar?')">
                Eliminar Todos los No-Admins
            </a>
        </div>

        <!-- Success/Error Messages -->
        <c:if test="${param.deleted != null}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                Se eliminaron ${param.deleted} usuario(s) exitosamente.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${param.error == 'no-self-delete'}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                No puedes eliminarte a ti mismo.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${param.error == 'protected-user'}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                El usuario administrador principal está protegido y no puede ser eliminado.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Nombre</th>
                        <th>Email</th>
                        <th>Rol</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${usuarios}">
                        <tr>
                            <td>${u.id}</td>
                            <td>${u.username}</td>
                            <td>${u.nombre}</td>
                            <td>${u.email}</td>
                            <td>
                                <span class="badge ${u.rol eq 'admin' ? 'bg-danger' : 'bg-primary'}">
                                    ${u.rol.toUpperCase()}
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.id eq 1}">
                                        <span class="text-muted fst-italic">Super Admin - Protegido</span>
                                    </c:when>
                                    <c:when test="${u.id eq sessionScope.usuario.id}">
                                        <span class="text-muted fst-italic">Usuario Actual</span>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="btn-group" role="group">
                                            <c:choose>
                                                <c:when test="${u.rol eq 'admin'}">
                                                    <a href="<c:url value='/admin/usuarios?action=demover&id=${u.id}'/>"
                                                       class="btn btn-sm btn-outline-secondary"
                                                       onclick="return confirm('¿Seguro que quieres quitar permisos de administrador?')">
                                                        Volver Autor
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="<c:url value='/admin/usuarios?action=promover&id=${u.id}'/>"
                                                       class="btn btn-sm btn-success"
                                                       onclick="return confirm('¿Seguro que quieres hacer administrador a este usuario?')">
                                                        Hacer Admin
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                            <a href="<c:url value='/admin/usuarios?action=eliminar&id=${u.id}'/>"
                                               class="btn btn-sm btn-danger"
                                               onclick="return confirm('¿Está seguro de eliminar a ${u.nombre}?\\nEsta acción no se puede deshacer.')">
                                                Eliminar
                                            </a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Footer -->
    <footer class="footer mt-5">
        <div class="container text-center">
            <p class="mb-0">&copy; 2025 Odally - Panel de Administración</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
