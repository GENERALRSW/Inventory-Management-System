package com.inventorymanagementsystem.Controllers;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class PurchaseOrdersController implements Initializable{


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public static void sendEmail(String toEmail, String subject, String body) {
        // SMTP server configuration (Gmail example)
        String host = "smtp.gmail.com"; // Replace with your SMTP server
        final String fromEmail = "peterstywaine@gmail.com"; // Your email
        final String password = "mpap tyra nubl euln"; // Your email password

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
}