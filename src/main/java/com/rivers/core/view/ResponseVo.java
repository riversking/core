package com.rivers.core.view;

import org.springframework.util.StringUtils;

import java.sql.Timestamp;

public class ResponseVo {
    private String message;
    private String code;
    private Timestamp timestamp;
    private boolean success;
    private Object data;

    protected ResponseVo(boolean success, String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.data = data;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    protected ResponseVo(boolean success, String code, String message) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String getCode() {
        return this.isSuccess() ? "0" : this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return StringUtils.isEmpty(this.message) ? (this.isSuccess() ? "请求成功" : this.message) : this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResponseVo ok() {
        return new ResponseVo(true, (String) null, (String) null);
    }

    public static ResponseVo ok(Object data) {
        return new ResponseVo(true, (String) null, (String) null, data);
    }

    public static ResponseVo fail(String errorCode, String message) {
        return new ResponseVo(false, errorCode, message);
    }

    public static ResponseVo fail(String errorCode, String message, Object data) {
        return new ResponseVo(false, errorCode, message, data);
    }
}
