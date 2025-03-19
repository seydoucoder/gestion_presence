package com.gestion_presence.gestion_presence.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class EmailUtil {

    public static void sendEmail(String toEmail, String subject, String body) {
        try {
            // Charger les propriétés SMTP depuis un fichier
            Properties props = new Properties();
            try (InputStream input = EmailUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new FileNotFoundException("Le fichier config.properties est introuvable !");
                }
                props.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
            }


            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(props.getProperty("mail.smtp.user"), props.getProperty("mail.smtp.password"));
                }
            });

            // Créer le message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(props.getProperty("mail.smtp.user"), "Support"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());

            // Activer HTML dans le corps du message
            msg.setContent(body, "text/html; charset=UTF-8");

            // Envoyer l'email
            Transport.send(msg);
            System.out.println("E-mail envoyé avec succès !");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}

