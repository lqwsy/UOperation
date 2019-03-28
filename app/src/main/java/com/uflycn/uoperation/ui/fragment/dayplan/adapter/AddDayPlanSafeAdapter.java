package com.uflycn.uoperation.ui.fragment.dayplan.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.AddDayPlanBean;
import com.uflycn.uoperation.bean.JobContent;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/28
 * Describe  :
 * 新增任务-安全措施和注意事项
 */
public class AddDayPlanSafeAdapter extends BaseQuickAdapter<AddDayPlanBean> {
    public AddDayPlanSafeAdapter(int layoutResId, List<AddDayPlanBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final AddDayPlanBean addDayPlanBean) {
        baseViewHolder.setText(R.id.tv_line_name, addDayPlanBean.getLineName());
//        baseViewHolder.setText(R.id.tv_safe_measures, addDayPlanBean.getJobContent().getSafetyMeasure());
//        baseViewHolder.setText(R.id.tv_notice, addDayPlanBean.getJobContent().getSafetyPrecaution());
        List<JobContent> jobContents = addDayPlanBean.getJobContents();
        StringBuilder safeSb = new StringBuilder();
        StringBuilder noticeSb = new StringBuilder();
        for (int i = 0; i < jobContents.size(); i++) {
            safeSb.append(jobContents.get(i).getSafetyMeasure());
            noticeSb.append(jobContents.get(i).getSafetyPrecaution());
            if (i != jobContents.size() - 1) {
                safeSb.append(",");
                noticeSb.append(",");
            }
        }
        baseViewHolder.setText(R.id.tv_safe_measures, safeSb.toString());
        baseViewHolder.setText(R.id.tv_notice, noticeSb.toString());
        //安全措施
        baseViewHolder.setOnClickListener(R.id.btn_edit_safe, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSafebtn(addDayPlanBean);
            }
        });
        //注意事项
        baseViewHolder.setOnClickListener(R.id.btn_edit_job_notice, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleNotice(addDayPlanBean);
            }
        });
    }

    private void handleSafebtn(final AddDayPlanBean addDayPlanBean) {
        if (listener != null) {
            listener.onSafe(addDayPlanBean);
        }
    }

    private void handleNotice(final AddDayPlanBean addDayPlanBean) {
        if (listener != null) {
            listener.onNotice(addDayPlanBean);
        }
    }

    private OnSafeAndNoticeClickListener listener;

    public interface OnSafeAndNoticeClickListener {
        //安全措施的按钮
        void onSafe(AddDayPlanBean bean);

        //注意事项的按钮
        void onNotice(AddDayPlanBean bean);
    }

    public void setOnSafeAndNoticeClickListener(OnSafeAndNoticeClickListener listener) {
        this.listener = listener;
    }
}
