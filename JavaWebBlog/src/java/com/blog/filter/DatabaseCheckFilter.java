package com.blog.filter;

import com.blog.dao.ConexionBD;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * DatabaseCheckFilter - Verifica la disponibilidad de la base de datos
 * y redirige a la página de configuración si no está disponible.
 * 
 * Este filtro intercepta todas las peticiones excepto:
 * - La página de setup (/setup)
 * - Recursos estáticos (CSS, JS, imágenes)
 * 
 * Si detecta que la base de datos no está disponible después de los
 * reintentos configurados, redirige al usuario a /setup para configurar
 * la conexión.
 */
public class DatabaseCheckFilter implements Filter {
    
    private static final String SETUP_PATH = "/setup";
    private static Boolean databaseAvailable = null;
    private static long lastCheck = 0;
    private static final long CHECK_INTERVAL_MS = 30000; // 30 segundos
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[DatabaseCheckFilter] Filter inicializado");
    }
    
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
     * Determina si se debe saltar la verificación de base de datos
     * para la ruta especificada
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
     * Verifica si la base de datos está disponible
     * Usa cache para evitar verificaciones constantes
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
     * Invalida el cache de disponibilidad de BD
     * Útil para forzar una nueva verificación después de cambios de configuración
     */
    public static void invalidateCache() {
        databaseAvailable = null;
        lastCheck = 0;
    }
    
    @Override
    public void destroy() {
        System.out.println("[DatabaseCheckFilter] Filter destruido");
    }
}
