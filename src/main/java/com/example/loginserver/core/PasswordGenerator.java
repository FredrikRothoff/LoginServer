package com.example.loginserver.core;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public class PasswordGenerator {

    public static String generateRandomPassword() {
        // Generate a random password with length 8
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    public static void sendPasswordByEmail(String email, String password) throws Exception {
        Properties emailProperties = loadEmailProperties();
        // Extract email configuration from properties
        String host = emailProperties.getProperty("mail.smtp.host");
        String port = emailProperties.getProperty("mail.smtp.port");
        String username = emailProperties.getProperty("mail.smtp.username");
        String passwordEmail = emailProperties.getProperty("mail.smtp.password");
        String from = emailProperties.getProperty("mail.smtp.username");
        String to = email;
        // Setup email properties
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        // Create a new session with authentication
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, passwordEmail);
            }
        });
        // Create and send message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        String subject = "Temporary Password";
        String body =
                "Hello,\n\nWe received a request to reset your password. Your temporary " +
                        "password is:\n\n" + password + "\n\nPlease log in using this temporary " + "password " + "and change it to a new password of your choice within 15 " + "minutes." + "\n\nBest " + "regards,\nThe Security Team";
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
    }

    private static Properties loadEmailProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream =
                     PasswordGenerator.class.getClassLoader().getResourceAsStream("application" +
                             ".properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            // Handle properties loading exception
            e.printStackTrace();
        }
        return properties;
    }
}
