package com.blog.filter;

import com.blog.model.Usuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AuthFilter
 */
@DisplayName("Tests para AuthFilter")
class AuthFilterTest {

    private AuthFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private HttpSession session;

    @Mock
    private FilterConfig filterConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new AuthFilter();
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
    @DisplayName("doFilter con usuario autenticado debe continuar la cadena")
    void testDoFilterUsuarioAutenticado() throws IOException, ServletException {
        Usuario usuario = new Usuario(1, "admin", "pass", "Admin", "admin@email.com", "admin");

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuario")).thenReturn(usuario);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("doFilter sin sesión debe redirigir a login")
    void testDoFilterSinSesion() throws IOException, ServletException {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/blog");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/blog/login");
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter con sesión sin usuario debe redirigir a login")
    void testDoFilterSesionSinUsuario() throws IOException, ServletException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuario")).thenReturn(null);
        when(request.getContextPath()).thenReturn("/blog");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/blog/login");
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter debe usar el context path correcto")
    void testDoFilterContextPath() throws IOException, ServletException {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/myapp");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/myapp/login");
    }

    @Test
    @DisplayName("doFilter con context path vacío debe funcionar")
    void testDoFilterContextPathVacio() throws IOException, ServletException {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/login");
    }
}
