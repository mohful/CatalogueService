package com.example.cataloguedb.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {

    private final static  String PUBLIC_DNS = "db-dev.c0b8q6uhcszv.us-east-1.rds.amazonaws.com";
    private final static String PORT = "3306";
    private final static String REMOTE_DATABASE_USERNAME = "admin";
    private final static String DATABASE_USER_PASSWORD = "password";
    private final static String DATABASE = "CatalogueDB";


    public static Connection connect() throws SQLException {
        Connection conn = null;
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://" + PUBLIC_DNS + ":" + PORT + "/" + DATABASE;
            Class.forName(driver);
            conn = DriverManager.getConnection(url, REMOTE_DATABASE_USERNAME, DATABASE_USER_PASSWORD);
            System.out.println("Connection to mysql has been established.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
