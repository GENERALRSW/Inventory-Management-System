package com.inventorymanagementsystem.Models;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Product {
    public final int ID;
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> unitPrice = new SimpleObjectProperty<>();
    private static final Map<Integer, Product> products = new HashMap<>();
    private static final ObservableList<Product> productList = FXCollections.observableArrayList();

    public Product(int productId, String name, String category, BigDecimal unitPrice) {
        ID = productId;
        this.name.set(name);
        this.category.set(category);
        this.unitPrice.set(unitPrice);
        add(this);
    }

    // Getters and setters
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice.get();
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public ObjectProperty<BigDecimal> unitPriceProperty() {
        return unitPrice;
    }

    public static void add(Product product) {
        if(product != null && !contains(product.ID)){
            products.put(product.ID, product);
            productList.add(product);
        }
        else if(product == null){
            System.out.println("Product is null. Was not added to Map and List");
        }
        else{
            System.out.println("Product is already present!");
        }
    }

    public static void update(Product product, String name, String category, BigDecimal unitPrice){
        if(product != null){
            product.setName(name);
            product.setCategory(category);
            product.setUnitPrice(unitPrice);
        }
        else{
            System.out.println("Product value is null");
        }
    }

    public static void remove(Product product) {
        if(valid(product)){
            productList.remove(product);
            products.remove(product.ID);
        }
        else{
            System.out.println("Product ID not found");
        }
    }

    public static Product get(int productID) {
        if(contains(productID)){
            return products.get(productID);
        }
        else{
            System.out.println("Product ID not found. Null was returned");
            return null;
        }
    }

    public static boolean valid(Product product){
        return product != null && contains(product.ID);
    }

    public static int getProductCount() {
        return products.size();
    }

    public static boolean contains(int productID) {
        return products.containsKey(productID);
    }

    public static ObservableList<Product> getList(){
        return productList;
    }

    public static void empty() {
        products.clear();
        productList.clear();
    }
}

