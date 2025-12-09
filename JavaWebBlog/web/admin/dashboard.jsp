<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Dashboard - Odally</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Literata:opsz,wght@7..72,300;7..72,400;7..72,600;7..72,700&display=swap" rel="stylesheet">
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
                                <a class="nav-link active"
                                    href="<c:url value='/admin/articulos?action=dashboard'/>">Dashboard</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="<c:url value='/admin/articulos?action=listar'/>">Artículos</a>
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

            <!-- Contenido Principal -->
            <div class="container mt-4">
                <h1 class="mb-4">Panel de Control</h1>

                <div class="row">
                    <!-- Tarjeta de Bienvenida y Estadísticas -->
                    <div class="col-md-4">
                        <div class="card mb-4 text-white bg-primary shadow-sm">
                            <div class="card-body">
                                <h5 class="card-title">Artículos Publicados</h5>
                                <h2 class="display-4">${totalArticulos}</h2>
                                <a href="<c:url value='/admin/articulos?action=listar'/>" class="btn btn-light mt-3">
                                    Ver Artículos
                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="card mb-4 shadow-sm">
                            <div class="card-body">
                                <h5 class="card-title">Crear Artículo</h5>
                                <p class="card-text">Publica un nuevo artículo en tu blog.</p>
                                <a href="<c:url value='/admin/articulos?action=nuevo'/>" class="btn btn-success">
                                    Nuevo Artículo
                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="card mb-4 shadow-sm">
                            <div class="card-body">
                                <h5 class="card-title">Usuario Actual</h5>
                                <p class="card-text">
                                    <strong>${sessionScope.usuarioNombre}</strong><br>
                                    ${sessionScope.usuario.username}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row mt-4">
                    <div class="col-12">
                        <div class="card shadow-sm">
                            <div class="card-body">
                                <h5 class="card-title">Acciones Rápidas</h5>
                                <div class="d-flex gap-2">
                                    <a href="<c:url value='/admin/articulos?action=listar'/>"
                                        class="btn btn-outline-primary">
                                        Gestionar Artículos
                                    </a>
                                    <a href="<c:url value='/admin/articulos?action=nuevo'/>"
                                        class="btn btn-outline-success">
                                        Crear Nuevo Artículo
                                    </a>
                                    <a href="<c:url value='/articulos'/>" class="btn btn-outline-secondary">
                                        Ver Blog Público
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <footer class="footer mt-5">
                <div class="container text-center">
                    <p class="mb-0">&copy; 2025 Odally - Panel de Administración</p>
                </div>
            </footer>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>