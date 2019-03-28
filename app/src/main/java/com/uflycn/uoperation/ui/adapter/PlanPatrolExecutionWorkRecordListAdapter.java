package com.uflycn.uoperation.ui.adapter;

import android.content.Context;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordList;

import java.util.List;

public class PlanPatrolExecutionWorkRecordListAdapter extends CommonAdapter<PlanPatrolExecutionWorkRecordList> {
    public PlanPatrolExecutionWorkRecordListAdapter(List<PlanPatrolExecutionWorkRecordList> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        PlanPatrolExecutionWorkRecordList bean = mDatas.get(position);
        holder.setText(R.id.tv_voltage, bean.getVoltageClass())
                .setText(R.id.tv_line_name, bean.getLineName())
                .setText(R.id.tv_tower_selection, bean.getTowerSection())
                .setText(R.id.tv_task_type, bean.getTaskType())
                .setText(R.id.tv_type_of_work, bean.getTypeOfWork())
                .setText(R.id.tv_task_name, bean.getTaskName())
                .setText(R.id.tv_work_content, bean.getWorkContent())
                .setText(R.id.tv_work_note, bean.getWorkNote());
    }
}
