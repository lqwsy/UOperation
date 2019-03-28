package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

@Entity(nameInDb = "t_GridLine")
public class Gridline {//线路
    @Id(autoincrement = false)//不需要自增长 由服务器获取
    private Long sysGridLineID;
    private String LineName;
    private String VoltageClass;
    private String MaintainClass;
    private String MaintainUser;
    private int TowerCount;
    private double LineLength;
    private boolean Deleted;

    @Transient
    private String VClass;
    @Transient
    private String FirstTower; //起塔
    @Transient
    private String EndTower; //止塔

    @Generated(hash = 43405049)
    public Gridline(Long sysGridLineID, String LineName, String VoltageClass,
                    String MaintainClass, String MaintainUser, int TowerCount,
                    double LineLength, boolean Deleted) {
        this.sysGridLineID = sysGridLineID;
        this.LineName = LineName;
        this.VoltageClass = VoltageClass;
        this.MaintainClass = MaintainClass;
        this.MaintainUser = MaintainUser;
        this.TowerCount = TowerCount;
        this.LineLength = LineLength;
        this.Deleted = Deleted;
    }

    @Generated(hash = 284222676)
    public Gridline() {
    }

    public Long getSysGridLineID() {
        return sysGridLineID;
    }

    public void setSysGridLineID(Long sysGridLineID) {
        this.sysGridLineID = sysGridLineID;
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

    public String getMaintainClass() {
        return MaintainClass;
    }

    public void setMaintainClass(String maintainClass) {
        MaintainClass = maintainClass;
    }

    public String getMaintainUser() {
        return MaintainUser;
    }

    public void setMaintainUser(String maintainUser) {
        MaintainUser = maintainUser;
    }

    public int getTowerCount() {
        return TowerCount;
    }

    public void setTowerCount(int towerCount) {
        TowerCount = towerCount;
    }

    public double getLineLength() {
        return LineLength;
    }

    public void setLineLength(double lineLength) {
        LineLength = lineLength;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public boolean getDeleted() {
        return this.Deleted;
    }

    public String getFirstTower() {
        return FirstTower;
    }

    public void setFirstTower(String firstTower) {
        FirstTower = firstTower;
    }

    public String getEndTower() {
        return EndTower;
    }

    public void setEndTower(String endTower) {
        EndTower = endTower;
    }

    public String getVClass() {
        return VClass;
    }

    public void setVClass(String VClass) {
        this.VClass = VClass;
    }

    @Override
    public String toString() {
        return "Gridline{" +
                "sysGridLineID=" + sysGridLineID +
                ", LineName='" + LineName + '\'' +
                ", VoltageClass='" + VoltageClass + '\'' +
                ", MaintainClass='" + MaintainClass + '\'' +
                ", MaintainUser='" + MaintainUser + '\'' +
                ", TowerCount=" + TowerCount +
                ", LineLength=" + LineLength +
                ", Deleted=" + Deleted +
                ", FirstTower='" + FirstTower + '\'' +
                ", EndTower='" + EndTower + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Gridline) {
            Gridline u = (Gridline) obj;
            return this.sysGridLineID.equals(u.sysGridLineID);
        }
        return super.equals(obj);
    }
}
