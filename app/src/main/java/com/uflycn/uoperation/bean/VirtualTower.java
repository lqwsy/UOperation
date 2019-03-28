package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "operation_VirtualTower")
public class VirtualTower {

    @Id(autoincrement = true)
    private Long id;
    private Long TowerId;
    private Long TowerChangedID;
    private int LineId;
    private int PreTowerOrder;
    private int NextTowerOrder;
    private int DisplayOrder;
    private int Status;
    private double Latitude;
    private double Longitude;
    private double OriginalLatitude;
    private double OriginalLongitude;
    private double Altitude;
    private String TowerName;

    public String getTowerName() {
        return this.TowerName;
    }
    public void setTowerName(String TowerName) {
        this.TowerName = TowerName;
    }
    public double getAltitude() {
        return this.Altitude;
    }
    public void setAltitude(double Altitude) {
        this.Altitude = Altitude;
    }
    public double getOriginalLongitude() {
        return this.OriginalLongitude;
    }
    public void setOriginalLongitude(double OriginalLongitude) {
        this.OriginalLongitude = OriginalLongitude;
    }
    public double getOriginalLatitude() {
        return this.OriginalLatitude;
    }
    public void setOriginalLatitude(double OriginalLatitude) {
        this.OriginalLatitude = OriginalLatitude;
    }
    public double getLongitude() {
        return this.Longitude;
    }
    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }
    public double getLatitude() {
        return this.Latitude;
    }
    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }
    public int getStatus() {
        return this.Status;
    }
    public void setStatus(int Status) {
        this.Status = Status;
    }
    public int getDisplayOrder() {
        return this.DisplayOrder;
    }
    public void setDisplayOrder(int DisplayOrder) {
        this.DisplayOrder = DisplayOrder;
    }
    public int getNextTowerOrder() {
        return this.NextTowerOrder;
    }
    public void setNextTowerOrder(int NextTowerOrder) {
        this.NextTowerOrder = NextTowerOrder;
    }
    public int getPreTowerOrder() {
        return this.PreTowerOrder;
    }
    public void setPreTowerOrder(int PreTowerOrder) {
        this.PreTowerOrder = PreTowerOrder;
    }
    public int getLineId() {
        return this.LineId;
    }
    public void setLineId(int LineId) {
        this.LineId = LineId;
    }
    public Long getTowerChangedID() {
        return this.TowerChangedID;
    }
    public void setTowerChangedID(Long TowerChangedID) {
        this.TowerChangedID = TowerChangedID;
    }
    public Long getTowerId() {
        return this.TowerId;
    }
    public void setTowerId(Long TowerId) {
        this.TowerId = TowerId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 19168789)
    public VirtualTower(Long id, Long TowerId, Long TowerChangedID, int LineId,
            int PreTowerOrder, int NextTowerOrder, int DisplayOrder, int Status,
            double Latitude, double Longitude, double OriginalLatitude,
            double OriginalLongitude, double Altitude, String TowerName) {
        this.id = id;
        this.TowerId = TowerId;
        this.TowerChangedID = TowerChangedID;
        this.LineId = LineId;
        this.PreTowerOrder = PreTowerOrder;
        this.NextTowerOrder = NextTowerOrder;
        this.DisplayOrder = DisplayOrder;
        this.Status = Status;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.OriginalLatitude = OriginalLatitude;
        this.OriginalLongitude = OriginalLongitude;
        this.Altitude = Altitude;
        this.TowerName = TowerName;
    }
    @Generated(hash = 284280365)
    public VirtualTower() {
    }
}
