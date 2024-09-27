package com.inventorymanagementsystem.Controllers;

import com.inventorymanagementsystem.Models.Model;
import com.inventorymanagementsystem.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button btnDashboard, btnViewInventory, btnAlerts, btnPurchaseOrders, btnReports, btnHistory, btnSignOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        addListeners();
    }

    private void setIcons() {
        // Directly set Ikonli icons on buttons using FontAwesomeSolid icons
        btnDashboard.setGraphic(createFontIcon(FontAwesomeSolid.TACHOMETER_ALT, 24));
        btnViewInventory.setGraphic(createFontIcon(FontAwesomeSolid.BOXES, 24));
        btnAlerts.setGraphic(createFontIcon(FontAwesomeSolid.EXCLAMATION_TRIANGLE, 24));
        btnPurchaseOrders.setGraphic(createFontIcon(FontAwesomeSolid.CLIPBOARD_LIST, 24));
        btnReports.setGraphic(createFontIcon(FontAwesomeSolid.CHART_BAR, 24));
        btnHistory.setGraphic(createFontIcon(FontAwesomeSolid.HISTORY, 24));
        btnSignOut.setGraphic(createFontIcon(FontAwesomeSolid.SIGN_OUT_ALT, 20));
    }

    private FontIcon createFontIcon(FontAwesomeSolid iconType, int size) {
        FontIcon icon = new FontIcon(iconType);
        icon.setIconSize(size);  // Set size of the icon
        icon.getStyleClass().add("icon");  // Add the CSS class to the icon
        return icon;
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
