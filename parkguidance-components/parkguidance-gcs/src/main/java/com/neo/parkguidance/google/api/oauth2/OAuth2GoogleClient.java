package com.neo.parkguidance.google.api.oauth2;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.neo.parkguidance.core.api.security.oauth2.OAuth2Client;
import com.neo.parkguidance.core.api.config.ConfigService;
import com.neo.parkguidance.core.entity.ConfigValue;
import com.neo.parkguidance.core.impl.security.oauth2.OAuth2ClientObject;
import com.neo.parkguidance.core.impl.security.exception.UnverifiedEmailException;
import com.neo.parkguidance.core.impl.utils.ConfigValueUtils;
import com.neo.parkguidance.google.api.constants.GoogleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

@ApplicationScoped
public class OAuth2GoogleClient implements OAuth2Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2GoogleClient.class);

    public static final String TYPE = "google";

    public static final String CLIENT_ID = "cloud.google.oauth.client.id";
    public static final String AUTH_ENDPOINT = "cloud.google.oauth.endpoint";

    private String clientId = null;
    private String authEndpoint = null;
    private boolean failedInit = false;

    @Inject
    ConfigService configService;

    @PostConstruct
    protected void init() {
        try {
            Map<String, ConfigValue> configMap = configService.getConfigMap(GoogleConstants.CONFIG_MAP);
            clientId = ConfigValueUtils.parseString(configMap.get(CLIENT_ID));
            authEndpoint = ConfigValueUtils.parseString(configMap.get(AUTH_ENDPOINT));
        } catch (Exception ex) {
            failedInit = true;
            LOGGER.error("Configs are missing unable to correctly initialize OAuth2GoogleClient", ex);
        }
    }

    public void reload() {
        init();
    }

    @Override
    public OAuth2ClientObject verifyToken(String token) {
        if (failedInit) {
            return null;
        }
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(clientId))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(token);

            if (idToken == null) {
                return null;
            }
            GoogleIdToken.Payload payload = idToken.getPayload();
            if (Boolean.FALSE.equals(payload.getEmailVerified())) {
                throw new UnverifiedEmailException();
            }

            return new OAuth2ClientObject(
                    payload.getSubject(),
                    (String) payload.get("name"),
                    payload.getEmail(),
                    (String) payload.get("picture")
            );
        } catch (GeneralSecurityException ex) {
            LOGGER.error("Security Exception", ex);
        } catch (IOException ex) {
            LOGGER.error("Unable to connect to google oauth2 endpoint", ex);
        }

        return null;
    }

    @Override
    public String getProvider() {
        return TYPE;
    }

    @Override
    public String renderXhtml() {
        return ""
                + "<script src=\"https://accounts.google.com/gsi/client\" async defer></script>\n"
                + "<div id=\"g_id_onload\"\n"
                + "    data-client_id=\"" + clientId +"\"\n"
                + "    data-login_uri=\"" + authEndpoint + "\"\n"
                + "    data-auto_prompt=\"false\"\n"
                + "    data-ux_mode=\"redirect\">\n"
                + "</div>\n"
                + "<div class=\"g_id_signin\"\n"
                + "    data-type=\"standard\"\n"
                + "    data-size=\"large\"\n"
                + "    data-theme=\"outline\"\n"
                + "    data-text=\"sign_in_with\"\n"
                + "    data-shape=\"rectangular\"\n"
                + "    data-logo_alignment=\"left\""
                + "    data-width=\"320\">\n"
                + "</div>";
    }
}
