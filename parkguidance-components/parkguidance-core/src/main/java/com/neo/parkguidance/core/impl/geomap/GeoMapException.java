package com.neo.parkguidance.core.impl.geomap;
/**
 * This class provides the base for a GeoMapException which shall be extend when used by a GeoMap Service
 * */
public abstract class GeoMapException extends RuntimeException {

    /**
     * Constructs a GeoMapException  with {@code null} as its
     * detail message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    protected GeoMapException() {
        super();
    }

    /**
     * Constructs a GeoMapException with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    protected GeoMapException(String message) {
        super(message);
    }

    /**
     * Constructs a GeoMapException with the specified detail message and
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
    protected GeoMapException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs a GeoMapExceptio) with the specified cause and a
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
    protected GeoMapException(Throwable cause) {
        super(cause);
    }
}
