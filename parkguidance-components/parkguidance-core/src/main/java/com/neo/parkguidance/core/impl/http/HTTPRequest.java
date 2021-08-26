package com.neo.parkguidance.core.impl.http;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a container for the data when sending a HTTP request through {@link HTTPRequestSender}
 */
public class HTTPRequest {

    public static final String GET = "GET";
    public static final String HEAD = "HEAD";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String CONNECT = "CONNECT";
    public static final String OPTIONS = "OPTIONS";
    public static final String TRACE = "TRACE";

    private String url;

    private String requestMethod;

    private String requestBody;

    private Map<String,String> requestProperty;

    public HTTPRequest() {
        requestProperty = new HashMap<>();
        requestBody = "";
    }

    public HTTPRequest(String url, String requestMethod) {
        this();
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public HTTPRequest(String url, String requestMethod, String requestBody) {
        this();
        this.url = url;
        this.requestMethod = requestMethod;
        this.requestBody = requestBody;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, String> getRequestProperty() {
        return requestProperty;
    }

    public void setRequestProperty(Map<String, String> requestProperty) {
        this.requestProperty = requestProperty;
    }

    public void addRequestProperty(String key, String  value) {
        requestProperty.put(key,value);
    }


    public void addRequestProperty(Map<String,String> map) {
        requestProperty.putAll(map);
    }
}
