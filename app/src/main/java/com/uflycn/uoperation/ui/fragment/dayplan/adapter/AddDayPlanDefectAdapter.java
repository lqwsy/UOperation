package com.uflycn.uoperation.ui.fragment.dayplan.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.AddDayPlanDefectBean;
import com.uflycn.uoperation.widget.SimpleDlg;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/3/5
 * Describe  :
 * 缺陷列表
 */
public class AddDayPlanDefectAdapter extends BaseQuickAdapter<AddDayPlanDefectBean> {
    private SimpleDlg mSimpleDlg;

    public AddDayPlanDefectAdapter(int layoutResId, List<AddDayPlanDefectBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final AddDayPlanDefectBean bean) {
        baseViewHolder.setText(R.id.tv_line_name, bean.getLineName())
                .setText(R.id.tv_tower_no, bean.getTowerNo())
                .setText(R.id.tv_defect_des, bean.getDefectDesc())
                .setText(R.id.tv_defect_status, bean.getDefectStatus());
        baseViewHolder.setOnClickListener(R.id.btn_remove, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder normalDialog = new AlertDialog.Builder(view.getContext());
                normalDialog.setTitle("移除确认");
                normalDialog.setMessage("确定要移除该缺陷吗?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (listener != null) {
                                    listener.onRemove(bean);
                                }
                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                // 显示
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
//                                    break;
//                                case R.id.dlg_btn_right:
//                                    if (listener != null) {
//                                        listener.onRemove(bean);
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
    }

    private OnRemoveListener listener;

    public interface OnRemoveListener {
        void onRemove(AddDayPlanDefectBean bean);
    }

    public void setOnRemoveListener(OnRemoveListener listener) {
        this.listener = listener;
    }
}
