package com.inventorymanagementsystem;
import com.inventorymanagementsystem.Controllers.PurchaseOrdersController;
import com.inventorymanagementsystem.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        PurchaseOrdersController.sendEmail(
                "peterstywaine@gmail.com",
                "mpap tyra nubl euln",
                "peterstywaine@gmail.com",
                "Test Email",
                "JavaFx Test Email Number #2 \n Testing Testing");
        Model.getInstance();
        Model.getInstance().getViewFactory().dataBaseConnectionWindow();
    }
}
