package com.inventorymanagementsystem.Controllers.Admin;

import com.inventorymanagementsystem.Controllers.LoginController;
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
    public Button btnViewInventory, btnAlerts, btnManageStaff, btnSuppliers,
            btnPurchaseOrders, btnReports, btnHistory, btnAccount, btnSignOut;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIcons();
        addListeners();
    }

    private void setIcons() {
        // Directly set Ikonli icons on buttons using FontAwesomeSolid icons
        btnViewInventory.setGraphic(createFontIcon(FontAwesomeSolid.BOXES, 24));
        btnAlerts.setGraphic(createFontIcon(FontAwesomeSolid.EXCLAMATION_TRIANGLE, 24));
        btnManageStaff.setGraphic(createFontIcon(FontAwesomeSolid.USER_COG, 24));  // Icon for Manage Staff
        btnSuppliers.setGraphic(createFontIcon(FontAwesomeSolid.TRUCK, 24));
        btnPurchaseOrders.setGraphic(createFontIcon(FontAwesomeSolid.CLIPBOARD_LIST, 24));
        btnReports.setGraphic(createFontIcon(FontAwesomeSolid.CHART_BAR, 24));
        btnHistory.setGraphic(createFontIcon(FontAwesomeSolid.HISTORY, 24));
        btnAccount.setGraphic(createFontIcon(FontAwesomeSolid.USER_CIRCLE, 24));  // Icon for Account
        btnSignOut.setGraphic(createFontIcon(FontAwesomeSolid.SIGN_OUT_ALT, 20));
    }

    private FontIcon createFontIcon(FontAwesomeSolid iconType, int size) {
        FontIcon icon = new FontIcon(iconType);
        icon.setIconSize(size);  // Set size of the icon
        icon.getStyleClass().add("icon");  // Add the CSS class to the icon
        return icon;
    }

    private void addListeners(){
        btnViewInventory.setOnAction(event -> onViewInventory());
        btnAlerts.setOnAction(event -> onAlerts());
        btnManageStaff.setOnAction(event -> onManageStaff());
        btnSuppliers.setOnAction(event -> onSuppliers());
        btnPurchaseOrders.setOnAction(event -> onPurchaseOrders());
        btnReports.setOnAction(event -> onReports());
        btnHistory.setOnAction(event -> onHistory());
        btnAccount.setOnAction(event -> onAccount());
        btnSignOut.setOnAction(event -> onSignOut());
    }

    private void onViewInventory(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.VIEW_INVENTORY);
    }

    private void onAlerts(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.ALERTS);
    }

    private void onManageStaff(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.MANAGE_STAFF);
    }

    private void onSuppliers(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.SUPPLIERS);
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

    private void onAccount(){
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.ACCOUNT);
    }

    private void onSignOut(){
        Stage stage = (Stage) btnViewInventory.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().resetViewFactory();
        LoginController.removeCredentials();
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}
