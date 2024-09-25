package com.inventorymangementsystem.Controllers;

import com.inventorymangementsystem.Models.Model;
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
                        //case ADD_INSTRUCTOR -> adminParent.setCenter(Model.getInstance().getViewFactory().getAddInstructorView());
                        //case ADD_COURSE -> adminParent.setCenter(Model.getInstance().getViewFactory().getAddCoursesView());
                        default -> adminParent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                    }
                }
        );
    }
}
