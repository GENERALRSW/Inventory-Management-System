package com.inventorymanagementsystem.Controllers.Admin;

import com.inventorymanagementsystem.Models.PurchaseOrder;
import com.inventorymanagementsystem.Models.Supplier;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PurchaseOrdersController implements Initializable{
    public TableView<PurchaseOrder> tableViewPurchaseOrders;
    public TableColumn<PurchaseOrder, Integer> columnOrderID;
    public TableColumn<PurchaseOrder, LocalDate> columnDate;
    public TableColumn<PurchaseOrder, Integer> columnSupplierID;
    public TableColumn<PurchaseOrder, String> columnSupplierName, columnProductName;
    public TableColumn<PurchaseOrder, Integer> columnQuantity;
    public TableColumn<PurchaseOrder, BigDecimal> columnTotalAmount;
    public TableColumn<PurchaseOrder, Void> columnDelete;

    public TableView<Supplier> tableViewSuppliers;
    public TableColumn<Supplier, Integer> columnSupplierID2;
    public TableColumn<Supplier, String> columnSupplierName2;

    public TextField txtSupplierName, txtSupplierEmail, txtQuantity, txtUnitPrice, txtTotalAmount, txtProductName;
    public TextField txtPurchaseOrderSearch, txtSupplierSearch;
    public Label lblQuantityError, lblUnitPriceError, lblSupplierID;
    public Button btnSendEmail;

    private final ObservableList<PurchaseOrder> purchaseOrdersList = PurchaseOrder.getList();
    private FilteredList<PurchaseOrder> filteredPurchaseOrdersList;

    private final ObservableList<Supplier> suppliersList = Supplier.getList();
    private FilteredList<Supplier> filteredSuppliersList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnOrderID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnDate.setCellValueFactory(cellData -> cellData.getValue().orderDateProperty());
        columnSupplierID.setCellValueFactory(cellData -> cellData.getValue().supplierIdProperty().asObject());
        columnSupplierName.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        columnProductName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        columnQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        columnTotalAmount.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty());

        setupDeleteColumn();

        columnSupplierID2.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnSupplierName2.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        filteredSuppliersList = new FilteredList<>(suppliersList, p -> true);
        tableViewSuppliers.setItems(filteredSuppliersList);
        txtSupplierSearch.textProperty().addListener((observable, oldValue, newValue) -> filterSupplierList());
        tableViewSuppliers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> populateSupplierFields(newValue));

        filteredPurchaseOrdersList = new FilteredList<>(purchaseOrdersList, p -> true);
        tableViewPurchaseOrders.setItems(filteredPurchaseOrdersList);
        txtPurchaseOrderSearch.textProperty().addListener((observable, oldValue, newValue) -> filterPurchaseOrderList());
    }

    private void filterSupplierList() {
        String searchText = txtSupplierSearch.getText().toLowerCase().trim();

        filteredSuppliersList.setPredicate(supplier -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return String.valueOf(supplier.ID).contains(searchText)
                    || supplier.getName().toLowerCase().contains(searchText)
                    || supplier.getContactEmail().toLowerCase().contains(searchText);
        });
    }

    private void setupDeleteColumn() {
        // Set a cell factory for the delete column
        columnDelete.setCellFactory(new Callback<>() {
            @Override
            public TableCell<PurchaseOrder, Void> call(final TableColumn<PurchaseOrder, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button();

                    {
                        // Set the Ikonli trash icon for the delete button
                        FontIcon deleteIcon = new FontIcon(FontAwesomeSolid.TRASH);
                        deleteIcon.setIconSize(16);
                        deleteButton.setGraphic(deleteIcon);

                        // Add delete action
                        deleteButton.setOnAction(event -> {
                            PurchaseOrder selectedPurchaseOrder = getTableView().getItems().get(getIndex());
                            deletePurchaseOrder(selectedPurchaseOrder);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });
    }

    private void filterPurchaseOrderList(){
        String searchText = txtPurchaseOrderSearch.getText().toLowerCase().trim();

        filteredPurchaseOrdersList.setPredicate(purchaseOrder -> {
            if (searchText.isEmpty()) {
                return true;
            }

            return String.valueOf(purchaseOrder.ID).contains(searchText)
                    || purchaseOrder.getOrderDate().toString().contains(searchText)
                    || String.valueOf(purchaseOrder.getSupplierId()).contains(searchText)
                    || purchaseOrder.getSupplierName().toLowerCase().contains(searchText)
                    || purchaseOrder.getProductName().toLowerCase().contains(searchText)
                    || String.valueOf(purchaseOrder.getQuantity()).contains(searchText)
                    || purchaseOrder.getTotalAmount().toString().contains(searchText);
        });
    }

    // Method to delete a PurchaseOrder from the list and the table
    private void deletePurchaseOrder(PurchaseOrder purchaseOrder) {
        tableViewPurchaseOrders.getItems().remove(purchaseOrder);
    }

    public void populateSupplierFields(Supplier supplier){
        if (supplier != null) {
            txtSupplierName.setText(supplier.getName());
            txtSupplierEmail.setText(supplier.getContactEmail());
            lblSupplierID.setText("Supplier ID: " + supplier.ID);
        }
    }

}