module com.inventorymangementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires mysql.connector.j;
    requires  java.sql;
    requires jbcrypt;
    requires com.google.gson;


    opens com.inventorymangementsystem to javafx.fxml, com.google.gson;
    exports com.inventorymangementsystem;
    exports com.inventorymangementsystem.Controllers;
    exports com.inventorymangementsystem.Models;
    exports com.inventorymangementsystem.Views;
}