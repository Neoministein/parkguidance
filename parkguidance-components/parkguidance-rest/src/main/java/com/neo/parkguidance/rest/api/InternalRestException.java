package com.neo.parkguidance.rest.api;

public class InternalRestException extends Exception {

    private final int responseStatus;

    /** Constructs a new InternalRestException with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     * @param   responseStatus  the response status which will be sended
     */
    public InternalRestException(int responseStatus, String message) {
        super(message);
        this.responseStatus = responseStatus;
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public InternalRestException(int responseStatus, String message, Throwable cause) {
        super(message, cause);
        this.responseStatus = responseStatus;
    }

    public int getResponseStatus() {
        return this.responseStatus;
    }
}

