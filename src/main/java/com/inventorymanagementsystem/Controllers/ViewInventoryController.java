package com.inventorymanagementsystem.Controllers;

import com.inventorymanagementsystem.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewInventoryController implements Initializable {
    public TableView<Product> tableViewInventory;
    public TableColumn<Product, Integer> columnProductID;
    public TableColumn<Product, String> columnName;
    public TableColumn<Product, String> columnCategory;
    public TableColumn<Product, BigDecimal> columnUnitPrice;
    public TableColumn<Product, Integer> columnStockCount;
    public TextField txtInventorySearch, txtProductName, txtUnitPrice;
    public Button btnInventoryCommand, btnClearFields, btnClearSelection;
    public ComboBox<String> comboBoxInventoryCommand, comboBoxCategory;
    public Label lblProductID, lblNameError, lblCategoryError, lblUnitPriceError, lblProductCommandError;

    private final ObservableList<Product> productList = Product.getList();
    private FilteredList<Product> filteredProductList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblProductID.setVisible(false);
        btnClearSelection.setDisable(true);
        btnInventoryCommand.setDisable(true);
        comboBoxCategory.setItems(Product.getCategoryList());

        ObservableList<String> staffCommandOptions = FXCollections.observableArrayList("Add", "Update", "Delete");
        comboBoxInventoryCommand.setItems(staffCommandOptions);
        comboBoxInventoryCommand.setValue("Add");

        txtProductName.setOnKeyReleased(this::handleProductNameKeyReleased);
        comboBoxCategory.getEditor().setOnKeyReleased(this::handleCategoryKeyReleased);
        txtUnitPrice.setOnKeyReleased(this::handleUnitPriceKeyReleased);

        txtProductName.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        comboBoxCategory.getEditor().textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        txtUnitPrice.textProperty().addListener((observable, oldValue, newValue) -> validateFields());

        // Initialize columns
        columnProductID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        columnUnitPrice.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty());
        columnStockCount.setCellValueFactory(cellData -> cellData.getValue().stockCountProperty().asObject());

        filteredProductList = new FilteredList<>(productList, p -> true);

        // Set the filtered list as the data source for the table
        tableViewInventory.setItems(filteredProductList);

        // Add listeners for filtering
        txtInventorySearch.textProperty().addListener((observable, oldValue, newValue) -> filterInventoryList());

        // Add listener to table selection
        tableViewInventory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> populateFields(newValue));

        comboBoxInventoryCommand.valueProperty().addListener((observable, oldValue, newValue) -> updateInventoryCommand(newValue));
    }

    public void filterInventoryList(){
        String searchText = txtInventorySearch.getText().toLowerCase().trim();

        filteredProductList.setPredicate(product -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return String.valueOf(product.ID).contains(searchText)
                    || product.getName().toLowerCase().contains(searchText)
                    || product.getCategory().toLowerCase().contains(searchText)
                    || String.valueOf(product.getUnitPrice()).contains(searchText)
                    || String.valueOf(product.getStockCount()).contains(searchText);
        });
    }

    private void handleProductNameKeyReleased(KeyEvent keyEvent) {
        String productName = txtProductName.getText();

        if(Product.containsName(productName)){
            lblNameError.setText("Product Name is already taken");
        }
        else{
            lblNameError.setText("");
        }

        validateFields();
    }

    private void handleProductNameUpdateKeyReleased(KeyEvent keyEvent) {
        if(!isInventorySelected()){
            return;
        }

        Product product = tableViewInventory.getSelectionModel().getSelectedItem();
        String productName = txtProductName.getText();

        if(Product.containsName(productName) && !product.getName().equals(productName)){
            lblNameError.setText("Product Name is already taken");
        }
        else{
            lblNameError.setText("");
        }

        validateFields();
    }

    private void handleCategoryKeyReleased(KeyEvent keyEvent) {
        validateFields();
    }

    private void handleUnitPriceKeyReleased(KeyEvent keyEvent) {
        String unitPrice = txtUnitPrice.getText();

        if(Product.isValidUnitPrice(unitPrice)){
            lblUnitPriceError.setText("");
        }
        else{
            lblUnitPriceError.setText("Invalid unit price");
        }

        validateFields();
    }

    private boolean isInventorySelected() {
        return tableViewInventory.getSelectionModel().getSelectedItem() != null;
    }

    private void updateInventoryCommand(String command) {
        Product product = null;

        if(isInventorySelected()){
            product = tableViewInventory.getSelectionModel().getSelectedItem();
        }

        switch (command) {
            case "Add":
                btnInventoryCommand.setText("Add Product");
                btnInventoryCommand.setOnAction(event -> addProduct());
                lblProductID.setVisible(false);
                txtProductName.setOnKeyReleased(this::handleProductNameKeyReleased);

                if(Product.containsName(product.getName())){
                    lblNameError.setText("Product Name is already taken");
                }
                else{
                    lblNameError.setText("");
                }

                validateFields();
                break;
            case "Update":
                btnInventoryCommand.setText("Update Product");
                btnInventoryCommand.setOnAction(event -> updateProduct());
                lblProductID.setVisible(true);
                txtProductName.setOnKeyReleased(this::handleProductNameUpdateKeyReleased);

                if(Product.containsName(txtProductName.getText()) && !product.getName().equals(txtProductName.getText())){
                    lblNameError.setText("Product Name is already taken");
                }
                else{
                    lblNameError.setText("");
                }

                validateFields();
                break;
            case "Delete":
                btnInventoryCommand.setText("Delete Product");
                btnInventoryCommand.setOnAction(event -> deleteProduct());
                lblProductID.setVisible(true);
                txtProductName.setOnKeyReleased(this::handleProductNameUpdateKeyReleased);

                if(Product.containsName(txtProductName.getText()) && !product.getName().equals(txtProductName.getText())){
                    lblNameError.setText("Product Name is already taken");
                }
                else{
                    lblNameError.setText("");
                }

                validateFields();
                break;
        }
    }

    public void validateFields(){
        if (comboBoxInventoryCommand.getValue().equals("Add")) {
            btnInventoryCommand.setDisable(!allFieldsValidForAdd());
        }
        else if (comboBoxInventoryCommand.getValue().equals("Update")) {
            btnInventoryCommand.setDisable(!allFieldsValidForUpdate());
        }
        else{
            btnInventoryCommand.setDisable(!allFieldsValidForDelete());
        }
    }

    private boolean allFieldsValidForAdd() {
        return !Product.containsName(txtProductName.getText())
                && !comboBoxCategory.getEditor().getText().isEmpty()
                && !txtUnitPrice.getText().isEmpty()
                && lblNameError.getText().isEmpty()
                && lblUnitPriceError.getText().isEmpty();
    }

    private boolean allFieldsValidForUpdate() {
        if(!isInventorySelected()){
            return false;
        }

        Product product = tableViewInventory.getSelectionModel().getSelectedItem();

        String productName = product.getName();
        String category = product.getCategory();
        String unitPrice = String.valueOf(product.getUnitPrice());

        if (txtProductName.getText().equals(productName) && comboBoxCategory.getEditor().getText().equals(category)
                && txtUnitPrice.getText().equals(unitPrice)) {
            System.out.println("No fields have been changed.");
            return false;
        }

        return !(Product.containsName(txtProductName.getText()) && !productName.equals(txtProductName.getText()))
                && lblNameError.getText().isEmpty()
                && lblUnitPriceError.getText().isEmpty();
    }

    private boolean allFieldsValidForDelete() {
        if(!isInventorySelected()){
            return false;
        }

        Product product = tableViewInventory.getSelectionModel().getSelectedItem();

        String name = product.getName();
        String category = product.getCategory();
        String unitPrice = String.valueOf(product.getUnitPrice());

        return txtProductName.getText().equals(name) && comboBoxCategory.getEditor().getText().equals(category)
                && txtUnitPrice.getText().equals(unitPrice);
    }


    public void addProduct() {
        String name = txtProductName.getText();
        String category = comboBoxCategory.getValue();
        BigDecimal unitPrice = BigDecimal.valueOf(Float.parseFloat(txtUnitPrice.getText())).setScale(2, RoundingMode.HALF_UP);
        DataBaseManager.addProduct(name, category, unitPrice);
        int id = Product.getLastAddedProductID();
        DataBaseManager.addInventoryAdjustment(
                id,
                name,
                -1,
                LocalDate.now(),
                "ADDITION",
                -1,
                -1
        );

        if(btnClearSelection.isDisable()){
            clearFields();
        }
        else{
            clearSelection();
        }

        Model.getInstance().showAlert(Alert.AlertType.INFORMATION, "Added Product",
                "Product with ID: " + id + " has been added.");
    }

    public void updateProduct(){
        Product product = tableViewInventory.getSelectionModel().getSelectedItem();
        String name = txtProductName.getText();
        String category = comboBoxCategory.getValue();
        BigDecimal unitPrice = BigDecimal.valueOf(Float.parseFloat(txtUnitPrice.getText()))
                .setScale(2, RoundingMode.HALF_UP);
        int stockCount = product.getStockCount();

        DataBaseManager.updateProduct(product, name, category, unitPrice);
        DataBaseManager.addInventoryAdjustment(
                product.ID,
                name,
                -1,
                LocalDate.now(),
                "UPDATE",
                -1,
                -1
        );

        tableViewInventory.refresh();
        validateFields();
        Model.getInstance().showAlert(Alert.AlertType.INFORMATION, "Updated Product",
                "Product with ID: " + product.ID + " has been updated.");
    }

    public void deleteProduct(){
        Product product = tableViewInventory.getSelectionModel().getSelectedItem();
        Alert alert = Model.getInstance().getConfirmationDialogAlert("Delete Product?",
                "Are you sure you want to delete this Product?\nProduct ID: " + product.ID);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            DataBaseManager.deleteProduct(product);
            DataBaseManager.addInventoryAdjustment(
                    product.ID,
                    product.getName(),
                    -1,
                    LocalDate.now(),
                    "DELETION",
                    -1,
                    -1
            );

            if(!Product.contains(product.ID)){
                clearSelection();
                Model.getInstance().showAlert(Alert.AlertType.INFORMATION, "Deleted Product",
                        "Product with ID: " + product.ID + " has been deleted.");
            }
        }
    }

    public void clearFields() {
        txtProductName.setText("");
        comboBoxCategory.setValue("");
        txtUnitPrice.setText("");
    }

    public void clearSelection() {
        tableViewInventory.getSelectionModel().clearSelection();
        txtProductName.setText("");
        comboBoxCategory.setValue("");
        txtUnitPrice.setText("");
        btnClearSelection.setDisable(true);

        lblProductID.setText("Product ID: ");
    }

    private void populateFields(Product product) {
        if (product != null) {
            txtProductName.setText(product.getName());
            comboBoxCategory.setValue(product.getCategory());
            txtUnitPrice.setText(String.valueOf(product.getUnitPrice()));
            lblProductID.setText("Product ID: " + product.ID);
        }
        else{
            txtProductName.setText("");
            comboBoxCategory.setValue("");
            txtUnitPrice.setText("");
            lblProductID.setText("Product ID: ");
        }
    }
}
