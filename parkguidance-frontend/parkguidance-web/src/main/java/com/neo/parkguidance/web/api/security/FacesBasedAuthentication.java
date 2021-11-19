package com.neo.parkguidance.web.api.security;

/**
 * This interfaces defines authentication interactions which a user can call
 */
public interface FacesBasedAuthentication {

    /**
     * Attempts to login the user via the stored cookies in the browser
     *
     * @return the username if successful otherwise null
     */
    void attemptCookieBasedLogin();

    /**
     * Attempts to login the user based on the given credentials <br>
     * If remember is true the credentials are stored in the cookies
     *
     * @param username the given username
     * @param password the given password
     * @param remember stores the credentials as cookies if true on successful login attempt
     */
    void login(String username, String password, boolean remember);

    /**
     * Logs out the user and deletes the stored cookie values
     */
    void logout();

    /**
     * Checks if the current sessions is logged in
     *
     * @return true if logged in else false
     */
    boolean isLoggedIn();
}
