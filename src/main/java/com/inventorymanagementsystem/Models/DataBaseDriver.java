package com.inventorymanagementsystem.Models;

import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseDriver {
    private Connection connection;
    private static final String MYSQL_URL = System.getenv("MYSQL_URL");
    private static final String MYSQL_USERNAME = System.getenv("MYSQL_USERNAME");
    private static final String MYSQL_PASSWORD = System.getenv("MYSQL_PASSWORD");

    public DataBaseDriver(){
        try{
            this.connection = DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
        }catch(SQLException e){
            e.printStackTrace();
            Model.getInstance().showAlert(AlertType.ERROR, "No Database Connection",
                    "No Database Connection Available");
            System.exit(1);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public void setConnection(String url, String user, String password){
        try{
            this.connection = DriverManager.getConnection(url, user, password);
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Connection to database failed!!!");
        }
    }

    public void closeConnection(){
        try{
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean connectionIsNull(){
        return connection == null;
    }

    public boolean connectionIsNotNull(){
        return connection != null;
    }

    public boolean connectionIsClosed(){
        try{
            return connection.isClosed();
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
