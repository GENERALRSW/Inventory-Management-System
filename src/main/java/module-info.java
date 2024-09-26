module com.inventorymangementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires  java.sql;
    requires jbcrypt;
    requires com.google.gson;
    requires java.prefs;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    opens com.inventorymangementsystem to javafx.fxml, com.google.gson;
    exports com.inventorymangementsystem;
    exports com.inventorymangementsystem.Controllers;
    exports com.inventorymangementsystem.Models;
    exports com.inventorymangementsystem.Views;
}