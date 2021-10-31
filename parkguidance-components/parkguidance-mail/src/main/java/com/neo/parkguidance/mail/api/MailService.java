package com.neo.parkguidance.mail.api;

import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.mail.impl.EmailPriority;
import com.neo.parkguidance.mail.impl.EmailSensitivity;

import javax.mail.Message;
import java.util.Map;

public interface MailService {

    /**
     * Sends a mail to the recipients
     *
     * @param recipient the recipients
     * @param subject the subject of the mail
     * @param body the body of the mail
     * @param priority the mail priority
     * @param sensitivity the mail sensitivity
     */
    void sendMail(String recipient, String subject, String body,
            EmailPriority priority, EmailSensitivity sensitivity);

    /**
     * Sends a mail to the recipients
     *
     * @param recipient the recipients
     * @param subject the subject of the mail
     * @param body the body of the mail
     * @param priority the mail priority
     * @param sensitivity the mail sensitivity
     */
    void sendMail(RegisteredUser recipient, String subject, String body,
            EmailPriority priority, EmailSensitivity sensitivity);

    /**
     * Sends a mail to all recipients via a blind carbon copy
     *
     * @param recipients a map with the recipients and the recipient type
     * @param subject the subject of the mail
     * @param body the body of the mail
     * @param priority the mail priority
     * @param sensitivity the mail sensitivity
     */
    void sendMail(Map<RegisteredUser, Message.RecipientType> recipients, String subject, String body,
            EmailPriority priority, EmailSensitivity sensitivity);

    /**
     * Reloads the configuration
     */
    void reload();
}
