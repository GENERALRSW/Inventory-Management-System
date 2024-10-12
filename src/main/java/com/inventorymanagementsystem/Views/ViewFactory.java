package com.inventorymanagementsystem.Views;

import com.inventorymanagementsystem.Controllers.AdminController;
import com.inventorymanagementsystem.Controllers.DBConnectionController;
import com.inventorymanagementsystem.Controllers.LoginController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    //Admin Views
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane viewInventoryView;
    private AnchorPane alertsView;
    private AnchorPane suppliersView;
    private AnchorPane purchaseOrdersView;
    private AnchorPane reportsView;
    private AnchorPane historyView;
    private Image image = new Image(getClass().getResourceAsStream("/Images/Inventory-Management-System_Icon.png"));

    public ViewFactory(){
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    public AnchorPane getDashboardView(){
        if(dashboardView == null){
            try{
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/dashBoard.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return dashboardView;
    }

    public AnchorPane getViewInventoryView(){
        if(viewInventoryView == null){
            try{
                viewInventoryView = new FXMLLoader(getClass().getResource("/Fxml/viewInventory.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return viewInventoryView;
    }

    public AnchorPane getAlertsView(){
        if(alertsView == null){
            try{
                alertsView = new FXMLLoader(getClass().getResource("/Fxml/alerts.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return alertsView;
    }

    public AnchorPane getPurchaseOrdersView(){
        if(purchaseOrdersView == null){
            try{
                purchaseOrdersView = new FXMLLoader(getClass().getResource("/Fxml/purchaseOrders.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return purchaseOrdersView;
    }

    public AnchorPane getSuppliersView(){
        if(suppliersView == null){
            try{
                suppliersView = new FXMLLoader(getClass().getResource("/Fxml/suppliers.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return suppliersView;
    }

    public AnchorPane getReportsView(){
        if(reportsView == null){
            try{
                reportsView = new FXMLLoader(getClass().getResource("/Fxml/reports.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return reportsView;
    }

    public AnchorPane getHistoryView(){
        if(historyView == null){
            try{
                historyView = new FXMLLoader(getClass().getResource("/Fxml/history.fxml")).load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return historyView;
    }

    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
    }

    public void showSignUpWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/signUp.fxml"));
        createStage(loader, "Sign Up");
    }

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
        createStage(loader);
    }

    public void showDataBaseConnectionWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/DBConnection.fxml"));
        createStage(loader);
    }

    public void dataBaseConnectionWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/DBConnection.fxml"));
        DBConnectionController dbController = null;

        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Database Connection");
            stage.setResizable(false);

            stage.getIcons().add(image);

            // Get the controller from the loader
            dbController = loader.getController();
            dbController.shouldShow();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/login.fxml"));
        LoginController loginController = null;

        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Inventory Management");
            stage.setResizable(false);

            stage.getIcons().add(image);

            // Get the controller from the loader
            loginController = loader.getController();
            loginController.shouldShow();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createStageDimension(FXMLLoader loader){
        try{
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Inventory Management");

            // Set stage size relative to screen (e.g., 60% of width, 80% of height)
            Rectangle2D screenBounds = Screen.getPrimary().getBounds();
            stage.setWidth(screenBounds.getWidth() * 0.80);  // 60% of screen width
            stage.setHeight(screenBounds.getHeight() * 0.80);  // 80% of screen height

            // Set the stage icon
            stage.getIcons().add(image);

            stage.show();
            stage.setResizable(false);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createStage(FXMLLoader loader){
        try{
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Inventory Management");

            stage.getIcons().add(image);

            stage.show();
            stage.setResizable(false);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createStage(FXMLLoader loader, String title){
        try{
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);

            stage.getIcons().add(image);

            stage.show();
            stage.setResizable(false);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void closeStage(Stage stage){
        stage.close();
    }
}
