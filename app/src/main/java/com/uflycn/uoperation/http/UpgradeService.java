package com.uflycn.uoperation.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ryan on 2016/12/30.
 */
public interface UpgradeService {
    @POST("AppVersion/CheckUpgrade")
    Call<ResponseBody> CheckUpgrade(@Query("appName") String appName, @Query("currentVersion") String currentVersion);
}
