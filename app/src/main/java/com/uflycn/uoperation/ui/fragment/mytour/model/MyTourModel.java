package com.uflycn.uoperation.ui.fragment.mytour.model;

import android.util.Log;

import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.BrokenDocument;
import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TowerDefects;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RequestCallback;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.mytour.contract.MyTourContract;
import com.uflycn.uoperation.util.UpdateTableUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public class MyTourModel implements MyTourContract.Model {

    private Call<DefectInfo> mGetDefectsRequest;
    private Call<TowerDefects> mGetTowerDefectRequest;
    private Call<BaseCallBack<List<LineCrossEntity>>> mGetLineCrossRequest;
    private Call<BaseCallBack<List<BrokenDocument>>> mGetBrokenListRequest;

    @Override
    public void addTowersById(int lineid, final RequestCallback<List<Tower>> callback) {
        List<Tower> towers = TowerDBHelper.getInstance().getLineTowers(lineid);
        callback.onDataCallBack(towers);
        getBrokenList(lineid);//更新外破表
        //        //更新交跨表
        //        //更新缺陷表
        updateAllDefectByLineId(lineid);
    }

    @Override
    public void addTowersBewteenTowerById(String firstTowerId, String endTowerId, int lineid, final RequestCallback<List<Tower>> callback) {
        List<Tower> towers = TowerDBHelper.getInstance().getTowerBetweenDisplaiOrder(firstTowerId, endTowerId, lineid);
        callback.onDataCallBack(towers);
        getBrokenList(lineid);//更新外破表
        //        //更新交跨表
        //        //更新缺陷表
        updateAllDefectByLineId(lineid);
    }


    public void updateAllDefectByLineId(final int lineId) {
        String planDailyPlanSectionIDs = "";
        String sysPatrolExecutionID = "";
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null) {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId());
            } else {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineSb.toString();
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            sysPatrolExecutionID = MyApplication.mPlanPatrolExecutionId;
        }
        mGetDefectsRequest = RetrofitManager.getInstance().getService(ApiService.class).getLineDefectList(lineId + "", "0",
                "0", "", "", "", "", sysPatrolExecutionID, planDailyPlanSectionIDs);
        mGetDefectsRequest.enqueue(new Callback<DefectInfo>() {
            @Override
            public void onResponse(Call<DefectInfo> call, Response<DefectInfo> response) {
                if (response == null || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 1) {
                    UpdateTableUtil.getInstance().UpdateLineDefects(response.body(), lineId);
                }
            }

            @Override
            public void onFailure(Call<DefectInfo> call, Throwable t) {
                //                Gridline gridline = GridLineDBHelper.getInstance().getLine(lineId);
               /* UpdateTableUtil.getInstance().updateDefectBean( DefectBeanDBHelper.getInstance().getAllDefectByLineId(gridline.getLineName()),gridline.getLineName());
                UpdateTableUtil.getInstance().updateTreeDefects(TreeDefectDBHelper.getInstance().getAllDefectByLineName(gridline.getLineName()));*/
            }
        });
    }

    //    @Override
    //    public void getAllDefectByTowerId(String towerid, final RequestCallback<TowerDefects> callback) {
    //        mGetTowerDefectRequest = RetrofitManager.getInstance().getService(ApiService.class).getTowerDefectList(towerid, "0", "0");
    //        mGetTowerDefectRequest.enqueue(new Callback<TowerDefects>() {
    //            @Override
    //            public void onResponse(Call<TowerDefects> call, Response<TowerDefects> response) {
    //                if (response == null || response.body() == null) {
    //                    Log.e("RequestErr", "getAllDefectByTowerId-response == null || response.body() == null");
    //                    return;
    //                }
    //                if (response.body().getCode() == 0) {
    //                    Log.e("RequestErr", response.body().getMessage());
    //                } else {
    //                    callback.onDataCallBack(response.body());
    //                }
    //            }
    //
    //            @Override
    //            public void onFailure(Call<TowerDefects> call, Throwable t) {
    //                if (t != null) {
    //                    Log.e("RequestErr", "" + t.getMessage());
    //                }
    //            }
    //        });
    //    }

    @Override
    public void getCrossList(String lineIds, final RequestCallback<List<LineCrossEntity>> callback) {
        String planDailyPlanSectionIDs = "";
        String sysPatrolExecutionID = "";
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null) {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId());
            } else {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineSb.toString();
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            sysPatrolExecutionID = MyApplication.mPlanPatrolExecutionId;
        }
        mGetLineCrossRequest = RetrofitManager.getInstance().getService(ApiService.class).getLineCrossList(lineIds, null,
                null, sysPatrolExecutionID, planDailyPlanSectionIDs);
        mGetLineCrossRequest.enqueue(new Callback<BaseCallBack<List<LineCrossEntity>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<LineCrossEntity>>> call, Response<BaseCallBack<List<LineCrossEntity>>> response) {
                if (response == null || response.body() == null) {
                    Log.e("RequestErr", "getCrossList-response == null || response.body() == null");
                    return;
                }
                if (response.body().getCode() == 0) {
                    Log.e("RequestErr", response.body().getMessage());
                } else {
                    callback.onDataCallBack(response.body().getData());
                    UpdateTableUtil.getInstance().saveLineCrossCache(response.body().getData());//保存到数据库
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<LineCrossEntity>>> call, Throwable t) {
                if (t != null) {
                    Log.e("RequestErr", "" + t.getMessage());
                }
            }
        });
    }


    @Override
    public void dettach() {
        if (mGetDefectsRequest != null && !mGetDefectsRequest.isCanceled()) {
            mGetDefectsRequest.cancel();
        }
        if (mGetTowerDefectRequest != null && !mGetTowerDefectRequest.isCanceled()) {
            mGetTowerDefectRequest.cancel();
        }
        if (mGetLineCrossRequest != null && !mGetLineCrossRequest.isCanceled()) {
            mGetLineCrossRequest.cancel();
        }
        if (mGetBrokenListRequest != null && !mGetBrokenListRequest.isCanceled()) {
            mGetBrokenListRequest.cancel();
        }
    }


    private void getBrokenList(int lineid) {
        String planDailyPlanSectionIDs = "";
        String sysPatrolExecutionID = "";
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null) {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId());
            } else {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineSb.toString();
            }
        }else if (MyApplication.gridlineTaskStatus==3){
            sysPatrolExecutionID = MyApplication.mPlanPatrolExecutionId;
        }
        mGetBrokenListRequest = RetrofitManager.getInstance().getService(ApiService.class).getBrokenDoucumentList(lineid + "",
                null, null, 1, 10000, null,sysPatrolExecutionID, planDailyPlanSectionIDs);
        mGetBrokenListRequest.enqueue(new Callback<BaseCallBack<List<BrokenDocument>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<BrokenDocument>>> call, Response<BaseCallBack<List<BrokenDocument>>> response) {
                if (response == null || response.body() == null) {
                    Log.e("getBrokenList", "getBrokenList-response == null || response.body() == null！");
                    return;
                }
                if (response.body().getCode() == 1) {
                    UpdateTableUtil.getInstance().updateBrokenDocumentCache(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<BrokenDocument>>> call, Throwable t) {
                Log.e("getBrokenList", "更新外破表失败！");
            }
        });
    }

}
