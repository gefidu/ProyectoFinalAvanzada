<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Blog - Sistema de Gestión de Contenidos</title>
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
                                <a class="nav-link active" href="<c:url value='/articulos'/>">Inicio</a>
                            </li>
                            <c:choose>
                                <c:when test="${not empty sessionScope.usuario}">
                                    <li class="nav-item">
                                        <a class="nav-link"
                                            href="<c:url value='/admin/articulos?action=dashboard'/>">Dashboard</a>
                                    </li>
                                    <li class="nav-item">
                                        <span class="nav-link">Hola, ${sessionScope.usuarioNombre}</span>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="<c:url value='/logout'/>">Cerrar Sesión</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="nav-item">
                                        <a class="nav-link" href="<c:url value='/login'/>">Iniciar Sesión</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>
                </div>
            </nav>

            <!-- Contenido Principal -->
            <div class="container mt-5">
                <div class="row">
                    <div class="col-lg-8 mx-auto">
                        <h1 class="mb-4">Artículos Recientes</h1>

                        <c:choose>
                            <c:when test="${not empty articulos}">
                                <c:forEach var="articulo" items="${articulos}">
                                    <div class="card mb-4">
                                        <div class="card-body">
                                            <h2 class="card-title">
                                                <a href="<c:url value='/articulos?action=ver&id=${articulo.id}'/>"
                                                    class="text-decoration-none">
                                                    ${articulo.titulo}
                                                </a>
                                            </h2>
                                            <p class="article-meta">
                                                Por ${articulo.autorNombre} |
                                                ${articulo.fechaPublicacionFormateada}
                                            </p>
                                            <p class="card-text article-excerpt">${articulo.contenido}</p>
                                            <a href="<c:url value='/articulos?action=ver&id=${articulo.id}'/>"
                                                class="btn btn-primary">Leer más</a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info">
                                    No hay artículos publicados aún.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- Footer -->
            <footer class="footer mt-5">
                <div class="container text-center">
                    <p class="mb-0 text-muted">&copy; 2025 Sistema de Blog JavaWeb</p>
                    <p class="mb-0 text-muted">Alejandra Munevar, Dylan Silva, Sergio Moreno</p>
                </div>
            </footer>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>