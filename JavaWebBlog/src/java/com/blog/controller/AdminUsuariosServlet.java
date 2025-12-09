package com.blog.controller;

import com.blog.dao.IUsuarioDAO;
import com.blog.dao.MySQLUsuarioDAO;
import com.blog.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminUsuariosServlet extends HttpServlet {

    private IUsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new MySQLUsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Ensure only admins access this
        if (!esAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }

        String action = request.getParameter("action");
        if (action == null)
            action = "listar";

        try {
            switch (action) {
                case "listar":
                    listarUsuarios(request, response);
                    break;
                case "promover":
                    cambiarRol(request, response, "admin");
                    break;
                case "demover":
                    cambiarRol(request, response, "autor");
                    break;
                default:
                    listarUsuarios(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/admin/usuarios.jsp").forward(request, response);
    }

    private void cambiarRol(HttpServletRequest request, HttpServletResponse response, String nuevoRol)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        // Protection: Cannot demote the original admin (ID 1) or 'admin' username
        Usuario targetUser = usuarioDAO.obtenerPorId(id);
        if (targetUser != null && "admin".equalsIgnoreCase(targetUser.getUsername()) && "autor".equals(nuevoRol)) {
            // Silently ignore or show error (redirecting to list for now)
            response.sendRedirect(request.getContextPath() + "/admin/usuarios");
            return;
        }

        usuarioDAO.actualizarRol(id, nuevoRol);
        response.sendRedirect(request.getContextPath() + "/admin/usuarios");
    }

    private boolean esAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return false;
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && "admin".equals(u.getRol());
    }
}
