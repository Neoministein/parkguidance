package com.neo.parkguidance.api.connection;

import com.neo.parkguidance.web.infra.table.RandomString;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class ServletConnetion {

    public static void main(String[] args) throws Exception {
        URL url = new URL("http://localhost:8080/ParkGuidance-1.0-SNAPSHOT//baseServlet");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        String sj = new RandomString(32).nextString();
        System.out.println(sj);
        byte[] out = sj.getBytes(StandardCharsets.UTF_8);
        int length = out.length;
        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        System.out.println(http.getResponseCode() + "|" +http.getResponseMessage());

    }
}
