package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.view.View;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectRemark;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TreeDefectPointBean;

import java.util.List;

/**
 * Created by Mao on 2017/10/30.
 */
public class TowerDtailAdapter extends CommonAdapter<Object>{

    public TowerDtailAdapter(List<Object> list, Context context, int resID){
        super(list,context,resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        String num = null;
        Object object = mDatas.get(position);
        if(object instanceof DefectBean){
            DefectBean defectBean = (DefectBean) object;
            num = getNumber(defectBean,null);
            setCommonDefect(defectBean,holder,num);
        }else if(object instanceof TreeDefectPointBean){
            TreeDefectPointBean treeDefectPointBean = (TreeDefectPointBean) object;
            num = getNumber(null,treeDefectPointBean);
            setTreeDefect(treeDefectPointBean,holder,num);
        }
    }

    private void setCommonDefect(DefectBean defectBean, CommomViewHolder holder,String num) {
        holder.setText(R.id.tv_grid_line_name,defectBean.getLineName());
        holder.setText(R.id.tv_tower_id, "#" + num);
        holder.setText(R.id.tv_type, defectBean.getDefectCategoryString());
        holder.setText(R.id.tv_defect_des, defectBean.getRemark());
    }

    private void setTreeDefect(TreeDefectPointBean treeDefectPointBean, CommomViewHolder holder,String num) {
        holder.setText(R.id.tv_grid_line_name, treeDefectPointBean.getLineName());
        holder.setText(R.id.tv_tower_id, "#" + num);
        holder.setText(R.id.tv_type, "树障("+treeDefectPointBean.getTreeDefectPointType()+")");
        holder.setText(R.id.tv_defect_des, treeDefectPointBean.getRemark());
    }

    private String getNumber(DefectBean defectBean,TreeDefectPointBean treeDefectPointBean){
        String num = null;
        if(defectBean != null){
            num = defectBean.getTowerNo();
            if(defectBean.getNearTowerNo() != null && !defectBean.getNearTowerNo().equalsIgnoreCase("")){
                num = showTowerId(num,defectBean.getNearTowerNo());
            }
        }else{
            num = treeDefectPointBean.getTowerA_Name();
            if(treeDefectPointBean.getTowerB_Name() != null){
                num = showTowerId(num,treeDefectPointBean.getTowerB_Name());
            }
        }
        return num;
    }

    private String showTowerId(String towerNo, String nearTowerNo) {
        int tower = Integer.valueOf(towerNo);
        int nearTower = Integer.valueOf(nearTowerNo);
        if (tower < nearTower){
            towerNo = towerNo +"-"+ nearTowerNo;
        }else{
            towerNo = nearTowerNo +"-"+ towerNo;
        }
        return towerNo;
    }

}
