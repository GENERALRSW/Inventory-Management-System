package com.inventorymanagementsystem.Models;

import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.List;
import java.io.FileWriter;

public class MyJson {

    public void exportToJson(List<Batch> batches, String filePath) throws IOException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(batches);

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonString);
        }
    }

    // Method to read JSON file and parse different tables
    public JsonObject readJson(String filePath) throws IOException {
        Gson gson = new Gson();
        try (FileReader fileReader = new FileReader(filePath)) {
            return gson.fromJson(fileReader, JsonObject.class);
        }
    }

    // Insert data into Batches table
    public void insertBatches(List<Batch> batches){
        Connection conn = Model.getInstance().getDataBaseDriver().getConnection();
        String insertQuery = "INSERT INTO Batches (product_id, current_stock, expiration_date) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            for (Batch batch : batches) {
                pstmt.setInt(1, batch.getProductId());
                pstmt.setInt(2, batch.getCurrentStock());
                pstmt.setDate(3, Date.valueOf(batch.getExpirationDate()));
                pstmt.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Insert data into Inventory Adjustment table
    public void insertInventoryAdjustment(List<InventoryAdjustment> inventoryAdjustments) {
        Connection conn = Model.getInstance().getDataBaseDriver().getConnection();
        String insertQuery = "INSERT INTO InventoryAdjustments (batch_id, adjustment_date, adjustment_type, quantity, reason) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            for (InventoryAdjustment inventoryAdjustment : inventoryAdjustments) {
                pstmt.setInt(1, inventoryAdjustment.getBatchId());
                pstmt.setDate(2, Date.valueOf(inventoryAdjustment.getAdjustmentDate()));
                pstmt.setString(3, inventoryAdjustment.getAdjustmentType());
                pstmt.setInt(4, inventoryAdjustment.getQuantity());
                pstmt.setString(5, inventoryAdjustment.getReason());
                pstmt.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyJson importer = new MyJson();
        try {
            // Step 1: Read the data from JSON file
            JsonObject jsonObject = importer.readJson("data.json");

            // Step 2: Parse the people data
            Type batchListType = new TypeToken<List<Batch>>() {}.getType();
            List<Batch> batches = new Gson().fromJson(jsonObject.get("Batches"), batchListType);

            // Step 3: Parse the orders data
            Type inventoryAdjustmentListType = new TypeToken<List<InventoryAdjustment>>() {}.getType();
            List<InventoryAdjustment> inventoryAdjustments = new Gson().fromJson(jsonObject.get("Inventory_Adjustments"), inventoryAdjustmentListType);

            // Step 4: Insert data into the respective tables
            importer.insertBatches(batches);
            importer.insertInventoryAdjustment(inventoryAdjustments);

            System.out.println("Data successfully imported into the database.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
