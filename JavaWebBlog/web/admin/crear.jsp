<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Crear Artículo - Administración</title>
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
                                <a class="nav-link" href="<c:url value='/articulos'/>">Ver Blog</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link"
                                    href="<c:url value='/admin/articulos?action=dashboard'/>">Dashboard</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="<c:url value='/admin/articulos?action=listar'/>">Artículos</a>
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

            <!-- Formulario de Creación -->
            <div class="container mt-5">
                <div class="row">
                    <div class="col-lg-8 mx-auto">
                        <h1 class="mb-4">Crear Nuevo Artículo</h1>

                        <c:if test="${not empty error}">
                            <div class="error-message">
                                ${error}
                            </div>
                        </c:if>

                        <div class="card">
                            <div class="card-body">
                                <form method="post" action="<c:url value='/admin/articulos'/>">
                                    <input type="hidden" name="action" value="crear">

                                    <div class="mb-3">
                                        <label for="titulo" class="form-label">Título *</label>
                                        <input type="text" class="form-control" id="titulo" name="titulo" required
                                            maxlength="200" autofocus>
                                    </div>

                                    <div class="mb-3">
                                        <label for="contenido" class="form-label">Contenido *</label>
                                        <textarea class="form-control" id="contenido" name="contenido" rows="10"
                                            required></textarea>
                                        <small class="form-text text-muted">
                                            El contenido se publicará tal como lo escribas.
                                        </small>
                                    </div>

                                    <div class="d-flex gap-2">
                                        <button type="submit" class="btn btn-success">Publicar Artículo</button>
                                        <a href="<c:url value='/admin/articulos?action=listar'/>"
                                            class="btn btn-secondary">Cancelar</a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Footer -->
            <footer class="footer mt-5">
                <div class="container text-center">
                    <p class="mb-0">&copy; 2025 Blog Management System - Panel de Administración</p>
                </div>
            </footer>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>