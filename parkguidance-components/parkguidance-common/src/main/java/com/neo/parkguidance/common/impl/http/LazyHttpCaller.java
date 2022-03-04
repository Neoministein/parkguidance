package com.neo.parkguidance.common.impl.http;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import com.neo.parkguidance.common.impl.http.verify.ResponseFormatVerification;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This utility class handles sending and retrying http calls through a apache http client.
 */
public class LazyHttpCaller {

    private static final Logger LOGGER =  LoggerFactory.getLogger(LazyHttpCaller.class);

    private static final int MAX_WAIT_IN_MILLI = 2 * 1000;

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
        return call(httpClient, httpUriRequest, formatVerifier, retries, 0);
    }

    protected static String call(HttpClient httpClient, HttpUriRequest httpUriRequest,
            ResponseFormatVerification formatVerifier, int retries, int count) {
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
        } catch (IOException | ParseException | InternalLogicException ex) {
            LOGGER.warn("Error while during lazy http call reason [{}]",  ex.getMessage());
            if (retries <= count) {
                throw new InternalLogicException("Lazy http client cannot fulfill request");
            }
            wait(count);
            return call(httpClient, httpUriRequest, formatVerifier,  retries, count+1);
        }
    }

    protected static void wait(int count) {
        long waitFor = (long) (100 * (Math.pow(2, count)));
        if (waitFor > MAX_WAIT_IN_MILLI) {
            waitFor = MAX_WAIT_IN_MILLI;
        }

        try {
            Thread.sleep(waitFor);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
