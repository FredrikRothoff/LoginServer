package com.example.loginserver.core;

import com.example.loginserver.core.ports.store.UserStorage;
import lombok.AllArgsConstructor;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@AllArgsConstructor
public class ConfirmationEmailGenerator {

    private final UserStorage userStorage;

    public void sendConfirmationEmail(String recipientEmail) {
        String uuid = userStorage.findUUIDByEmail(recipientEmail);
        Properties properties = loadEmailProperties();
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("mail.smtp.username"),
                        properties.getProperty("mail.smtp.password"));
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("mail.smtp.username")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Email Confirmation");
            String confirmationLink = "http://localhost:8080/user/confirm/" + uuid;
            String htmlContent =
                    "<html><body>" + "<p>Please click the button below to confirm " + "your " +
                            "email:</p>" + "<form id=\"confirmForm\" action=\"" + confirmationLink + "\" method=\"post\">" + "<input type=\"hidden\" name=\"_csrf\" value=\"${_csrf.token}\" />" + "<input type=\"submit\" value=\"Confirm Email\">" + "</form>" + "<script>" + "document.getElementById('confirmForm').submit();" + "</script>" + "</body></html>";
            message.setContent(htmlContent, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private Properties loadEmailProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                "application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
