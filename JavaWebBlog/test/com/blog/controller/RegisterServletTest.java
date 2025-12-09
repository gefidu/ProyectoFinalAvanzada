package com.blog.controller;

import com.blog.dao.IUsuarioDAO;
import com.blog.model.Usuario;

import jakarta.servlet.RequestDispatcher;
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
import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

@DisplayName("Tests para RegisterServlet")
class RegisterServletTest {

    private RegisterServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private IUsuarioDAO usuarioDAO;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        servlet = new RegisterServlet();

        // Inject DAO mock
        Field daoField = RegisterServlet.class.getDeclaredField("usuarioDAO");
        daoField.setAccessible(true);
        daoField.set(servlet, usuarioDAO);
    }

    @Test
    @DisplayName("doGet debe mostrar formulario de registro")
    void testDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher("register.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost registro exitoso debe crear usuario y redirigir")
    void testDoPostRegistroExitoso() throws ServletException, IOException, SQLException {
        when(request.getParameter("nombre")).thenReturn("Test User");
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getContextPath()).thenReturn("/blog");
        when(request.getSession()).thenReturn(session);

        when(usuarioDAO.buscarPorUsername("newuser")).thenReturn(null);
        when(usuarioDAO.crear(any(Usuario.class))).thenReturn(true);

        servlet.doPost(request, response);

        verify(usuarioDAO).crear(argThat(user -> user.getUsername().equals("newuser") &&
                user.getEmail().equals("test@example.com") &&
                !user.getPassword().equals("password123") // Should be hashed
        ));
        verify(session).setAttribute(eq("mensaje"), anyString());
        verify(response).sendRedirect("/blog/login");
    }

    @Test
    @DisplayName("doPost usuario existente debe mostrar error")
    void testDoPostUsuarioExistente() throws ServletException, IOException, SQLException {
        when(request.getParameter("username")).thenReturn("existinguser");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getRequestDispatcher("register.jsp")).thenReturn(dispatcher);

        when(usuarioDAO.buscarPorUsername("existinguser")).thenReturn(new Usuario());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), contains("ya está en uso"));
        verify(dispatcher).forward(request, response);
        verify(usuarioDAO, never()).crear(any());
    }

    @Test
    @DisplayName("doPost error en base de datos debe ser manejado")
    void testDoPostDBError() throws ServletException, IOException, SQLException {
        when(request.getParameter("username")).thenReturn("user");
        when(request.getParameter("password")).thenReturn("pass");
        when(request.getRequestDispatcher("register.jsp")).thenReturn(dispatcher);

        when(usuarioDAO.buscarPorUsername(anyString())).thenThrow(new SQLException("DB Down"));

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), contains("Error de base de datos"));
        verify(dispatcher).forward(request, response);
    }
}
