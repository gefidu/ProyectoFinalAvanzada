package com.blog.controller;

import com.blog.dao.IUsuarioDAO;
import com.blog.model.Usuario;
import com.blog.util.PasswordUtil;
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

/**
 * Pruebas unitarias para LoginServlet
 */
@DisplayName("Tests para LoginServlet")
class LoginServletTest {

    private LoginServlet servlet;

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
        servlet = new LoginServlet();

        // Inyectar el mock del DAO usando reflexión
        Field daoField = LoginServlet.class.getDeclaredField("usuarioDAO");
        daoField.setAccessible(true);
        daoField.set(servlet, usuarioDAO);
    }

    @Test
    @DisplayName("doGet debe forward a login.jsp")
    void testDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).getRequestDispatcher("/login.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost con credenciales válidas debe crear sesión y redirigir")
    void testDoPostCredencialesValidas() throws ServletException, IOException, SQLException {
        String password = "password123";
        String hashedPassword = PasswordUtil.hashPassword(password);

        Usuario usuario = new Usuario(1, "admin", hashedPassword, "Administrador", "admin@email.com", "admin");

        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn(password);
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn("/blog");
        when(usuarioDAO.buscarPorUsername("admin")).thenReturn(usuario);

        servlet.doPost(request, response);

        verify(session).setAttribute("usuario", usuario);
        verify(session).setAttribute("usuarioId", 1);
        verify(session).setAttribute("usuarioNombre", "Administrador");
        verify(response).sendRedirect("/blog/admin/articulos?action=dashboard");
    }

    @Test
    @DisplayName("doPost con credenciales inválidas debe mostrar error")
    void testDoPostCredencialesInvalidas() throws ServletException, IOException, SQLException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
        when(usuarioDAO.buscarPorUsername("admin")).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Usuario o contraseña incorrectos");
        verify(dispatcher).forward(request, response);
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    @DisplayName("doPost con username vacío debe mostrar error")
    void testDoPostUsernameVacio() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Usuario y contraseña son requeridos");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost con password vacío debe mostrar error")
    void testDoPostPasswordVacio() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Usuario y contraseña son requeridos");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost con username null debe mostrar error")
    void testDoPostUsernameNull() throws ServletException, IOException {
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Usuario y contraseña son requeridos");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost con SQLException debe mostrar error de conexión")
    void testDoPostSQLException() throws ServletException, IOException, SQLException {
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
        when(usuarioDAO.buscarPorUsername("admin")).thenThrow(new SQLException("Database error"));

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Error de conexión. Por favor, intente nuevamente.");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost con usuario existente pero password incorrecta debe fallar")
    void testDoPostPasswordIncorrecta() throws ServletException, IOException, SQLException {
        String correctPassword = "correctPassword";
        String hashedPassword = PasswordUtil.hashPassword(correctPassword);
        Usuario usuario = new Usuario(1, "admin", hashedPassword, "Admin", "admin@email.com", "admin");

        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrongPassword");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
        when(usuarioDAO.buscarPorUsername("admin")).thenReturn(usuario);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Usuario o contraseña incorrectos");
        verify(dispatcher).forward(request, response);
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    @DisplayName("doPost con intento de SQL Injection debe fallar")
    void testDoPostSQLInjectionAttempt() throws ServletException, IOException, SQLException {
        when(request.getParameter("username")).thenReturn("' OR '1'='1");
        when(request.getParameter("password")).thenReturn("whatever");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
        when(usuarioDAO.buscarPorUsername("' OR '1'='1")).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Usuario o contraseña incorrectos");
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost con espacios en blanco debe fallar si usuario no existe")
    void testDoPostEspaciosEnBlanco() throws ServletException, IOException, SQLException {
        when(request.getParameter("username")).thenReturn("  admin  ");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
        when(usuarioDAO.buscarPorUsername("  admin  ")).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Usuario o contraseña incorrectos");
    }
}
