package com.inventorymangementsystem.Views;

public enum AdminMenuOptions {
    DASHBOARD("Dashboard"),
    VIEW_INVENTORY("View Inventory");

    private final String description;

    AdminMenuOptions(String description) {
        this.description = description;
    }
}
