package com.uflycn.uoperation.ui.fragment.dayplan.adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.util.StringUtils;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/3/4
 * Describe  :
 * 缺陷列表
 */
public class DayPlanDefectAdapter extends BaseQuickAdapter<Object> {
    public DayPlanDefectAdapter(int layoutResId, List<Object> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Object object) {
        if (object instanceof DefectBean) {
            DefectBean channelDefectBean = (DefectBean) object;
            setCommonDefect(channelDefectBean, holder);
        } else if (object instanceof TreeDefectPointBean) {
            TreeDefectPointBean channelDefectBean = (TreeDefectPointBean) object;
            setTreeDefect(channelDefectBean, holder);
        }
    }

    //设置缺陷
    private void setCommonDefect(final DefectBean channelDefectBean, BaseViewHolder holder) {
        String towerNo = channelDefectBean.getTowerNo();
        String nearTowerNo = channelDefectBean.getNearTowerNo();
        if (!StringUtils.isEmptyOrNull(nearTowerNo) && !nearTowerNo.equals(towerNo)) {
            towerNo = showTowerId(towerNo, nearTowerNo);
        }
        holder.setText(R.id.tv_grid_line_name, channelDefectBean.getLineName());
        holder.setText(R.id.tv_tower_id, "#" + towerNo);
        holder.setText(R.id.tv_type, channelDefectBean.getDefectCategoryString());
        holder.setText(R.id.tv_defect_des, channelDefectBean.getRemark());
        CheckBox checkBox = holder.getView(R.id.cb_select);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(channelDefectBean.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                channelDefectBean.setChecked(b);
            }
        });
    }

    //构造杆塔
    private String showTowerId(String towerNo, String nearTowerNo) {
        try {
            int tower = Integer.valueOf(towerNo);
            int nearTower = Integer.valueOf(nearTowerNo);
            if (tower < nearTower) {
                towerNo = towerNo + "-" + nearTowerNo;
            } else {
                towerNo = nearTowerNo + "-" + towerNo;
            }
        } catch (Exception e) {
            towerNo = towerNo + "-" + nearTowerNo;
        }

        return towerNo;
    }

    //设置树障
    private void setTreeDefect(final TreeDefectPointBean channelDefectBean, BaseViewHolder holder) {
        String towerNo = channelDefectBean.getTowerA_Name();
        String nearTowerNo = channelDefectBean.getTowerB_Name();
        if (!StringUtils.isEmptyOrNull(nearTowerNo) && !nearTowerNo.equals(towerNo)) {
            towerNo = showTowerId(towerNo, nearTowerNo);
        }
        holder.setText(R.id.tv_grid_line_name, channelDefectBean.getLineName());
        holder.setText(R.id.tv_tower_id, "#" + towerNo);
        holder.setText(R.id.tv_type, channelDefectBean.getTreeDefectType() + "(" + channelDefectBean.getTreeDefectPointType() + ")");
        holder.setText(R.id.tv_defect_des, channelDefectBean.getRemark());
        CheckBox checkBox = holder.getView(R.id.cb_select);
        checkBox.setChecked(channelDefectBean.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                channelDefectBean.setChecked(b);
            }
        });
    }
}
