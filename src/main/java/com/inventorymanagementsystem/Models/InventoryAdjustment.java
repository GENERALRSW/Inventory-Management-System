package com.inventorymanagementsystem.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class InventoryAdjustment {
    public final int ID;
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty productId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> adjustmentDate = new SimpleObjectProperty<>();
    private final IntegerProperty batchId = new SimpleIntegerProperty();
    private final StringProperty adjustmentType = new SimpleStringProperty();
    private final IntegerProperty previous_stock = new SimpleIntegerProperty();
    private final IntegerProperty adjusted_stock = new SimpleIntegerProperty();
    private final StringProperty productName = new SimpleStringProperty();
    private static final Map<Integer, InventoryAdjustment> inventoryAdjustments = new HashMap<>();
    private static final ObservableList<InventoryAdjustment> inventoryAdjustmentList = FXCollections.observableArrayList();

    public InventoryAdjustment(int adjustmentId, int product_id, String productName, int batchId, LocalDate adjustmentDate,
                               String adjustmentType, int previous_stock, int adjusted_stock) {
        ID = adjustmentId;
        this.id.set(adjustmentId);
        this.productId.set(product_id);
        this.productName.set(productName);
        this.batchId.set(batchId);
        this.adjustmentDate.set(adjustmentDate);
        this.adjustmentType.set(adjustmentType);
        this.previous_stock.set(previous_stock);
        this.adjusted_stock.set(adjusted_stock);
        add(this);
    }

    // Getters and setters
    public IntegerProperty idProperty() {
        return id;
    }

    public int getBatchId() {
        return batchId.get();
    }

    public StringProperty batchIdStringProperty(){
        StringProperty batch_id = new SimpleStringProperty();

        if(getBatchId() == -1){
            batch_id.set("NULL");
        }
        else{
            batch_id.set(String.valueOf(getBatchId()));
        }

        return batch_id;
    }

    public IntegerProperty batchIdProperty() {
        return batchId;
    }

    public int getProductId(){
        return productId.get();
    }

    public IntegerProperty productIdProperty(){
        return productId;
    }

    public String getProductName(){
        return productName.get();
    }

    public StringProperty productNameProperty(){
        return productName;
    }

    public LocalDate getAdjustmentDate() {
        return adjustmentDate.get();
    }

    public ObjectProperty<LocalDate> adjustmentDateProperty() {
        return adjustmentDate;
    }

    public String getAdjustmentType() {
        return adjustmentType.get();
    }

    public StringProperty adjustmentTypeProperty() {
        return adjustmentType;
    }

    public int getPrevious_stock() {
        return previous_stock.get();
    }

    public IntegerProperty previous_stockProperty() {
        return previous_stock;
    }

    public StringProperty previous_stockStringProperty(){
        StringProperty previousStock = new SimpleStringProperty();

        if(getPrevious_stock() == -1){
            previousStock.set("NULL");
        }
        else{
            previousStock.set(String.valueOf(getPrevious_stock()));
        }

        return previousStock;
    }

    public int getAdjusted_stock() {
        return adjusted_stock.get();
    }

    public IntegerProperty adjusted_stockProperty() {
        return adjusted_stock;
    }

    public StringProperty adjusted_stockStringProperty(){
        StringProperty adjustedStock = new SimpleStringProperty();

        if(getAdjusted_stock() == -1){
            adjustedStock.set("NULL");
        }
        else{
            adjustedStock.set(String.valueOf(getAdjusted_stock()));
        }

        return adjustedStock;
    }

    public static void add(InventoryAdjustment inventoryAdjustment) {
        if(inventoryAdjustment != null && !contains(inventoryAdjustment.ID)){
            inventoryAdjustments.put(inventoryAdjustment.ID, inventoryAdjustment);
            inventoryAdjustmentList.add(inventoryAdjustment);
        }
        else if(inventoryAdjustment == null){
            System.out.println("Inventory Adjustment is null. Was not added to Map and List");
        }
        else{
            System.out.println("Inventory Adjustment is already present!");
        }
    }

    public static void remove(InventoryAdjustment inventoryAdjustment) {
        if(valid(inventoryAdjustment)){
            inventoryAdjustmentList.remove(inventoryAdjustment);
            inventoryAdjustments.remove(inventoryAdjustment.ID);
        }
        else{
            System.out.println("Inventory Adjustment ID not found");
        }
    }

    public static InventoryAdjustment getInventoryAdjustment(int inventoryAdjustmentID) {
        if(contains(inventoryAdjustmentID)){
            return inventoryAdjustments.get(inventoryAdjustmentID);
        }
        else{
            System.out.println("Inventory Adjustment ID not found. Null was returned");
            return null;
        }
    }

    public static boolean valid(InventoryAdjustment inventoryAdjustment){
        return inventoryAdjustment != null && contains(inventoryAdjustment.ID);
    }

    public static int getInventoryAdjustmentCount() {
        return inventoryAdjustments.size();
    }

    public static boolean contains(int inventoryAdjustmentID) {
        return inventoryAdjustments.containsKey(inventoryAdjustmentID);
    }

    public static ObservableList<InventoryAdjustment> getList(){
        return inventoryAdjustmentList;
    }

    public static void empty() {
        inventoryAdjustments.clear();
        inventoryAdjustmentList.clear();
    }
}

