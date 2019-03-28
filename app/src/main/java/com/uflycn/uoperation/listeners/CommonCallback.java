package com.uflycn.uoperation.listeners;

import android.util.Log;

import com.uflycn.uoperation.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/18.
 */
public abstract class CommonCallback<T> implements Callback<T>{

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response == null || response.body() == null){
            Log.e("onResponse","CommonCallback  response.body() == null");
            return;
        }
        handleResponse(response);
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        if(t != null){
            Log.e("onFailure",""+t.getMessage());
        }
//        ToastUtil.show("获取更新信息失败！");
    }

    public abstract void handleResponse(Response<T> response);
}
