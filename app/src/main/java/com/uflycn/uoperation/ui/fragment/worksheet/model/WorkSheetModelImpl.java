package com.uflycn.uoperation.ui.fragment.worksheet.model;

import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.WorkSheetTask;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.BrokenDocumentDBHelper;
import com.uflycn.uoperation.greendao.DefectBeanDBHelper;
import com.uflycn.uoperation.greendao.LineCrossDBHelper;
import com.uflycn.uoperation.greendao.ProjectEntityDbHelper;
import com.uflycn.uoperation.greendao.TreeDefectDBHelper;
import com.uflycn.uoperation.greendao.WorkSheetTaskDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.worksheet.WorkSheetListener;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/1/15.
 */


public class WorkSheetModelImpl implements WorkSheetModel {
    private Call<BaseCallBack<List<WorkSheetTask>>> mWorkSheetTask;

    @Override
    public void getWeekSheet(final WorkSheetListener.loadWorkSheetListener listener) {

        mWorkSheetTask = RetrofitManager.getInstance().getService(ApiService.class).getWorksheetTask();
        mWorkSheetTask.enqueue(new Callback<BaseCallBack<List<WorkSheetTask>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<WorkSheetTask>>> call, Response<BaseCallBack<List<WorkSheetTask>>> response) {
                if (response == null || response.body() == null) {
                    ToastUtil.show("网络请求出错,请尝试下拉刷新");
                    listener.onSuccess(getWorkSheetTaskNoNet());
                    return;
                }
                if (response.body().getCode() == 1 && response.body().getData() != null) {
                    listener.onSuccess(response.body().getData());
                    saveWorkSheetTaskDetail(response.body().getData());
                } else if (response.body().getData() == null) {
                    listener.onFailed("暂无工单任务");
                } else {
                    if(response.body().getMessage() != null && response.body().getMessage().isEmpty()){
                        ToastUtil.show(response.body().getMessage());
                    }
                    listener.onSuccess(getWorkSheetTaskNoNet());
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<WorkSheetTask>>> call, Throwable t) {
                //
                listener.onSuccess(getWorkSheetTaskNoNet());
            }
        });
    }

    private void saveWorkSheetTaskDetail(final List<WorkSheetTask> data) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                WorkSheetTaskDBHelper.getInstance().deleteAll();
                WorkSheetTaskDBHelper.getInstance().insertAll(data);
                for (int i = 0; i < data.size(); i++) {
                    switch (data.get(i).getBusinessType()) {
                        case AppConstant.CHANNEL_DEFECT_SHEET:
                            DefectBeanDBHelper.getInstance().insertFromWsm(data.get(i).getTowerDefect());

                            break;
                        case AppConstant.FINE_DEFECT_SHEET:
                            DefectBeanDBHelper.getInstance().insertFromWsm(data.get(i).getTowerDefect());

                            break;
                        case AppConstant.PATROL_DEFECT_SHEET:
                            DefectBeanDBHelper.getInstance().insertFromWsm(data.get(i).getTowerDefect());
                            break;
                        case AppConstant.TREE_DEFECT_SHEET:
                            TreeDefectDBHelper.getInstance().insert( data.get(i).getTreeDefect());
                            break;
                        case AppConstant.CROSS_DEFECT_SHEET:
                            LineCrossDBHelper.getInstance().insertFromWsm(data.get(i).getLineCross());
                            break;
                        case AppConstant.BROKEN_SHEET:
                            BrokenDocumentDBHelper.getInstance().insertFromWsm(data.get(i).getBrokenDocument());
                            break;
                        case AppConstant.PROJECT_SHEET:
                            ProjectEntityDbHelper.getInstance().insertFromWsm(data.get(i).getOptProject());
                            break;

                    }
                }

            }
        }.start();

    }


    private List<WorkSheetTask> getWorkSheetTaskNoNet(){
        return WorkSheetTaskDBHelper.getInstance().getUploadList();
    }


}
