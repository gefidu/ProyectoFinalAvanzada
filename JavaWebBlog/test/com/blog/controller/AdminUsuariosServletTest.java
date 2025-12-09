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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("Tests para AdminUsuariosServlet")
class AdminUsuariosServletTest {

    private AdminUsuariosServlet servlet;

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
        servlet = new AdminUsuariosServlet();

        // Inject DAO mock
        Field daoField = AdminUsuariosServlet.class.getDeclaredField("usuarioDAO");
        daoField.setAccessible(true);
        daoField.set(servlet, usuarioDAO);
    }

    @Test
    @DisplayName("doGet sin sesión debe denegar acceso")
    void testDoGetSinSesion() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
    }

    @Test
    @DisplayName("doGet como no-admin debe denegar acceso")
    void testDoGetNoAdmin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        Usuario usuario = new Usuario(2, "user", "pass", "User Name", "user@test.com", "User"); // Default Role (not
                                                                                                // admin)
        when(session.getAttribute("usuario")).thenReturn(usuario);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
    }

    @Test
    @DisplayName("doGet como admin debe listar usuarios")
    void testDoGetAdminListar() throws ServletException, IOException, SQLException {
        when(request.getSession(false)).thenReturn(session);
        Usuario admin = new Usuario(1, "admin", "pass", "Admin Name", "admin@test.com", "Admin");
        admin.setRol("admin");
        when(session.getAttribute("usuario")).thenReturn(admin);

        when(request.getParameter("action")).thenReturn("listar");
        when(request.getRequestDispatcher("/admin/usuarios.jsp")).thenReturn(dispatcher);

        List<Usuario> usuarios = new ArrayList<>();
        when(usuarioDAO.listarTodos()).thenReturn(usuarios);

        servlet.doGet(request, response);

        verify(request).setAttribute("usuarios", usuarios);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doGet promover usuario debe llamar a DAO")
    void testPromoverUsuario() throws ServletException, IOException, SQLException {
        when(request.getSession(false)).thenReturn(session);
        Usuario admin = new Usuario(1, "admin", "pass", "Admin Name", "admin@test.com", "Admin");
        admin.setRol("admin");
        when(session.getAttribute("usuario")).thenReturn(admin);

        when(request.getParameter("action")).thenReturn("promover");
        when(request.getParameter("id")).thenReturn("5");
        when(request.getContextPath()).thenReturn("/blog");

        // Mock target user check (optional based on logic, but safe to mock)
        Usuario target = new Usuario(5, "user", "pass", "Target User", "target@test.com", "User");
        when(usuarioDAO.obtenerPorId(5)).thenReturn(target);

        servlet.doGet(request, response);

        verify(usuarioDAO).actualizarRol(5, "admin");
        verify(response).sendRedirect("/blog/admin/usuarios");
    }

    @Test
    @DisplayName("doGet demover admin original debe ser ignorado")
    void testDemoverAdminOriginal() throws ServletException, IOException, SQLException {
        when(request.getSession(false)).thenReturn(session);
        Usuario admin = new Usuario(1, "admin", "pass", "Admin Name", "admin@test.com", "Admin");
        admin.setRol("admin");
        when(session.getAttribute("usuario")).thenReturn(admin);

        when(request.getParameter("action")).thenReturn("demover");
        when(request.getParameter("id")).thenReturn("1");
        when(request.getContextPath()).thenReturn("/blog");

        Usuario superAdmin = new Usuario(1, "admin", "pass", "Super Admin", "admin@test.com", "Super Admin");
        when(usuarioDAO.obtenerPorId(1)).thenReturn(superAdmin);

        servlet.doGet(request, response);

        // Verify we DID NOT update role
        verify(usuarioDAO, never()).actualizarRol(anyInt(), anyString());
        verify(response).sendRedirect("/blog/admin/usuarios");
    }
}
