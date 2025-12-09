<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Error - Mi Blog</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link href="<c:url value='/css/styles.css'/>" rel="stylesheet">
        </head>

        <body>
            <!-- Navbar -->
            <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                <div class="container">
                    <a class="navbar-brand" href="<c:url value='/articulos'/>">Mi Blog</a>
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