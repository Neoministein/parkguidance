package com.neo.parkguidance.core.api;

public class HTTPResponse {

    private Integer code;

    private String message;

    private String body;

    public HTTPResponse()  {}
    public HTTPResponse(Integer code, String  message, String body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
