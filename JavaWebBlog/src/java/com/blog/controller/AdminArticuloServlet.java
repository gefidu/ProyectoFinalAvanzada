package com.blog.controller;

import com.blog.dao.IArticuloDAO;
import com.blog.dao.MySQLArticuloDAO;
import com.blog.model.Articulo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * AdminArticuloServlet - Controlador para gestión CRUD de artículos (área
 * protegida)
 * Solo responsabilidad: gestionar operaciones de administración de artículos
 */
public class AdminArticuloServlet extends HttpServlet {
    // Controller for Admin Article Management

    private IArticuloDAO articuloDAO;

    @Override
    public void init() throws ServletException {
        this.articuloDAO = new MySQLArticuloDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null)
            action = "listar";

        try {
            switch (action) {
                case "dashboard":
                    mostrarDashboard(request, response);
                    break;
                case "listar":
                    listarArticulos(request, response);
                    break;
                case "nuevo":
                    mostrarFormularioCrear(request, response);
                    break;
                case "editar":
                    mostrarFormularioEditar(request, response);
                    break;
                default:
                    listarArticulos(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar la solicitud. Por favor, intente nuevamente.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "crear":
                    crearArticulo(request, response);
                    break;
                case "actualizar":
                    actualizarArticulo(request, response);
                    break;
                case "eliminar":
                    eliminarArticulo(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar la solicitud. Por favor, intente nuevamente.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * Muestra el dashboard con estadísticas
     */
    private void mostrarDashboard(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int totalArticulos = articuloDAO.contarTotal();
        request.setAttribute("totalArticulos", totalArticulos);
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    /**
     * Lista todos los artículos en el panel de administración
     */
    private void listarArticulos(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        request.setAttribute("articulos", articuloDAO.listarTodos());
        request.getRequestDispatcher("/admin/listar.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para crear un artículo
     */
    private void mostrarFormularioCrear(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/admin/crear.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para editar un artículo
     */
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Articulo articulo = articuloDAO.obtenerPorId(id);

        if (articulo != null) {
            request.setAttribute("articulo", articulo);
            request.getRequestDispatcher("/admin/editar.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Artículo no encontrado");
        }
    }

    /**
     * Crea un nuevo artículo
     */
    private void crearArticulo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String titulo = request.getParameter("titulo");
        String contenido = request.getParameter("contenido");

        // Validaciones básicas
        if (titulo == null || titulo.trim().isEmpty() ||
                contenido == null || contenido.trim().isEmpty()) {
            request.setAttribute("error", "Título y contenido son requeridos");
            request.getRequestDispatcher("/admin/crear.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        Articulo articulo = new Articulo();
        articulo.setTitulo(titulo);
        articulo.setContenido(contenido);
        articulo.setFechaPublicacion(LocalDateTime.now());
        articulo.setAutorId(usuarioId);

        if (articuloDAO.crear(articulo)) {
            response.sendRedirect(request.getContextPath() + "/admin/articulos?action=listar");
        } else {
            request.setAttribute("error", "No se pudo crear el artículo");
            request.getRequestDispatcher("/admin/crear.jsp").forward(request, response);
        }
    }

    /**
     * Actualiza un artículo existente
     */
    private void actualizarArticulo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        // --- Security Check ---
        if (!puedeEditar(request, id)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para editar este artículo");
            return;
        }
        // ----------------------

        String titulo = request.getParameter("titulo");
        String contenido = request.getParameter("contenido");

        // Validaciones básicas
        if (titulo == null || titulo.trim().isEmpty() ||
                contenido == null || contenido.trim().isEmpty()) {
            request.setAttribute("error", "Título y contenido son requeridos");
            Articulo articulo = articuloDAO.obtenerPorId(id);
            request.setAttribute("articulo", articulo);
            request.getRequestDispatcher("/admin/editar.jsp").forward(request, response);
            return;
        }

        Articulo articulo = articuloDAO.obtenerPorId(id);
        if (articulo != null) {
            articulo.setTitulo(titulo);
            articulo.setContenido(contenido);
            articulo.setFechaPublicacion(LocalDateTime.now());

            if (articuloDAO.actualizar(articulo)) {
                response.sendRedirect(request.getContextPath() + "/admin/articulos?action=listar");
            } else {
                request.setAttribute("error", "No se pudo actualizar el artículo");
                request.setAttribute("articulo", articulo);
                request.getRequestDispatcher("/admin/editar.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Artículo no encontrado");
        }
    }

    /**
     * Elimina un artículo
     */
    private void eliminarArticulo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        // --- Security Check ---
        if (!puedeEditar(request, id)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para eliminar este artículo");
            return;
        }
        // ----------------------

        articuloDAO.eliminar(id);
        response.sendRedirect(request.getContextPath() + "/admin/articulos?action=listar");
    }

    /**
     * Verifica si el usuario actual puede editar el artículo
     */
    private boolean puedeEditar(HttpServletRequest request, int articuloId) throws SQLException {
        HttpSession session = request.getSession();
        com.blog.model.Usuario usuario = (com.blog.model.Usuario) session.getAttribute("usuario");

        if (usuario == null)
            return false;
        if ("admin".equals(usuario.getRol()))
            return true; // Admins can edit everything

        // Normal users can only edit their own
        Articulo articulo = articuloDAO.obtenerPorId(articuloId);
        return articulo != null && articulo.getAutorId() == usuario.getId();
    }
}
