<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Editar Artículo - Administración</title>
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

            <!-- Formulario de Edición -->
            <div class="container mt-5">
                <div class="row">
                    <div class="col-lg-8 mx-auto">
                        <h1 class="mb-4">Editar Artículo</h1>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                ${error}
                            </div>
                        </c:if>

                        <div class="card shadow-sm">
                            <div class="card-body">
                                <form method="post" action="<c:url value='/admin/articulos'/>">
                                    <input type="hidden" name="action" value="actualizar">
                                    <input type="hidden" name="id" value="${articulo.id}">

                                    <div class="mb-3">
                                        <label for="titulo" class="form-label">Título *</label>
                                        <input type="text" class="form-control" id="titulo" name="titulo"
                                            value="${articulo.titulo}" required maxlength="200" autofocus>
                                    </div>

                                    <div class="mb-3">
                                        <label for="contenido" class="form-label">Contenido *</label>
                                        <textarea class="form-control" id="contenido" name="contenido" rows="10"
                                            required>${articulo.contenido}</textarea>
                                        <small class="form-text text-muted">
                                            El contenido se publicará tal como lo escribas.
                                        </small>
                                    </div>

                                    <div class="d-flex justify-content-between align-items-center mt-4">
                                        <div>
                                            <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                                            <a href="<c:url value='/admin/articulos?action=listar'/>"
                                                class="btn btn-outline-secondary ms-2">Cancelar</a>
                                        </div>
                                    </div>
                                </form>

                                <!-- Separate form for separation of concerns and simpler styling -->
                                <div class="border-top mt-4 pt-3">
                                    <p class="text-danger mb-2">Zona de Peligro</p>
                                    <form method="post" action="<c:url value='/admin/articulos'/>">
                                        <input type="hidden" name="action" value="eliminar">
                                        <input type="hidden" name="id" value="${articulo.id}">
                                        <button type="submit" class="btn btn-danger btn-sm"
                                            onclick="return confirm('¿Está seguro de eliminar este artículo? Esta acción no se puede deshacer.')">
                                            Eliminar Artículo
                                        </button>
                                    </form>
                                </div>

                            </div>
                        </div>
                    </div>
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