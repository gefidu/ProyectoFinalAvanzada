package com.blog.filter;

import com.blog.dao.ConexionBD;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro de verificación de disponibilidad de base de datos.
 * 
 * <p>Este filtro intercepta todas las peticiones HTTP (excepto las configuradas como excepciones)
 * y verifica que la base de datos esté disponible antes de permitir el acceso. Si la base de
 * datos no está disponible, redirige automáticamente a la página de configuración {@code /setup}.</p>
 * 
 * <h3>Rutas excluidas de la verificación:</h3>
 * <ul>
 *   <li>{@code /setup} - Página de configuración de base de datos</li>
 *   <li>Recursos estáticos: CSS, JavaScript, imágenes</li>
 * </ul>
 * 
 * <h3>Características:</h3>
 * <ul>
 *   <li><b>Caché inteligente:</b> Evita verificar la BD en cada petición usando un caché
 *   de 30 segundos</li>
 *   <li><b>Reintentos automáticos:</b> Utiliza el mecanismo de reintentos de
 *   {@link ConexionBD}</li>
 *   <li><b>Redirección automática:</b> Guía al usuario a la configuración cuando es necesario</li>
 * </ul>
 * 
 * <h3>Principios SOLID aplicados:</h3>
 * <ul>
 *   <li><b>S - Single Responsibility Principle (SRP):</b> Este filtro solo verifica
 *   disponibilidad de BD y redirige. No maneja configuración ni autenticación.
 *   Ver Sección 2.1.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>D - Dependency Inversion Principle (DIP):</b> Depende de la abstracción
 *   {@link ConexionBD} y su método estático de verificación. Ver Sección 2.1.5 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Otros principios de diseño:</h3>
 * <ul>
 *   <li><b>SoC (Separation of Concerns):</b> Separa la verificación de conectividad
 *   de la lógica de negocio. Ver Sección 2.3.4 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>Fail-Fast:</b> Detecta problemas de conexión temprano y redirige al usuario
 *   antes de fallos inesperados</li>
 * </ul>
 * 
 * <h3>Ejemplo de uso:</h3>
 * <p>Si el usuario intenta acceder a {@code /articulos} pero la BD no está disponible,
 * será redirigido automáticamente a {@code /setup} para configurar la conexión.</p>
 * 
 * @author Dylan David Silva Orrego
 * @author Maria Alejandra Munevar Barrera
 * @version 1.0
 * @since 2025-12-09
 * @see com.blog.dao.ConexionBD
 * @see com.blog.controller.SetupServlet
 */
public class DatabaseCheckFilter implements Filter {
    
    /** Ruta de la página de setup */
    private static final String SETUP_PATH = "/setup";
    
    /** Estado cacheado de disponibilidad de la base de datos */
    private static Boolean databaseAvailable = null;
    
    /** Timestamp de la última verificación */
    private static long lastCheck = 0;
    
    /** Intervalo entre verificaciones (30 segundos) */
    private static final long CHECK_INTERVAL_MS = 30000;
    
    /**
     * Inicializa el filtro de verificación de base de datos.
     * 
     * @param filterConfig Configuración del filtro proporcionada por el contenedor
     * @throws ServletException Si ocurre un error durante la inicialización
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[DatabaseCheckFilter] Filter inicializado");
    }
    
    /**
     * Ejecuta la lógica de filtrado de verificación de base de datos.
     * 
     * <p>Verifica si la ruta solicitada debe ser verificada. Si es así, comprueba
     * la disponibilidad de la BD. Si no está disponible, redirige a {@code /setup}.</p>
     * 
     * @param request Petición HTTP del cliente
     * @param response Respuesta HTTP al cliente
     * @param chain Cadena de filtros para continuar el procesamiento
     * @throws IOException Si ocurre un error de I/O
     * @throws ServletException Si ocurre un error de servlet
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        // Excluir la página de setup y recursos estáticos del check
        if (shouldSkipCheck(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Verificar disponibilidad de la base de datos con cache
        if (isDatabaseAvailable()) {
            chain.doFilter(request, response);
        } else {
            // Redirigir a la página de configuración
            System.out.println("[DatabaseCheckFilter] Base de datos no disponible, redirigiendo a setup");
            httpResponse.sendRedirect(contextPath + SETUP_PATH);
        }
    }
    
    /**
     * Determina si se debe saltar la verificación de base de datos para la ruta especificada.
     * 
     * <p>Las rutas que se saltan son:</p>
     * <ul>
     *   <li>La página de setup ({@code /setup})</li>
     *   <li>Recursos estáticos: CSS, JS, imágenes</li>
     * </ul>
     * 
     * @param path Ruta relativa de la petición
     * @return true si se debe saltar la verificación, false en caso contrario
     */
    private boolean shouldSkipCheck(String path) {
        // Saltar verificación para la página de setup
        if (path.startsWith(SETUP_PATH)) {
            return true;
        }
        
        // Saltar verificación para recursos estáticos
        if (path.startsWith("/css/") || 
            path.startsWith("/js/") || 
            path.startsWith("/images/") || 
            path.startsWith("/img/") ||
            path.endsWith(".css") ||
            path.endsWith(".js") ||
            path.endsWith(".png") ||
            path.endsWith(".jpg") ||
            path.endsWith(".jpeg") ||
            path.endsWith(".gif") ||
            path.endsWith(".ico")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Verifica si la base de datos está disponible usando un mecanismo de caché.
     * 
     * <p>Para evitar verificaciones constantes que degraden el rendimiento, se cachea
     * el resultado durante 30 segundos. Solo se realiza una nueva verificación si
     * el caché ha expirado.</p>
     * 
     * @return true si la base de datos está disponible, false en caso contrario
     */
    private boolean isDatabaseAvailable() {
        long now = System.currentTimeMillis();
        
        // Si tenemos un resultado cacheado y no ha pasado el intervalo, usar el cache
        if (databaseAvailable != null && (now - lastCheck) < CHECK_INTERVAL_MS) {
            return databaseAvailable;
        }
        
        // Realizar nueva verificación
        try {
            boolean available = ConexionBD.verificarConexion();
            databaseAvailable = available;
            lastCheck = now;
            
            if (!available) {
                System.err.println("[DatabaseCheckFilter] Verificación de BD falló");
            }
            
            return available;
        } catch (Exception e) {
            System.err.println("[DatabaseCheckFilter] Error al verificar BD: " + e.getMessage());
            databaseAvailable = false;
            lastCheck = now;
            return false;
        }
    }
    
    /**
     * Invalida el caché de disponibilidad de base de datos.
     * 
     * <p>Este método es útil para forzar una nueva verificación después de cambios
     * de configuración. Puede ser llamado desde {@link com.blog.controller.SetupServlet}
     * después de guardar una nueva configuración.</p>
     */
    public static void invalidateCache() {
        databaseAvailable = null;
        lastCheck = 0;
    }
    
    /**
     * Destruye el filtro cuando el contenedor lo descarga.
     */
    @Override
    public void destroy() {
        System.out.println("[DatabaseCheckFilter] Filter destruido");
    }
}
