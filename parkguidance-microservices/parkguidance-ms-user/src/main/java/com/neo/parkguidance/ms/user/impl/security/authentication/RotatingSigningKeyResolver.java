package com.neo.parkguidance.ms.user.impl.security.authentication;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RotatingSigningKeyResolver extends SigningKeyResolverAdapter {

    private static final Logger LOGGER =  LoggerFactory.getLogger(RotatingSigningKeyResolver.class);

    private static final int TEN_SECONDS = 10 * 1000;
    private long lastUpdate;

    private final HttpGet publicKeyEndpoint;
    protected Map<String, JWTPublicKey> keyMap = new HashMap<>();

    public RotatingSigningKeyResolver(String publicKeyEndpoint, boolean isSecurityService) {
        this.publicKeyEndpoint = new HttpGet(publicKeyEndpoint);
        if (!isSecurityService) {
            updateCache();
        }
    }

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
        LOGGER.trace("Resolving public key");
        String kid = jwsHeader.getKeyId();
        if (kid == null) {
            throw new MalformedJwtException("Kid header parameter is missing");
        }

        JWTPublicKey jwtPublicKey =  keyMap.get(kid);

        if (jwtPublicKey != null) {
            LOGGER.trace("Public key found [{}]", kid);
            return jwtPublicKey.getPublicKey();
        }

        LOGGER.trace("Cannot find public key [{}] trying to update cache", kid);
        if (updateCache()) {
            jwtPublicKey =  keyMap.get(kid);

            if (jwtPublicKey != null) {
                return jwtPublicKey.getPublicKey();
            }
        } else {
            LOGGER.trace("Last cache refresh call has been less then {} seconds ago refresh skipped", TEN_SECONDS/1000);
        }
        throw new SignatureException("Cannot find matching public key for kid");
    }

    private synchronized boolean updateCache() {
        if (lastUpdate < System.currentTimeMillis() - TEN_SECONDS) {
            return checkForNewKey();
        }
        return false;
    }

    protected boolean checkForNewKey() {
        //TODO IMPL retry pattern
        boolean hasChanged;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            LOGGER.trace("Calling public key endpoint [{}]", publicKeyEndpoint.getURI());
            CloseableHttpResponse response = httpClient.execute(publicKeyEndpoint);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity responseEntity = response.getEntity();

                if (responseEntity != null) {
                    Map<String, JWTPublicKey> newMap = parseEndpointResult(EntityUtils.toString(responseEntity));
                    lastUpdate = System.currentTimeMillis();
                    LOGGER.debug("Updated last public key refresh call time stamp to [{}]", lastUpdate);
                    hasChanged = !newMap.keySet().equals(keyMap.keySet());
                    if (hasChanged) {
                        LOGGER.debug("Updating cached public keys removing keys {} adding keys {}",
                                keyMap.keySet().removeAll(newMap.keySet()),
                                newMap.keySet().removeAll(keyMap.keySet()));
                        keyMap = newMap;
                    } else {
                        LOGGER.trace("No new keys have been found");
                    }

                    return hasChanged;
                }
                LOGGER.error("Public key endpoint response didn't have a body");
            }
            LOGGER.error("Cannot reach public key endpoint, response code [{}]", response.getStatusLine().getStatusCode());
            throw new InternalLogicException("Error occurred when reaching out to PublicKey Endpoint");
        } catch (IOException e) {
            throw new InternalLogicException("Cannot reach PublicKey Endpoint");
        }
    }

    protected Map<String, JWTPublicKey> parseEndpointResult(String resultString) {
        try {
            JSONObject result = new JSONObject(new JSONTokener(resultString));
            int status = result.getInt("status");
            LOGGER.trace("Repose body status [{}]", status);
            if (status == 200) {
                Map<String, JWTPublicKey> newMap = new HashMap<>();
                JSONArray data = result.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jwtPublicKeyObject = data.getJSONObject(i);


                    JWTPublicKey jwtPublicKey = new JWTPublicKey(
                            jwtPublicKeyObject.getString("kid"),
                            parseToPublicKey(jwtPublicKeyObject.getString("key")),
                            new Date(jwtPublicKeyObject.getLong("exp"))
                    );

                    newMap.put(jwtPublicKey.getId(), jwtPublicKey);
                }
                LOGGER.trace("Received public kid {}", newMap.keySet());
                return newMap;
            }
            LOGGER.error("PublicKey Endpoint returned the error [{}] [{}]", status, result.getJSONObject("error").getString("message"));
            throw new InternalLogicException("PublicKey Endpoint returned the error " + status + " " + result.getString("message"));
        } catch (JSONException ex) {
            LOGGER.error("Cannot parse json result from PublicKey Endpoint");
            throw new InternalLogicException("Cannot parse json result from PublicKey Endpoint");
        }
    }

    protected PublicKey parseToPublicKey(String key) {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalLogicException("Invalid public key parsing key format");
        } catch (InvalidKeySpecException ex) {
            throw new InternalLogicException("The public key provided by the PublicKey Endpoint cannot be parsed to a key");
        } catch (IllegalArgumentException ex) {
            throw new InternalLogicException("The public key provided by the PublicKey Endpoint is not Base64 encoded");
        }

    }
}
