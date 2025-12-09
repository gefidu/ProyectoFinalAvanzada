package com.blog.controller;

import com.blog.dao.IUsuarioDAO;
import com.blog.dao.MySQLUsuarioDAO;
import com.blog.model.Usuario;
import com.blog.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {

    private IUsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        usuarioDAO = new MySQLUsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Mostrar formulario de registro (simple forward a register.jsp)
        // Aunque generalmente se accede directamente al JSP, esto permite url limpia
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Validar si el usuario ya existe
            if (usuarioDAO.buscarPorUsername(username) != null) {
                request.setAttribute("error", "El nombre de usuario ya está en uso.");
                request.setAttribute("nombre", nombre);
                request.setAttribute("username", username);
                request.setAttribute("email", email);
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            // Crear nuevo usuario
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setUsername(username);
            usuario.setEmail(email);
            // Hashear contraseña
            usuario.setPassword(PasswordUtil.hashPassword(password));

            if (usuarioDAO.crear(usuario)) {
                // Éxito: Redirigir a login con mensaje
                request.getSession().setAttribute("mensaje", "Cuenta creada exitosamente. Por favor inicia sesión.");
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                request.setAttribute("error", "Error al crear la cuenta.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de base de datos: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
