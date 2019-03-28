package com.uflycn.uoperation.http;

/**
 * Created by diyang on 2017/9/11.
 */
public interface RequestCallback<T> {
    void onDataCallBack(T data);
}
