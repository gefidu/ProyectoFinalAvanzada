<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${articulo.titulo} - Odally</title>
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
                                <a class="nav-link" href="<c:url value='/articulos'/>">Inicio</a>
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

            <!-- Contenido del Artículo -->
            <div class="container mt-5">
                <div class="row">
                    <div class="col-lg-8 mx-auto">
                        <article>
                            <h1 class="mb-3">${articulo.titulo}</h1>
                            <p class="article-meta mb-4">
                                Por ${articulo.autorNombre} |
                                ${articulo.fechaPublicacion}
                            </p>
                            <div class="article-content mt-4">
                                ${articulo.contenido}
                            </div>
                        </article>

                        <div class="mt-4">
                            <a href="<c:url value='/articulos'/>" class="btn btn-secondary">
                                &larr; Volver a la lista
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Footer -->
            <footer class="footer mt-5">
                <div class="container text-center">
                    <p class="mb-0">&copy; 2025 Odally - Desarrollado con JavaWeb</p>
                    <p class="mb-0">Alejandra Munevar, Dylan Silva, Sergio Moreno</p>
                </div>
            </footer>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>