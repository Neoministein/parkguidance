package com.neo.parkguidance.core.api;

import com.neo.parkguidance.core.entity.ApiRequest;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.apache.commons.io.IOUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Stateless
public class HTTPRequestSender {

    @Inject
    AbstractEntityDao<ApiRequest> entityDao;

    public void call(ApiRequest request, String key) {
        try {
            call(request, new URL((request.getUrl() + key)));
        }catch (IOException ex) {
            request.setResponseCode(-1);
        }
        entityDao.create(request);
    }

    public void call(ApiRequest request) {
        try {
            call(request, new URL((request.getUrl())));
        }catch (IOException ex) {
            request.setResponseCode(-1);
        }
        entityDao.create(request);
    }

    protected void call(ApiRequest request, URL url) {
        try {
            URLConnection con = url.openConnection();
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

            if(request.getResponseCode() == HttpServletResponse.SC_OK) {
                try(InputStream is = http.getInputStream()) {
                    request.setResponseInput(IOUtils.toString(is, StandardCharsets.UTF_8));
                }
            }
        }catch (IOException ex) {
            request.setResponseCode(-1);
        }
    }
}
