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

    public void call(HTTPRequest request) {
        try {
            URLConnection con = new URL((request.getUrl())).openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod(request.getRequestMethod());
            http.setRequestProperty("Accept-Charset", "UTF-8");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            if(request.getRequestBody() != null && !request.getRequestBody().equals("")) {
                http.setDoOutput(true);
                byte[] out = request.getRequestBody().getBytes(StandardCharsets.UTF_8);
                int length = out.length;
                http.setFixedLengthStreamingMode(length);
                http.connect();
                try(OutputStream os = http.getOutputStream()) {
                    os.write(out);
                }
            } else {
                http.connect();
            }

            request.setResponseCode(http.getResponseCode());
            try(InputStream is = http.getInputStream()) {
                request.setResponseInput(IOUtils.toString(is, StandardCharsets.UTF_8));
            }

        }catch (IOException ex) {
            request.setResponseCode(-1);
        }
    }
}
