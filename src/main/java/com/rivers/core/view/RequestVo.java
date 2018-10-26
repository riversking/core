package com.rivers.core.view;

public class RequestVo<T> {
    private String appKey;
    private String sign;
    private String timestamp;
    private String format;
    private String accessToken;
    private T param;
    private Object clientInfo;

    public RequestVo() {
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public T getParam() {
        return this.param;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setParam(T param) {
        this.param = param;
    }

    public Object getClientInfo() {
        return this.clientInfo;
    }

    public void setClientInfo(Object clientInfo) {
        this.clientInfo = clientInfo;
    }
}
