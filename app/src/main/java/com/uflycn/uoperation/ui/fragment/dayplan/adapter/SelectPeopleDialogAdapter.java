package com.uflycn.uoperation.ui.fragment.dayplan.adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.SelectPeopleBean;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/20
 * Describe  :
 */
public class SelectPeopleDialogAdapter extends BaseQuickAdapter<SelectPeopleBean> {

    public SelectPeopleDialogAdapter(int layoutResId, List<SelectPeopleBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final SelectPeopleBean peopleBean) {
        baseViewHolder.setText(R.id.tv_tower_no, peopleBean.getNum());
        CheckBox checkBox = baseViewHolder.getView(R.id.cb_select);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(peopleBean.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                peopleBean.setChecked(b);
            }
        });
    }
}
