<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Odally | Blog - Gestión de Artículos</title>
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
                                <a class="nav-link" href="<c:url value='/articulos'/>">Ver Blog</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link"
                                    href="<c:url value='/admin/articulos?action=dashboard'/>">Dashboard</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link active"
                                    href="<c:url value='/admin/articulos?action=listar'/>">Artículos</a>
                            </li>
                            <c:if test="${sessionScope.usuario.rol eq 'admin'}">
                                <li class="nav-item">
                                    <a class="nav-link" href="<c:url value='/admin/usuarios'/>">Usuarios</a>
                                </li>
                            </c:if>
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

            <!-- Contenido -->
            <div class="container mt-5">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1>Gestión de Artículos</h1>
                    <a href="<c:url value='/admin/articulos?action=nuevo'/>" class="btn btn-success">
                        Nuevo Artículo
                    </a>
                </div>

                <c:choose>
                    <c:when test="${not empty articulos}">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead class="table-dark">
                                    <tr>
                                        <th>ID</th>
                                        <th>Título</th>
                                        <th>Autor</th>
                                        <th>Fecha de Publicación</th>
                                        <th class="table-actions">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="articulo" items="${articulos}">
                                        <tr>
                                            <td>${articulo.id}</td>
                                            <td>${articulo.titulo}</td>
                                            <td>${articulo.autorNombre}</td>
                                            <td>
                                                ${articulo.fechaPublicacionFormateada}
                                            </td>
                                            <td class="table-actions">
                                                <a href="<c:url value='/articulos?action=ver&id=${articulo.id}'/>"
                                                    class="btn btn-sm btn-info" target="_blank">Ver</a>

                                                <%-- Check permissions: Admin or Owner --%>
                                                    <c:if
                                                        test="${sessionScope.usuario.rol eq 'admin' or sessionScope.usuario.id eq articulo.autorId}">
                                                        <a href="<c:url value='/admin/articulos?action=editar&id=${articulo.id}'/>"
                                                            class="btn btn-sm btn-warning">Editar</a>
                                                        <form method="post" action="<c:url value='/admin/articulos'/>"
                                                            style="display:inline;">
                                                            <input type="hidden" name="action" value="eliminar">
                                                            <input type="hidden" name="id" value="${articulo.id}">
                                                            <button type="submit" class="btn btn-sm btn-danger"
                                                                onclick="return confirm('¿Está seguro de eliminar este artículo?')">
                                                                Eliminar
                                                            </button>
                                                        </form>
                                                    </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info">
                            No hay artículos para mostrar.
                            <a href="<c:url value='/admin/articulos?action=nuevo'/>">Crear el primero</a>
                        </div>
                    </c:otherwise>
                </c:choose>
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