package com.neo.parkguidance.core.api.security.oauth2;

import com.neo.parkguidance.core.impl.security.oauth2.OAuth2ClientObject;
import com.neo.parkguidance.core.impl.security.exception.UnverifiedEmailException;

/**
 * This interfaces defines functionality a oAuth2 provider needs
 * **IMPORTANT**
 * DO NOT IMPLEMENT A OAUTH2 CLIENT IF THE PROVIDER DOESN'T VALIDATED THE EMAIL OF THE CLIENT
 */
public interface OAuth2Client {

    /**
     * Calls the oAuth2Service and verifies if the given token is valid and returns a {@link OAuth2ClientObject}
     * based in the user information
     *
     * @param token the token used for backend authentication
     * @exception UnverifiedEmailException is thrown if the email of the service is not yet verified
     *
     * @return
     */
    OAuth2ClientObject verifyToken(String token);

    /**
     * The name of the provider which is used to identify the which {@link OAuth2Client} is needed
     *
     * @return the name of the prover
     */
    String getProvider();

    /**
     * reloads oAuth2 client
     */
    void reload();

    /**
     * Renders the html needed in order to use this service in the frontend
     * @return the html which will be rendered
     */
    String renderXhtml();

}
