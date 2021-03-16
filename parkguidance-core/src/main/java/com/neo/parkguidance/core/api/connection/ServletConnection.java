package com.neo.parkguidance.core.api.connection;

import com.neo.parkguidance.core.entity.ParkingGarage;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class ServletConnection {

    public static void main(String[] args) throws Exception {
        URL url = new URL("http://localhost:8080/park-guidance/park-data/receiver/api");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        String content = getContent();
        byte[] out = content.getBytes(StandardCharsets.UTF_8);
        int length = out.length;
        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        System.out.println(http.getResponseCode() + "|" +http.getResponseMessage());

    }

    public static String getContent() {
        JSONObject content = new JSONObject();

        content.put(ParkingGarage.C_ACCESS_KEY, "abc");
        content.put("type","incr");
      //content.put("type","decr");

        return content.toString();
    }
}
