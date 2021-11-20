package com.neo.parkguidance.framework.impl.mail;

import com.neo.parkguidance.framework.api.mail.MailClient;
import com.neo.parkguidance.framework.api.mail.MailService;
import com.neo.parkguidance.framework.entity.RegisteredUser;
import com.neo.parkguidance.framework.impl.mail.exception.MailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.Message;
import java.util.HashMap;
import java.util.Map;

/**
 * //TODO Implement a global queue system in order to resend mails if it fails
 */
@ApplicationScoped
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Inject
    MailClient mailClient;

    @Override
    public void sendMail(String recipient, String subject, String body, EmailPriority priority,
            EmailSensitivity sensitivity) {
        try {
            mailClient.sendMail(recipient, subject, body, priority, sensitivity);
        } catch (MailException ex) {
            LOGGER.error("Unable to send a mail", ex);
        }

    }

    @Override
    public void sendMail(RegisteredUser recipient, String subject, String body, EmailPriority priority,
            EmailSensitivity sensitivity) {
        try {
            mailClient.sendMail(recipient.getEmail(), subject, body, priority, sensitivity);
        } catch (MailException ex) {
            LOGGER.error("Unable to send a mail", ex);
        }

    }

    @Override
    public void sendMail(Map<RegisteredUser, Message.RecipientType> recipients, String subject, String body,
            EmailPriority priority, EmailSensitivity sensitivity) {
        Map<String, Message.RecipientType> recipientAddress = new HashMap<>();
        for (Map.Entry<RegisteredUser, Message.RecipientType> recipient: recipients.entrySet()) {
            recipientAddress.put(recipient.getKey().getEmail(),recipient.getValue());
        }
        try {
            mailClient.sendMail(recipientAddress, subject, body, priority, sensitivity);
        } catch (MailException ex) {
            LOGGER.error("Unable to send a mail", ex);
        }

    }

    @Override
    public void reload() {
        mailClient.reload();
    }
}
