package com.inventorymanagementsystem.Controllers;

import com.inventorymanagementsystem.Models.PurchaseOrder;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class PurchaseOrdersController implements Initializable{
    public TableView<PurchaseOrder> tableViewPurchaseOrders;
    public TableColumn<PurchaseOrder, String> columnOrderID, columnDate, columnQuantity;
    public TableColumn<PurchaseOrder, String> columnSupplierID, columnTotalAmount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public static void sendEmail(String fromEmail, String password, String toEmail, String subject, String body) {
        String host = getSmtpHost(fromEmail);

        // Setup properties for the mail session
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // TLS support

        // Create a session with an authenticator
        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    // Method to get the SMTP host based on the email service
    private static String getSmtpHost(String fromEmail) {
        String domain = fromEmail.substring(fromEmail.indexOf("@") + 1).toLowerCase();
        System.out.println("Email is being sent from " + domain);

        return switch (domain) {
            case "gmail.com" -> "smtp.gmail.com";
            case "proton.me", "protonmail.com", "protonmail.ch", "pm.me" -> "smtp.protonmail.com";
            case "yahoo.com" -> "smtp.mail.yahoo.com";
            case "outlook.com", "hotmail.com" -> "smtp.office365.com";
            default -> "Other";
        };
    }
}