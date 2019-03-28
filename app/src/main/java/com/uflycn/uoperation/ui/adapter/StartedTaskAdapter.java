package com.uflycn.uoperation.ui.adapter;

import android.content.Context;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.StartedTask;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public class StartedTaskAdapter extends CommonAdapter<StartedTask> {
    public StartedTaskAdapter(List<StartedTask> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        final StartedTask tourLine = mDatas.get(position);
        DecimalFormat df = new DecimalFormat("#0.000");
        String length = df.format(tourLine.getLineLength() / 1000);
        holder.setText(R.id.tv_line_name, tourLine.getVClass()+" "+tourLine.getLineName());

        String start,end;
        if(tourLine.getStartTower() == null){
            start = "0";
        }else{
            start = tourLine.getStartTower();
        }
        if (tourLine.getEndTower() == null){
            end = "0";
        }else{
            end = tourLine.getEndTower();
        }
        holder.setText(R.id.tv_start_point, start + " - " + end);

        if (tourLine.getInspectedTowerA() == null || tourLine.getInspectedTowerA().equalsIgnoreCase("")) {
            holder.setText(R.id.tv_end_point, "0");
        } else {
            holder.setText(R.id.tv_end_point, tourLine.getInspectedTowerA() + "-" + tourLine.getInspectedTowerB());
        }
        holder.setText(R.id.tv_tower_num, tourLine.getTowerCount() + "");
        holder.setText(R.id.tv_line_length, length + "公里");
        holder.setText(R.id.tv_defect_num, tourLine.getDefectCount() + ""); //已发现缺陷数


    }
}
