package com.inventorymanagementsystem.Controllers;

import com.inventorymanagementsystem.Models.DataBaseManager;
import com.inventorymanagementsystem.Models.Model;
import com.inventorymanagementsystem.Models.PreferenceKeys;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import javafx.scene.control.*;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LoginController implements Initializable {
    @FXML
    public TextField txtEmail, txtVisiblePassword;
    @FXML
    public PasswordField pwdPassword;
    @FXML
    public Button btnLogin, btnDBConnection, btnVisible;
    @FXML
    public Label lblError;
    @FXML
    public CheckBox chkSaveCredentials;

    private final FontIcon visibleIcon = createFontIcon(FontAwesomeSolid.EYE);
    private final FontIcon invisibleIcon = createFontIcon(FontAwesomeSolid.EYE_SLASH);
    private static final Preferences preferences = Preferences.userNodeForPackage(LoginController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnVisible.setGraphic(visibleIcon);

        txtVisiblePassword.setManaged(false);  // Makes it invisible to layout
        txtVisiblePassword.setVisible(false);

        // Bind the text property of the password field and the visible text field
        txtVisiblePassword.textProperty().bindBidirectional(pwdPassword.textProperty());

        btnVisible.setOnAction(event -> togglePasswordVisibility());
    }

    private FontIcon createFontIcon(FontAwesomeSolid iconType) {
        FontIcon icon = new FontIcon(iconType);
        icon.getStyleClass().add("icon");  // Add the CSS class to the icon
        return icon;
    }

    public void login(){
        String email = txtEmail.getText();
        String password = (pwdPassword.isVisible()) ? pwdPassword.getText() : txtVisiblePassword.getText();

        if(email.isEmpty() || password.isEmpty()){
            lblError.setText("Text field/s cannot be empty");
        }
        else if(DataBaseManager.userEmailExists(email)){
            if(DataBaseManager.validUser(email, password)){
                Model.getInstance().setUser(DataBaseManager.getUser(email));
                String role = Model.getInstance().getUser().getRole();
                System.out.println("User was found in the database: " + role);
                lblError.setText("");
                Stage stage = (Stage) lblError.getScene().getWindow();
                showAdminWindow(email, password, stage, role);
            }
            else{
                lblError.setText("Password is incorrect");
            }
        }
        else{
            lblError.setText("Email does not exist");
        }
    }

    private void togglePasswordVisibility() {
        if (txtVisiblePassword.isVisible()) {
            btnVisible.setGraphic(visibleIcon);

            txtVisiblePassword.setManaged(false);
            txtVisiblePassword.setVisible(false);
            pwdPassword.setManaged(true);
            pwdPassword.setVisible(true);
        }
        else {
            btnVisible.setGraphic(invisibleIcon);

            txtVisiblePassword.setManaged(true);
            txtVisiblePassword.setVisible(true);
            pwdPassword.setManaged(false);
            pwdPassword.setVisible(false);
        }
    }

    public void backToDBConnection(){
        DataBaseManager.removeInfo();
        Stage stage = (Stage) lblError.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showDataBaseConnectionWindow();
    }

    public static void removeCredentials(){
        preferences.put(PreferenceKeys.USER_EMAIL.getKey(), "");
        preferences.put(PreferenceKeys.USER_PASSWORD.getKey(), "");
    }

    public void shouldShow() {
        String email = preferences.get(PreferenceKeys.USER_EMAIL.getKey(), "");
        String password = preferences.get(PreferenceKeys.USER_PASSWORD.getKey(), "");
        Stage stage = (Stage) lblError.getScene().getWindow();

        if(DataBaseManager.userEmailExists(email) && DataBaseManager.validUser(email, password)){
            Model.getInstance().setUser(DataBaseManager.getUser(email));
            String role = Model.getInstance().getUser().getRole();
            System.out.println("User was found in the database: " + role);
            lblError.setText("");
            showAdminWindow(email, password, stage, role);
        }
        else{
            removeCredentials();
            stage.show();
        }


    }

    private void showAdminWindow(String email, String password, Stage stage, String role) {
        Model.getInstance().getViewFactory().closeStage(stage);

        if(role.equals("ADMIN")){
            if(chkSaveCredentials.isSelected()){
                preferences.put(PreferenceKeys.USER_EMAIL.getKey(), email);
                preferences.put(PreferenceKeys.USER_PASSWORD.getKey(), password);
            }

            Model.getInstance().getViewFactory().showAdminWindow();
        }
        else{
            Model.getInstance().showAlert(AlertType.ERROR, "There is no User other than Admin", "How did this happen?????");
            System.exit(1);
        }
    }
}
