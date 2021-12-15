package com.neo.parkguidance.microservices.impl.rest;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.core.Response;

public class DefaultV1Response {

    private DefaultV1Response() {}

    protected static JSONObject defaultResponse(String content) {
        JSONObject responseMessage = new JSONObject();
        responseMessage.put("status", 200);
        responseMessage.put("apiVersion", "1.0");
        responseMessage.put("context", content);
        return responseMessage;
    }

    public static Response success(String context) {
        return Response.ok().entity(defaultResponse(context).toString()).build();
    }

    public static Response success(JSONArray data, String context) {
        JSONObject responseMessage = defaultResponse(context);
        responseMessage.put("data", data);

        return Response.ok().entity(responseMessage.toString()).build();
    }

    public static Response error(String message, String context) {
        JSONObject response = defaultResponse(context);
        response.put("error", errorObject(500, message));

        return Response.ok().entity(response.toString()).build();
    }

    public static Response error(JSONObject error, String context) {
        JSONObject response = defaultResponse(context);
        response.put("error", error);

        return Response.ok().entity(response.toString()).build();
    }

    public static JSONObject errorObject(int code, Exception ex) {
        return errorObject(code, ex.getMessage());
    }

    public static JSONObject errorObject(int code, String message) {
        JSONObject errorObject = new JSONObject();
        errorObject.put("code", code);
        errorObject.put("message", message);
        return errorObject;
    }
}
