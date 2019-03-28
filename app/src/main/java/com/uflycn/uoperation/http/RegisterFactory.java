package com.uflycn.uoperation.http;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Ryan on 2016/7/13.
 */
public class RegisterFactory {
    private static String baseUrl = "http://120.78.188.24:88/";

    private static OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1000, TimeUnit.MILLISECONDS)
            .readTimeout(750,TimeUnit.MILLISECONDS)
            .writeTimeout(750,TimeUnit.MILLISECONDS)
            .build();

    private static Retrofit mRetrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(client)
            .build();

    public static RegisterService getRegisterService() {
        RegisterService service = mRetrofit.create(RegisterService.class);
        return service;
    }

    public static UpgradeService getUpgraService() {
        UpgradeService service = mRetrofit.create(UpgradeService.class);
        return service;
    }
}
