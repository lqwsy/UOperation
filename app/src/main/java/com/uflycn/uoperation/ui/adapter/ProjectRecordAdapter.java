package com.uflycn.uoperation.ui.adapter;

import android.content.Context;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.ProjectInspection;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */
public class ProjectRecordAdapter extends CommonAdapter<ProjectInspection> {
    public ProjectRecordAdapter(List<ProjectInspection> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        ProjectInspection record = getItem(position);
        String dateTime = record.getCheckedTime();
        if(dateTime == null){
            dateTime = record.getCreateDate();
        }
        if(dateTime.lastIndexOf(".")>-1)
            dateTime = dateTime.substring(0,dateTime.lastIndexOf("."));
        if(dateTime.indexOf("T") > -1) {
            holder.setText(R.id.tv_inspect_date, dateTime.replace("T","  "));
        }else{
            holder.setText(R.id.tv_inspect_date, dateTime);
        }
        holder.setText(R.id.tv_inpect_state, record.getProjectStatus() == 0 ? "未消除" : "已消除");
        holder.setText(R.id.tv_inspect_remark, record.getRemark());
    }
}
