package com.uflycn.uoperation.ui.fragment.myMission.model;

import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.PlanPatrolExecution;
import com.uflycn.uoperation.bean.PlanPatrolExecutionTowerList;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.myMission.MyMissionListen;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyMissionModelImp implements MyMissionModel {
    private Call<BaseCallBack<List<PlanPatrolExecution>>> mCall;
    private Call<BaseCallBack<List<PlanPatrolExecutionWorkRecordInfo>>> mCall1;
    private Call<BaseCallBack<List<PlanPatrolExecutionTowerList>>> mCall2;

    @Override
    public void getPlanPatrolExecutionList(final MyMissionListen.getPlanPatrolExecutionListListener listener) {

        mCall = RetrofitManager.getInstance().getService(ApiService.class).getPlanPatrolExecutionList();
        mCall.enqueue(new Callback<BaseCallBack<List<PlanPatrolExecution>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<PlanPatrolExecution>>> call, Response<BaseCallBack<List<PlanPatrolExecution>>> response) {
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
            public void onFailure(Call<BaseCallBack<List<PlanPatrolExecution>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    listener.onFailed("网络请求出错!");
                }
            }
        });

    }

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
    public void cancel() {
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
        if (mCall1 != null && !mCall1.isCanceled()) {
            mCall1.cancel();
        }
        if (mCall2 != null && !mCall2.isCanceled()) {
            mCall2.cancel();
        }
    }

    @Override
    public void getPlanPatrolExecutionLine(String id, final MyMissionListen.getPlanPatrolExecutionLineListListener listener) {
        mCall2 = RetrofitManager.getInstance().getService(ApiService.class).getPlanPatrolExecutionLineTowerList(id);
        mCall2.enqueue(new Callback<BaseCallBack<List<PlanPatrolExecutionTowerList>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<PlanPatrolExecutionTowerList>>> call, Response<BaseCallBack<List<PlanPatrolExecutionTowerList>>> response) {
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
            public void onFailure(Call<BaseCallBack<List<PlanPatrolExecutionTowerList>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    listener.onFailed("网络请求出错!");
                }
            }
        });
    }
}
