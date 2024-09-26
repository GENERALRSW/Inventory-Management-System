package com.inventorymangementsystem.Controllers;

import com.inventorymangementsystem.Models.DataBaseManager;
import com.inventorymangementsystem.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public TextField txtEmail, txtVisiblePassword;
    @FXML
    public PasswordField pwdPassword;
    @FXML
    public Button btnLogin, btnDBConnection;;
    @FXML
    public Label lblError;
    @FXML
    public CheckBox chkPasswordVisible;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtVisiblePassword.setVisible(false);
    }

    public void login(){

    }

    public void passwordVisible(){
        if(chkPasswordVisible.isSelected()){
            txtVisiblePassword.setText(pwdPassword.getText());
            pwdPassword.setVisible(false);
            txtVisiblePassword.setVisible(true);
        }
        else{
            pwdPassword.setText(txtVisiblePassword.getText());
            pwdPassword.setVisible(true);
            txtVisiblePassword.setVisible(false);
        }
    }

    public void backToDBConnection(){
        DataBaseManager.removeInfo();
        Stage stage = (Stage) lblError.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showDataBaseConnectionWindow();
    }
}
