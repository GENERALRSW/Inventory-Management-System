package com.inventorymangementsystem.Views;

import com.inventorymangementsystem.Controllers.DBConnectionController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    //Admin Views
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane viewInventoryView;
    private AnchorPane alertsView;
    private AnchorPane purchaseOrdersView;
    private AnchorPane reportsView;
    private AnchorPane historyView;

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

            // Get the controller from the loader
            dbController = loader.getController();
            dbController.shouldShow();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createStage(FXMLLoader loader){
        try{
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Student Management");
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
