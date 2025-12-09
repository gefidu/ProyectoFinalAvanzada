package com.blog.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * SetupServlet - Handles database configuration setup
 */
public class SetupServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Display the setup page
        request.getRequestDispatcher("/setup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String host = request.getParameter("host");
        String port = request.getParameter("port");
        String database = request.getParameter("database");
        String user = request.getParameter("user");
        String password = request.getParameter("password");

        // Build connection URL
        String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&allowPublicKeyRetrieval=true",
                host, port, database);

        if ("test".equals(action)) {
            // Test the connection
            if (testConnection(url, user, password)) {
                // Connection successful - redirect back with success message
                response.sendRedirect(request.getContextPath() + "/setup?success=test&host=" + host + 
                        "&port=" + port + "&database=" + database + "&user=" + user + "&password=" + password);
            } else {
                // Connection failed - redirect back with error
                response.sendRedirect(request.getContextPath() + "/setup?error=connection&host=" + host + 
                        "&port=" + port + "&database=" + database + "&user=" + user + "&password=" + password);
            }
        } else if ("save".equals(action)) {
            // Test connection before saving
            if (!testConnection(url, user, password)) {
                response.sendRedirect(request.getContextPath() + "/setup?error=connection&host=" + host + 
                        "&port=" + port + "&database=" + database + "&user=" + user + "&password=" + password);
                return;
            }

            // Save the configuration
            if (saveConfiguration(url, user, password)) {
                response.sendRedirect(request.getContextPath() + "/setup?success=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/setup?error=save&host=" + host + 
                        "&port=" + port + "&database=" + database + "&user=" + user + "&password=" + password);
            }
        }
    }

    /**
     * Tests database connection with the provided parameters
     */
    private boolean testConnection(String url, String user, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                return conn != null && !conn.isClosed();
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("[SetupServlet] Connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Saves configuration to db.properties file
     */
    private boolean saveConfiguration(String url, String user, String password) {
        try {
            // Load existing properties to preserve other settings
            Properties props = new Properties();
            
            // Try to load existing properties
            try (InputStream input = getClass().getResourceAsStream("/com/blog/dao/db.properties")) {
                if (input != null) {
                    props.load(input);
                }
            } catch (Exception e) {
                // If can't load, start with empty properties
                System.err.println("[SetupServlet] Could not load existing properties: " + e.getMessage());
            }

            // Update connection properties
            props.setProperty("db.url", url);
            props.setProperty("db.user", user);
            props.setProperty("db.password", password);

            // Find the properties file location
            String classesPath = getServletContext().getRealPath("/WEB-INF/classes");
            File propsFile = new File(classesPath, "com/blog/dao/db.properties");
            
            // Ensure parent directory exists
            propsFile.getParentFile().mkdirs();

            // Save properties
            try (OutputStream output = new FileOutputStream(propsFile)) {
                props.store(output, "Database Configuration - Updated by Setup Wizard");
                System.out.println("[SetupServlet] Configuration saved to: " + propsFile.getAbsolutePath());
                return true;
            }
        } catch (Exception e) {
            System.err.println("[SetupServlet] Failed to save configuration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
