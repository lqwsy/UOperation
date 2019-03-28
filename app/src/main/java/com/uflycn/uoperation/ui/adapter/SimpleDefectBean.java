package com.uflycn.uoperation.ui.adapter;

import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by Administrator on 2018/3/8.
 */
public class SimpleDefectBean {

    private String DefectID;

    private String GridLine;

    private String Remark;

    private int Type;//1 树障 2 人殉树障

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    @Transient

    private boolean isSelect = true;


    public String getDefectID() {
        return DefectID;
    }

    public void setDefectID(String defectID) {
        DefectID = defectID;
    }

    public String getGridLine() {
        return GridLine;
    }

    public void setGridLine(String gridLine) {
        GridLine = gridLine;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
