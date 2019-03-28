package com.uflycn.uoperation.bean;

/**
 * 我的巡视
 * Created by xiaoyehai on 2017/9/7.
 */
public class StartedTask {
    /**
     * "LineId": 9,
     * "VoltageClass": "220kV",
     * "VoltageOrder": 83,
     * "LineName": "罗渝Ⅰ、Ⅱ线",
     * "TowerCount": 35,
     * "LineLength": 12053.178948821804,
     * "StartTower": "11",
     * "StartTowerOrder": 11,
     * "EndTower": "45",
     * "EndTowerOrder": 45,
     * "DefectCount": 2,
     * "InspectedTowerCount": 3,9414673,
     * "InspectedTowerA": "12",
     * "TowerAOrder": 0,
     * "InspectedTowerB": "18",
     * "TowerBOrder": 0
     */


    private int LineId;//线ID
    private String VoltageClass;//多少V
    private String LineName;//线路名
    private int TowerCount;//塔数量
    private double LineLength;//线长度
    private String StartTower;//起塔
    private String EndTower;//止塔
    private int DefectCount;//缺陷数量
    private int InspectedTowerCount;//已巡塔数量
    private String InspectedTowerA;//已巡塔始
    private String InspectedTowerB;//已巡塔末

    private String VClass;

    public String getVClass() {
        return VClass;
    }

    public void setVClass(String VClass) {
        this.VClass = VClass;
    }

    public String getInspectedTowerB() {
        return InspectedTowerB;
    }

    public void setInspectedTowerB(String inspectedTowerB) {
        InspectedTowerB = inspectedTowerB;
    }

    public String getInspectedTowerA() {
        return InspectedTowerA;
    }

    public void setInspectedTowerA(String inspectedTowerA) {
        InspectedTowerA = inspectedTowerA;
    }

    public int getInspectedTowerCount() {
        return InspectedTowerCount;
    }

    public void setInspectedTowerCount(int inspectedTowerCount) {
        InspectedTowerCount = inspectedTowerCount;
    }

    public int getDefectCount() {
        return DefectCount;
    }

    public void setDefectCount(int defectCount) {
        DefectCount = defectCount;
    }

    public String getEndTower() {
        return EndTower;
    }

    public void setEndTower(String endTower) {
        EndTower = endTower;
    }

    public String getStartTower() {
        return StartTower;
    }

    public void setStartTower(String startTower) {
        StartTower = startTower;
    }

    public double getLineLength() {
        return LineLength;
    }

    public void setLineLength(double lineLength) {
        LineLength = lineLength;
    }

    public int getTowerCount() {
        return TowerCount;
    }

    public void setTowerCount(int towerCount) {
        TowerCount = towerCount;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

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

    public void setFromGridLine(Gridline line){
        this.LineId = line.getSysGridLineID().intValue();
        this.LineName = line.getLineName();
        this.LineLength = line.getLineLength();
        this.TowerCount = line.getTowerCount();
        this.StartTower = line.getFirstTower();
        this.EndTower = line.getEndTower();
        this.VoltageClass = line.getVoltageClass();

    }
}
