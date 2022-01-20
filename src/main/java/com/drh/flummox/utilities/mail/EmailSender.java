package com.drh.flummox.utilities.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
    Properties properties;
    Session session;

    public EmailSender(){
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        session = Session.getInstance(properties, null);
    }

    //TODO add authentication when I can pull secrets from password manager
    public void send(String subject, String message){
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("email@address.com"));
            msg.setRecipients(Message.RecipientType.TO, "addresseshere");
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(message);
            Transport.send(msg);
        } catch (MessagingException mex) {
            LOGGER.error("send failed, exception: ", mex);
        }
    }

}
