package com.uflycn.uoperation.ui.fragment.dayplan.adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.TreeDefectPointBean;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/3/3
 * Describe  : 新增日计划中的选择缺陷对话框中的adapter
 */
public class DayPlanSelectDefectsAdapter extends BaseQuickAdapter<Object> {
    public DayPlanSelectDefectsAdapter(int layoutResId, List<Object> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Object object) {
        if (object instanceof DefectBean) {
            final DefectBean defectBean = (DefectBean) object;
            baseViewHolder
                    .setText(R.id.tv_voltage, defectBean.getVoltageClass())
                    .setText(R.id.tv_line_name, defectBean.getLineName())
                    .setText(R.id.tv_tower_no, defectBean.getTowerNo())
                    .setText(R.id.tv_defect_des, defectBean.getRemark())
                    .setText(R.id.tv_find_time, defectBean.getFoundedTime())
                    .setText(R.id.tv_defect_from, defectBean.getServerityLevelString())
                    .setText(R.id.tv_defect_type, defectBean.getDefectCategoryString())
                    .setText(R.id.tv_defect_status, defectBean.getDefectStateString())
                    .setText(R.id.tv_remark, defectBean.getRemark());
            CheckBox checkBox = baseViewHolder.getView(R.id.cb_select);
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(defectBean.isChecked());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    defectBean.setChecked(b);
                }
            });
        } else if (object instanceof TreeDefectPointBean) {
            final TreeDefectPointBean treeDefectBean = (TreeDefectPointBean) object;
            baseViewHolder
                    .setText(R.id.tv_voltage, treeDefectBean.getVoltageClassName())
                    .setText(R.id.tv_line_name, treeDefectBean.getLineName())
                    .setText(R.id.tv_tower_no, treeDefectBean.getTowerA_Name()+"-"+treeDefectBean.getTowerB_Name())
                    .setText(R.id.tv_defect_des, treeDefectBean.getRemark())
                    .setText(R.id.tv_find_time, treeDefectBean.getFoundTime())
                    .setText(R.id.tv_defect_from, treeDefectBean.getCategory())
                    .setText(R.id.tv_defect_type, treeDefectBean.getTreeDefectPointType())
                    .setText(R.id.tv_defect_status, treeDefectBean.getDefectStateString())
                    .setText(R.id.tv_remark, treeDefectBean.getRemark());
            CheckBox checkBox = baseViewHolder.getView(R.id.cb_select);
            checkBox.setChecked(treeDefectBean.isChecked());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    treeDefectBean.setChecked(b);
                }
            });
        }
    }
}
