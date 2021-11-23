package com.neo.parkguidance.microservices.impl.rest.v1;

import org.json.JSONArray;
import org.json.JSONObject;

public class DefaultV1Response {

    private DefaultV1Response() {}

    public static JSONObject success(JSONArray data, String context) {
        JSONObject response = new JSONObject();
        response.put("status", 200);
        response.put("apiVersion", "1.0");
        response.put("context", context);
        response.put("data", data);

        return response;
    }

    public static JSONObject error(Exception ex, String context) {
        JSONObject response = new JSONObject();
        response.put("status", 200);
        response.put("apiVersion", "1.0");
        response.put("context", context);
        response.put("error", errorObject(500, ex));

        return response;
    }

    private static JSONObject errorObject(int code, Exception ex) {
        JSONObject errorObject = new JSONObject();
        errorObject.put("code" ,code);
        errorObject.put("message", ex.getMessage());
        return errorObject;
    }
}
