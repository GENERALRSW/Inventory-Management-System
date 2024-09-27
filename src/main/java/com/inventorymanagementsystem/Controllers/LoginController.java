package com.inventorymanagementsystem.Controllers;

import com.inventorymanagementsystem.Models.DataBaseManager;
import com.inventorymanagementsystem.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import javafx.scene.control.*;

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
        String email = txtEmail.getText();
        String password = (pwdPassword.isVisible()) ? pwdPassword.getText() : txtVisiblePassword.getText();

        if(email.isEmpty() || password.isEmpty()){
            lblError.setText("Text field/s cannot be empty");
        }
        else if(DataBaseManager.emailExists(email)){
            if(DataBaseManager.validUser(email, password)){
                Model.getInstance().setUser(DataBaseManager.getUser(email));
                String role = Model.getInstance().getUser().getRole();
                System.out.println("User was found in the database: " + role);
                lblError.setText("");
                Stage stage = (Stage) lblError.getScene().getWindow();
                Model.getInstance().getViewFactory().closeStage(stage);

                if(role.equals("ADMIN")){
                    Model.getInstance().getViewFactory().showAdminWindow();
                }
                else{
                    Model.getInstance().showAlert(AlertType.ERROR, "There is no User other than Admin", "How did this happen?????");
                    System.exit(1);
                }
            }
            else{
                lblError.setText("Password is incorrect");
            }
        }
        else{
            lblError.setText("Email does not exist");
        }
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
