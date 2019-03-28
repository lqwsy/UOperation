package com.uflycn.uoperation.ui.fragment.dayplan.adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.DayPlanFromWeekPlanBean;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/3/6
 * Describe  :
 * 周计划适配器
 */
public class DayPlanFromWeekAdapter extends BaseQuickAdapter<DayPlanFromWeekPlanBean> {
    public DayPlanFromWeekAdapter(int layoutResId, List<DayPlanFromWeekPlanBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final DayPlanFromWeekPlanBean weekPlanBean) {
        baseViewHolder.setText(R.id.tv_voltage, weekPlanBean.getVoltageClass())
                .setText(R.id.tv_line_name, weekPlanBean.getLineName())
                .setText(R.id.tv_danger_defects, weekPlanBean.getDangerDefectCount() == null ? 0 + "" : weekPlanBean.getDangerDefectCount() + "")
                .setText(R.id.tv_serious_defects, weekPlanBean.getSeriousDefectCount() == null ? 0 + "" : weekPlanBean.getSeriousDefectCount() + "")
                .setText(R.id.tv_general_defects, weekPlanBean.getNormalDefectCount() == null ? 0 + "" : weekPlanBean.getNormalDefectCount() + "")
                .setText(R.id.tv_tour_towers, weekPlanBean.getTowerNos() == null ? "" : weekPlanBean.getTowerNos())
                .setText(R.id.tv_type_of_work, weekPlanBean.getTypeOfWorkString())
                .setText(R.id.tv_job_content, weekPlanBean.getJobContentString())
                .setText(R.id.tv_response_class, weekPlanBean.getClassName())
                .setText(R.id.tv_response_name, weekPlanBean.getResponsiblePersonName())
                .setText(R.id.tv_work_peoples, weekPlanBean.getOfficeHolderNames());
        CheckBox checkBox = baseViewHolder.getView(R.id.btn_select_people);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                weekPlanBean.setCheck(isCheck);
            }
        });
    }
}
