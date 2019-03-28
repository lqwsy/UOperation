package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.widget.SpinnerAdapter;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.Tower;

import java.util.List;

public class PlanPatrolExecutionRecordTowerInfoAdapter extends CommonAdapter<Tower> implements SpinnerAdapter {

    public PlanPatrolExecutionRecordTowerInfoAdapter(List<Tower> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        Tower tower = mDatas.get(position);
        holder.setText(R.id.tv_line_name, tower.getTowerNo());

    }
}
