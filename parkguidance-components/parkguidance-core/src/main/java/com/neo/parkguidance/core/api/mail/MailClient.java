package com.neo.parkguidance.core.api.mail;

import com.neo.parkguidance.core.impl.mail.EmailPriority;
import com.neo.parkguidance.core.impl.mail.EmailSensitivity;
import com.neo.parkguidance.core.impl.mail.exception.*;

import javax.mail.Message;
import java.util.Map;

/**
 * This interfaces defines the minimum functionality of an E-Mail Client to send mails
 */
public interface MailClient {

    /**
     * Sends a mail to the recipients
     *
     * @param recipient the recipients
     * @param subject the subject of the mail
     * @param body the body of the mail
     * @param priority the mail priority
     * @param sensitivity the mail sensitivity
     *
     * @exception MailHostException If the host configuration is wrong or cannot be contacted
     * @exception MailRecipientException If the recipient cannot be parsed or reached
     * @exception UnknownMailException If something unknown happens
     */
    void sendMail(String recipient, String subject, String body,
            EmailPriority priority, EmailSensitivity sensitivity);

    /**
     * Sends a mail to all recipients via a blind carbon copy
     *
     * @param recipients a map with the recipients and the recipient type
     * @param subject the subject of the mail
     * @param body the body of the mail
     * @param priority the mail priority
     * @param sensitivity the mail sensitivity
     *
     * @exception MailHostException If the host configuration is wrong or cannot be contacted
     * @exception MailRecipientException If the recipient cannot be parsed or reached
     * @exception UnknownMailException If something unknown happens
     */
    void sendMail(Map<String, Message.RecipientType> recipients, String subject, String body,
            EmailPriority priority, EmailSensitivity sensitivity);

    /**
     * Reloads the mail clients configuration
     */
    void reload();
}
