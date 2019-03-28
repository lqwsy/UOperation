package com.uflycn.uoperation.ui.fragment.dayplan.presenter;

import com.uflycn.uoperation.bean.DayPlanDetail;
import com.uflycn.uoperation.bean.DayPlanInfo;
import com.uflycn.uoperation.bean.SelectTower;

import java.util.List;

import okhttp3.MultipartBody;

public interface DayPlanPresenter {


    void getDayPlanList(boolean isFromNet, int type);

//    /**
//     * @param planId   日计划主键
//     * @param status   0-未完成，1-已完成
//     * @param WorkNote 工作备注or未完成原因
//     */
//    void updatePlanDailyPlanSection(String planId, int status, String WorkNote);

    void cancelAll();


    void getDailyTaskInfo(String planId);

    void openPlanDailyPlanSection(DayPlanInfo dayPlanInfo);

    void closePlanDailyPlanSection(String sysDailyPlanTimeSpanID, DayPlanInfo dayPlanInfo, List<SelectTower.TowerListBean> datas);

    void postDayPlanDetailToNet(final DayPlanDetail dayPlanDetail, final List<MultipartBody.Part> requestImgParts);

    void getSelectTowers(String sysDailyPlanTimeSpanID);

    void getYearPlanList();
    void getWeekPlanList();
    void getMonthPlanList();
}
