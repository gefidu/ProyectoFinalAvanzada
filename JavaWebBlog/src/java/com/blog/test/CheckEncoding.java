package com.blog.test;

import com.blog.dao.ConexionBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckEncoding {
    public static void main(String[] args) {
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id, titulo, contenido FROM articulos")) {

            System.out.println("--- DB CONTENT CHECK ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("titulo");
                // Print raw bytes to see hidden issues
                System.out.println("ID: " + id);
                System.out.println("Title: " + title);
                // System.out.println("Hex: " +
                // javax.xml.bind.DatatypeConverter.printHexBinary(title.getBytes())); //
                // Dependency might be missing
            }
            System.out.println("------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
