package com.uflycn.uoperation.http;

import android.util.Log;

import com.uflycn.uoperation.util.LogUtils;
import com.uflycn.uoperation.util.ShareUtil;
import com.uflycn.uoperation.util.UIUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 系统异常退出专用
 * Created by Administrator on 2018/3/1.
 */
public class RetrofitManagerLoginOut {
    private Retrofit mRetrofit;
    private String mToken;
    private LogUtils mLogUtils = LogUtils.getInstance();


    private RetrofitManagerLoginOut() {
        //String url = "http://wanandroid.com/tools/mockapi/3632/";
        String url = "http://" + ShareUtil.getString(UIUtils.getContext(), "ip", "") + ":" + ShareUtil.getString(UIUtils.getContext(), "port", "");
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request origin = chain.request();
                mToken = ShareUtil.getString(UIUtils.getContext(), "LoginToken", "");
                Request request = origin.newBuilder().addHeader("Authorization", mToken).build();
                return chain.proceed(request);
            }
        }).build();

        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .client(client)
                .build();
    }

    public String getToken() {
        return mToken;
    }


    public void resetRetrofit() {
        SingletonHolder.sInstance = new RetrofitManagerLoginOut();
    }


    public static RetrofitManagerLoginOut getInstance() {
        return SingletonHolder.sInstance;
    }

    public <T> T getService(Class<T> tclass) {
        return mRetrofit.create(tclass);
    }

    private static class SingletonHolder {
        private static RetrofitManagerLoginOut sInstance = new RetrofitManagerLoginOut();
    }

    public Retrofit getRetrofit() {
        if (mRetrofit != null) {
            return mRetrofit;
        }
        Log.e("err", "mRetrofit:==null ");
        return null;
    }
}
