package com.uflycn.uoperation.ui.fragment.dayplan.presenter;

import com.uflycn.uoperation.bean.DayPlan;
import com.uflycn.uoperation.bean.DayPlanDetail;
import com.uflycn.uoperation.bean.DayPlanInfo;
import com.uflycn.uoperation.bean.DayPlanSection;
import com.uflycn.uoperation.bean.MonthPlanBean;
import com.uflycn.uoperation.bean.SelectTower;
import com.uflycn.uoperation.bean.WeekPlanBean;
import com.uflycn.uoperation.bean.YearPlanBean;
import com.uflycn.uoperation.ui.fragment.dayplan.DayPlanListener;
import com.uflycn.uoperation.ui.fragment.dayplan.model.DayPlanModelImp;
import com.uflycn.uoperation.ui.fragment.dayplan.view.DayPlanView;

import java.util.List;

import okhttp3.MultipartBody;

public class DayPlanPresenterImp implements DayPlanPresenter {

    private DayPlanView.DayPlanListView mListView;
    private DayPlanView.DayPlanDetailView mDetailView;
    private DayPlanView.DayPlanWorkDetailView mWorkDetailView;
    private DayPlanView.AddDayPlanView mAddDayPlanView;
    private DayPlanModelImp mMode;

    public DayPlanPresenterImp(DayPlanView.DayPlanListView view) {
        mListView = view;
        mMode = new DayPlanModelImp();
    }

    public DayPlanPresenterImp(DayPlanView.DayPlanDetailView view) {
        mDetailView = view;
        mMode = new DayPlanModelImp();
    }

    public DayPlanPresenterImp(DayPlanView.DayPlanWorkDetailView view) {
        mWorkDetailView = view;
        mMode = new DayPlanModelImp();
    }
    public DayPlanPresenterImp(DayPlanView.AddDayPlanView view) {
        this.mAddDayPlanView = view;
        mMode = new DayPlanModelImp();
    }


    @Override
    public void getDayPlanList(boolean isFromNet, final int type) {
        mMode.getDayPlanList(isFromNet, type, new DayPlanListener.DayPlanListListener() {
            @Override
            public void onSuccess(List<DayPlan> dayPlans) {
                mListView.onRefreshData(dayPlans,type);
            }

            @Override
            public void onFail(String msg) {
                mListView.onShowMessage(msg);
            }
        });
    }

    //    @Override
    //    public void updatePlanDailyPlanSection(String planId, int status, String WorkNote) {
    //        mMode.updatePlanDailyPlanSection(planId, status, WorkNote, new DayPlanListener.UpdateDayPlanListener() {
    //            @Override
    //            public void onSuccess(String msg) {
    //                mListView.onShowMessage(msg);
    //            }
    //
    //            @Override
    //            public void onFail(String msg) {
    //                mListView.onShowMessage(msg);
    //            }
    //        });
    //    }

    @Override
    public void getDailyTaskInfo(String planId) {
        mMode.getDailyTaskInfo(planId, new DayPlanListener.DayPlanDetailListener() {
            @Override
            public void onSuccess(List<DayPlanInfo> infoList) {
                mDetailView.onRefreshData(infoList);
            }

            @Override
            public void onFail(String msg) {
                mDetailView.onShowMessage(msg);
            }
        });
    }

    /**
     * 负责人关闭计划，获取信息
     */
    public void getDailyTaskInfoFromResponse(String planId) {
        mMode.getDailyTaskInfoFromResponse(planId, new DayPlanListener.DayPlanDetaiFromResponselListener() {
            @Override
            public void onSuccess(List<DayPlanInfo> infoList) {
                mWorkDetailView.onRefreshData(infoList);
            }

            @Override
            public void onFail(String msg) {
                mWorkDetailView.onShowMessage(msg);
            }
        });
    }

    @Override
    public void openPlanDailyPlanSection(DayPlanInfo info) {
        mMode.openPlanDailyPlanSection(info, new DayPlanListener.OpenPlanDailyPlanSectionListener() {
            @Override
            public void onSuccess(DayPlanSection dayPlanSection, DayPlanInfo dayPlanInfo) {
                mDetailView.onOpenDayPlanSectionSuccess(dayPlanSection, dayPlanInfo);
            }

            @Override
            public void onFail(String msg) {
                mDetailView.onShowMessage(msg);
            }
        });
    }

    @Override
    public void closePlanDailyPlanSection(String sysDailyPlanTimeSpanID, final DayPlanInfo dayPlanInfo, List<SelectTower.TowerListBean> datas) {
        mMode.closePlanDailyPlanSection(sysDailyPlanTimeSpanID, datas, new DayPlanListener.ClosePlanDailyPlanSectionListener() {
            @Override
            public void onSuccess(DayPlanInfo info) {
                mDetailView.onCloseDayPlanSectionSuccess(dayPlanInfo);
            }

            @Override
            public void onFail(String msg) {
                mDetailView.onShowMessage(msg);
            }
        });
    }

    @Override
    public void postDayPlanDetailToNet(DayPlanDetail dayPlanDetail, List<MultipartBody.Part> requestImgParts) {
        mMode.postDayPlanDetailToNet(dayPlanDetail, requestImgParts, new DayPlanListener.PostDayPlanDetailToNetListener() {
            @Override
            public void onSuccess(DayPlanDetail dayPlanDetail) {
                mWorkDetailView.onCloseDayPlan(dayPlanDetail);
            }

            @Override
            public void onFail(String msg) {
                mWorkDetailView.onShowMessage(msg);
            }
        });
    }

    @Override
    public void getSelectTowers(String sysDailyPlanTimeSpanID) {
        mMode.getSelectTowers(sysDailyPlanTimeSpanID, new DayPlanListener.PostSelectTowers() {
            @Override
            public void onSuccess(List<SelectTower> towers) {
                mDetailView.onRefreshSelectData(towers);
            }

            @Override
            public void onFail(String msg) {
                mDetailView.onShowMessage("获取塔得数据失败!");
            }
        });
    }

    @Override
    public void getYearPlanList() {
        mMode.getYearPlanList(new DayPlanListener.PostYearPlanList() {
            @Override
            public void onSuccess(List<YearPlanBean> planBeans) {
                mListView.onRefreshYearPlanList(planBeans);
            }

            @Override
            public void onFail(String msg) {
                mListView.onShowMessage(msg);
            }
        });
    }

    @Override
    public void getWeekPlanList() {
        mMode.getWeekPlanList(new DayPlanListener.PostWeekPlanList() {
            @Override
            public void onSuccess(List<WeekPlanBean> planBeans) {
                mListView.onRefreshWeekPlanList(planBeans);
            }

            @Override
            public void onFail(String msg) {
                mListView.onShowMessage(msg);
            }
        });
    }

    @Override
    public void getMonthPlanList() {
        mMode.getMonthPlanList(new DayPlanListener.PostMonthPlanList() {
            @Override
            public void onSuccess(List<MonthPlanBean> planBeans) {
                mListView.onRefreshMonthPlanList(planBeans);
            }

            @Override
            public void onFail(String msg) {
                mListView.onShowMessage(msg);
            }
        });
    }

    @Override
    public void cancelAll() {
        mMode.cancelAll();
    }


}
