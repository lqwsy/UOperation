package com.uflycn.uoperation.bean;

/**
 * Created by Xiong on 2017/9/28.
 */
public class LineCrossType {
    private int ParentId;
    private int sysDefectTypeID;
    private String DefectName1;
    private String DefectName2;

    public int getParentId() {
        return ParentId;
    }

    public void setParentId(int parentId) {
        ParentId = parentId;
    }

    public int getSysDefectTypeID() {
        return sysDefectTypeID;
    }

    public void setSysDefectTypeID(int sysDefectTypeID) {
        this.sysDefectTypeID = sysDefectTypeID;
    }

    public String getDefectName1() {
        return DefectName1;
    }

    public void setDefectName1(String defectName1) {
        DefectName1 = defectName1;
    }

    public String getDefectName2() {
        return DefectName2;
    }

    public void setDefectName2(String defectName2) {
        DefectName2 = defectName2;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof LineCrossType){
            if((((LineCrossType) o).getDefectName1().equalsIgnoreCase(getDefectName1())
                    && ((LineCrossType) o).getDefectName2().equalsIgnoreCase(getDefectName2()))
                    || sysDefectTypeID == ((LineCrossType) o).sysDefectTypeID){
                return true;
            }
        }
        return false;
    }
}
