package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.TempTask;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.ui.fragment.temptask.contract.TempTaskContract;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public class TempTaskAdapter extends CommonAdapter<TempTask> {
    private Reference<TempTaskContract.TempTaskView> mTaskView;
    private QBadgeView mBadgeView;
    private LocalBroadcastManager localBroadcastManager;

    public TempTaskAdapter(List<TempTask> list, Context context, int resID) {
        super(list, context, resID);
        mBadgeView = new QBadgeView(context);
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        final TempTask tampTask = mDatas.get(position);
        String title = tampTask.getTitle();
        if (tampTask.getReadState().equals("未接收")){
            title = "<font color=\"#FF0000\">[新]</font>"+ title;
            holder.setHtmlText(R.id.tv_title, title);
        }
        holder.setHtmlText(R.id.tv_title, title);
        //holder.setText(R.id.tv_message, tampTask.getMessageContent());
        holder.setText(R.id.tv_public_time, tampTask.getPublishTime().substring(0, tampTask.getPublishTime().indexOf("T")));
        holder.setText(R.id.tv_expire_time, tampTask.getExpireTime().substring(0, tampTask.getExpireTime().indexOf("T")));

        holder.getView(R.id.btn_view_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskView.get().viewDetail(tampTask);
                if (tampTask.getReadState().equals("未接收")){
                    tampTask.setReadState("已读");
                    //发送广播  让侧栏减1.
                    Intent intent = new Intent(AppConstant.TEMP_TASK_NUM_DEL);
                    intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    localBroadcastManager.sendBroadcast(intent);
                }
            }
        });
    }

    public void setTaskView(TempTaskContract.TempTaskView view) {
        mTaskView = new WeakReference<TempTaskContract.TempTaskView>(view);
    }
}
