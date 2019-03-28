package com.uflycn.uoperation.ui.fragment.dayplan.model;

import com.uflycn.uoperation.bean.DayPlanDetail;
import com.uflycn.uoperation.bean.DayPlanInfo;
import com.uflycn.uoperation.bean.SelectTower;
import com.uflycn.uoperation.ui.fragment.dayplan.DayPlanListener;

import java.util.List;

import okhttp3.MultipartBody;

public interface DayPlanModel {

    void getDayPlanList(boolean isFromNet, int type, DayPlanListener.DayPlanListListener listener);

//    void updatePlanDailyPlanSection(String planId, int status, String WorkNote, DayPlanListener.UpdateDayPlanListener listener);

    void getDailyTaskInfo(String planId, DayPlanListener.DayPlanDetailListener listener);

    void openPlanDailyPlanSection(DayPlanInfo sysDailyPlanSectionID, DayPlanListener.OpenPlanDailyPlanSectionListener listener);


    void closePlanDailyPlanSection(String sysDailyPlanTimeSpanID, List<SelectTower.TowerListBean> datas, DayPlanListener.ClosePlanDailyPlanSectionListener listener);

    void postDayPlanDetailToNet(final DayPlanDetail dayPlanDetail, final List<MultipartBody.Part> requestImgParts, DayPlanListener.PostDayPlanDetailToNetListener listener);

    void cancelAll();

    void getSelectTowers(String sysDailyPlanTimeSpanID, DayPlanListener.PostSelectTowers postSelectTowers);

    void getYearPlanList(DayPlanListener.PostYearPlanList postYearPlanList);

    void getWeekPlanList(DayPlanListener.PostWeekPlanList postWeekPlanList);

    void getMonthPlanList(DayPlanListener.PostMonthPlanList postMonthPlanList);
}
