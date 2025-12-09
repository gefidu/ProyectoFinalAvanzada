<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Iniciar Sesión - Mi Blog</title>
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
                                <a class="nav-link" href="<c:url value='/articulos'/>">Inicio</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>

            <!-- Formulario de Login -->
            <div class="container">
                <div class="login-container">
                    <div class="card">
                        <div class="card-body">
                            <h2 class="card-title text-center mb-4">Iniciar Sesión</h2>

                            <c:if test="${not empty error}">
                                <div class="error-message">
                                    ${error}
                                </div>
                            </c:if>

                            <form method="post" action="<c:url value='/login'/>">
                                <div class="mb-3">
                                    <label for="username" class="form-label">Usuario</label>
                                    <input type="text" class="form-control" id="username" name="username" required
                                        autofocus>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Contraseña</label>
                                    <input type="password" class="form-control" id="password" name="password" required>
                                </div>
                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary">Iniciar Sesión</button>
                                </div>
                            </form>



                            <div class="mt-3 text-center">
                                <p>¿No tienes cuenta? <a href="<c:url value='/register.jsp'/>">Regístrate aquí</a></p>
                            </div>

                        </div>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>