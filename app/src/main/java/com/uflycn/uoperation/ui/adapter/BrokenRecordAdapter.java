package com.uflycn.uoperation.ui.adapter;

import android.content.Context;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.BrokenInspectRecord;
import com.uflycn.uoperation.bean.BrokenPatrolDetail;

import java.text.SimpleDateFormat;
import java.util.List;

public class BrokenRecordAdapter extends CommonAdapter<BrokenInspectRecord>{
    public BrokenRecordAdapter(List<BrokenInspectRecord> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        BrokenInspectRecord record = getItem(position);
       // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateTime = record.getCreateDate();




        if(dateTime.lastIndexOf(".")>-1)
            dateTime = dateTime.substring(0,dateTime.lastIndexOf("."));
        if(dateTime.indexOf("T") > -1) {
            holder.setText(R.id.tv_broken_date, dateTime.replace("T","  "));
        }else{
            holder.setText(R.id.tv_broken_date, dateTime);
        }



        holder.setText(R.id.tv_broken_state,record.getBrokenStatusName()+"");
        holder.setText(R.id.tv_broken_remark,record.getRemark());
    }

}
