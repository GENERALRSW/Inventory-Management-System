package com.inventorymangementsystem.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public Button btnSignUp, btnDBConnection;
    public TextField txtName, txtEmail, txtPassword, txtConfirmPassword;
    public Label lblNameError, lblEmailError, lblError;
    public CheckBox chkPasswordVisible;
    public PasswordField pwdPassword, pwdConfirmPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void signUp(){

    }

    public void backToDBConnection(ActionEvent actionEvent) {

    }

    public void passwordVisible(ActionEvent actionEvent) {

    }
}
