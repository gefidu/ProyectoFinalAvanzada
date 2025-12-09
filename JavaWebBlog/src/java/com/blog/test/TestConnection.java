package com.blog.test;

import com.blog.dao.ConexionBD;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Testing DB Connection...");
        try {
            Connection conn = ConexionBD.getInstancia().getConexion();
            if (conn != null) {
                System.out.println("SUCCESS: Connection established!");
                conn.close();
            } else {
                System.out.println("FAILURE: Connection object is null.");
            }
        } catch (Exception e) {
            System.out.println("FAILURE: Exception occurred.");
            e.printStackTrace();
        }
    }
}
