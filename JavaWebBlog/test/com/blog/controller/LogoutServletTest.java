package com.blog.controller;

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
 * Pruebas unitarias para LogoutServlet
 */
@DisplayName("Tests para LogoutServlet")
class LogoutServletTest {
    
    private LogoutServlet servlet;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HttpSession session;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new LogoutServlet();
    }
    
    @Test
    @DisplayName("doGet debe invalidar sesión existente y redirigir")
    void testDoGetConSesion() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(request.getContextPath()).thenReturn("/blog");
        
        servlet.doGet(request, response);
        
        verify(session).invalidate();
        verify(response).sendRedirect("/blog/articulos");
    }
    
    @Test
    @DisplayName("doGet sin sesión debe redirigir sin error")
    void testDoGetSinSesion() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/blog");
        
        servlet.doGet(request, response);
        
        verify(response).sendRedirect("/blog/articulos");
        // No debe intentar invalidar una sesión nula
    }
    
    @Test
    @DisplayName("doGet debe usar context path correcto")
    void testDoGetContextPath() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("/myapp");
        
        servlet.doGet(request, response);
        
        verify(response).sendRedirect("/myapp/articulos");
    }
    
    @Test
    @DisplayName("doGet con context path vacío debe funcionar")
    void testDoGetContextPathVacio() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);
        when(request.getContextPath()).thenReturn("");
        
        servlet.doGet(request, response);
        
        verify(response).sendRedirect("/articulos");
    }
}
