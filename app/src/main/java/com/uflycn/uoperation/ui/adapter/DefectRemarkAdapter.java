package com.uflycn.uoperation.ui.adapter;

import android.content.Context;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.DefectRemark;

import java.util.List;

/**
 * Created by diyang on 2017/9/21.
 */
public class DefectRemarkAdapter extends CommonAdapter<DefectRemark>{

    public DefectRemarkAdapter(List<DefectRemark> list, Context context, int resID) {
        super(list, context, resID);
    }
    @Override
    public void fillData(int position, CommomViewHolder holder) {
        DefectRemark remark = getItem(position);
        String dateTime = remark.getCreatedTime();
        if (remark.getRemark() != null) {
            holder.setText(R.id.tv_defect_remark,remark.getRemark());
        }
        if(dateTime.lastIndexOf(".")>-1)
            dateTime = dateTime.substring(0,dateTime.lastIndexOf("."));
        if(dateTime.indexOf("T") > -1) {
            holder.setText(R.id.tv_defect_date, dateTime.replace("T","  "));
        }else{
            holder.setText(R.id.tv_defect_date, dateTime);
        }
    }
}
