package com.blog.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

/**
 * Filtro para asegurar codificación UTF-8 en todas las peticiones y respuestas.
 * 
 * <p>Este filtro establece la codificación de caracteres UTF-8 para todas las peticiones
 * y respuestas HTTP, garantizando el correcto manejo de caracteres especiales del español
 * (tildes, ñ, etc.) y otros caracteres Unicode.</p>
 * 
 * <h3>Problema que resuelve:</h3>
 * <p>Sin este filtro, los formularios que envían caracteres especiales pueden interpretarse
 * incorrectamente, causando problemas de codificación (caracteres "�" o mal formados).</p>
 * 
 * <h3>Principios SOLID aplicados:</h3>
 * <ul>
 *   <li><b>S - Single Responsibility Principle (SRP):</b> Este filtro tiene una única
 *   responsabilidad: establecer la codificación de caracteres. Ver Sección 2.1.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Otros principios de diseño:</h3>
 * <ul>
 *   <li><b>DRY (Don't Repeat Yourself):</b> Centraliza la configuración de codificación
 *   en un solo lugar en vez de establecerla en cada servlet. Ver Sección 2.3.1 en PRINCIPIOS_Y_PATRONES.tex</li>
 *   <li><b>SoC (Separation of Concerns):</b> Separa la preocupación de codificación
 *   de la lógica de negocio. Ver Sección 2.3.4 en PRINCIPIOS_Y_PATRONES.tex</li>
 * </ul>
 * 
 * <h3>Configuración en web.xml:</h3>
 * <pre>{@code
 * <filter>
 *     <filter-name>CharacterEncodingFilter</filter-name>
 *     <filter-class>com.blog.filter.CharacterEncodingFilter</filter-class>
 *     <init-param>
 *         <param-name>encoding</param-name>
 *         <param-value>UTF-8</param-value>
 *     </init-param>
 * </filter>
 * <filter-mapping>
 *     <filter-name>CharacterEncodingFilter</filter-name>
 *     <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * }</pre>
 * 
 * @author Dylan David Silva Orrego
 * @author Maria Alejandra Munevar Barrera
 * @version 1.0
 * @since 2025-12-09
 * @see jakarta.servlet.Filter
 */
public class CharacterEncodingFilter implements Filter {

    /** Codificación de caracteres a aplicar (por defecto UTF-8) */
    private String encoding = "UTF-8";

    /**
     * Inicializa el filtro y lee la codificación configurada.
     * 
     * <p>Si se proporciona un parámetro de inicialización "encoding" en web.xml,
     * se usa ese valor. De lo contrario, se usa UTF-8 por defecto.</p>
     * 
     * @param filterConfig Configuración del filtro proporcionada por el contenedor
     * @throws ServletException Si ocurre un error durante la inicialización
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null) {
            encoding = encodingParam;
        }
    }

    /**
     * Establece la codificación UTF-8 para la petición y respuesta.
     * 
     * <p>Este método se ejecuta antes de que la petición llegue al servlet,
     * garantizando que todos los datos se procesen con la codificación correcta.</p>
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

        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);

        // Next filter in the chain
        chain.doFilter(request, response);
    }

    /**
     * Destruye el filtro cuando el contenedor lo descarga.
     */
    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
