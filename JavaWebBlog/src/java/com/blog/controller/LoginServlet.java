package com.blog.controller;

import com.blog.dao.IUsuarioDAO;
import com.blog.dao.MySQLUsuarioDAO;
import com.blog.model.Usuario;
import com.blog.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * LoginServlet - Controlador para autenticación de usuarios
 * Solo responsabilidad: gestionar el proceso de login
 */
public class LoginServlet extends HttpServlet {
    
    private IUsuarioDAO usuarioDAO;
    
    @Override
    public void init() throws ServletException {
        this.usuarioDAO = new MySQLUsuarioDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Mostrar formulario de login
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validaciones básicas
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Usuario y contraseña son requeridos");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        try {
            Usuario usuario = usuarioDAO.buscarPorUsername(username);
            
            if (usuario != null && PasswordUtil.verificarPassword(password, usuario.getPassword())) {
                // Login exitoso - crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("usuarioNombre", usuario.getNombre());
                
                // Redirigir al dashboard
                response.sendRedirect(request.getContextPath() + "/admin/articulos?action=dashboard");
            } else {
                // Login fallido
                request.setAttribute("error", "Usuario o contraseña incorrectos");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error de conexión. Por favor, intente nuevamente.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
