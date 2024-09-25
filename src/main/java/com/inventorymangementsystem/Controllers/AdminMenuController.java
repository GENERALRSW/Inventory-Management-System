package com.inventorymangementsystem.Controllers;

import com.inventorymangementsystem.Models.Model;
import com.inventorymangementsystem.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button btnDashboard, btnViewInventory, btnAlerts, btnPurchaseOrders, btnReports, btnHistory, btnSignOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners(){
        btnDashboard.setOnAction(event -> onDashboard());
        btnViewInventory.setOnAction(event -> onViewInventory());
        btnAlerts.setOnAction(event -> onAlerts());
        btnPurchaseOrders.setOnAction(event -> onPurchaseOrders());
        btnReports.setOnAction(event -> onReports());
        btnHistory.setOnAction(event -> onHistory());
        btnSignOut.setOnAction(event -> onSignOut());
    }

    private void onDashboard(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DASHBOARD);
    }

    private void onViewInventory(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.VIEW_INVENTORY);
    }

    private void onAlerts(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.ALERTS);
    }

    private void onPurchaseOrders(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.PURCHASE_ORDERS);
    }

    private void onReports(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.REPORTS);
    }

    private void onHistory(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.HISTORY);
    }

    private void onSignOut(){
        Stage stage = (Stage) btnDashboard.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().resetViewFactory();
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}
