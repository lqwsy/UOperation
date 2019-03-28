package com.uflycn.uoperation.ui.adapter;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.DayPlan;
import com.uflycn.uoperation.bean.DayPlanBaseBean;
import com.uflycn.uoperation.bean.MonthPlanBean;
import com.uflycn.uoperation.bean.WeekPlanBean;
import com.uflycn.uoperation.bean.YearPlanBean;
import com.uflycn.uoperation.ui.fragment.dayplan.view.DayPlanFragment;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.List;

/**
 * 日计划
 */
public class DayPlanAdapter extends BaseMultiItemQuickAdapter<DayPlanBaseBean> {
    DayPlanClickListener mListener;

    public DayPlanAdapter(List<DayPlanBaseBean> data) {
        super(data);
        addItemType(DayPlanBaseBean.TOP_TYPE_DAY, R.layout.item_day_plan);
        addItemType(DayPlanBaseBean.TOP_TYPE_WEEK, R.layout.item_week_plan);
        addItemType(DayPlanBaseBean.TOP_TYPE_MONTH, R.layout.item_month_plan);
        addItemType(DayPlanBaseBean.TOP_TYPE_YEAR, R.layout.item_year_plan);
    }



    /*public DayPlanAdapter(int layoutResId, List<DayPlanBaseBean> data) {
        super(layoutResId, data);
    }*/

    public void setListener(DayPlanClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final DayPlanBaseBean dayPlanBase) {
        switch (baseViewHolder.getItemViewType()) {
            case DayPlanBaseBean.TOP_TYPE_DAY:
                final DayPlan dayPlan = (DayPlan) dayPlanBase;
                baseViewHolder.setText(R.id.tv_plan_date, dayPlan.getStartDateString())
                        .setText(R.id.tv_complete_status, dayPlan.getStatusString())
                        .setText(R.id.tv_work_type, dayPlan.getTypeOfWorkString())
                        .setText(R.id.tv_response_name, dayPlan.getResponsiblePersonName())
                        .setText(R.id.tv_work_content, dayPlan.getJobContent())
                        .setText(R.id.tv_remark, dayPlan.getRemark());
                //添加开启计划后，提示已开启计划
                if (MyApplication.gridlineTaskStatus == 2 && MyApplication.mCurrentDayPlan != null && MyApplication.mCurrentDayPlan.getSysDailyPlanSectionID().equals(dayPlan.getSysDailyPlanSectionID())) {
                    baseViewHolder.setText(R.id.btn_check_day_plan, "已开启");
                    baseViewHolder.setBackgroundRes(R.id.btn_check_day_plan,R.drawable.btn_start_bg);
                } else {
                    baseViewHolder.setText(R.id.btn_check_day_plan, "查看计划");
                    baseViewHolder.setBackgroundRes(R.id.btn_check_day_plan,R.drawable.btn_green_bg);
                }

                View bottomView = baseViewHolder.getView(R.id.ll_bottom);
                if (dayPlan.getType() == DayPlanFragment.TYPE_ME) {
                    bottomView.setVisibility(View.VISIBLE);
                } else {
                    bottomView.setVisibility(View.GONE);
                }
                baseViewHolder.setOnClickListener(R.id.btn_close_day_plan, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onCloseDayPlan(dayPlan);
                        }
                    }
                });
                baseViewHolder.setOnClickListener(R.id.btn_check_day_plan, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onCheckDayPlan(dayPlan);
                        }
                    }
                });
                break;
            case DayPlanBaseBean.TOP_TYPE_WEEK:
                WeekPlanBean weekPlanBean = (WeekPlanBean) dayPlanBase;
                baseViewHolder.setText(R.id.tv_start_date, weekPlanBean.getStartDateString())
                        .setText(R.id.tv_end_date, weekPlanBean.getEndDateString())
                        .setText(R.id.tv_response_class, weekPlanBean.getClassName())
                        .setText(R.id.tv_response_name, weekPlanBean.getResponsiblePersonName())
                        .setText(R.id.tv_work_peoples, weekPlanBean.getOfficeHolderNames())
                        .setText(R.id.tv_line_name, weekPlanBean.getVoltageClass() + "  " + weekPlanBean.getLineName())
                        .setText(R.id.tv_tower_num, weekPlanBean.getTowerNos())
                        .setText(R.id.tv_work_type, weekPlanBean.getTypeOfWork())
                        .setText(R.id.tv_work_content, weekPlanBean.getJobContent());
                break;
            case DayPlanBaseBean.TOP_TYPE_MONTH:
                MonthPlanBean monthPlanBean = (MonthPlanBean) dayPlanBase;
                baseViewHolder.setText(R.id.tv_year, monthPlanBean.getPlanYear())
                        .setText(R.id.tv_month, monthPlanBean.getPlanMonth())
                        .setText(R.id.tv_work_type, monthPlanBean.getTypeOfWork())
                        .setText(R.id.tv_response_class, monthPlanBean.getClassName())
                        .setText(R.id.tv_line_name, monthPlanBean.getVoltageClass() + "  " + monthPlanBean.getLineName())
                        .setText(R.id.tv_tower_num, monthPlanBean.getTowerNos())
                        .setText(R.id.tv_work_content, monthPlanBean.getJobContent());
                break;
            case DayPlanBaseBean.TOP_TYPE_YEAR:
                YearPlanBean yearPlanBean = (YearPlanBean) dayPlanBase;
                //设置数据
                baseViewHolder.setText(R.id.tv_year, yearPlanBean.getPlanYear())
                        .setText(R.id.tv_month, yearPlanBean.getPlanMonth())
                        .setText(R.id.tv_work_type, yearPlanBean.getTypeOfWork())
                        .setText(R.id.tv_response_class, yearPlanBean.getClassName())
                        .setText(R.id.tv_line_name, yearPlanBean.getVoltageClass() + "  " + yearPlanBean.getLineName())
                        .setText(R.id.tv_tower_num, yearPlanBean.getTowerNos())
                        .setText(R.id.tv_work_content, yearPlanBean.getJobContent());
                break;
            default:
                break;
        }
    }

    public interface DayPlanClickListener {
        void onCheckDayPlan(DayPlan dayPlan);

        void onCloseDayPlan(DayPlan dayPlan);
    }
}
