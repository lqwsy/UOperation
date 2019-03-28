package com.uflycn.uoperation.ui.fragment.dayplan.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.AddDayPlanFronLineBean;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.widget.SimpleDlg;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/26
 * Describe  :
 * 从线路中添加-线路列表
 */
public class AddDayPlanFromLineAdapter extends BaseQuickAdapter<AddDayPlanFronLineBean> {
    private SimpleDlg mSimpleDlg;


    public AddDayPlanFromLineAdapter(int layoutResId, List<AddDayPlanFronLineBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final AddDayPlanFronLineBean bean) {
        baseViewHolder.setText(R.id.tv_voltage, bean.getVoltageClass())
                .setText(R.id.tv_line_name, bean.getLineName())
                .setText(R.id.tv_danger_defects, bean.getDangerDefectCount() == null ? 0 + "" : bean.getDangerDefectCount())
                .setText(R.id.tv_serious_defects, bean.getSeriousDefectCount() == null ? 0 + "" : bean.getSeriousDefectCount())
                .setText(R.id.tv_general_defects, bean.getNormalDefectCount() == null ? 0 + "" : bean.getNormalDefectCount())
                .setText(R.id.tv_tour_towers, bean.getTourNums() == null ? "" : bean.getTourNums());
        baseViewHolder.setOnClickListener(R.id.btn_select, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSelect(bean);
                }
            }
        }).setOnClickListener(R.id.btn_remove, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (mSimpleDlg == null) {
                    SimpleDlg.Builder builder = new SimpleDlg.Builder();
                    mSimpleDlg = builder.create(view.getContext());
                    builder.setTitle("是否移除");
                    builder.setTvGone();
                    builder.setOnclickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.dlg_btn_left:
                                    mSimpleDlg.dismiss();
                                    break;
                                case R.id.dlg_btn_right:
                                    if (listener != null) {
                                        listener.remove(getData().get(baseViewHolder.getAdapterPosition()));
                                        Log.d("lqwtest","remove data");
                                    }
                                    mSimpleDlg.dismiss();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                }
                mSimpleDlg.show();*/
                final AlertDialog.Builder normalDialog = new AlertDialog.Builder(view.getContext());
                normalDialog.setTitle("移除确认");
                normalDialog.setMessage("确定要移除该线路吗?");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (listener != null) {
                                    listener.remove(bean);
                                }
                            }
                        });
                normalDialog.setNegativeButton("关闭",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                // 显示
                normalDialog.show();
            }
        });
    }

    OnSelectOrRemoveListener listener;

    public interface OnSelectOrRemoveListener {
        void onSelect(AddDayPlanFronLineBean bean);

        void remove(AddDayPlanFronLineBean bean);
    }

    public void setOnSelectOrRemoveListener(OnSelectOrRemoveListener listener) {
        this.listener = listener;
    }
}
