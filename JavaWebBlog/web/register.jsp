<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Odally | Blog - Registro</title>
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
                            <li class="nav-item">
                                <a class="nav-link" href="<c:url value='/login'/>">Iniciar Sesión</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>

            <!-- Formulario de Registro -->
            <div class="container mt-5">
                <div class="row justify-content-center">
                    <div class="col-md-6">
                        <div class="card shadow">
                            <div class="card-body p-4">
                                <h2 class="card-title text-center mb-4">Crear Cuenta</h2>

                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger">
                                        ${error}
                                    </div>
                                </c:if>

                                <form method="post" action="<c:url value='/register'/>">
                                    <div class="mb-3">
                                        <label for="nombre" class="form-label">Nombre Completo *</label>
                                        <input type="text" class="form-control" id="nombre" name="nombre" required
                                            autofocu maxlength="100">
                                    </div>

                                    <div class="mb-3">
                                        <label for="username" class="form-label">Nombre de Usuario *</label>
                                        <input type="text" class="form-control" id="username" name="username" required
                                            maxlength="50">
                                        <div class="form-text">Será tu identificador para iniciar sesión.</div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="email" class="form-label">Correo Electrónico *</label>
                                        <input type="email" class="form-control" id="email" name="email" required
                                            maxlength="100">
                                    </div>

                                    <div class="mb-3">
                                        <label for="password" class="form-label">Contraseña *</label>
                                        <input type="password" class="form-control" id="password" name="password"
                                            required minlength="4">
                                    </div>

                                    <div class="d-grid gap-2">
                                        <button type="submit" class="btn btn-primary btn-lg">Registrarse</button>
                                        <a href="<c:url value='/login'/>" class="btn btn-outline-secondary">¿Ya tienes
                                            cuenta? Inicia sesión</a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>