package com.uflycn.uoperation.ui.fragment.dayplan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.AddDayPlanBean;
import com.uflycn.uoperation.bean.JobContent;
import com.uflycn.uoperation.ui.fragment.dayplan.view.AddDayPlanActivity;
import com.uflycn.uoperation.widget.SimpleDlg;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/28
 * Describe  :
 * 新增任务-线路列表
 */
public class AddDayPlanAdapter extends BaseQuickAdapter<AddDayPlanBean> {

    private SimpleDlg mSimpleDlg;
    private Context mcontext;
    private AddDayPlanActivity addDayPlanActivity=new AddDayPlanActivity();


    public AddDayPlanAdapter(int layoutResId, List<AddDayPlanBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final AddDayPlanBean addDayPlanBean) {
        baseViewHolder.setText(R.id.tv_voltage, addDayPlanBean.getVoltageClass())
                .setText(R.id.tv_line_name, addDayPlanBean.getLineName())
                .setText(R.id.tv_danger_defects, addDayPlanBean.getDangerDefectCount() == null ? 0 + "" : addDayPlanBean.getDangerDefectCount())
                .setText(R.id.tv_serious_defects, addDayPlanBean.getSeriousDefectCount() == null ? 0 + "" : addDayPlanBean.getSeriousDefectCount())
                .setText(R.id.tv_general_defects, addDayPlanBean.getNormalDefectCount() == null ? 0 + "" : addDayPlanBean.getNormalDefectCount())
                .setText(R.id.tv_tour_towers, addDayPlanBean.getTourNums() == null ? "" : addDayPlanBean.getTourNums())
                .setText(R.id.tv_type_of_work, addDayPlanBean.getTypeOfWork().getItemsName());
        //工作内容
        List<JobContent> jobContents = addDayPlanBean.getJobContents();
        StringBuilder safeSb = new StringBuilder();
        for (int i = 0; i < jobContents.size(); i++) {
            safeSb.append(jobContents.get(i).getSafetyMeasure());
            if (i != jobContents.size() - 1) {
                safeSb.append(",");
            }
        }
        baseViewHolder.setText(R.id.tv_job_content, safeSb.toString());
        //设置后面三个
        baseViewHolder.setText(R.id.tv_response_class, addDayPlanBean.getClassName() == null ? "" : addDayPlanBean.getClassName());
        baseViewHolder.setText(R.id.tv_response_name, addDayPlanBean.getResponseName() == null ? "" : addDayPlanBean.getResponseName());
        baseViewHolder.setText(R.id.tv_work_peoples, addDayPlanBean.getWorkPeopleNames() == null ? "" : addDayPlanBean.getWorkPeopleNames());
        //选择塔
        baseViewHolder.setOnClickListener(R.id.btn_select_tower, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSelectNums(addDayPlanBean);
                }
            }
        });
        //移除操作
        baseViewHolder.setOnClickListener(R.id.btn_remove, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder normalDialog=new AlertDialog.Builder(view.getContext());
                normalDialog.setTitle("是否移除");
                normalDialog.setMessage("确定要移除该路线吗？");
                normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                    if (listener != null) {
                                        listener.remove(addDayPlanBean);

                                    }
                    }
                });
                normalDialog.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                normalDialog.show();
//                if (mSimpleDlg == null) {
//                    SimpleDlg.Builder builder = new SimpleDlg.Builder();
//                    mSimpleDlg = builder.create(view.getContext());
//                    builder.setTitle("是否移除");
//                    builder.setTvGone();
//                    builder.setOnclickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            switch (v.getId()) {
//                                case R.id.dlg_btn_left:
//                                    mSimpleDlg.dismiss();
//
//                                    break;
//                                case R.id.dlg_btn_right:
//                                    if (listener != null) {
//                                        listener.remove(addDayPlanBean);
//                                    }
//                                    mSimpleDlg.dismiss();
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    });
//                }
//                mSimpleDlg.show();
            }
        });
        //选择负责人操作
        baseViewHolder.setOnClickListener(R.id.btn_select_people, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSelectPeoples(addDayPlanBean);
                }
            }
        });
    }

    private OnMulteChildClickListener listener;

    public interface OnMulteChildClickListener {
        /**
         * 选择塔号的点击事件
         */
        void onSelectNums(AddDayPlanBean bean);

        /**
         * 选择人员
         */
        void onSelectPeoples(AddDayPlanBean bean);

        /**
         * 移除
         */
        void remove(AddDayPlanBean bean);
    }

    public void setOnMulteChildClickListener(OnMulteChildClickListener listener) {
        this.listener = listener;
    }
}
