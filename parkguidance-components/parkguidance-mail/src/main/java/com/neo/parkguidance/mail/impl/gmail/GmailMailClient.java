package com.neo.parkguidance.mail.impl.gmail;

import com.neo.parkguidance.core.api.config.ConfigService;
import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.mail.api.MailClient;
import com.neo.parkguidance.mail.impl.EmailPriority;
import com.neo.parkguidance.mail.impl.EmailSensitivity;
import com.neo.parkguidance.mail.impl.exception.MailHostException;
import com.neo.parkguidance.mail.impl.exception.MailRecipientException;
import com.neo.parkguidance.mail.impl.exception.UnknownMailException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * //TODO Implement auth via OAuth 2.0
 */
@ApplicationScoped
public class GmailMailClient implements MailClient {

    public static final String GMAIL_CONFIG_MAP = "com.neo.parkguidance.mail.gmail";

    public static final String HOST = "mail.smtp.host";
    public static final String START_TLS = "mail.smtp.starttls.enable";
    public static final String PORT = "mail.smtp.port";
    public static final String AUTH = "mail.smtp.auth";

    // GMail user name (just the part before "@gmail.com")
    public static final String USER = "mail.smtp.user";
    // GMail password
    public static final String PASSWORD = "mail.smtp.password";

    private static final Properties DEFAULT_PROPERTIES = new Properties();
    private Properties mailProperties = new Properties();

    @Inject
    ConfigService storedValueService;

    @PostConstruct
    protected void init() {
        DEFAULT_PROPERTIES.put(HOST, "smtp.gmail.com");
        DEFAULT_PROPERTIES.put(START_TLS,"true");
        DEFAULT_PROPERTIES.put(PORT, "587");
        DEFAULT_PROPERTIES.put(AUTH, "true");
        reloadConfig();
    }

    protected void reloadConfig() {
        Map<String, ConfigValue> config = storedValueService.getConfigMap(GMAIL_CONFIG_MAP);
        Properties newProperties = new Properties();
        newProperties.putAll(DEFAULT_PROPERTIES);
        for (Map.Entry<String, ConfigValue> entry: config.entrySet()) {
            newProperties.put(entry.getKey(), entry.getValue().getValue());
        }
        mailProperties = newProperties;
    }

    public void reload() {
        reloadConfig();
    }

    public void sendMail(String recipient, String subject, String body,
            EmailPriority priority, EmailSensitivity sensitivity) {
        Session session = Session.getDefaultInstance(mailProperties);
        MimeMessage message = createMessage(session, subject, body, priority, sensitivity);

        try {
            message.addRecipient(Message.RecipientType.TO, createRecipientAddress(recipient));
        } catch (MessagingException ex) {
            throw new UnknownMailException(ex);
        }

        sendMessage(session, message);
    }

    public void sendMail(Map<String, Message.RecipientType> recipients, String subject, String body,
            EmailPriority priority, EmailSensitivity sensitivity) {
        Session session = Session.getDefaultInstance(mailProperties);
        Message message = createMessage(session, subject, body, priority, sensitivity);

        List<String> failedRecipients = new ArrayList<>();
        for (Map.Entry<String, Message.RecipientType> recipient: recipients.entrySet()) {
            try {
                message.addRecipient(recipient.getValue(), createRecipientAddress(recipient.getKey()));
            } catch (MailRecipientException | MessagingException ex) {
                failedRecipients.add(recipient.getKey());
            }
        }
        sendMessage(session, message);

        if (failedRecipients.isEmpty()) {
            throw new MailRecipientException("Cannot send mail to recipients, address parsing error", failedRecipients);
        }
    }

    protected MimeMessage createMessage(Session session, String subject, String body,
            EmailPriority emailPriority, EmailSensitivity sensitivity) {
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(createHostAddress());
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");
            message.setHeader("X-Priority", String.valueOf(emailPriority.getValue()));
            message.setHeader("sensitivity", sensitivity.getValue());
        } catch (MessagingException ex) {
            throw new UnknownMailException(ex);
        }
        return message;
    }

    protected void sendMessage(Session session, Message message) {
        try {
            Transport transport = session.getTransport("smtp");
            sendMessage(transport, message);
            transport.close();
        } catch (MessagingException ex) {
            throw new UnknownMailException(ex);
        }
    }

    protected void sendMessage(Transport transport, Message message) {
        try {
            transport.connect(getProperty(HOST), getProperty(USER), getProperty(PASSWORD));
            transport.sendMessage(message, message.getAllRecipients());
        } catch (AuthenticationFailedException ex) {
            throw new MailHostException("Cannot establish connection to host Gmail, authentication failed", ex);
        } catch (SendFailedException ex) {
            throw new MailRecipientException(new ArrayList<>());
        } catch (MessagingException ex) {
            throw new UnknownMailException(ex);
        }
    }

    protected InternetAddress createHostAddress() {
        try {
            return new InternetAddress(getProperty(HOST));
        } catch (AddressException ex) {
            throw new MailHostException("Cannot establish connection to Gmail host Gmail, url parsing error", ex);
        }
    }

    protected InternetAddress createRecipientAddress(String recipient) {
        try {
            return new InternetAddress(recipient);
        } catch (AddressException ex) {
            throw new MailRecipientException("Cannot send mail to recipient, address parsing error",
                    Collections.singletonList(recipient), ex);
        }
    }

    protected String getProperty(String key) {
        return mailProperties.get(key).toString();
    }
}
