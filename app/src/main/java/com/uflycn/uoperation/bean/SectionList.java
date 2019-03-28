package com.uflycn.uoperation.bean;

/**
 * Created by UF_PC on 2017/10/23.
 */
public class SectionList {

    private String VoltageClass;
    private int LineId;
    private String LineName;
    private String StartTower;
    private String EndTower;

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String voltageClass) {
        VoltageClass = voltageClass;
    }

    public int getLineId() {
        return LineId;
    }

    public void setLineId(int lineId) {
        LineId = lineId;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getStartTower() {
        return StartTower;
    }

    public void setStartTower(String startTower) {
        StartTower = startTower;
    }

    public String getEndTower() {
        return EndTower;
    }

    public void setEndTower(String endTower) {
        EndTower = endTower;
    }
}
