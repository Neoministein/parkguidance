package com.neo.parkguidance.core.impl.mail.exception;

/**
 * This exception shall be thrown when the cause is unknown
 */
public class UnknownMailException extends MailException {
    /**
     * Constructs a UnkownMailException  with {@code null} as its
     * detail message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public UnknownMailException() {
        super();
    }

    /**
     * Constructs a UnkownMailException with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public UnknownMailException(String message) {
        super(message);
    }

    /**
     * Constructs a UnkownMailException with the specified detail message and
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
    public UnknownMailException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs a UnkownMailException with the specified cause and a
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
    public UnknownMailException(Throwable cause) {
        super(cause);
    }
}
