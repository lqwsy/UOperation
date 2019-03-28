package com.uflycn.uoperation.bean;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/27
 * Describe  : 从线路中添加的选择对话框中的塔的bean类
 */
public class AddPlanTowerNum extends TowerNum{
    private boolean isTour;//是否被巡视

    public boolean isTour() {
        return isTour;
    }

    public void setTour(boolean tour) {
        isTour = tour;
    }
}
