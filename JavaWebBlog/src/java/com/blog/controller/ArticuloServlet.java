package com.blog.controller;

import com.blog.dao.IArticuloDAO;
import com.blog.dao.MySQLArticuloDAO;
import com.blog.model.Articulo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * ArticuloServlet - Controlador para visualización pública de artículos
 * 
 * Principio SOLID aplicado: S - Single Responsibility Principle
 * (Responsabilidad Única).
 * Ver Sección 1.1 en PRINCIPIOS_Y_PATRONES.tex
 * 
 * Principio SOLID aplicado: D - Dependency Inversion Principle (Inversión de
 * Dependencias).
 * Ver Sección 1.5 en PRINCIPIOS_Y_PATRONES.tex
 * 
 * Principio de Ingeniería: SoC (Separation of Concerns).
 * Ver Sección 2.3 en PRINCIPIOS_Y_PATRONES.tex
 * 
 * Este Servlet tiene un trabajo muy claro: recibir peticiones del navegador y
 * decidir qué mostrar.
 * No se pone a guardar datos en la BD (eso es trabajo del DAO) ni a maquetar
 * HTML (eso es trabajo del JSP).
 */
public class ArticuloServlet extends HttpServlet {

    private IArticuloDAO articuloDAO;

    @Override
    public void init() throws ServletException {
        /*
         * Aquí ocurre la magia de la Inyección de Dependencias (manual por ahora).
         * Inicializamos 'articuloDAO' con la implementación concreta de MySQL.
         * Si mañana nos mudamos a PostgreSQL, solo cambiamos esta línea aquí.
         * ¡El resto del Servlet ni se enterará del cambio! (Open/Closed Principle)
         */
        this.articuloDAO = new MySQLArticuloDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("ver".equals(action)) {
                verArticulo(request, response);
            } else {
                listarArticulos(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar el contenido. Por favor, intente nuevamente.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * Lista todos los artículos
     */
    private void listarArticulos(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        request.setAttribute("articulos", articuloDAO.listarTodos());
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    /**
     * Muestra el detalle de un artículo
     */
    private void verArticulo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Articulo articulo = articuloDAO.obtenerPorId(id);

        if (articulo != null) {
            request.setAttribute("articulo", articulo);
            request.getRequestDispatcher("/articulo.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Artículo no encontrado");
        }
    }
}
