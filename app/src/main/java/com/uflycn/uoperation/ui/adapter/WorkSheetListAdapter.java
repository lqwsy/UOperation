package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.WorkSheetTask;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.WorkSheetTaskDBHelper;
import com.uflycn.uoperation.ui.activity.SheekDetailsActivity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */
public class WorkSheetListAdapter extends CommonAdapter<WorkSheetTask> {

    public WorkSheetListAdapter(List<WorkSheetTask> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        final WorkSheetTask workSheetTask = mDatas.get(position);
        if (workSheetTask.getLineName() != null){
            holder.setText(R.id.tv_order_line,workSheetTask.getLineName());
        }

        if (workSheetTask.getNearDeviceNo() == null||workSheetTask.getNearDeviceNo().isEmpty()){
            holder.setText(R.id.tv_order_tower,"#"+workSheetTask.getDeviceNo());
        }else{
            holder.setText(R.id.tv_order_tower,"#"+workSheetTask.getDeviceNo()+"-#"+workSheetTask.getNearDeviceNo());
        }

        if (workSheetTask.getTaskContent() != null){
            holder.setText(R.id.tv_order_details,workSheetTask.getTaskContent());
        }else{
            holder.setText(R.id.tv_order_details,"无");
        }

        WorkSheetTask mWorkSheetTask = WorkSheetTaskDBHelper.getInstance().queryWorkSheet(workSheetTask.getSysTaskId().intValue());
        if (mWorkSheetTask != null){
            if (mWorkSheetTask.getReceiverId() != null){
                if (workSheetTask.getReceiverId().equals(AppConstant.currentUser.getUserId())) {
                    holder.setText(R.id.tv_order_id,"工单编号:" + workSheetTask.getTaskNo());
                }else{
                    holder.setHtmlText(R.id.tv_order_id,"<font color=\"#008d7c\">[被接单]</font>工单编号:" + workSheetTask.getTaskNo());
                }
            }else{
                holder.setHtmlText(R.id.tv_order_id,"<font color=\"#FF0000\">[未接单]</font>工单编号:" + workSheetTask.getTaskNo());
            }
        }else{
            if (workSheetTask.getReceiverId() != null){
                if (workSheetTask.getReceiverId().equals(AppConstant.currentUser.getUserId())) {
                    holder.setText(R.id.tv_order_id,"工单编号:" + workSheetTask.getTaskNo());
                }else{
                    holder.setHtmlText(R.id.tv_order_id,"<font color=\"#008d7c\">[被接单]</font>工单编号:" + workSheetTask.getTaskNo());
                }
            }else{
                holder.setHtmlText(R.id.tv_order_id,"<font color=\"#FF0000\">[未接单]</font>工单编号:" + workSheetTask.getTaskNo());
            }
        }

        switch (workSheetTask.getBusinessType()){
            case AppConstant.CHANNEL_DEFECT_SHEET:
                holder.setText(R.id.tv_order_type,"通道缺陷");
                break;
            case AppConstant.FINE_DEFECT_SHEET:
                holder.setText(R.id.tv_order_type,"精细化缺陷");
                break;
            case AppConstant.PATROL_DEFECT_SHEET:
                holder.setText(R.id.tv_order_type,"人巡缺陷");
                break;
            case AppConstant.TREE_DEFECT_SHEET:
                holder.setText(R.id.tv_order_type,"树障缺陷");
                break;
            case AppConstant.CROSS_DEFECT_SHEET:
                holder.setText(R.id.tv_order_type,"交跨处理");
                break;
            case AppConstant.BROKEN_SHEET:
                holder.setText(R.id.tv_order_type,"外破巡视");
                break;
            case AppConstant.PROJECT_SHEET:
                holder.setText(R.id.tv_order_type,"项目特巡");
                break;



        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_order_details:
                        Intent toDetails = new Intent(mContextRef.get(), SheekDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("WorkSheetTask", workSheetTask);
                        toDetails.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        toDetails.putExtras(bundle);
                        mContextRef.get().startActivity(toDetails);
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
