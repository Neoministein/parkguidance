package com.neo.parkguidance.google.api.maps;

import com.neo.parkguidance.core.impl.geomap.GeoMapException;

/**
 * This class handles Google Cloud Service Exceptions which aren't from invalid user input
 */
public class GoogleCloudServiceException extends GeoMapException {

    /** Constructs a GoogleCloudServiceException  with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public GoogleCloudServiceException() {
        super();
    }

    /** Constructs a GoogleCloudServiceException with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public GoogleCloudServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a GoogleCloudServiceException with the specified detail message and
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
     */
    public GoogleCloudServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs a GoogleCloudServiceException with the specified cause and a
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
    public GoogleCloudServiceException(Throwable cause) {
        super(cause);
    }
}
