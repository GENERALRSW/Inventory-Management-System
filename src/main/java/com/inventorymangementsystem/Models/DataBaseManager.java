package com.inventorymangementsystem.Models;

import java.sql.Connection;

public class DataBaseManager {

    private static void loadBatches(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();

    }

    private static void loadInventoryAdjustments(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();

    }

    private static void loadProducts(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();

    }

    private static void loadPurchaseOrders(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();

    }

    private static void loadSales(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();

    }

    private static void loadSuppliers(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();

    }

    // For adding to database
    public static void addBatch(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
    }


    // For update info to the database
    public static void updateBatch(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
    }


    // For deleting info from the database
    public static void deleteBatch(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
    }



    public static void loadInfo() {
        loadBatches();
        loadInventoryAdjustments();
        loadProducts();
        loadPurchaseOrders();
        loadSales();
        loadSuppliers();
    }

    public static void removeInfo() {
        Batch.empty();
        InventoryAdjustment.empty();
        Product.empty();
        PurchaseOrder.empty();
        Sale.empty();
        Supplier.empty();
    }
}
