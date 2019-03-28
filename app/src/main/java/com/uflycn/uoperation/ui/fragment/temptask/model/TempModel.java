package com.uflycn.uoperation.ui.fragment.temptask.model;

import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.TempTask;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.temptask.contract.TempTaskContract;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public class TempModel implements TempTaskContract.Model {
    private Call<BaseCallBack<List<TempTask>>> tempTaskCall;
    private Call<BaseCallBack<String>> readMessageCall;
    @Override
    public void getTempTaskList(String lineName,TempTaskCallBack callBack) {
        queryTempTaskList(lineName,callBack);
    }

    private void queryTempTaskList(String title, final TempTaskCallBack callBack) {
            tempTaskCall = RetrofitManager.getInstance().getService(ApiService.class).getTempTaskList(title);
            tempTaskCall.enqueue(new Callback<BaseCallBack<List<TempTask>>>() {
                @Override
                public void onResponse(Call<BaseCallBack<List<TempTask>>> call, Response<BaseCallBack<List<TempTask>>> response) {
                    if (response == null || response.body() == null) {
                        callBack.onGetTempTaskCallBack(null);
                        return;
                    }
                    if (response.body().getCode() == 0) {
                        ToastUtil.show(MyApplication.getContext(), response.body().getMessage());
                        callBack.onGetTempTaskCallBack(null);
                    } else {
                        callBack.onGetTempTaskCallBack(response.body().getData());
                    }
                }

                @Override
                public void onFailure(Call<BaseCallBack<List<TempTask>>> call, Throwable t) {
                    ToastUtil.show(MyApplication.getContext(), "网络错误！");
                    callBack.onGetTempTaskCallBack(null);
                }
            });
    }

    @Override
    public void updateReadStatus(int iSysMessageInfoId){
        readMessageCall = RetrofitManager.getInstance().getService(ApiService.class).readMessageInfo(iSysMessageInfoId);
        readMessageCall.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response == null || response.body() == null) {
                    return;
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                //ToastUtil.show(MyApplication.getContext(), "网络错误！");
            }
        });
    }
}
