package com.inventorymanagementsystem.Controllers;

import com.inventorymanagementsystem.Models.Batch;
import com.inventorymanagementsystem.Models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InventoryBatchesController implements Initializable {
    public TableView<Product> tableViewProducts;
    public TableColumn<Product, Integer> columnProductId;
    public TableColumn<Product, String> columnProductName;
    public TableColumn<Product, BigDecimal> columnUnitPrice;

    public TableView<Batch> tableViewBatches;
    public TableColumn<Batch, Integer> columnBatchId;
    public TableColumn<Batch, Integer> columnCurrentStock;
    public TableColumn<Batch, LocalDate> columnExpirationDate;

    public TextField txtProductSearch, txtBatchesSearch, txtProductName, txtUnitPrice, txtStockAmount,
            txtLowStockAmount;
    public Button btnBatchCommand, btnClearSelection;
    public DatePicker datePickerExpirationDate;
    public ComboBox<String> comboBoxBatchCommands;
    public Label lblProductId, lblBatchId, lblExpirationDateError, lblStockAmountError, lblLowStockAlertError;

    private final ObservableList<Product> productList = Product.getList();
    private FilteredList<Product> filteredProductList;

    private final ObservableList<Batch> batchList = Batch.getList();
    private FilteredList<Batch> filteredBatchList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblBatchId.setVisible(false);
        btnClearSelection.setDisable(true);
        btnBatchCommand.setDisable(true);

        ObservableList<String> staffCommandOptions = FXCollections.observableArrayList("Add", "Update", "Delete");
        comboBoxBatchCommands.setItems(staffCommandOptions);
        comboBoxBatchCommands.setValue("Add");

        txtStockAmount.setOnKeyReleased(this::handleStockAmountKeyReleased);
        datePickerExpirationDate.getEditor().setOnKeyReleased(this::handleExpirationDateKeyReleased);
        txtLowStockAmount.setOnKeyReleased(this::handleLowStockAmountKeyReleased);

        txtStockAmount.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        datePickerExpirationDate.getEditor().textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        txtLowStockAmount.textProperty().addListener((observable, oldValue, newValue) -> validateFields());

        columnProductId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnProductName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnUnitPrice.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty());

        columnBatchId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnCurrentStock.setCellValueFactory(cellData -> cellData.getValue().currentStockProperty().asObject());
        columnExpirationDate.setCellValueFactory(cellData -> cellData.getValue().expirationDateProperty());

        filteredProductList = new FilteredList<>(productList, p -> true);
        tableViewProducts.setItems(filteredProductList);
        txtProductSearch.textProperty().addListener((observable, oldValue, newValue) -> filterProductList());
        tableViewProducts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> populateProductFields(newValue));

        filteredBatchList = new FilteredList<>(batchList, p -> true);
        tableViewBatches.setItems(filteredBatchList);
        txtBatchesSearch.textProperty().addListener((observable, oldValue, newValue) -> filterBatchList());
        tableViewBatches.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> populateFields(newValue));

        comboBoxBatchCommands.valueProperty().addListener((observable, oldValue, newValue) -> updateBatchCommand(newValue));
    }

    public void filterProductList(){
        String searchText = txtProductSearch.getText().toLowerCase().trim();

        filteredProductList.setPredicate(product -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return String.valueOf(product.ID).contains(searchText)
                    || product.getName().toLowerCase().contains(searchText)
                    || product.getCategory().toLowerCase().contains(searchText)
                    || String.valueOf(product.getUnitPrice()).contains(searchText);
        });
    }

    public void filterBatchList(){
        String searchText = txtBatchesSearch.getText().toLowerCase().trim();

        filteredBatchList.setPredicate(batch -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return String.valueOf(batch.ID).contains(searchText)
                    || String.valueOf(batch.getCurrentStock()).contains(searchText)
                    || String.valueOf(batch.getExpirationDate()).contains(searchText);
        });
    }

    private void handleStockAmountKeyReleased(KeyEvent keyEvent) {

    }

    private void handleExpirationDateKeyReleased(KeyEvent keyEvent) {

    }

    private void handleLowStockAmountKeyReleased(KeyEvent keyEvent) {

    }

    private void updateBatchCommand(String command) {

    }

    private void validateFields() {
        if (comboBoxBatchCommands.getValue().equals("Add")) {
            btnBatchCommand.setDisable(!allFieldsValidForAdd());
        }
        else if (comboBoxBatchCommands.getValue().equals("Update")) {
            btnBatchCommand.setDisable(!allFieldsValidForUpdate());
        }
        else{
            btnBatchCommand.setDisable(!allFieldsValidForDelete());
        }
    }

    private boolean allFieldsValidForAdd() {
        return false;
    }

    private boolean allFieldsValidForUpdate() {
        return false;
    }

    private boolean allFieldsValidForDelete() {
        return false;
    }

    public void addBatch(){

    }

    public void updateBatch(){

    }

    public void deleteBatch(){

    }

    public void clearSelection(){

    }

    private void populateProductFields(Product product) {
        if (product != null) {
            txtProductName.setText(product.getName());
            txtUnitPrice.setText(String.valueOf(product.getUnitPrice()));
            lblProductId.setText("Product ID: " + product.ID);
        }
        else{
            txtProductName.setText("");
            txtUnitPrice.setText("");
            lblProductId.setText("Product ID: ");
        }
    }

    private void populateFields(Batch batch) {
        if (batch != null) {
            txtStockAmount.setText(String.valueOf(batch.getCurrentStock()));
            datePickerExpirationDate.setValue(batch.getExpirationDate());
            //txtLowStockAmount.setText(String.valueOf(batch.getLow()));
            lblBatchId.setText("Product ID: " + batch.ID);
        }
        else{
            txtStockAmount.setText("");
            datePickerExpirationDate.setValue(null);
            txtLowStockAmount.setText("");
            lblBatchId.setText("Product ID: ");
        }
    }
}
