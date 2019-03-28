package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.ui.fragment.plantask.contract.PlanTaskContract;
import com.uflycn.uoperation.util.ToastUtil;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by UF_PC on 2017/10/23.
 */
public class PlanTaskAdapter extends BaseQuickAdapter<Gridline> {


    private Reference<PlanTaskContract.PlanTaskView> mTaskView;


    private Context mContext;

    public PlanTaskAdapter(Context mContext, int layoutResId, List<Gridline> data) {
        super(layoutResId, data);
        this.mContext = mContext;
    }

    public PlanTaskAdapter(List<Gridline> data) {
        super(data);
    }

    public PlanTaskAdapter(View contentView, List<Gridline> data) {
        super(contentView, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final Gridline gridline) {
        DecimalFormat df = new DecimalFormat("#0.000");
        String length = df.format(gridline.getLineLength() / 1000);
        holder.setText(R.id.tv_line_name, gridline.getVClass() + " " + gridline.getLineName());
        String start,end;
        if(gridline.getFirstTower() == null){
            start = "0";
        }else{
            start = gridline.getFirstTower();
        }
        if (gridline.getEndTower() == null){
            end = "0";
        }else{
            end = gridline.getEndTower();
        }
        holder.setText(R.id.tv_start_point, start + " - " + end);
        holder.setText(R.id.tv_tower_num, gridline.getTowerCount() + "");
        holder.setText(R.id.tv_line_length, length + "公里");

        holder.setOnClickListener(R.id.btn_start_tour, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.mLineIdNamePairs.size() >= 3) {
                    ToastUtil.show("线路最多开启3个");
                    return;
                }
                if (!MyApplication.mLineIdNamePairs.containsKey(gridline.getSysGridLineID() + "")) {
                    MyApplication.mLineIdNamePairs.put(gridline.getSysGridLineID() + "", gridline.getLineName());
                    mTaskView.get().startInspect(gridline.getSysGridLineID().intValue());
                } else {
                    ToastUtil.show("线路已开启，无需再次开启");
                    return;
                }
                if (!MyApplication.gridlines.containsKey(gridline.getSysGridLineID().intValue())) {
                    MyApplication.gridlines.put(gridline.getSysGridLineID().intValue(), gridline);
                }
            }
        });

    }

    public void setTaskView(PlanTaskContract.PlanTaskView view) {
        mTaskView = new WeakReference<PlanTaskContract.PlanTaskView>(view);
    }
}
