package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/2/10.
 * 缺陷
 */
public class BatchDefectAdapter extends CommonAdapter<SimpleDefectBean> {
    public BatchDefectAdapter(List<SimpleDefectBean> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        SimpleDefectBean treeDefect = mDatas.get(position);
        if (treeDefect.getDefectID() == null){
            holder.setText(R.id.tv_grid_line_name, treeDefect.getGridLine());
            CheckBox checkBox = holder.getView(R.id.cb_batch);
            checkBox.setVisibility(View.GONE);
        }else{
            holder.setText(R.id.tv_grid_line_name, treeDefect.getGridLine());
            CheckBox checkBox = holder.getView(R.id.cb_batch);
            if (treeDefect.isSelect()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }
        holder.setText(R.id.tv_tower_id, "");

        holder.setText(R.id.tv_defect_des, treeDefect.getRemark());


    }

}
