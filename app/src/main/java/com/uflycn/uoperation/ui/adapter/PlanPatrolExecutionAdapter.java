package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.PlanPatrolExecution;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.List;

public class PlanPatrolExecutionAdapter extends CommonAdapter<PlanPatrolExecution> {

    private onMissionAdapterListener mListener;
    private PlanPatrolExecution mPlanPatrolExecution;

    public PlanPatrolExecutionAdapter(List<PlanPatrolExecution> list, Context context, int resID) {
        super(list, context, resID);
    }

    public void setListener(onMissionAdapterListener listener) {
        mListener = listener;
    }

    @Override
    public void fillData(final int position, CommomViewHolder holder) {

        PlanPatrolExecution planPatrolExecution = mDatas.get(position);
        holder.setText(R.id.tv_title, planPatrolExecution.getTaskName())
                .setText(R.id.tv_start_time, planPatrolExecution.getStartDateString())
                .setText(R.id.tv_end_time, planPatrolExecution.getEndDateString())
                .setText(R.id.tv_responsibility_team, planPatrolExecution.getResponsibleClassName())
                .setText(R.id.tv_functionary, planPatrolExecution.getResponsiblePersonName())
                .setText(R.id.tv_office_holder, planPatrolExecution.getOfficeHolderNames())
                .setText(R.id.tv_line_name, planPatrolExecution.getLineNameSection().replace(",", "\r\n"))
                .setText(R.id.tv_mission_type, planPatrolExecution.getTypeString())
                .setText(R.id.tv_work_type, planPatrolExecution.getTypeOfWork())
                .setText(R.id.tv_mission_content, planPatrolExecution.getWorkContent());
        //检测和巡视的任务不显示任务内容项
        if (planPatrolExecution.getTypeString().equals("巡视") || planPatrolExecution.getTypeString().equals("检测")) {
            holder.getView(R.id.tv_mission_content_name).setVisibility(View.GONE);
            holder.getView(R.id.tv_mission_content).setVisibility(View.GONE);
        }

        Button button = holder.getView(R.id.btn_open_mission);
        if (planPatrolExecution.equals(mPlanPatrolExecution)) {
            button.setText("关闭任务");
            button.setBackgroundResource(R.drawable.shape_btn_close_bg);
        } else {
            button.setText("开启任务");
            button.setBackgroundResource(R.drawable.shape_btn_sure_bg);
        }

        holder.getView(R.id.btn_mission_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickMissionInfo(mDatas.get(position));
                }
            }
        });
        holder.getView(R.id.btn_open_mission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickOpenMission(mDatas.get(position));
                }
            }
        });
    }

    public boolean isCurrenMission(PlanPatrolExecution planPatrolExecution) {
        return mPlanPatrolExecution == null || mPlanPatrolExecution.equals(planPatrolExecution);
    }

    public PlanPatrolExecution getPlanPatrolExecution() {
        return mPlanPatrolExecution;
    }

    /**
     * 开启关闭
     */
    public void updatePlanPatrolState(PlanPatrolExecution planPatrolExecution) {
        if (mPlanPatrolExecution != null && !mPlanPatrolExecution.equals(planPatrolExecution)) {
            ToastUtil.show("请先关闭其他的任务");
            return;
        }
        if (mPlanPatrolExecution == null) {
            mPlanPatrolExecution = planPatrolExecution;
        } else {
            mPlanPatrolExecution = null;
        }
        notifyDataSetChanged();
    }


    public boolean isPlanPatrol(PlanPatrolExecution planPatrolExecution) {
        return mPlanPatrolExecution != null && mPlanPatrolExecution.equals(planPatrolExecution);
    }

    public interface onMissionAdapterListener {
        void onClickMissionInfo(PlanPatrolExecution execution);

        void onClickOpenMission(PlanPatrolExecution execution);
    }
}
