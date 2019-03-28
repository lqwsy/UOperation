package com.uflycn.uoperation.bean;

import java.io.Serializable;

/**
 * Created by Ryan on 2016/7/5.
 */



public class BaseEntity<T> implements Serializable {

    // 成功:1     其他为错误代码
    private int code;

    // 错误信息
    private String message;

    // 内容实体
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}