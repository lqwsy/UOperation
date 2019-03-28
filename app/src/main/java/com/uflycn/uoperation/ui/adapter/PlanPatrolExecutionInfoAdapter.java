package com.uflycn.uoperation.ui.adapter;

import android.content.Context;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;

import java.util.List;

public class PlanPatrolExecutionInfoAdapter extends CommonAdapter<PlanPatrolExecutionWorkRecordInfo> {

    public PlanPatrolExecutionInfoAdapter(List<PlanPatrolExecutionWorkRecordInfo> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        PlanPatrolExecutionWorkRecordInfo planPatrolExecutionWorkRecordInfo = mDatas.get(position);
        holder.setText(R.id.tv_line_name, planPatrolExecutionWorkRecordInfo.getLineName())
                .setText(R.id.tv_mission_type, planPatrolExecutionWorkRecordInfo.getTypeOfWorkString());

    }
}
