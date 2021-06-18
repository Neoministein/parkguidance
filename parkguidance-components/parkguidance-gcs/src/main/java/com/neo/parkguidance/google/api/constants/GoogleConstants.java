package com.neo.parkguidance.google.api.constants;

import com.neo.parkguidance.core.entity.Address;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class GoogleConstants {

    public static final String JSON = "json?";
    public static final String XML = "xml?";
    public static final String KEY = "&key=";
    public static final String QUERY = "&query=";
    public static final String ORIGIN = "&origin=";
    public static final String DIRECTION = "&dir=";

    public static final String E_TRY_AGAIN = "Please try again in a couple of minutes";
    public static final String E_INVALID_ADDRESS = "This doesn't seem to be a valid address";
    public static final String E_SYS_ADMIN = "Please contact a system administrator: ";
    public static final String E_EXTERNAL_ERROR = "External Server ERROR Please contact a system administrator: ";
    public static final String E_INTERNAL_ERROR = "Internal Server ERROR Please contact a system administrator:";

    public static final String ELASTIC_INDEX = "gcs";

    private GoogleConstants() {}

    public static String addressQuery(Address address) throws RuntimeException{
        String query =  address.getStreet() + "+" +
                address.getNumber() + "+" +
                address.getCityName() + "+" +
                address.getPlz();

        try {
            return URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(StandardCharsets.UTF_8.toString() + " is not supported");
        }
    }

    public static String elasticLog(String type, String query) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("query", query);
        jsonObject.put("timestamp", new Date().getTime());
        return jsonObject.toString();
    }
}
