package com.neo.parkguidance.core.api;

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

    private Integer responseCode;

    private String responseInput;

    public HTTPRequest() {
        requestBody = "";
    }

    public HTTPRequest(String url, String requestMethod) {
        super();
        this.url = url;
        this.requestMethod = requestMethod;
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

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseInput() {
        return responseInput;
    }

    public void setResponseInput(String responseInput) {
        this.responseInput = responseInput;
    }
}
