package com.inventorymangementsystem.Models;

public enum PreferenceKeys {
    DB_HOST("dbHost"),
    DB_PORT("dbPort"),
    DB_AUTH("dbAuthentication"),
    DB_USERNAME("dbUsername"),
    DB_PASSWORD("dbPassword"),
    DB_SAVE_PASSWORD("dbSavePassword"),
    DB_DATABASE("dbDataBase");

    private final String key;

    PreferenceKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
