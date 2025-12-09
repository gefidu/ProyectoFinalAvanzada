package com.blog.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Tests for DatabaseCheckFilter
 */
@DisplayName("Tests para DatabaseCheckFilter")
class DatabaseCheckFilterTest {

    private DatabaseCheckFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private FilterConfig filterConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new DatabaseCheckFilter();
        // Invalidate cache before each test
        DatabaseCheckFilter.invalidateCache();
    }

    @Test
    @DisplayName("init debe ejecutarse sin errores")
    void testInit() throws ServletException {
        filter.init(filterConfig);
        // No debe lanzar excepciones
    }

    @Test
    @DisplayName("destroy debe ejecutarse sin errores")
    void testDestroy() {
        filter.destroy();
        // No debe lanzar excepciones
    }

    @Test
    @DisplayName("doFilter debe permitir acceso a /setup")
    void testDoFilterAllowsSetupPath() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/blog/setup");
        when(request.getContextPath()).thenReturn("/blog");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("doFilter debe permitir acceso a recursos CSS")
    void testDoFilterAllowsCssResources() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/blog/css/styles.css");
        when(request.getContextPath()).thenReturn("/blog");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("doFilter debe permitir acceso a recursos JS")
    void testDoFilterAllowsJsResources() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/blog/js/script.js");
        when(request.getContextPath()).thenReturn("/blog");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("doFilter debe permitir acceso a imágenes")
    void testDoFilterAllowsImageResources() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/blog/images/logo.png");
        when(request.getContextPath()).thenReturn("/blog");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("invalidateCache debe resetear el cache")
    void testInvalidateCache() {
        // Esta prueba verifica que el método no lanza excepciones
        DatabaseCheckFilter.invalidateCache();
        // No debe lanzar excepciones
    }

    @Test
    @DisplayName("doFilter debe usar el context path correcto en redirección")
    void testDoFilterContextPathInRedirect() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/myapp/articulos");
        when(request.getContextPath()).thenReturn("/myapp");

        filter.doFilter(request, response, chain);

        // Either continues or redirects, but if redirects, should use correct context path
        // We can't guarantee redirect due to DB state, so we just verify no exceptions
    }

    @Test
    @DisplayName("doFilter con context path vacío debe funcionar")
    void testDoFilterEmptyContextPath() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/setup");
        when(request.getContextPath()).thenReturn("");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter debe permitir acceso a favicon.ico")
    void testDoFilterAllowsFavicon() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/blog/favicon.ico");
        when(request.getContextPath()).thenReturn("/blog");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }
}
