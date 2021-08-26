package com.neo.parkguidance.core.impl.http;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Handles the sending and receiving of HTTP requests
 */
public class HTTPRequestSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPRequestSender.class);

    public HTTPResponse call(HTTPRequest request) {
        LOGGER.debug("Sending HTTP {} request", request.getRequestMethod());
        try {
            URLConnection con = new URL((request.getUrl())).openConnection();
            HttpURLConnection connection = (HttpURLConnection) con;
            connection.setRequestMethod(request.getRequestMethod());

            for (Map.Entry<String,String> entry: request.getRequestProperty().entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }

            if(request.getRequestBody() != null && !request.getRequestBody().equals("")) {
                connection.setDoOutput(true);
                byte[] out = request.getRequestBody().getBytes(StandardCharsets.UTF_8);
                int length = out.length;
                connection.setFixedLengthStreamingMode(length);
                connection.connect();
                try(OutputStream os = connection.getOutputStream()) {
                    os.write(out);
                }
            } else {
                connection.connect();
            }
            LOGGER.debug("HTTP request connected");

            return getHttpResponse(connection);
        }catch (IOException ex) {
            LOGGER.error("Something went wrong with the HTTP request {}", ex.getMessage());
            return new HTTPResponse(-1,"Something went wrong with the HTTP request", ex.getMessage());
        }
    }

    private HTTPResponse getHttpResponse(HttpURLConnection connection) throws IOException{
        HTTPResponse response = new HTTPResponse();
        InputStream responseStream;

        try
        {
            response.setCode(connection.getResponseCode());
            response.setMessage(connection.getResponseMessage());
            responseStream = connection.getInputStream();
        }
        catch(IOException e)
        {
            LOGGER.warn("Receiving HTTP response from error stream");
            response.setCode(connection.getResponseCode());
            response.setMessage(connection.getResponseMessage());
            responseStream = connection.getErrorStream();
        }
        response.setBody(IOUtils.toString(responseStream, connection.getContentEncoding()));
        return response;
    }
}
