package com.uflycn.uoperation.ui.fragment.dayplan.view;

import com.uflycn.uoperation.bean.DayPlan;
import com.uflycn.uoperation.bean.DayPlanDetail;
import com.uflycn.uoperation.bean.DayPlanInfo;
import com.uflycn.uoperation.bean.DayPlanSection;
import com.uflycn.uoperation.bean.MonthPlanBean;
import com.uflycn.uoperation.bean.SelectTower;
import com.uflycn.uoperation.bean.WeekPlanBean;
import com.uflycn.uoperation.bean.YearPlanBean;

import java.util.List;

public interface DayPlanView {

    public interface DayPlanListView {
        //type代表了是所有的日计划还是自己的日计划
        void onRefreshData(List<DayPlan> planList,int type);

        void onShowMessage(String msg);

        void onRefreshYearPlanList(List<YearPlanBean> planBeans);

        void onRefreshWeekPlanList(List<WeekPlanBean> planBeans);

        void onRefreshMonthPlanList(List<MonthPlanBean> planBeans);
    }

    public interface DayPlanDetailView {
        void onRefreshData(List<DayPlanInfo> planList);

        void onShowMessage(String msg);

        void onOpenDayPlanSectionSuccess(DayPlanSection dayPlanSection, DayPlanInfo dayPlanInfo);

        void onCloseDayPlanSectionSuccess(DayPlanInfo dayPlanInfo);

        void onRefreshSelectData(List<SelectTower> towers);

    }

    public interface DayPlanWorkDetailView {
        void onCloseDayPlan(DayPlanDetail dayPlanDetail);

        void onShowMessage(String msg);

        void onRefreshData(List<DayPlanInfo> infoList);
    }

    public interface AddDayPlanView {
        void onShowMessage(String msg);
    }
}
