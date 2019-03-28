package com.uflycn.uoperation.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8.
 */
public class DefectListBean {
    private List<TreeDefectPointBean> TreeDefectList;
    private List<DefectBean> TowerDefectList;

    public List<TreeDefectPointBean> getTreeDefectList() {
        return TreeDefectList;
    }

    public void setTreeDefectList(List<TreeDefectPointBean> treeDefectList) {
        TreeDefectList = treeDefectList;
    }

    public List<DefectBean> getTowerDefectList() {
        return TowerDefectList;
    }

    public void setTowerDefectList(List<DefectBean> towerDefectList) {
        TowerDefectList = towerDefectList;
    }
}
