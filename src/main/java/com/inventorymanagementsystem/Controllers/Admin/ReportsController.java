package com.inventorymanagementsystem.Controllers.Admin;

import com.inventorymanagementsystem.Models.Model;
import com.inventorymanagementsystem.Models.Sale;
import com.lowagie.text.pdf.PdfPCell;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ReportsController implements Initializable {
    public TableView<Sale> tableViewSales;
    public TableColumn<Sale, Integer> columnSaleId;
    public TableColumn<Sale, Integer> columnProductId;
    public TableColumn<Sale, String> columnProductName;
    public TableColumn<Sale, LocalDate> columnSaleDate;
    public TableColumn<Sale, Integer> columnQuantitySold;
    public TableColumn<Sale, BigDecimal> columnSalePrice;
    public DatePicker datePickerStart, datePickerEnd;
    public Button btnGeneratePDF;
    public Label lblStartDateError, lblEndDateError;

    private final ObservableList<Sale> saleList = Sale.getList();
    private FilteredList<Sale> filteredSaleList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePickerStart.getEditor().setOnKeyReleased(this::handleStartDateKeyReleased);
        datePickerEnd.getEditor().setOnKeyReleased(this::handleEndDateKeyReleased);

        datePickerStart.getEditor().textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        datePickerEnd.getEditor().textProperty().addListener((observable, oldValue, newValue) -> validateFields());

        columnSaleId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        columnProductId.setCellValueFactory(cellData -> cellData.getValue().productIdProperty().asObject());
        columnProductName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        columnSaleDate.setCellValueFactory(cellData -> cellData.getValue().saleDateProperty());
        columnQuantitySold.setCellValueFactory(cellData -> cellData.getValue().quantitySoldProperty().asObject());
        columnSalePrice.setCellValueFactory(cellData -> cellData.getValue().salePriceProperty());

        filteredSaleList = new FilteredList<>(saleList, p -> true);
        tableViewSales.setItems(filteredSaleList);

        datePickerStart.valueProperty().addListener((observable, oldValue, newValue) -> filterSalesByDate());
        datePickerEnd.valueProperty().addListener((observable, oldValue, newValue) -> filterSalesByDate());
    }

    private void filterSalesByDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = datePickerStart.getValue();
        LocalDate endDate = datePickerEnd.getValue();

        filteredSaleList.setPredicate(sale -> {
            if (startDate == null && endDate == null) {
                return true;
            }

            if (startDate != null && endDate == null) {
                return !sale.getSaleDate().isBefore(startDate);
            }

            if (startDate == null) {
                return !sale.getSaleDate().isAfter(endDate);
            }

            return !sale.getSaleDate().isBefore(startDate) && !sale.getSaleDate().isAfter(endDate);
        });
    }

    private void handleStartDateKeyReleased(KeyEvent keyEvent) {
        handleDateError(datePickerStart, lblStartDateError);
    }

    private void handleEndDateKeyReleased(KeyEvent keyEvent) {
        handleDateError(datePickerEnd, lblEndDateError);
    }

    private void handleDateError(DatePicker datePicker, Label lblDateError) {
        String dateStr = datePicker.getEditor().getText();

        if(dateStr.isEmpty()){
            lblDateError.setText("");
            validateFields();
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            datePicker.setValue(date);
            lblDateError.setText("");
        } catch (DateTimeException e) {
            System.err.println("Error: " + e.getMessage());
            lblDateError.setText("Not a valid date.");
        }

        validateFields();
    }

    private void validateFields() {
        if(!lblStartDateError.getText().isEmpty() || !lblEndDateError.getText().isEmpty()){
            btnGeneratePDF.setDisable(true);
        }
        else{
            if(datePickerStart.getEditor().getText().isEmpty() || datePickerEnd.getEditor().getText().isEmpty()){
                btnGeneratePDF.setDisable(false);
            }
        }
    }

    public void generatePDF() {
        if (tableViewSales.getItems().isEmpty()) {
            Model.getInstance().showAlert(Alert.AlertType.ERROR, "TableView is Empty.",
                    "The TableView is Empty. Therefore, there is no sales report to generate.");
            return;
        }

        LocalDate startDate = datePickerStart.getValue();
        LocalDate endDate = datePickerEnd.getValue();

        if (startDate != null && endDate != null && startDate.isAfter(endDate) && !startDate.isEqual(endDate)) {
            Model.getInstance().showAlert(Alert.AlertType.ERROR, "Start Date is After End Date.",
                    "The Start Date cannot be after the End Date.");
            return;
        }

        if (startDate != null && endDate != null && endDate.isBefore(startDate) && !startDate.isEqual(endDate)) {
            Model.getInstance().showAlert(Alert.AlertType.ERROR, "End Date is Before Start Date.",
                    "The End Date cannot be before the Start Date.");
            return;
        }

        Alert alert = Model.getInstance().getConfirmationDialogAlert("Confirmation",
                "Are you sure you want to generate a PDF for the sales information?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(btnGeneratePDF.getScene().getWindow());

            if (file != null) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    // Create a new PDF document
                    Document document = new Document();
                    PdfWriter.getInstance(document, fos);
                    document.open();

                    // Add Title
                    Paragraph title = new Paragraph("Sales Report",
                            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);

                    // Add Date
                    Paragraph date = new Paragraph("Date: " + LocalDate.now(),
                            FontFactory.getFont(FontFactory.HELVETICA, 12));
                    date.setSpacingAfter(10);
                    document.add(date);

                    // Add Sales Table
                    PdfPTable salesTable = getSalesTable();
                    document.add(salesTable);

                    // Add Summary Table
                    document.add(new Paragraph("\n")); // Add spacing
                    PdfPTable summaryTable = getProductSummaryTable();
                    document.add(summaryTable);

                    document.close();
                    Model.getInstance().showAlert(Alert.AlertType.INFORMATION, "Successfully Generated PDF",
                            "The PDF report has been saved successfully.");
                } catch (Exception e) {
                    e.printStackTrace();
                    Model.getInstance().showAlert(Alert.AlertType.ERROR, "Error Generating PDF",
                            "An error occurred while generating the PDF.");
                }
            }
        }
    }

    private PdfPTable getSalesTable() {
        // Define column widths for the table
        float[] columnWidths = {1, 2, 2, 2, 1, 2};
        PdfPTable salesTable = new PdfPTable(columnWidths);
        salesTable.setWidthPercentage(100); // Optional: Set table width to fill the page

        // Add header cells
        salesTable.addCell(new PdfPCell(new Phrase("Sale ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        salesTable.addCell(new PdfPCell(new Phrase("Product ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        salesTable.addCell(new PdfPCell(new Phrase("Product Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        salesTable.addCell(new PdfPCell(new Phrase("Sale Date", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        salesTable.addCell(new PdfPCell(new Phrase("Quantity Sold", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        salesTable.addCell(new PdfPCell(new Phrase("Sale Price", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));

        // Add rows for each sale
        for (Sale sale : tableViewSales.getItems()) {
            salesTable.addCell(String.valueOf(sale.ID));
            salesTable.addCell(String.valueOf(sale.getProductId()));
            salesTable.addCell(sale.getProductName());
            salesTable.addCell(sale.getSaleDate().toString());
            salesTable.addCell(String.valueOf(sale.getQuantitySold()));
            salesTable.addCell("$" + sale.getSalePrice().toString());
        }

        return salesTable;
    }


    private PdfPTable getProductSummaryTable() {
        // Define column widths for the table
        float[] columnWidths = {2, 4, 2, 2};
        PdfPTable summaryTable = new PdfPTable(columnWidths);
        summaryTable.setWidthPercentage(100); // Optional: Set table width to fill the page

        // Add header cells
        summaryTable.addCell(new PdfPCell(new Phrase("Product ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        summaryTable.addCell(new PdfPCell(new Phrase("Product Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        summaryTable.addCell(new PdfPCell(new Phrase("Total Quantity Sold", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        summaryTable.addCell(new PdfPCell(new Phrase("Total Sales", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));

        Set<Integer> processedProductIds = new HashSet<>();

        // Populate the table with data
        for (Sale sale : tableViewSales.getItems()) {
            int productId = sale.getProductId();

            if (processedProductIds.add(productId)) {
                int totalQuantitySold = getTotalQuantitySold(productId);
                BigDecimal totalSales = getTotalSales(productId);

                summaryTable.addCell(String.valueOf(productId));
                summaryTable.addCell(sale.getProductName());
                summaryTable.addCell(String.valueOf(totalQuantitySold));
                summaryTable.addCell("$" + totalSales.toString());
            }
        }

        return summaryTable;
    }



    public BigDecimal getTotalSales(int productId) {
        BigDecimal totalSales = BigDecimal.ZERO;

        for (Sale sale : tableViewSales.getItems()) {
            if (sale.getProductId() == productId) {
                BigDecimal saleAmount = sale.getSalePrice().multiply(BigDecimal.valueOf(sale.getQuantitySold()));
                totalSales = totalSales.add(saleAmount);
            }
        }
        return totalSales;
    }

    public int getTotalQuantitySold(int productId) {
        int totalQuantitySold = 0;

        for (Sale sale : tableViewSales.getItems()) {
            if (sale.getProductId() == productId) {
                totalQuantitySold += sale.getQuantitySold();
            }
        }
        return totalQuantitySold;
    }

    public Sale getTopSellingProduct(){
        Sale saleResult = null;

        for(Sale currentSale: tableViewSales.getItems()){
            if(saleResult == null){
                saleResult = currentSale;
            }
            else{
                if(currentSale.getQuantitySold() > saleResult.getQuantitySold()){
                    saleResult = currentSale;
                }
            }
        }

        return saleResult;
    }

}
