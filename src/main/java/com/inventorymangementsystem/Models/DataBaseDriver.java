package com.inventorymangementsystem.Models;

import java.sql.Connection;

public class DataBaseDriver {
    private static DataBaseDriver dataBaseDriver;
    private Connection connection;

    public DataBaseDriver(){}

    public DataBaseDriver(Connection connection){
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public void loadInfo() {
    }

    public void removeInfo() {
        
    }
}
