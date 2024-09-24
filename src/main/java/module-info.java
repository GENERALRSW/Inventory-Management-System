module com.inventorymangementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires mysql.connector.j;
    requires  java.sql;
    requires jbcrypt;


    opens com.inventorymangementsystem to javafx.fxml;
    exports com.inventorymangementsystem;
    exports com.inventorymangementsystem.Controllers;
    exports com.inventorymangementsystem.Models;
    exports com.inventorymangementsystem.Views;
}