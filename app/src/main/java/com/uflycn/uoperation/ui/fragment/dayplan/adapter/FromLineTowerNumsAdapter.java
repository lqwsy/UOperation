package com.uflycn.uoperation.ui.fragment.dayplan.adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TowerNum;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/27
 * Describe  :
 */
public class FromLineTowerNumsAdapter extends BaseQuickAdapter<Tower> {
    public FromLineTowerNumsAdapter(int layoutResId, List<Tower> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Tower towerNum) {
        baseViewHolder.setText(R.id.tv_tower_no, towerNum.getTowerNo());
        CheckBox checkBox = baseViewHolder.getView(R.id.cb_select);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(towerNum.isChecked());
        //设置checkbox的点击事件
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                towerNum.setChecked(b);
            }
        });
    }
}
