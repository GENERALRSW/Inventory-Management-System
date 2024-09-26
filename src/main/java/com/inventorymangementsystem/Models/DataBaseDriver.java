package com.inventorymangementsystem.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseDriver {
    private Connection connection;

    public DataBaseDriver(){}

    public DataBaseDriver(Connection connection){
        this.connection = connection;
    }

    public DataBaseDriver(String url, String user, String password){
        try{
            this.connection = DriverManager.getConnection(url, user, password);
        }catch(SQLException e){
            e.printStackTrace();
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
        }
    }
}
