package com.inventorymanagementsystem.Controllers;

import com.inventorymanagementsystem.Models.DataBaseManager;
import com.inventorymanagementsystem.Models.EncryptionUtils;
import com.inventorymanagementsystem.Models.Model;
import com.inventorymanagementsystem.Models.PreferenceKeys;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class DBConnectionController implements Initializable {
    public TextField txtHost, txtPort, txtUser;
    public ChoiceBox<String> choiceBoxAuth, choiceBoxSavePwd;
    public PasswordField password;
    public ComboBox<String> comboBoxDB;
    public Button btnTestConnection, btnContinue;
    public Label lblUser, lblPassword, lblSave, lblDataBase;
    private Preferences preferences = Preferences.userNodeForPackage(DBConnectionController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBoxAuth.getItems().addAll("User & Password", "No Authentication");
        choiceBoxAuth.setValue("User & Password");

        choiceBoxSavePwd.getItems().addAll("Forever", "Forget");
        choiceBoxSavePwd.setValue("Forever");

        loadCredentials();
        loadDatabaseNames();

        // Add listener to choiceBoxAuth to adjust UI when "No Authentication" is selected
        choiceBoxAuth.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("No Authentication".equals(newValue)) {
                // Move comboBoxDB down (adjust the Y axis)
                comboBoxDB.setLayoutY(175);

                txtUser.setVisible(false);
                password.setVisible(false);
                choiceBoxSavePwd.setVisible(false);
                lblUser.setText("Database: ");
                lblPassword.setVisible(false);
                lblSave.setVisible(false);
                lblDataBase.setVisible(false);
            } else {
                // Move comboBoxDB back to its original position
                comboBoxDB.setLayoutY(275);

                txtUser.setVisible(true);
                password.setVisible(true);
                choiceBoxSavePwd.setVisible(true);
                lblUser.setText("User: ");
                lblPassword.setVisible(true);
                lblSave.setVisible(true);
                lblDataBase.setVisible(true);
            }
        });
    }

    public void shouldShow() {
        Stage stage = (Stage) txtUser.getScene().getWindow();

        String Url = "jdbc:mysql://" + txtHost.getText() + ':' + txtPort.getText() + "/" + comboBoxDB.getValue();
        String username = (txtUser.isVisible()) ? txtUser.getText() : "";
        String pwd = (password.isVisible()) ? password.getText() : "";

        try{
            Connection connection = DriverManager.getConnection(Url, username, pwd);
            Model.getInstance().getViewFactory().closeStage(stage);
            Model.getInstance().getDataBaseDriver().setConnection(connection);

            if(doesUserExists(connection)){
                DataBaseManager.loadInfo();
                Model.getInstance().getViewFactory().loginWindow();
            }
            else{
                ensureTablesExist(connection);
                showSignUpWindow();
            }
        } catch (SQLException _) {
            LoginController.removeCredentials();
            stage.show();
        }
    }

    private void saveCredentials() {
        preferences.put(PreferenceKeys.DB_HOST.getKey(), txtHost.getText());
        preferences.put(PreferenceKeys.DB_PORT.getKey(), txtPort.getText());
        preferences.put(PreferenceKeys.DB_AUTH.getKey(), choiceBoxAuth.getValue());

        if (choiceBoxAuth.getValue().equals("User & Password")) {
            preferences.put(PreferenceKeys.DB_USERNAME.getKey(), txtUser.getText());

            try {
                // Encrypt the password before storing it
                String encryptedPassword = EncryptionUtils.encrypt(password.getText());
                preferences.put(PreferenceKeys.DB_PASSWORD.getKey(), encryptedPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }

            preferences.put(PreferenceKeys.DB_SAVE_PASSWORD.getKey(), choiceBoxSavePwd.getValue());
        } else {
            preferences.put(PreferenceKeys.DB_USERNAME.getKey(), "");
            preferences.put(PreferenceKeys.DB_PASSWORD.getKey(), "");
            preferences.put(PreferenceKeys.DB_SAVE_PASSWORD.getKey(), "");
        }

        preferences.put(PreferenceKeys.DB_DATABASE.getKey(), comboBoxDB.getValue());
    }

    private void clearPassword() {
        preferences.remove(PreferenceKeys.DB_PASSWORD.getKey());
    }

    private void loadCredentials() {
        String host = preferences.get(PreferenceKeys.DB_HOST.getKey(), "");
        String port = preferences.get(PreferenceKeys.DB_PORT.getKey(), "");
        String auth = preferences.get(PreferenceKeys.DB_AUTH.getKey(), "");
        String username = preferences.get(PreferenceKeys.DB_USERNAME.getKey(), "");
        String encryptedPwd = preferences.get(PreferenceKeys.DB_PASSWORD.getKey(), "");
        String savePass = preferences.get(PreferenceKeys.DB_SAVE_PASSWORD.getKey(), "");
        String database = preferences.get(PreferenceKeys.DB_DATABASE.getKey(), "");

        if (!host.isEmpty()) {
            txtHost.setText(host);
        }

        if (!port.isEmpty()) {
            txtPort.setText(port);
        }

        if (!auth.isEmpty()) {
            choiceBoxAuth.setValue(auth);
        }

        if (!username.isEmpty()) {
            txtUser.setText(username);
        }

        if (!encryptedPwd.isEmpty()) {
            try {
                // Decrypt the password before using it
                String decryptedPassword = EncryptionUtils.decrypt(encryptedPwd);
                password.setText(decryptedPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!savePass.isEmpty()) {
            choiceBoxSavePwd.setValue(savePass);
        }

        if (!database.isEmpty()) {
            comboBoxDB.setValue(database);
        }
    }

    private void loadDatabaseNames() {
        String currentVal = comboBoxDB.getValue();
        comboBoxDB.getItems().clear();
        comboBoxDB.setValue(currentVal);
        String url = "jdbc:mysql://" + txtHost.getText() + ':' + txtPort.getText() + "/";
        String username = (txtUser.isVisible()) ? txtUser.getText() : "";
        String pwd = (password.isVisible()) ? password.getText() : "";

        try (Connection connection = DriverManager.getConnection(url, username, pwd);
             ResultSet resultSet = connection.getMetaData().getCatalogs()) {

            while (resultSet.next()) {
                String dbName = resultSet.getString(1);

                if(!dbName.equals("sys") && !dbName.equals("information_schema") &&
                        !dbName.equals("performance_schema") && !dbName.equals("mysql")){
                    comboBoxDB.getItems().add(dbName); // Add database name to ComboBox
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testConnection(){
        String databaseName = (comboBoxDB.getValue() == null) ? "" : comboBoxDB.getValue();

        String url = "jdbc:mysql://" + txtHost.getText() + ':' + txtPort.getText() + "/" + databaseName;
        String username = (txtUser.isVisible()) ? txtUser.getText() : "";
        String pwd = (password.isVisible()) ? password.getText() : "";

        try(Connection connection = DriverManager.getConnection(url, username, pwd)){
            loadDatabaseNames();
            Model.getInstance().showAlert(Alert.AlertType.INFORMATION, "Connection Successful", "Connection to the database was successful");
        } catch (SQLException e) {
            Model.getInstance().showAlert(Alert.AlertType.ERROR, "Connection Unsuccessful", "Connection to the database was unsuccessful\n" + e.getMessage());
        }
    }

    public void handleContinue(){
        String url = "jdbc:mysql://" + txtHost.getText() + ':' + txtPort.getText() + "/" + comboBoxDB.getValue();
        String username = (txtUser.isVisible()) ? txtUser.getText() : "";
        String pwd = (password.isVisible()) ? password.getText() : "";

        try{
            Connection connection = DriverManager.getConnection(url, username, pwd);
            Model.getInstance().getDataBaseDriver().setConnection(connection);
            saveCredentials();

            if(!choiceBoxSavePwd.getValue().equals("Forever")) {
                clearPassword();
            }

            Stage stage = (Stage) txtUser.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
            ensureTablesExist(connection);
            DataBaseManager.loadInfo();

            if(doesUserExists(connection)){
                Model.getInstance().getViewFactory().showLoginWindow();
            }
            else{
                showSignUpWindow();
            }
        } catch (SQLException e) {
            url = "jdbc:mysql://" + txtHost.getText() + ':' + txtPort.getText() + "/";

            try{
                Connection connection = DriverManager.getConnection(url, username, pwd);
                Alert alert = Model.getInstance().getConfirmationDialogAlert("Connection Successful but database doesn't exist",
                        "The connection was successful but the database doesn't exist. Do you wish to create it?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.YES) {
                    createDatabase(connection, comboBoxDB.getValue());
                    url = "jdbc:mysql://" + txtHost.getText() + ':' + txtPort.getText() + "/" + comboBoxDB.getValue();
                    connection = DriverManager.getConnection(url, username, pwd);
                    Model.getInstance().getDataBaseDriver().setConnection(connection);
                    ensureTablesExist(connection);
                    Stage stage = (Stage) txtUser.getScene().getWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);
                    saveCredentials();

                    if(!choiceBoxSavePwd.getValue().equals("Forever")) {
                        clearPassword();
                    }

                    showSignUpWindow();
                }
            }catch(SQLException t){
                Model.getInstance().showAlert(Alert.AlertType.ERROR, "Connection Unsuccessful", "Connection to the database was unsuccessful\n" + e.getMessage());
            }}
    }



    private void ensureTablesExist(Connection connection) {
        // Array of table creation SQL statements
        String[] createTableStatements = {
                "CREATE TABLE IF NOT EXISTS Products (" +
                        "product_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "category VARCHAR(50) NULL, " +
                        "unit_price DECIMAL(10, 2) NOT NULL)",

                "CREATE TABLE IF NOT EXISTS Suppliers (" +
                        "supplier_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "contact_email VARCHAR(100) NULL, " +
                        "phone_number VARCHAR(20) NULL, " +
                        "address TEXT NULL)",

                "CREATE TABLE IF NOT EXISTS Batches (" +
                        "batch_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "product_id INT NOT NULL, " +
                        "current_stock INT NOT NULL, " +
                        "expiration_date DATE NOT NULL, " +
                        "supplier_id INT NULL, " +
                        "FOREIGN KEY (product_id) REFERENCES Products(product_id))",

                "CREATE TABLE IF NOT EXISTS InventoryAdjustments (" +
                        "adjustment_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "batch_id INT NOT NULL, " +
                        "adjustment_date DATE NOT NULL, " +
                        "adjustment_type ENUM('ADD', 'REMOVE') NOT NULL, " +
                        "quantity INT NOT NULL, " +
                        "reason TEXT NULL, " +
                        "FOREIGN KEY (batch_id) REFERENCES Batches(batch_id))",

                "CREATE TABLE IF NOT EXISTS PurchaseOrders (" +
                        "order_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "order_date DATE NOT NULL, " +
                        "supplier_id INT NULL, " +
                        "supplier_name VARCHAR(100) NOT NULL, " +
                        "product_name VARCHAR(100) NOT NULL, " +
                        "quantity INT NOT NULL, " +
                        "total_amount DECIMAL(10, 2) DEFAULT 0.00 NULL)",

                "CREATE TABLE IF NOT EXISTS Sales (" +
                        "sale_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "batch_id INT NOT NULL, " +
                        "sale_date DATE NOT NULL, " +
                        "quantity_sold INT NOT NULL, " +
                        "sale_price DECIMAL(10, 2) NULL, " +
                        "FOREIGN KEY (batch_id) REFERENCES Batches(batch_id))",

                "CREATE TABLE IF NOT EXISTS Users (" +
                        "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(50) NOT NULL, " +
                        "password_hash VARCHAR(255) NOT NULL, " +
                        "role ENUM('ADMIN', 'STAFF') DEFAULT 'ADMIN' NULL, " +
                        "email VARCHAR(100) NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL, " +
                        "UNIQUE (email))",

                "CREATE TABLE IF NOT EXISTS Admins (" +
                        "admin_id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "user_id INT NOT NULL UNIQUE, " +
                        "email_password_encrypted VARCHAR(255), " +
                        "FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE)"
        };

        String[] createIndexStatements = {
                "CREATE INDEX product_id_idx ON Batches (product_id)",
                "CREATE INDEX batch_id_idx ON InventoryAdjustments (batch_id)",
                "CREATE INDEX sales_batch_id_idx ON Sales (batch_id)"
        };

        try (Statement statement = connection.createStatement()) {
            // Create tables
            for (String sql : createTableStatements) {
                statement.execute(sql);
            }

            // Create indexes if they do not exist
            for (String indexSql : createIndexStatements) {
                try {
                    statement.execute(indexSql);
                } catch (SQLException e) {
                    // Ignore error if index already exists
                    if (!e.getMessage().contains("Duplicate key name")) {
                        e.printStackTrace(); // Log other errors
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showSignUpWindow(){
        Model.getInstance().getViewFactory().showSignUpWindow();
        Model.getInstance().showAlert(AlertType.INFORMATION, "First time creating a User account",
                """
                        Since there is no user registered in the system you will create an Account.
                        Make sure to remember the details, specifically the EMAIL and obviously the PASSWORD.
                        You will need them to login. THERE IS NO RECOVERY SYSTEM (As of now)
                        You can change the password later if you like (Not yet implemented)""");
    }

    public boolean doesUserExists(Connection connection) {
        String query = "SELECT COUNT(*) FROM Users";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()){

                if (resultSet.next()) {
                    int userCount = resultSet.getInt(1);
                    return userCount > 0;
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void createDatabase(Connection connection, String dbName) {
        String query = "CREATE DATABASE " + dbName;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            Model.getInstance().showAlert(AlertType.INFORMATION, "Database created Successfully", "Database created successfully!!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String generateSecretKey(int keyLength) {
        // Create a byte array for the secret key (keyLength bytes)
        byte[] secretKey = new byte[keyLength];

        // Use SecureRandom for cryptographic strength random numbers
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secretKey); // Fill the byte array with random bytes

        // Encode the key as a Base64 string for easy storage
        return Base64.getEncoder().encodeToString(secretKey);
    }
}
