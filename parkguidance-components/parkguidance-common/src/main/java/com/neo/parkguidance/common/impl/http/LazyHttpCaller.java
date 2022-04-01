package com.neo.parkguidance.common.impl.http;

import com.neo.parkguidance.common.api.action.Action;
import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import com.neo.parkguidance.common.api.http.ResponseFormatVerification;
import com.neo.parkguidance.common.impl.util.LazyAction;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * This utility class handles sending and retrying http calls through a apache http client.
 */
public class LazyHttpCaller {

    private LazyHttpCaller() {}

    /**
     * Executes the httpUriRequest via the httpClient and verifies if the response format is correct and reties if it fails
     *
     * @param httpClient the http client which send the request
     * @param httpUriRequest the request to send
     * @param formatVerifier the formatter to check the response against
     * @param retries the amount of retries available
     *
     * @throws InternalLogicException if it fails and no more retries are available
     *
     * @return the response message
     */
    public static String call(HttpClient httpClient, HttpUriRequest httpUriRequest, ResponseFormatVerification formatVerifier, int retries) {
        Action<String> action = () -> {
            try {
                HttpResponse httpResponse = httpClient.execute(httpUriRequest);
                if (httpResponse.getStatusLine().getStatusCode() != 200) {
                    throw new InternalLogicException("Http call failed "
                            + "code " + httpResponse.getStatusLine().getStatusCode()
                            + "message " + httpResponse.getStatusLine().getReasonPhrase());
                }
                HttpEntity responseEntity = httpResponse.getEntity();

                String message = EntityUtils.toString(responseEntity);

                if (!formatVerifier.verify(message)) {
                    throw new InternalLogicException("The http message does not meet the required format");
                }
                return message;
            } catch (IOException | ParseException ex) {
                throw new InternalLogicException("Error while during lazy http call reason " + ex.getMessage());
            }
        };

        return LazyAction.call(action, retries);
    }
}
