package com.inventorymangementsystem.Models;

import com.inventorymangementsystem.Views.ViewFactory;

import java.sql.Connection;

public class Model {
    private static Model model;
    private ViewFactory viewFactory;
    private DataBaseDriver dataBaseDriver;

    private Model(){
        this.viewFactory = new ViewFactory();
        dataBaseDriver = new DataBaseDriver();
    }

    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }

        return model;
    }

    public ViewFactory getViewFactory(){
        return viewFactory;
    }

    public void resetViewFactory(){
        viewFactory = new ViewFactory();
    }

    public DataBaseDriver getDataBaseDriver() {
        return dataBaseDriver;
    }

    public Connection getConnection(){
        return dataBaseDriver.getConnection();
    }

    public void setConnection(Connection connection){
        dataBaseDriver.setConnection(connection);
    }
}
