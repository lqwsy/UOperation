package com.uflycn.uoperation.bean;

/**
 * Created by UF_PC on 2017/11/15.
 */
public class DefectCount {
    private int DefectCount;

    private int Code;

    private String Message;

    private int sysGridLineID;

    public int getSysGridLineID() {
        return sysGridLineID;
    }

    public void setSysGridLineID(int sysGridLineID) {
        this.sysGridLineID = sysGridLineID;
    }

    public int getDefectCount() {
        return DefectCount;
    }

    public void setDefectCount(int defectCount) {
        DefectCount = defectCount;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
