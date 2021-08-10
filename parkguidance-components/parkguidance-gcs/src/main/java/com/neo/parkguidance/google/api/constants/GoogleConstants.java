package com.neo.parkguidance.google.api.constants;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.impl.utils.StringUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Contains for the google cloud platform
 */
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

    public static final String E_GOOGLE_CLOUD_PLATFORM = "Google Cloud Platform ERROR [{}]";

    public static final String ELASTIC_INDEX = "gcs";

    private GoogleConstants() {}

    /**
     * Formats an {@link Address} to be sent to google could platform
     *
     * @param address address to be formatted
     * @return the formatted address
     */
    public static String addressQuery(Address address) {
        StringBuilder query = new StringBuilder();
        if (!StringUtils.isEmpty(address.getStreet())) {
            query.append(address.getStreet());
        }

        if (address.getNumber() != null) {
            query.append('+');
            query.append(address.getNumber());
        }

        if (!StringUtils.isEmpty(address.getCityName())) {
            query.append('+');
            query.append(address.getCityName());
        }

        if (address.getPlz() != null) {
            query.append('+');
            query.append(address.getPlz());
        }
        try {
            return URLEncoder.encode(query.toString(), StandardCharsets.UTF_8.toString());
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
