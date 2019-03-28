package com.uflycn.uoperation.ui.activity.workManage.model;

import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordList;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.myMission.MyMissionListen;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWorkManageModelImp implements AddWorkManageModel {

    private Call<BaseCallBack<List<PlanPatrolExecutionWorkRecordInfo>>> mCall1;
    private Call<BaseCallBack<List<PlanPatrolExecutionWorkRecordList>>> mCall2;



    @Override
    public void getPlanPatrolExecutionRecordInfo(String id, final MyMissionListen.getPlanPatrolExecutionRecordInfoListener listener) {
        mCall1 = RetrofitManager.getInstance().getService(ApiService.class).getPlanPatrolExecutionWorkRecordInfo(id);
        mCall1.enqueue(new Callback<BaseCallBack<List<PlanPatrolExecutionWorkRecordInfo>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<PlanPatrolExecutionWorkRecordInfo>>> call, Response<BaseCallBack<List<PlanPatrolExecutionWorkRecordInfo>>> response) {
                if (response != null && response.body() != null) {
                    if (response.body().getCode() == 1) {
                        listener.onSuccess(response.body().getData());
                    } else {
                        listener.onFailed(response.body().getMessage());
                    }
                } else {
                    listener.onFailed("获取数据失败!");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<PlanPatrolExecutionWorkRecordInfo>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    listener.onFailed("网络请求出错!");
                }
            }
        });
    }

    @Override
    public void getPlanPatrolExecutionWorkRecordList(final MyMissionListen.getPlanPatrolExecutionWorkRecordListListener listener) {
        mCall2 = RetrofitManager.getInstance().getService(ApiService.class).getPlanPatrolExecutionWorkRecordList();
        mCall2.enqueue(new Callback<BaseCallBack<List<PlanPatrolExecutionWorkRecordList>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<PlanPatrolExecutionWorkRecordList>>> call, Response<BaseCallBack<List<PlanPatrolExecutionWorkRecordList>>> response) {
                if (response != null && response.body() != null) {
                    if (response.body().getCode() == 1) {
                        listener.onSuccess(response.body().getData());
                    } else {
                        listener.onFailed(response.body().getMessage());
                    }
                } else {
                    listener.onFailed("获取数据失败!");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<PlanPatrolExecutionWorkRecordList>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    listener.onFailed("网络请求出错!");
                }
            }
        });

    }


    @Override
    public void cancel() {
        if (mCall1 != null && !mCall1.isCanceled()) {
            mCall1.cancel();
        }
    }
}
