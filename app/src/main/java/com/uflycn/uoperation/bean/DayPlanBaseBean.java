package com.uflycn.uoperation.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/25
 * Describe  : 多布局的基类  根据top类型改变相应的布局
 */
public class DayPlanBaseBean extends MultiItemEntity {

    public static final int TOP_TYPE_DAY = 0;
    public static final int TOP_TYPE_WEEK = 1;
    public static final int TOP_TYPE_MONTH = 2;
    public static final int TOP_TYPE_YEAR = 3;

    private int topType;

    public int getTopType() {
        return topType;
    }

    public void setTopType(int topType) {
        this.topType = topType;
    }

    @Override
    public int getItemType() {
        return topType;
    }
}
