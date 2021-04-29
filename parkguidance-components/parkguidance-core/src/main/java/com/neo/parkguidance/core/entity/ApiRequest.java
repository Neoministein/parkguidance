package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = ApiRequest.TABLE_NAME)
public class ApiRequest implements DataBaseEntity<ApiRequest> {

    public static final String TABLE_NAME = "apiRequest";
    public static final String C_DATE = "date";
    public static final String C_URL = "url";
    public static final String C_REQUEST_METHOD = "requestMethod";
    public static final String C_REQUEST_BODY = "requestBody";
    public static final String C_RESPONSE_CODE = "responseCode";

    @Id
    @Column(name = C_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = C_DATE, nullable = false)
    private Date date;

    @Column(name = C_URL, length = 500, nullable = false)
    private String url;

    @Column(name = C_REQUEST_METHOD, nullable = false)
    private String requestMethod;

    @Column(name = C_REQUEST_BODY, nullable = false)
    private String requestBody;

    @Column(name = C_RESPONSE_CODE, nullable = false)
    private Integer responseCode;

    @Transient
    private String responseInput;

    public ApiRequest() {
        requestBody = "";
        date = new Date();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    @Override
    public boolean compareValues(ApiRequest o) {
        if(!date.equals(o.date)) {
            return false;
        }
        if(!url.equals(o.getUrl())) {
            return false;
        }
        if(!requestMethod.equals(o.getRequestMethod())) {
            return false;
        }

        if(!requestBody.equals(o.getRequestBody())) {
            return false;
        }
        return responseCode.equals(o.getResponseCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ApiRequest that = (ApiRequest) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
