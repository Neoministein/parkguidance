package com.neo.parkguidance.framework.impl.mail.exception;

import java.util.List;

/**
 * This exception is thrown if the Mail recipient cannot be reached or parsed
 */
public class MailRecipientException extends MailException {

    private final List<String> recipients;

    /**
     * Constructs a MailRecipientException  with {@code null} as its
     * detail message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public MailRecipientException(List<String> recipients) {
        super();
        this.recipients = recipients;
    }

    /**
     * Constructs a MailRecipientException with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public MailRecipientException(String message, List<String> recipients) {
        super(message);
        this.recipients = recipients;
    }

    /**
     * Constructs a MailRecipientException with the specified detail message and
     * cause.<p> Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public MailRecipientException(String message, List<String> recipients , Throwable cause) {
        super(message, cause);
        this.recipients = recipients;
    }

    /** Constructs a MailRecipientException with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public MailRecipientException(Throwable cause, List<String> recipients) {
        super(cause);
        this.recipients = recipients;
    }

    public List<String> getRecipients() {
        return recipients;
    }
}
