package com.neo.parkguidance.ms.security.impl.authentication;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

public abstract class AbstractJWTTest {

    private static final Logger LOGGER =  LoggerFactory.getLogger(AbstractJWTTest.class);

    private static String privateKey;
    private static String publicKey;


    @BeforeAll
    public static void retrieveKeys() {
        try {
            StringBuilder sb = new StringBuilder();
            ClassLoader classLoader = MethodHandles.lookup().lookupClass().getClassLoader();


            InputStream inputStream = classLoader.getResourceAsStream("jwt-keys.json");
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            for (String line; (line = reader.readLine()) != null;) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(new JSONTokener(sb.toString()));
            privateKey = jsonObject.getString("private");
            publicKey = jsonObject.getString("public");
        } catch ( NullPointerException| IOException | JSONException ex) {
            LOGGER.error("Unable to retrieve private key from resources");
            throw new InternalLogicException();
        }

    }

    protected String getPublicKey() {
        return publicKey;
    }

    protected String getPrivateKey() {
        return privateKey;
    }
}
