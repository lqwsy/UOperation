package com.uflycn.uoperation.ui.fragment.dayplan.adapter;

import android.util.SparseBooleanArray;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.SelectTower;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/20
 * Describe  :
 */
public class DayPlanCloseLineAdapter extends BaseQuickAdapter<SelectTower.TowerListBean> {
    public DayPlanCloseLineAdapter(int layoutResId, List<SelectTower.TowerListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final SelectTower.TowerListBean towerListBean) {
        baseViewHolder.setText(R.id.tv_tower_no, towerListBean.getTowerNo());
        CheckBox checkBox = baseViewHolder.getView(R.id.cb_select);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(towerListBean.getCheck());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                towerListBean.setCheck(b);
            }
        });
    }
}
