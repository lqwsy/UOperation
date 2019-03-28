package com.uflycn.uoperation.ui.fragment.hiddendanger.model;

import android.util.Log;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.TreeBarrierBean;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.hiddendanger.HiddenDangerListener;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 隐患管理模型-新增树障缺陷-获取树障列表
 */
public class HiddenDangerModelImp implements HiddenDangerModel {
    private List<Call> mRequestList = new ArrayList<>();
    private Call<DefectInfo> mSubmitCall;

    /**
     * 新增树障缺陷
     */
    @Override
    public void createTreeBarrierDefect(TreeBarrierBean treeBarrierBean, List<MultipartBody.Part> requestImgParts, final HiddenDangerListener.CreateTreeBarrierDefectListener listener) {
        Gson gson = new Gson();
        String json = gson.toJson(treeBarrierBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        final Call call;
        call = RetrofitManager.getInstance().getService(ApiService.class).postAddTreeBarrierDefect(body, requestImgParts);
        cancelAll();
        mRequestList.add(call);
        call.enqueue(new Callback<BaseCallBack<Integer>>() {
            @Override
            public void onResponse(Call<BaseCallBack<Integer>> call, Response<BaseCallBack<Integer>> response) {
                if (response == null || response.body() == null) {
                    listener.onFail("返回数据失败!");
                    return;
                }
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    listener.onSuccess(null);
                } else if (response.body() != null && response.body().getCode() == 401) {
                    listener.onFail("登录失效");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<Integer>> call, Throwable t) {
                if (!call.isCanceled()) {
                    listener.onFail("网络请求出错");
                }
            }
        });
        mRequestList.add(call);
    }

    /**
     * 获取树障列表
     */
    @Override
    public void getTreeBarrierDefectList(String LineId, String Category, String Status, String lineName, String towerID, final HiddenDangerListener.GetTreeBarrierDefectListListener listener) {
        mSubmitCall = RetrofitManager.getInstance().getService(ApiService.class).getLineDefectList(LineId, Category, Status,"",lineName,towerID,"","","");
        mSubmitCall.enqueue(new Callback<DefectInfo>() {
            @Override
            public void onResponse(Call<DefectInfo> call, Response<DefectInfo> response) {
                if (response == null || response.body() == null) {
                    listener.onFail("返回数据失败!");
                    return;
                }
                if (response.body().getCode() == 0) {
                    listener.onFail("获取数据失败,参数错误!");
                } else if (response.body() != null && response.body().getCode() == 401) {
                    listener.onFail("登录失效!");
                } else {
                    DefectInfo defectInfo = response.body();
                    listener.onSuccess(defectInfo);
                }
            }

            @Override
            public void onFailure(Call<DefectInfo> call, Throwable t) {
                listener.onFail("请求失败！");
            }
        });
    }

    @Override
    public void cancelAll() {
        for (Call call : mRequestList) {
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
    }
}
