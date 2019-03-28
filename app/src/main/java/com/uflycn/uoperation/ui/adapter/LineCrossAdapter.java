package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.view.View;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.ui.activity.CrossclearActivity;
import com.uflycn.uoperation.ui.activity.EditCrossActivity;
import com.uflycn.uoperation.ui.activity.TourResultActivity;
import com.uflycn.uoperation.util.ProjectManageUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/9/21.
 */
public class LineCrossAdapter extends CommonAdapter<LineCrossEntity> {

    public LineCrossAdapter(List<LineCrossEntity> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        final LineCrossEntity lineCrossEntity = getItem(position);
        String tower = showTowerId(lineCrossEntity.getStartTower(), lineCrossEntity.getEndTower());
        holder.setText(R.id.tv_line_name, lineCrossEntity.getVoltageClass() + " " + lineCrossEntity.getLineName());
        holder.setText(R.id.tv_tower_num, tower);


        String dateTime = lineCrossEntity.getCreatedTime();
        if (dateTime.lastIndexOf(".") > -1)
            dateTime = dateTime.substring(0, dateTime.lastIndexOf("."));
        if (dateTime.indexOf("T") > -1) {
            holder.setText(R.id.tv_register_date, dateTime.replace("T", "  "));
        } else {
            holder.setText(R.id.tv_register_date, dateTime);
        }

        holder.setText(R.id.tv_cross_type1, lineCrossEntity.getCrossTypeFirst());
        holder.setText(R.id.tv_cross_type2, lineCrossEntity.getCrossTypeName());
        holder.setText(R.id.tv_cross_state, lineCrossEntity.getCrossStatus());
        holder.setText(R.id.tv_cross_remark, lineCrossEntity.getRemark());
        holder.getView(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProjectManageUtil.isShanDong()) {
                    mContextRef.get().startActivity(EditCrossActivity.newInstance(mContextRef.get(), lineCrossEntity));
                } else {
                    ((TourResultActivity) mContextRef.get()).changeToEditCross(lineCrossEntity, -1);
                }
            }
        });
        holder.getView(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProjectManageUtil.isShanDong()) {
                    mContextRef.get().startActivity(CrossclearActivity.newInstance(mContextRef.get(), lineCrossEntity));
                } else {
                    ((TourResultActivity) mContextRef.get()).changeToDeleteCross(lineCrossEntity, -1);
                }
            }
        });
    }

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
}
