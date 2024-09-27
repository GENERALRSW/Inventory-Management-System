package com.inventorymanagementsystem.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Batch {
    public final int ID;
    private final IntegerProperty productId = new SimpleIntegerProperty();
    private final IntegerProperty currentStock = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> expirationDate = new SimpleObjectProperty<>();
    private final IntegerProperty supplierId = new SimpleIntegerProperty();
    private static final Map<Integer, Batch> batches = new HashMap<>();
    private static final ObservableList<Batch> batchList = FXCollections.observableArrayList();

    public Batch(int batchId, int productId, int currentStock, LocalDate expirationDate, int supplierId) {
        ID = batchId;
        this.productId.set(productId);
        this.currentStock.set(currentStock);
        this.expirationDate.set(expirationDate);
        this.supplierId.set(supplierId);
        add(this);
    }

    // Getters and setters
    public int getProductId() {
        return productId.get();
    }

    public void setProductId(int productId) {
        this.productId.set(productId);
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public int getCurrentStock() {
        return currentStock.get();
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock.set(currentStock);
    }

    public IntegerProperty currentStockProperty() {
        return currentStock;
    }

    public LocalDate getExpirationDate() {
        return expirationDate.get();
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate.set(expirationDate);
    }

    public ObjectProperty<LocalDate> expirationDateProperty() {
        return expirationDate;
    }

    public int getSupplierId() {
        return supplierId.get();
    }

    public void setSupplierId(int supplierId) {
        this.supplierId.set(supplierId);
    }

    public IntegerProperty supplierIdProperty() {
        return supplierId;
    }

    public static void add(Batch batch) {
        if(batch != null && !contains(batch.ID)){
            batches.put(batch.ID, batch);
            batchList.add(batch);
        }
        else if(batch == null){
            System.out.println("Batch is null. Was not added to Map and List");
        }
        else{
            System.out.println("Batch is already present!");
        }
    }

    public static void update(Batch batch, int productId, int currentStock, LocalDate expirationDate, int supplierId){
        if(batch != null){
            batch.setProductId(productId);
            batch.setCurrentStock(currentStock);
            batch.setExpirationDate(expirationDate);
            batch.setSupplierId(supplierId);
        }
        else{
            System.out.println("Batch value is null");
        }
    }

    public static void remove(Batch batch) {
        if(valid(batch)){
            batchList.remove(batch);
            batches.remove(batch.ID);
        }
        else{
            System.out.println("Batch ID not found");
        }
    }

    public static Batch get(int batchID) {
        if(contains(batchID)){
            return batches.get(batchID);
        }
        else{
            System.out.println("Batch ID not found. Null was returned");
            return null;
        }
    }

    public static boolean valid(Batch batch){
        return batch != null && contains(batch.ID);
    }

    public static int getBatchCount() {
        return batches.size();
    }

    public static boolean contains(int batchID) {
        return batches.containsKey(batchID);
    }

    public static ObservableList<Batch> getList(){
        return batchList;
    }

    public static void empty() {
        batches.clear();
        batchList.clear();
    }
}

