package com.inventorymanagementsystem.Controllers.Admin;

import com.inventorymanagementsystem.Models.Model;
import com.inventorymanagementsystem.Models.Sale;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.scene.control.Alert.AlertType;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

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

    private final ObservableList<Sale> saleList = Sale.getList();
    private FilteredList<Sale> filteredSaleList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        datePickerStart.getEditor().textProperty().addListener((observable, oldValue, newValue) -> filterSalesByDate());
        datePickerEnd.getEditor().textProperty().addListener((observable, oldValue, newValue) -> filterSalesByDate());
    }


    private void filterSalesByDate() {
        LocalDate startDate = LocalDate.parse(datePickerStart.getEditor().getText());
        LocalDate endDate = LocalDate.parse(datePickerEnd.getEditor().getText());

        filteredSaleList.setPredicate(sale -> {
            // If no startDate and no endDate, show all sales
            if (startDate == null && endDate == null) {
                return true;
            }

            // If only startDate is specified, show sales after or on the startDate
            if (startDate != null && endDate == null) {
                return !sale.getSaleDate().isBefore(startDate);
            }

            // If only endDate is specified, show sales before or on the endDate
            if (startDate == null && endDate != null) {
                return !sale.getSaleDate().isAfter(endDate);
            }

            // If both startDate and endDate are specified, show sales between the dates
            return !sale.getSaleDate().isBefore(startDate) && !sale.getSaleDate().isAfter(endDate);
        });
    }

    public void generatePDF() {
        if(tableViewSales.getItems().isEmpty()){
            Model.getInstance().showAlert(AlertType.ERROR, "TableView is Empty.",
                    "The TableView is Empty. Therefore there is no sales report to generate.");
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
                try (PdfWriter writer = new PdfWriter(file);
                     PdfDocument pdfDoc = new PdfDocument(writer);
                     Document document = new Document(pdfDoc)) {

                    // Add Title and Date
                    document.add(new Paragraph("Sales Report")
                            .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
                    document.add(new Paragraph("Date: " + LocalDate.now()).setFontSize(12));

                    // First Table: Sale Details
                    Table salesTable = getSalesTable();
                    document.add(salesTable);

                    document.add(new Paragraph("\n"));

                    // Second Table: Product Summaries
                    Table summaryTable = getProductSummaryTable();
                    document.add(summaryTable);

                    // Close the document
                    document.close();

                    // Confirmation
                    Model.getInstance().showAlert(AlertType.INFORMATION, "Successfully Generated PDF",
                            "The PDF report has been saved successfully.");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Table getSalesTable() {
        Table salesTable = new Table(new float[]{1, 2, 2, 2, 1, 2});
        salesTable.addHeaderCell("Sale ID");
        salesTable.addHeaderCell("Product ID");
        salesTable.addHeaderCell("Product Name");
        salesTable.addHeaderCell("Sale Date");
        salesTable.addHeaderCell("Quantity Sold");
        salesTable.addHeaderCell("Sale Price");

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

    private Table getProductSummaryTable(){
        Table summaryTable = new Table(new float[]{2, 4, 2, 2});
        summaryTable.addHeaderCell("Product ID");
        summaryTable.addHeaderCell("Product Name");
        summaryTable.addHeaderCell("Total Quantity Sold");
        summaryTable.addHeaderCell("Total Sales");

        // Use a Set to track processed product IDs
        Set<Integer> processedProductIds = new HashSet<>();

        for (Sale sale : tableViewSales.getItems()) {
            int productId = sale.getProductId();

            // Skip if this product ID has already been processed
            if (processedProductIds.add(productId)) {

                // Use your provided methods to calculate totals
                int totalQuantitySold = getTotalQuantitySold(productId);
                BigDecimal totalSales = getTotalSales(productId);

                // Add data to the summary table
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
