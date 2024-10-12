package com.inventorymanagementsystem.Controllers;

import com.inventorymanagementsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane adminParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener(
                (observableValue, oldVal, newVal) -> {
                    switch(newVal){
                        case VIEW_INVENTORY -> adminParent.setCenter(Model.getInstance().getViewFactory().getViewInventoryView());
                        case ALERTS -> adminParent.setCenter(Model.getInstance().getViewFactory().getAlertsView());
                        case SUPPLIERS -> adminParent.setCenter(Model.getInstance().getViewFactory().getSuppliersView());
                        case PURCHASE_ORDERS -> adminParent.setCenter(Model.getInstance().getViewFactory().getPurchaseOrdersView());
                        case REPORTS -> adminParent.setCenter(Model.getInstance().getViewFactory().getReportsView());
                        case HISTORY -> adminParent.setCenter(Model.getInstance().getViewFactory().getHistoryView());
                        default -> adminParent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                    }
                }
        );
    }
}
