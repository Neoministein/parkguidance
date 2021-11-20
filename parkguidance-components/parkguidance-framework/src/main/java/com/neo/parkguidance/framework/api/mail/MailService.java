package com.neo.parkguidance.framework.api.mail;

import com.neo.parkguidance.framework.entity.RegisteredUser;
import com.neo.parkguidance.framework.impl.mail.EmailPriority;
import com.neo.parkguidance.framework.impl.mail.EmailSensitivity;

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
