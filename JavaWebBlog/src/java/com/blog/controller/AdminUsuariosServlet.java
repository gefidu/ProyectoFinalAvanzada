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
                case "eliminar":
                    eliminarUsuario(request, response);
                    break;
                case "eliminarTodos":
                    eliminarTodosExceptoAdmins(request, response);
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

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        // Get current session user
        HttpSession session = request.getSession(false);
        Usuario currentUser = (Usuario) session.getAttribute("usuario");

        // Protection: Cannot delete self
        if (currentUser.getId() == id) {
            response.sendRedirect(request.getContextPath() + "/admin/usuarios?error=no-self-delete");
            return;
        }

        // Protection: Cannot delete user with ID 1 (super admin)
        if (id == 1) {
            response.sendRedirect(request.getContextPath() + "/admin/usuarios?error=protected-user");
            return;
        }

        // Delete the user
        usuarioDAO.eliminar(id);
        response.sendRedirect(request.getContextPath() + "/admin/usuarios");
    }

    private void eliminarTodosExceptoAdmins(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        // Delete all non-admin users
        int deletedCount = usuarioDAO.eliminarTodosExceptoAdmins();
        response.sendRedirect(request.getContextPath() + "/admin/usuarios?deleted=" + deletedCount);
    }

    private boolean esAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null)
            return false;
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && "admin".equals(u.getRol());
    }
}
