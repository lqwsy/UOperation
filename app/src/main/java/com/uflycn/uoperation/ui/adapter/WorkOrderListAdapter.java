package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.BrokenDocument;
import com.uflycn.uoperation.bean.DefectType;
import com.uflycn.uoperation.bean.WorkSheetTask;
import com.uflycn.uoperation.ui.activity.SpecialInspectActivity;
import com.uflycn.uoperation.ui.activity.SpecialRecordActivity;
import com.uflycn.uoperation.ui.fragment.mytour.contract.MyTourContract;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.UIUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */
public class WorkOrderListAdapter extends CommonAdapter<WorkSheetTask> {

    public WorkOrderListAdapter(List<WorkSheetTask> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        WorkSheetTask workSheetTask = mDatas.get(position);
        holder.setText(R.id.tv_order_id,workSheetTask.getTaskNo());
        holder.setText(R.id.tv_order_line,workSheetTask.getLineName());
        holder.setText(R.id.tv_order_tower,"#39");
        holder.setText(R.id.tv_order_details,workSheetTask.getTaskContent());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_order_details:
                        ToastUtil.show("详情");
                        break;
                }
            }
        };
        holder.getView(R.id.btn_order_details).setOnClickListener(listener);
    }

    public void onDataChange() {
        notifyDataSetChanged();
    }
}
