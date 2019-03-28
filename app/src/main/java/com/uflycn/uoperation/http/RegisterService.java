package com.uflycn.uoperation.http;


import com.uflycn.uoperation.bean.BaseEntity;
import com.uflycn.uoperation.bean.RegistrationInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Ryan on 2016/7/13.
 */
public interface RegisterService {

    /**
     * 提交注册信息
     * @param registerInfo bean包下的RegisterInfo类的json字符串
     * @return 已注册:1  等待审核:2  未注册:3 过期:4 被拒绝:5
     */
    @FormUrlEncoded
    @POST("Register/Register")
    Call<BaseEntity<String>> register(@Field("registerInfo") String registerInfo);

    /**
     * 检查本机状态
     * @return
     */
    @FormUrlEncoded
    @POST("Register/CheckStatus")
    Call<BaseEntity<String>> checkStatus(@Field("deviceNo") String deviceNo, @Field("productName") String productName);

    /**
     * 获取注册码信息
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("Register/GetRegistrationInfoNew")
    Call<BaseEntity<List<RegistrationInfo>>> getRegisterCode(@Field("deviceNo") String deviceNo, @Field("productName") String productName);

    /**
     * 获取注册码信息
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("Register/RenewalApply")
    Call<BaseEntity<String>> renew(@Field("deviceNo") String deviceNo, @Field("productName") String productName);

    @FormUrlEncoded
    @POST("Register/GetRegistrationInfo")
    Call<BaseEntity<List<RegistrationInfo>>> getRegisterInfo(@Field("deviceNo") String deviceNo, @Field("productName") String productName);

}
