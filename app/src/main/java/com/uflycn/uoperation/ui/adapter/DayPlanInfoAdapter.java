package com.uflycn.uoperation.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.DayPlanInfo;

import java.util.List;

public class DayPlanInfoAdapter extends BaseQuickAdapter<DayPlanInfo> {

    public onStartLineClickListener mListener;

    public void setListener(onStartLineClickListener listener) {
        mListener = listener;
    }

    public DayPlanInfoAdapter(int layoutResId, List<DayPlanInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final DayPlanInfo dayPlanInfo) {

        baseViewHolder.setText(R.id.tv_line_name, dayPlanInfo.getLineName())
                .setText(R.id.tv_start_end_tower, dayPlanInfo.getTowerNos());

        baseViewHolder.setOnClickListener(R.id.btn_start_line, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.mOpenDayPlanLineMap.containsKey(dayPlanInfo.getSysDailyPlanSectionID())) {
                    if (mListener != null) {
                        mListener.onCloseLine(dayPlanInfo);
                    }
                } else {
                    if (mListener != null) {
                        mListener.onStartLine(dayPlanInfo);
                    }
                }


            }
        });

        if (MyApplication.mOpenDayPlanLineMap.containsKey(dayPlanInfo.getSysDailyPlanSectionID())) {
            baseViewHolder.setText(R.id.btn_start_line, "关闭线路");
            baseViewHolder.setBackgroundRes(R.id.btn_start_line, R.drawable.btn_orange_bg);
        } else {
            baseViewHolder.setText(R.id.btn_start_line, "开启线路");
            baseViewHolder.setBackgroundRes(R.id.btn_start_line, R.drawable.btn_green_bg);
        }
    }


    public interface onStartLineClickListener {
        void onStartLine(DayPlanInfo dayPlanInfo);

        void onCloseLine(DayPlanInfo dayPlanInfo);
    }
}
