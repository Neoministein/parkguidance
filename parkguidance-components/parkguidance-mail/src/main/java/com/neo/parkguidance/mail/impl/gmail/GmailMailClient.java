package com.neo.parkguidance.mail.impl.gmail;

import com.neo.parkguidance.core.api.storedvalue.StoredValueService;
import com.neo.parkguidance.mail.api.MailClient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@ApplicationScoped
public class GmailMailClient implements MailClient {

    public static final String HOST = "mail.smtp.host";
    public static final String START_TLS = "mail.smtp.starttls.enable";
    public static final String USER = "mail.smtp.user";
    public static final String PASSWORD = "mail.smtp.password";
    public static final String PORT = "mail.smtp.port";
    public static final String AUTH = "mail.smtp.auth";

    private static final Properties MAIL_PROP = new Properties();

    @Inject
    StoredValueService storedValueService;

    @PostConstruct
    public void init() {
        String host = "smtp.gmail.com";
        MAIL_PROP.clear();
        MAIL_PROP.put(HOST, host);
        MAIL_PROP.put(START_TLS, "true");
        // GMail user name (just the part before "@gmail.com")
        MAIL_PROP.put(USER, storedValueService.getStoredValue(USER));
        // GMail password
        MAIL_PROP.put(PASSWORD, storedValueService.getStoredValue(PASSWORD));
        MAIL_PROP.put(PORT, "587");
        MAIL_PROP.put(AUTH, "true");
    }

    public void sendMail(String recipient, String subject, String body) {
        Session session = Session.getDefaultInstance(MAIL_PROP);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(MAIL_PROP.get(HOST).toString()));
            InternetAddress toAddress = new InternetAddress(recipient);

            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(
                    MAIL_PROP.get(HOST).toString(),
                    MAIL_PROP.get(USER).toString(),
                    MAIL_PROP.get(PASSWORD).toString());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
