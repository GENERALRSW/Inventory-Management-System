package com.inventorymanagementsystem.Models;

import javafx.scene.control.Alert.AlertType;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DataBaseManager {

    public static boolean userEmailExists(String email){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT COUNT(*) FROM Users WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking Email: " + e.getMessage());
        }

        return false;
    }

    public static boolean validUser(String email, String password){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT password_hash FROM Users WHERE email = ?";
        String passwordHashFromDb;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                passwordHashFromDb = resultSet.getString("password_hash");

                // Compare the hashed password with the one stored in the database
                return MyBCrypt.isPasswordEqual(password, passwordHashFromDb);
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
        }

        return false;
    }

    public static User getUser(String email){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT * FROM Users WHERE email = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String role = resultSet.getString("role");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                return new User(userId, name, role, email, createdAt);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isSupplierEmailTaken(String email) {
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT COUNT(*) FROM Suppliers WHERE contact_email = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking Email: " + e.getMessage());
        }
        return false;
    }

    public static boolean isSupplierPhoneNumTaken(String phoneNumber) {
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT COUNT(*) FROM Suppliers WHERE phone_number = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, phoneNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking PhoneNumber: " + e.getMessage());
        }
        return false;
    }

    private static void loadBatches(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT * FROM Batches";

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                int batchId = resultSet.getInt("batch_id");
                int productId = resultSet.getInt("product_id");
                int currentStock = resultSet.getInt("current_stock");
                LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();
                int supplierId = resultSet.getInt("supplier_id");

                new Batch(batchId, productId, currentStock, expirationDate, supplierId);
            }

        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error loading Batches",
                    e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadInventoryAdjustments(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT * FROM InventoryAdjustments";

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                int adjustmentId = resultSet.getInt("adjustment_id");
                int batchId = resultSet.getInt("batch_id");
                LocalDate adjustmentDate = resultSet.getDate("adjustment_date").toLocalDate();
                String adjustmentType = resultSet.getString("adjustment_type");
                int quantity = resultSet.getInt("adjustment_date");
                String reason = resultSet.getString("reason");

                new InventoryAdjustment(adjustmentId, batchId, adjustmentDate, adjustmentType, quantity, reason);
            }

        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error loading Inventory Adjustments",
                    e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadProducts(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT * FROM Products";

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                int productId = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                BigDecimal unitPrice = resultSet.getBigDecimal("unit_price");

                new Product(productId, name, category, unitPrice);
            }

        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error loading Products",
                    e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadPurchaseOrders(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT * FROM PurchaseOrders";

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                int orderId = resultSet.getInt("order_id");
                LocalDate orderDate = resultSet.getDate("order_date").toLocalDate();
                int supplierId = resultSet.getInt("supplier_id");
                String supplierName = resultSet.getString("supplier_name");
                String productName = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                BigDecimal totalAmount = resultSet.getBigDecimal("total_amount");

                new PurchaseOrder(orderId, orderDate, supplierId, supplierName, productName, quantity, totalAmount);
            }

        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error loading Purchase Orders",
                    e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadSales(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT * FROM Sales";

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                int saleId = resultSet.getInt("sale_id");
                int batchId = resultSet.getInt("batch_id");
                LocalDate saleDate = resultSet.getDate("sale_date").toLocalDate();
                int quantitySold = resultSet.getInt("quantity_sold");
                BigDecimal salePrice = resultSet.getBigDecimal("sale_price");

                new Sale(saleId, batchId, saleDate, quantitySold, salePrice);
            }

        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error loading Sales",
                    e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadSuppliers(){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT * FROM Suppliers";

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                int saleId = resultSet.getInt("supplier_id");
                String name = resultSet.getString("name");
                String contactEmail = resultSet.getString("contact_email");
                String phoneNumber = resultSet.getString("phone_number");
                String address = resultSet.getString("address");

                new Supplier(saleId, name, contactEmail, phoneNumber, address);
            }

        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error loading Suppliers",
                    e.getMessage());
            e.printStackTrace();
        }
    }


    // For adding to database
    public static void addBatch(int productId, int currentStock, LocalDate expirationDate, int supplierId){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "INSERT INTO Batches (product_id, current_stock, expiration_date, supplier_id) VALUES (?, ?, ?, ?)";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            statement.setInt(2, currentStock);
            statement.setDate(3, Date.valueOf(expirationDate));
            statement.setInt(4, supplierId);
            int rowsAdded = statement.executeUpdate();
            int batchId = -1;

            if(rowsAdded > 0){
                batchId = getLastBatchID();
                new Batch(batchId, productId, currentStock, expirationDate, supplierId);
            }

            addMessage("Batch", batchId, rowsAdded);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error adding Batch",
                    e.getMessage());
        }
    }

    public static void addInventoryAdjustment(int batchId, LocalDate adjustmentDate, String adjustmentType, int quantity, String reason){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "INSERT INTO InventoryAdjustments (batch_id, adjustment_date, adjustment_type, quantity, reason) VALUES (?, ?, ?, ?, ?)";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, batchId);
            statement.setDate(2, Date.valueOf(adjustmentDate));
            statement.setString(3, adjustmentType);
            statement.setInt(3, quantity);
            statement.setString(4, reason);
            int rowsAdded = statement.executeUpdate();
            int adjustment_id = -1;

            if(rowsAdded > 0){
                adjustment_id = getLastInventoryAdjustmentID();
                new InventoryAdjustment(adjustment_id, batchId, adjustmentDate, adjustmentType, quantity, reason);
            }

            addMessage("Inventory Adjustment", adjustment_id, rowsAdded);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error adding Inventory Adjustment",
                    e.getMessage());
        }
    }

    public static void addProduct(String name, String category, BigDecimal unitPrice){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "INSERT INTO Products (name, category, unit_price) VALUES (?, ?, ?)";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, category);
            statement.setBigDecimal(3, unitPrice);
            int rowsAdded = statement.executeUpdate();
            int productId = -1;

            if(rowsAdded > 0){
                productId = getLastProductID();
                new Product(productId, name, category, unitPrice);
            }

            addMessage("Product", productId, rowsAdded);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error adding Product",
                    e.getMessage());
        }
    }

    public static void addPurchaseOrder(LocalDate orderDate, int supplierId, String supplierName,
                                        String productName, int quantity, BigDecimal totalAmount){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "INSERT INTO PurchaseOrders (order_date, supplier_id, supplier_name," +
                " product_name, quantity, total_amount) VALUES (?, ?, ?, ?, ?, ?)";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(orderDate));
            statement.setInt(2, supplierId);
            statement.setString(3, supplierName);
            statement.setString(4, productName);
            statement.setInt(5, quantity);
            statement.setBigDecimal(6, totalAmount);
            int rowsAdded = statement.executeUpdate();
            int orderID = -1;

            if(rowsAdded > 0){
                orderID = getLastPurchaseOrderID();
                new PurchaseOrder(orderID, orderDate, supplierId, supplierName, productName, quantity, totalAmount);
            }

            addMessage("Purchase Order", orderID, rowsAdded);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error adding Purchase Order",
                    e.getMessage());
        }
    }

    public static void addSale(int batchId, LocalDate saleDate, int quantitySold, BigDecimal salePrice){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "INSERT INTO Sales (batch_id, sale_date, quantity_sold, sale_price) VALUES (?, ?, ?, ?)";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, batchId);
            statement.setDate(2, Date.valueOf(saleDate));
            statement.setInt(3, quantitySold);
            statement.setBigDecimal(4, salePrice);
            int rowsAdded = statement.executeUpdate();
            int saleId = -1;

            if(rowsAdded > 0){
                saleId = getLastSaleID();
                new Sale(saleId, batchId, saleDate, quantitySold, salePrice);
            }

            addMessage("Sale", saleId, rowsAdded);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error adding Sale",
                    e.getMessage());
        }
    }

    public static void addSupplier(String name, String contactEmail, String phoneNumber, String address){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "INSERT INTO Suppliers (name, contact_email, phone_number, address) VALUES (?, ?, ?, ?)";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, contactEmail);
            statement.setString(3, phoneNumber);
            statement.setString(4, address);
            int rowsAdded = statement.executeUpdate();
            int supplierId = -1;

            if(rowsAdded > 0){
                supplierId = getLastSupplierID();
                new Supplier(supplierId, name, contactEmail, phoneNumber, address);
            }

            addMessage("Supplier", supplierId, rowsAdded);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error adding Supplier",
                    e.getMessage());
        }
    }

    public static void addUser(String name, String role, String password, String email){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "INSERT INTO Users (name, password_hash, role, email) VALUES (?, ?, ?, ?)";
        String salt = MyBCrypt.generateSalt();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, MyBCrypt.hashPassword(password, salt));
            statement.setString(3, role);
            statement.setString(4, email);
            int rowsAdded = statement.executeUpdate();
            int userId = -1;

            if(rowsAdded > 0){
                userId = getLastUserID();
                new User(userId, name, role, email, getUserCreated_at(userId));
            }

            addMessage("User", userId, rowsAdded);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error adding User",
                    e.getMessage());
        }
    }

    public static void addUser(String name, String password, String email){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "INSERT INTO Users (name, password_hash, email) VALUES (?, ?, ?)";
        String salt = MyBCrypt.generateSalt();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, MyBCrypt.hashPassword(password, salt));
            statement.setString(3, email);
            int rowsAdded = statement.executeUpdate();
            int userId = -1;

            if(rowsAdded > 0){
                userId = getLastUserID();
                new User(userId, name, "ADMIN", email, getUserCreated_at(userId));
            }

            addMessage("User", userId, rowsAdded);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error adding User",
                    e.getMessage());
        }
    }


    // For update info to the database
    public static void updateBatch(Batch batch, int productId, int currentStock, LocalDate expirationDate, int supplierId){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "UPDATE Batches SET product_id = ?, current_stock = ?, expiration_date = ?, supplier_id = ? WHERE batch_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            statement.setInt(2, currentStock);
            statement.setDate(3, Date.valueOf(expirationDate));
            statement.setInt(4, supplierId);
            statement.setInt(5, batch.ID);
            int rowsUpdated = statement.executeUpdate();

            if(rowsUpdated > 0){
                Batch.update(batch, productId, currentStock, expirationDate, supplierId);
            }

            updateMessage("Batch", batch.ID, rowsUpdated);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error updating Batch",
                    e.getMessage());
        }
    }

    public static void updateInventoryAdjustment(InventoryAdjustment inventoryAdjustment, int batchId,
                                                 LocalDate adjustmentDate, String adjustmentType, int quantity, String reason){

        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "UPDATE InventoryAdjustments SET batch_id = ?, adjustment_date = ?, adjustment_type = ?, quantity = ?, reason = ? WHERE adjustment_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, batchId);
            statement.setDate(2, Date.valueOf(adjustmentDate));
            statement.setString(3, adjustmentType);
            statement.setInt(4, quantity);
            statement.setString(5, reason);
            statement.setInt(6, inventoryAdjustment.ID);
            int rowsUpdated = statement.executeUpdate();

            if(rowsUpdated > 0){
                InventoryAdjustment.update(inventoryAdjustment, batchId, adjustmentDate, adjustmentType, quantity, reason);
            }

            updateMessage("Inventory Adjustment", inventoryAdjustment.ID, rowsUpdated);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error updating Inventory Adjustment",
                    e.getMessage());
        }
    }

    public static void updateProduct(Product product, String name, String category, BigDecimal unitPrice){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "UPDATE Products SET name = ?, category = ?, unit_price = ? WHERE product_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, category);
            statement.setBigDecimal(3, unitPrice);
            statement.setInt(4, product.ID);
            int rowsUpdated = statement.executeUpdate();

            if(rowsUpdated > 0){
                Product.update(product, name, category, unitPrice);
            }

            updateMessage("Product", product.ID, rowsUpdated);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error updating Product",
                    e.getMessage());
        }
    }

    public static void updatePurchaseOrder(PurchaseOrder purchaseOrder, LocalDate orderDate, int supplierId,
                                           String supplierName, String productName, int quantity, BigDecimal totalAmount){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "UPDATE PurchaseOrders SET order_date = ?, supplier_id = ?, supplier_name = ?," +
                " product_name = ?, quantity = ?, total_amount = ? WHERE order_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(orderDate));
            statement.setInt(2, supplierId);
            statement.setString(3, supplierName);
            statement.setString(4, productName);
            statement.setInt(5, quantity);
            statement.setBigDecimal(6, totalAmount);
            statement.setInt(7, purchaseOrder.ID);
            int rowsUpdated = statement.executeUpdate();

            if(rowsUpdated > 0){
                PurchaseOrder.update(purchaseOrder, orderDate, supplierId, supplierName, productName, quantity, totalAmount);
            }

            updateMessage("Purchase Order", purchaseOrder.ID, rowsUpdated);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error updating Purchase Order",
                    e.getMessage());
        }
    }

    public static void updateSale(Sale sale, int batchId, LocalDate saleDate, int quantitySold, BigDecimal salePrice){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "UPDATE Sales SET batch_id = ?, sale_date = ?, quantity_sold = ?, sale_price = ? WHERE sale_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, batchId);
            statement.setDate(2, Date.valueOf(saleDate));
            statement.setInt(3, quantitySold);
            statement.setBigDecimal(4, salePrice);
            statement.setInt(5, sale.ID);
            int rowsUpdated = statement.executeUpdate();

            if(rowsUpdated > 0){
                Sale.update(sale, batchId, saleDate, quantitySold, salePrice);
            }

            updateMessage("Sale", sale.ID, rowsUpdated);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error updating Sale",
                    e.getMessage());
        }
    }

    public static void updateSupplier(Supplier supplier, String name, String contactEmail, String phoneNumber, String address){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "UPDATE Suppliers SET name = ?, contact_email = ?, phone_number = ?, address = ? WHERE supplier_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, contactEmail);
            statement.setString(3, phoneNumber);
            statement.setString(4, address);
            statement.setInt(5, supplier.ID);
            int rowsUpdated = statement.executeUpdate();

            if(rowsUpdated > 0){
                Supplier.update(supplier, name, contactEmail, phoneNumber, address);
            }

            updateMessage("Supplier", supplier.ID, rowsUpdated);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error updating Supplier",
                    e.getMessage());
        }
    }

    public static void updateUser(User user, String name, String email){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "UPDATE Users SET name = ?,  email = ? WHERE user_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setInt(3, user.ID);
            int rowsUpdated = statement.executeUpdate();

            if(rowsUpdated > 0){
                User.update(user, name, email);
            }

            updateMessage("User", user.ID, rowsUpdated);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error updating User",
                    e.getMessage());
        }
    }


    // For deleting info from the database
    public static void deleteBatch(Batch batch){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "DELETE FROM Batches WHERE batch_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, batch.ID);
            int rowsDeleted = statement.executeUpdate();

            if(rowsDeleted > 0){
                Batch.remove(batch);
            }

            deleteMessage("Batch", batch.ID, rowsDeleted);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error deleting Batch",
                    e.getMessage());
        }
    }

    public static void deleteInventoryAdjustment(InventoryAdjustment inventoryAdjustment){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "DELETE FROM InventoryAdjustments WHERE adjustment_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, inventoryAdjustment.ID);
            int rowsDeleted = statement.executeUpdate();

            if(rowsDeleted > 0){
                InventoryAdjustment.remove(inventoryAdjustment);
            }

            deleteMessage("Inventory Adjustment", inventoryAdjustment.ID, rowsDeleted);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error deleting Inventory Adjustment",
                    e.getMessage());
        }
    }

    public static void deleteProduct(Product product){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "DELETE FROM Products WHERE product_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, product.ID);
            int rowsDeleted = statement.executeUpdate();

            if(rowsDeleted > 0){
                Product.remove(product);
            }

            deleteMessage("Product", product.ID, rowsDeleted);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error deleting Product",
                    e.getMessage());
        }
    }

    public static void deletePurchaseOrder(PurchaseOrder purchaseOrder){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "DELETE FROM PurchaseOrders WHERE order_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, purchaseOrder.ID);
            int rowsDeleted = statement.executeUpdate();

            if(rowsDeleted > 0){
                PurchaseOrder.remove(purchaseOrder);
            }

            deleteMessage("Purchase Order", purchaseOrder.ID, rowsDeleted);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error deleting Purchase Order",
                    e.getMessage());
        }
    }

    public static void deleteSale(Sale sale){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "DELETE FROM Sales WHERE sale_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, sale.ID);
            int rowsDeleted = statement.executeUpdate();

            if(rowsDeleted > 0){
                Sale.remove(sale);
            }

            deleteMessage("Sale", sale.ID, rowsDeleted);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error deleting Sale",
                    e.getMessage());
        }
    }

    public static void deleteSupplier(Supplier supplier){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "DELETE FROM Suppliers WHERE supplier_id = ?";

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, supplier.ID);
            int rowsDeleted = statement.executeUpdate();

            if(rowsDeleted > 0){
                Supplier.remove(supplier);
            }

            deleteMessage("Supplier", supplier.ID, rowsDeleted);
        }catch(SQLException e){
            Model.getInstance().showAlert(AlertType.ERROR, "Error deleting Supplier",
                    e.getMessage());
        }
    }

    private static int getLastBatchID() {
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT batch_id FROM Batches ORDER BY batch_id DESC LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("batch_id");
            }
            else {
                System.out.println("No Batches found in the database.");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int getLastInventoryAdjustmentID() {
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT adjustment_id FROM InventoryAdjustments ORDER BY adjustment_id DESC LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("adjustment_id");
            }
            else {
                System.out.println("No Inventory Adjustments found in the database.");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int getLastProductID() {
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT product_id FROM Products ORDER BY product_id DESC LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("product_id");
            }
            else {
                System.out.println("No Products found in the database.");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int getLastPurchaseOrderID() {
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT order_id FROM PurchaseOrders ORDER BY order_id DESC LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("order_id");
            }
            else {
                System.out.println("No Purchase Orders found in the database.");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int getLastSaleID() {
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT sale_id FROM Sales ORDER BY sale_id DESC LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("sale_id");
            }
            else {
                System.out.println("No Sales found in the database.");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getLastSupplierID() {
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT supplier_id FROM Suppliers ORDER BY supplier_id DESC LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("supplier_id");
            }
            else {
                System.out.println("No Suppliers found in the database.");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int getLastUserID() {
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT user_id FROM Users ORDER BY user_id DESC LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
            else {
                System.out.println("No Users found in the database.");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static LocalDateTime getUserCreated_at(int userId){
        Connection connection = Model.getInstance().getDataBaseDriver().getConnection();
        String query = "SELECT created_at FROM Users WHERE user_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getTimestamp("created_at").toLocalDateTime();
            }
            else {
                System.out.println("No Users found in the database.");
            }
        } catch (SQLException e) {
            Model.getInstance().showAlert(AlertType.ERROR, "Error with User Created_at", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private static void addMessage(String tableName, int rowsAdded){
        if (rowsAdded > 0) {
            System.out.println(tableName + " was added");
        }
        else {
            System.out.println(tableName + " was not added");
        }
    }

    private static void addMessage(String tableName, int id, int rowsAdded){
        if (rowsAdded > 0) {
            System.out.println(tableName + " with ID Number " + id + " was added");
        }
        else {
            System.out.println(tableName + " with ID Number " + id + " was not added");
        }
    }

    private static void updateMessage(String tableName, int id, int rowsUpdated){
        if (rowsUpdated > 0) {
            System.out.println(tableName + " with ID " + id + " updated successfully.");
        }
        else {
            System.out.println("No " + tableName + " found with ID " + id);
        }
    }

    private static void deleteMessage(String tableName, int Id, int rowsDeleted){
        if (rowsDeleted > 0) {
            System.out.println(tableName + " with ID " + Id + " deleted successfully.");
        }
        else {
            System.out.println("No " + tableName + " found with ID " + Id);
        }
    }

    public static void loadInfo() {
        loadBatches();
        loadInventoryAdjustments();
        loadProducts();
        loadPurchaseOrders();
        loadSales();
        loadSuppliers();
    }

    public static void removeInfo() {
        Batch.empty();
        InventoryAdjustment.empty();
        Product.empty();
        PurchaseOrder.empty();
        Sale.empty();
        Supplier.empty();
    }
}
