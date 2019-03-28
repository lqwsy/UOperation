package com.uflycn.uoperation.ui.fragment.cleardefectlist.model;

import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.WorksheetApanageTask;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.cleardefectlist.ClearDefectListListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/3/6.
 */
public class ClearDefectListModelImpl implements ClearDefectListModel{

    @Override
    public void getClearDefectList(String num, final ClearDefectListListener clearDefectListListener) {
        //http://wanandroid.com/tools/mockapi/3632/123qwe
        Call<BaseCallBack<List<WorksheetApanageTask>>> mCall = RetrofitManager.getInstance().getService(ApiService.class).getMyWorksheetApanageTask();
        mCall.enqueue(new Callback<BaseCallBack<List<WorksheetApanageTask>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<WorksheetApanageTask>>> call, Response<BaseCallBack<List<WorksheetApanageTask>>> response) {
                if (response.body() == null) {
                    return;
                }

                if (response.body() != null && response.body().getCode() == 1) {
                    clearDefectListListener.onSeccess(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<WorksheetApanageTask>>> call, Throwable t) {
                clearDefectListListener.onFailed("网络连接失败");
            }
        });
    }
}
