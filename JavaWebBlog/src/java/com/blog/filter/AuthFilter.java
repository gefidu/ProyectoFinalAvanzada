package com.blog.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filtro de autenticación para proteger rutas administrativas.
 * 
 * <p>Este filtro intercepta todas las peticiones a rutas que comienzan con {@code /admin/*}
 * y verifica que el usuario esté autenticado antes de permitir el acceso. Si el usuario
 * no ha iniciado sesión, lo redirige a la página de login.</p>
 * 
 * <h3>Funcionamiento:</h3>
 * <ol>
 *   <li>Intercepta la petición HTTP antes de que llegue al servlet</li>
 *   <li>Verifica si existe una sesión HTTP con un usuario autenticado</li>
 *   <li>Si está autenticado, permite continuar con la petición</li>
 *   <li>Si no está autenticado, redirige a {@code /login}</li>
 * </ol>
 * 
 * <h3>Principios SOLID aplicados:</h3>
 * <ul>
 *   <li><b>S - Single Responsibility Principle (SRP):</b> Este filtro tiene una única
 *   responsabilidad: verificar autenticación. No maneja autorización por roles ni
 *   otras funcionalidades. Ver Sección 2.1.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>O - Open/Closed Principle (OCP):</b> Si necesitamos agregar verificación
 *   de roles, podemos crear un nuevo filtro sin modificar este. Ver Sección 2.1.2 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Otros principios de diseño:</h3>
 * <ul>
 *   <li><b>SoC (Separation of Concerns):</b> Separa la lógica de autenticación de los
 *   controladores, evitando código repetido. Ver Sección 2.3.4 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>DRY (Don't Repeat Yourself):</b> Evita duplicar validación de sesión en
 *   cada servlet administrativo. Ver Sección 2.3.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Configuración en web.xml:</h3>
 * <pre>{@code
 * <filter>
 *     <filter-name>AuthFilter</filter-name>
 *     <filter-class>com.blog.filter.AuthFilter</filter-class>
 * </filter>
 * <filter-mapping>
 *     <filter-name>AuthFilter</filter-name>
 *     <url-pattern>/admin/*</url-pattern>
 * </filter-mapping>
 * }</pre>
 * 
 * @author Dylan David Silva Orrego
 * @author Maria Alejandra Munevar Barrera
 * @version 1.0
 * @since 2025-12-09
 * @see jakarta.servlet.Filter
 * @see com.blog.controller.LoginServlet
 */
public class AuthFilter implements Filter {

    /**
     * Inicializa el filtro de autenticación.
     * 
     * @param filterConfig Configuración del filtro proporcionada por el contenedor
     * @throws ServletException Si ocurre un error durante la inicialización
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización del filtro (si es necesaria)
    }

    /**
     * Ejecuta la lógica de filtrado de autenticación.
     * 
     * <p>Verifica si existe un usuario en la sesión HTTP. Si existe, permite continuar
     * con la cadena de filtros. Si no existe, redirige al login.</p>
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

        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("usuario") != null);

        if (isLoggedIn) {
            // Usuario autenticado, continuar con la petición
            chain.doFilter(request, response);
        } else {
            // Usuario no autenticado, redirigir al login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }

    /**
     * Destruye el filtro cuando el contenedor lo descarga.
     */
    @Override
    public void destroy() {
        // Limpieza del filtro (si es necesaria)
    }
}
