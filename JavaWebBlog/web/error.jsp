<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Error - Odally</title>
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
                </div>
            </nav>

            <!-- Mensaje de Error -->
            <div class="container mt-5">
                <div class="row">
                    <div class="col-lg-6 mx-auto">
                        <div class="card">
                            <div class="card-body text-center">
                                <h1 class="display-1 text-danger">¡Oops!</h1>
                                <h2 class="mb-4">Ha ocurrido un error</h2>

                                <c:choose>
                                    <c:when test="${not empty error}">
                                        <p class="text-muted">${error}</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-muted">
                                            Lo sentimos, ha ocurrido un error inesperado.
                                        </p>
                                    </c:otherwise>
                                </c:choose>

                                <a href="<c:url value='/articulos'/>" class="btn btn-primary mt-3">
                                    Volver al Inicio
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>