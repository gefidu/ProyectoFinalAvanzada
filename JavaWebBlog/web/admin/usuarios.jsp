<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Gestión de Usuarios - Administración</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link href="<c:url value='/css/styles.css'/>" rel="stylesheet">
        </head>

        <body>
            <!-- Navbar -->
            <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <div class="container">
                    <a class="navbar-brand" href="<c:url value='/articulos'/>">Mi Blog</a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarNav">
                        <ul class="navbar-nav ms-auto">
                            <li class="nav-item">
                                <a class="nav-link"
                                    href="<c:url value='/admin/articulos?action=dashboard'/>">Dashboard</a>
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
                <h1 class="mb-4">Gestión de Usuarios</h1>

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
                                        <%-- Prevent modifying self or the original 'admin' (ID 1) --%>
                                            <c:if test="${u.id ne sessionScope.usuario.id and u.id ne 1}">
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
                                            </c:if>
                                            <c:if test="${u.id eq 1}">
                                                <span class="text-muted fst-italic">Super Admin</span>
                                            </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
                        <%@ taglib prefix="c" uri="jakarta.tags.core" %>
                            <!DOCTYPE html>
                            <html lang="es">

                            <head>
                                <meta charset="UTF-8">
                                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                <title>Gestión de Usuarios - Administración</title>
                                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
                                    rel="stylesheet">
                                <link href="<c:url value='/css/styles.css'/>" rel="stylesheet">
                            </head>

                            <body>
                                <!-- Navbar -->
                                <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                                    <div class="container">
                                        <a class="navbar-brand" href="<c:url value='/articulos'/>">Mi Blog</a>
                                        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#navbarNav">
                                            <span class="navbar-toggler-icon"></span>
                                        </button>
                                        <div class="collapse navbar-collapse" id="navbarNav">
                                            <ul class="navbar-nav ms-auto">
                                                <li class="nav-item">
                                                    <a class="nav-link"
                                                        href="<c:url value='/admin/articulos?action=dashboard'/>">Dashboard</a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link"
                                                        href="<c:url value='/admin/articulos?action=listar'/>">Artículos</a>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link active"
                                                        href="<c:url value='/admin/usuarios'/>">Usuarios</a>
                                                </li>
                                                <li class="nav-item">
                                                    <span class="nav-link">Hola, ${sessionScope.usuarioNombre}</span>
                                                </li>
                                                <li class="nav-item">
                                                    <a class="nav-link" href="<c:url value='/logout'/>">Cerrar
                                                        Sesión</a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </nav>

                                <!-- Content -->
                                <div class="container mt-5">
                                    <h1 class="mb-4">Gestión de Usuarios</h1>

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
                                                            <span
                                                                class="badge ${u.rol eq 'admin' ? 'bg-danger' : 'bg-primary'}">
                                                                ${u.rol.toUpperCase()}
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <%-- Prevent modifying self or the original 'admin' (ID 1)
                                                                --%>
                                                                <c:if
                                                                    test="${u.id ne sessionScope.usuario.id and u.id ne 1}">
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
                                                                </c:if>
                                                                <c:if test="${u.id eq 1}">
                                                                    <span class="text-muted fst-italic">Super
                                                                        Admin</span>
                                                                </c:if>
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
                                        <p class="mb-0">&copy; 2025 Blog Management System - Panel de Administración</p>
                                    </div>
                                </footer>

                                <script
                                    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                            </body>

                            </html>