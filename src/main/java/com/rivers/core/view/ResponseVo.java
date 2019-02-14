package com.rivers.core.view;

public class ResponseVo {
    private String msg;
    private String code;
    private Object rsp;

    protected ResponseVo(final String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    protected ResponseVo(final String code, String msg, Object rsp) {
        this.code = code;
        this.msg = msg;
        this.rsp = rsp;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getRsp() {
        return this.rsp;
    }

    public void setRsp(Object rsp) {
        this.rsp = rsp;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ResponseVo ok() {
        return new ResponseVo("0", (String) null, (Object) null);
    }

    public static ResponseVo ok(Object rsp) {
        return new ResponseVo("0", "查询成功", rsp);
    }

    public static ResponseVo fail(String errorCode, String message) {
        return new ResponseVo(errorCode, message);
    }

    public static ResponseVo fail(String errorCode, String message, Object rsp) {
        return new ResponseVo(errorCode, message, rsp);
    }

    public static ResponseVo systemFail(String message) {
        return new ResponseVo("500", message, (Object) null);
    }
}
