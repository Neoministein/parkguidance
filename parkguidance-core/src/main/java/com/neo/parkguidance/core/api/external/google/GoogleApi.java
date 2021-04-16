package com.neo.parkguidance.core.api.external.google;

import com.neo.parkguidance.core.entity.Address;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GoogleApi {

    public static final String JSON = "json?";
    public static final String XML = "xml?";
    public static final String KEY = "&key=";

    public static final String E_TRY_AGAIN = "Please try again in a couple of minutes";
    public static final String E_UNVALID_ADDRESS = "This doesn't seem to be a valid address";
    public static final String E_SYS_ADMIN = "Please contact a system administrator: ";
    public static final String E_EXTERNAL_ERROR = "External Server ERROR Please contact a system administrator: ";
    public static final String E_INTERNAL_ERROR = "Internal Server ERROR Please contact a system administrator:";

    private GoogleApi() {}

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
}
