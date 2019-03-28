package com.uflycn.uoperation.bean;

/**
 * Created by Administrator on 2017/9/6.
 */
public class BaseCallBack<T> {
    private int Code;
    private String Message;
    private T Data;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
