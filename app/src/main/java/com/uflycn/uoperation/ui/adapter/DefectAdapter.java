package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.ui.activity.DisDefectRemarkActivity;
import com.uflycn.uoperation.ui.activity.SolveActivity;

import java.util.List;

/**
 * Created by UF_PC on 2017/9/5.
 */
public class DefectAdapter extends CommonAdapter<Object> {
    public DefectAdapter(List<Object> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(final int position, CommomViewHolder holder) {

        try{
            Object object = mDatas.get(position);

            if(object instanceof DefectBean){
                DefectBean channelDefectBean = (DefectBean) object;
                if(channelDefectBean.getDefectCategoryString().contains("交跨")){
                    holder.getView(R.id.btn_dis_defect).setEnabled(false);
                    holder.getView(R.id.btn_dis_defect).setBackgroundColor(Color.GRAY);
                }else{
                    holder.getView(R.id.btn_dis_defect).setEnabled(true);
                    holder.getView(R.id.btn_dis_defect).setBackground(mContextRef.get().getResources().getDrawable(R.drawable.shape_btn_sure_bg));
                }
                setCommonDefect(channelDefectBean,holder);
            }else if(object instanceof TreeDefectPointBean){
                holder.getView(R.id.btn_dis_defect).setEnabled(true);
                holder.getView(R.id.btn_dis_defect).setBackground(mContextRef.get().getResources().getDrawable(R.drawable.shape_btn_sure_bg));
                TreeDefectPointBean channelDefectBean = (TreeDefectPointBean) object;
                setTreeDefect(channelDefectBean,holder);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setCommonDefect(final DefectBean channelDefectBean, CommomViewHolder holder){
        String towerNo = channelDefectBean.getTowerNo();
        String nearTowerNo = channelDefectBean.getNearTowerNo();
        if(nearTowerNo != null && !nearTowerNo.equalsIgnoreCase("")){
            towerNo = showTowerId(towerNo,nearTowerNo);
        }
        holder.setText(R.id.tv_grid_line_name,channelDefectBean.getLineName());
        holder.setText(R.id.tv_tower_id, "#" + towerNo);
        holder.setText(R.id.tv_type, channelDefectBean.getDefectCategoryString());
        holder.setText(R.id.tv_defect_des, channelDefectBean.getRemark());

        holder.getView(R.id.btn_dis_defect).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContextRef.get(), SolveActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("channelDefectBean", channelDefectBean);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                mContextRef.get().startActivity(intent);
            }
        });

        holder.getView(R.id.btn_remark).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContextRef.get(), DisDefectRemarkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("channelDefectBean", channelDefectBean);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                mContextRef.get().startActivity(intent);
            }
        });

        if (channelDefectBean.getDefectStateString().equals("已联合勘察")||channelDefectBean.getDefectStateString().equals("已商谈")){
            holder.getView(R.id.btn_dis_defect).setEnabled(false);
            holder.getView(R.id.btn_dis_defect).setBackgroundColor(Color.GRAY);
            holder.setText(R.id.btn_dis_defect,channelDefectBean.getDefectStateString());
        }else{
            holder.setText(R.id.btn_dis_defect, "消缺");
        }
    }

    private String showTowerId(String towerNo, String nearTowerNo) {
        try{
            int tower = Integer.valueOf(towerNo);
            int nearTower = Integer.valueOf(nearTowerNo);
            if (tower < nearTower) {
                towerNo = towerNo + "-" + nearTowerNo;
            } else {
                towerNo = nearTowerNo + "-" + towerNo;
            }
        }catch (Exception e){
            towerNo = towerNo + "-" + nearTowerNo;
        }

        return towerNo;
    }


    private void setTreeDefect(final TreeDefectPointBean channelDefectBean, CommomViewHolder holder){
        String towerNo = channelDefectBean.getTowerA_Name();
        String nearTowerNo = channelDefectBean.getTowerB_Name();
        if(nearTowerNo != null && !nearTowerNo.equalsIgnoreCase("")){
            towerNo = showTowerId(towerNo,nearTowerNo);
        }
        holder.setText(R.id.tv_grid_line_name, channelDefectBean.getLineName());
        holder.setText(R.id.tv_tower_id, "#" + towerNo);
        holder.setText(R.id.tv_type, "树障("+channelDefectBean.getTreeDefectPointType()+")");
        holder.setText(R.id.tv_defect_des, channelDefectBean.getRemark());
        holder.getView(R.id.btn_dis_defect).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContextRef.get(), SolveActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("channelDefectBean", channelDefectBean);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                mContextRef.get().startActivity(intent);
            }
        });

        holder.getView(R.id.btn_remark).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContextRef.get(), DisDefectRemarkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("channelDefectBean", channelDefectBean);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                mContextRef.get().startActivity(intent);
            }
        });

        if (channelDefectBean.getDefectStateString().equals("已联合勘察")||channelDefectBean.getDefectStateString().equals("已商谈")){
            holder.getView(R.id.btn_dis_defect).setEnabled(false);
            holder.getView(R.id.btn_dis_defect).setBackgroundColor(Color.GRAY);
            holder.setText(R.id.btn_dis_defect,channelDefectBean.getDefectStateString());
        }else{
            holder.setText(R.id.btn_dis_defect, "消缺");
        }
    }

}
