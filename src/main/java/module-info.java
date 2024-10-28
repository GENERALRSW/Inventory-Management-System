module com.inventorymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;
    requires jbcrypt;
    requires com.google.gson;
    requires java.prefs;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires jakarta.mail;
    requires jakarta.activation;

    opens com.inventorymanagementsystem to javafx.fxml, com.google.gson;
    exports com.inventorymanagementsystem;
    exports com.inventorymanagementsystem.Controllers;
    exports com.inventorymanagementsystem.Controllers.Admin;
    exports com.inventorymanagementsystem.Controllers.Staff;
    exports com.inventorymanagementsystem.Models;
    exports com.inventorymanagementsystem.Views;
}