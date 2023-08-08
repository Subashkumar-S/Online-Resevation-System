package com.example.onlinereservationsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseHandler {
    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName = "onlineReservationSystem";
        String databaseUser = "root";
        String databasePassword = "5098";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("Database connected.");
        }catch (Exception e){
            e.getStackTrace();
            e.getCause();
        }
        return databaseLink;
    }
}
