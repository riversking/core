package com.rivers.core.bean;

import java.io.Serializable;

public class LoginUser implements Serializable {

    private Integer id;

    private String userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
