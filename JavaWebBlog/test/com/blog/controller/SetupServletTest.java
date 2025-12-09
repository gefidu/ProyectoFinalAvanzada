package com.blog.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
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
 * Tests for SetupServlet
 */
@DisplayName("Tests para SetupServlet")
class SetupServletTest {

    private SetupServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private ServletContext servletContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new SetupServlet();
    }

    @Test
    @DisplayName("doGet debe mostrar página de setup")
    void testDoGetShowsSetupPage() throws ServletException, IOException {
        when(request.getRequestDispatcher("/setup.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).getRequestDispatcher("/setup.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost con acción test y parámetros válidos")
    void testDoPostTestAction() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("test");
        when(request.getParameter("host")).thenReturn("localhost");
        when(request.getParameter("port")).thenReturn("3306");
        when(request.getParameter("database")).thenReturn("test_db");
        when(request.getParameter("user")).thenReturn("test_user");
        when(request.getParameter("password")).thenReturn("test_pass");
        when(request.getContextPath()).thenReturn("/blog");

        servlet.doPost(request, response);

        // Should redirect (either with success or error)
        verify(response).sendRedirect(anyString());
    }

    @Test
    @DisplayName("doPost con acción save")
    void testDoPostSaveAction() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("save");
        when(request.getParameter("host")).thenReturn("localhost");
        when(request.getParameter("port")).thenReturn("3306");
        when(request.getParameter("database")).thenReturn("test_db");
        when(request.getParameter("user")).thenReturn("test_user");
        when(request.getParameter("password")).thenReturn("test_pass");
        when(request.getContextPath()).thenReturn("/blog");

        servlet.doPost(request, response);

        // Should redirect (either with success or error)
        verify(response).sendRedirect(anyString());
    }
}
