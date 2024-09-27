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
    private final IntegerProperty batchId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> adjustmentDate = new SimpleObjectProperty<>();
    private final StringProperty adjustmentType = new SimpleStringProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final StringProperty reason = new SimpleStringProperty();
    private static final Map<Integer, InventoryAdjustment> inventoryAdjustments = new HashMap<>();
    private static final ObservableList<InventoryAdjustment> inventoryAdjustmentList = FXCollections.observableArrayList();

    public InventoryAdjustment(int adjustmentId, int batchId, LocalDate adjustmentDate, String adjustmentType, int quantity, String reason) {
        ID = adjustmentId;
        this.batchId.set(batchId);
        this.adjustmentDate.set(adjustmentDate);
        this.adjustmentType.set(adjustmentType);
        this.quantity.set(quantity);
        this.reason.set(reason);
        add(this);
    }

    // Getters and setters
    public int getBatchId() {
        return batchId.get();
    }

    public void setBatchId(int batchId) {
        this.batchId.set(batchId);
    }

    public IntegerProperty batchIdProperty() {
        return batchId;
    }

    public LocalDate getAdjustmentDate() {
        return adjustmentDate.get();
    }

    public void setAdjustmentDate(LocalDate adjustmentDate) {
        this.adjustmentDate.set(adjustmentDate);
    }

    public ObjectProperty<LocalDate> adjustmentDateProperty() {
        return adjustmentDate;
    }

    public String getAdjustmentType() {
        return adjustmentType.get();
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType.set(adjustmentType);
    }

    public StringProperty adjustmentTypeProperty() {
        return adjustmentType;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public String getReason() {
        return reason.get();
    }

    public void setReason(String reason) {
        this.reason.set(reason);
    }

    public StringProperty reasonProperty() {
        return reason;
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

    public static void update(InventoryAdjustment inventoryAdjustment, int batchId, LocalDate adjustmentDate, String adjustmentType, int quantity, String reason){
        if(inventoryAdjustment != null){
            inventoryAdjustment.setBatchId(batchId);
            inventoryAdjustment.setAdjustmentDate(adjustmentDate);
            inventoryAdjustment.setAdjustmentType(adjustmentType);
            inventoryAdjustment.setQuantity(quantity);
            inventoryAdjustment.setReason(reason);
        }
        else{
            System.out.println("Inventory Adjustment value is null");
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

