package com.neo.parkguidance.core.api;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class HTTPRequestSender {

    public HTTPResponse call(HTTPRequest request) {
        try {
            URLConnection con = new URL((request.getUrl())).openConnection();
            HttpURLConnection connection = (HttpURLConnection) con;
            connection.setRequestMethod(request.getRequestMethod());
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

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

            return getHttpResponse(connection);
        }catch (IOException ex) {
            return new HTTPResponse(-1,"Unable to correctly", ex.getMessage());
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
            response.setCode(connection.getResponseCode());
            response.setMessage(connection.getResponseMessage());
            responseStream = connection.getErrorStream();
        }
        response.setBody(IOUtils.toString(responseStream, connection.getContentEncoding()));
        return response;
    }
}
