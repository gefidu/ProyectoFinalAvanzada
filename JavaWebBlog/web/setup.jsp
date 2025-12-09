<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Configuración de Base de Datos - Odally</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Literata:opsz,wght@7..72,300;7..72,400;7..72,600;7..72,700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value='/css/styles.css'/>" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .setup-container {
            max-width: 600px;
            width: 100%;
            margin: 2rem;
        }
        .setup-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
        }
        .setup-header {
            background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
            color: white;
            padding: 2rem;
            text-align: center;
        }
        .setup-header h1 {
            color: white;
            margin-bottom: 0.5rem;
        }
        .setup-body {
            padding: 2rem;
        }
        .form-label {
            font-weight: 600;
            color: #334155;
        }
        .help-text {
            font-size: 0.875rem;
            color: #64748b;
            margin-top: 0.25rem;
        }
    </style>
</head>

<body>
    <div class="setup-container">
        <div class="setup-card">
            <div class="setup-header">
                <h1>⚙️ Configuración de Base de Datos</h1>
                <p class="mb-0">Configure la conexión a MySQL para Odally</p>
            </div>
            
            <div class="setup-body">
                <!-- Success/Error Messages -->
                <c:if test="${param.error == 'connection'}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <strong>Error de Conexión:</strong> No se pudo conectar a la base de datos con los parámetros proporcionados.
                        Por favor, verifique los datos e intente nuevamente.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.error == 'save'}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <strong>Error al Guardar:</strong> No se pudo guardar la configuración.
                        Verifique los permisos del archivo db.properties.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.success == 'true'}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <strong>¡Éxito!</strong> La configuración se guardó correctamente.
                        Redirigiendo al inicio...
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <script>
                        setTimeout(function() {
                            window.location.href = '<c:url value="/articulos"/>';
                        }, 2000);
                    </script>
                </c:if>

                <form action="<c:url value='/setup'/>" method="post">
                    <div class="mb-3">
                        <label for="host" class="form-label">Host de MySQL</label>
                        <input type="text" class="form-control" id="host" name="host" 
                               value="${param.host != null ? param.host : 'localhost'}" required>
                        <div class="help-text">Dirección del servidor MySQL (por defecto: localhost)</div>
                    </div>

                    <div class="mb-3">
                        <label for="port" class="form-label">Puerto</label>
                        <input type="number" class="form-control" id="port" name="port" 
                               value="${param.port != null ? param.port : '3306'}" required min="1" max="65535">
                        <div class="help-text">Puerto de MySQL (por defecto: 3306)</div>
                    </div>

                    <div class="mb-3">
                        <label for="database" class="form-label">Nombre de la Base de Datos</label>
                        <input type="text" class="form-control" id="database" name="database" 
                               value="${param.database != null ? param.database : 'blog_db'}" required>
                        <div class="help-text">Nombre de la base de datos (por defecto: blog_db)</div>
                    </div>

                    <div class="mb-3">
                        <label for="user" class="form-label">Usuario de MySQL</label>
                        <input type="text" class="form-control" id="user" name="user" 
                               value="${param.user != null ? param.user : 'root'}" required>
                        <div class="help-text">Usuario con permisos en la base de datos</div>
                    </div>

                    <div class="mb-4">
                        <label for="password" class="form-label">Contraseña de MySQL</label>
                        <input type="password" class="form-control" id="password" name="password" 
                               value="${param.password != null ? param.password : ''}">
                        <div class="help-text">Contraseña del usuario (dejar en blanco si no tiene)</div>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" name="action" value="test" class="btn btn-outline-primary">
                            🔍 Probar Conexión
                        </button>
                        <button type="submit" name="action" value="save" class="btn btn-primary">
                            💾 Guardar Configuración
                        </button>
                    </div>
                </form>

                <div class="mt-4 text-center">
                    <small class="text-muted">
                        Asegúrese de que la base de datos exista antes de guardar la configuración.
                    </small>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
