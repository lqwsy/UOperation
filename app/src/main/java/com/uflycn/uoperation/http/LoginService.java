package com.uflycn.uoperation.http;

import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.LoginCallbackEntity;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
//spinner
/**
 * Created by Administrator on 2017/9/7.
 */
public interface LoginService {
    /**
     * 登陆
     *
     * @param account
     * @param password
     * @return
     */
    @POST("Login/Login")
    @FormUrlEncoded
    Call<BaseCallBack<LoginCallbackEntity>> doLogin(@Field("account") String account,
                                                    @Field("password") String password,
                                                    @Field("application") String application,
                                                    @Field("version") String version,
                                                    @Field("terminalCode") String terminalCode);

    /**
     * 退出
     *
     * @return
     */
    @POST("Login/Logout")
    @FormUrlEncoded
    Call<BaseCallBack<String>> doLogout(@Field("LoginToken") String LoginToken);


    /**
     * 修改密码
     * @param OldPassword 旧密码
     * @param NewPassword 新密码
     * @param ConfirmPassword 确认密码
     * @return
     */
    @POST("Common/ChangePassword")
    @FormUrlEncoded
    Call<BaseCallBack<String>> changePwd(@Field("OldPassword") String OldPassword,
                                         @Field("NewPassword") String NewPassword,
                                         @Field("ConfirmPassword") String ConfirmPassword);
}
