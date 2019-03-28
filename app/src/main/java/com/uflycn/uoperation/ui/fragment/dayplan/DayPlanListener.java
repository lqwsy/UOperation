package com.uflycn.uoperation.ui.fragment.dayplan;

import com.uflycn.uoperation.bean.DayPlan;
import com.uflycn.uoperation.bean.DayPlanDetail;
import com.uflycn.uoperation.bean.DayPlanInfo;
import com.uflycn.uoperation.bean.DayPlanSection;
import com.uflycn.uoperation.bean.MonthPlanBean;
import com.uflycn.uoperation.bean.SelectTower;
import com.uflycn.uoperation.bean.WeekPlanBean;
import com.uflycn.uoperation.bean.YearPlanBean;

import java.util.List;

public interface DayPlanListener {

    interface DayPlanListListener {
        void onSuccess(List<DayPlan> dayPlans);

        void onFail(String msg);
    }

    interface DayPlanDetailListener {
        void onSuccess(List<DayPlanInfo> infoList);

        void onFail(String msg);
    }

    interface DayPlanDetaiFromResponselListener {
        void onSuccess(List<DayPlanInfo> infoList);

        void onFail(String msg);
    }

    interface UpdateDayPlanListener {
        void onSuccess(String msg);

        void onFail(String msg);
    }

    interface OpenPlanDailyPlanSectionListener {
        void onSuccess(DayPlanSection dayPlanSection, DayPlanInfo dayPlanInfo);

        void onFail(String msg);
    }

    interface ClosePlanDailyPlanSectionListener {
        void onSuccess(DayPlanInfo dayPlanInfo);

        void onFail(String msg);
    }

    interface PostDayPlanDetailToNetListener {
        void onSuccess(DayPlanDetail dayPlanDetail);

        void onFail(String msg);

    }

    interface PostSelectTowers {
        void onSuccess(List<SelectTower> towers);

        void onFail(String msg);
    }

    interface PostYearPlanList {
        void onSuccess(List<YearPlanBean> planBeans);

        void onFail(String msg);
    }

    interface PostWeekPlanList {
        void onSuccess(List<WeekPlanBean> planBeans);

        void onFail(String msg);
    }

    interface PostMonthPlanList {
        void onSuccess(List<MonthPlanBean> planBeans);

        void onFail(String msg);
    }


}
