package com.uflycn.uoperation.bean;

/**
 * 树障缺陷
 */


public class TreeBarrierBean {

    private int TowerA_ID;//起始杆塔id
    private int TowerB_ID;//终止杆塔id
    private float Latitude;//纬度
    private float Longitude;//经度
    private float DistanceFromTower;//距离小号塔距离
    private int DefectLevel;//缺陷等级
    private float Temperature;//温度
    private float DistanceFromLine;//净空距离
    private float DistanceFromLineH;//水平距离
    private float DistanceFromLineV;//垂直距离
    private String TreeSeed;//树种
    private String TreeSeedNumber;//数量

    public int getTowerA_ID() {
        return TowerA_ID;
    }

    public void setTowerA_ID(int towerA_ID) {
        TowerA_ID = towerA_ID;
    }

    public int getTowerB_ID() {
        return TowerB_ID;
    }

    public void setTowerB_ID(int towerB_ID) {
        TowerB_ID = towerB_ID;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public float getDistanceFromTower() {
        return DistanceFromTower;
    }

    public void setDistanceFromTower(float distanceFromTower) {
        DistanceFromTower = distanceFromTower;
    }

    public int getDefectLevel() {
        return DefectLevel;
    }

    public void setDefectLevel(int defectLevel) {
        DefectLevel = defectLevel;
    }

    public float getTemperature() {
        return Temperature;
    }

    public void setTemperature(float temperature) {
        Temperature = temperature;
    }

    public float getDistanceFromLine() {
        return DistanceFromLine;
    }

    public void setDistanceFromLine(float distanceFromLine) {
        DistanceFromLine = distanceFromLine;
    }

    public float getDistanceFromLineH() {
        return DistanceFromLineH;
    }

    public void setDistanceFromLineH(float distanceFromLineH) {
        DistanceFromLineH = distanceFromLineH;
    }

    public float getDistanceFromLineV() {
        return DistanceFromLineV;
    }

    public void setDistanceFromLineV(float distanceFromLineV) {
        DistanceFromLineV = distanceFromLineV;
    }

    public String getTreeSeed() {
        return TreeSeed;
    }

    public void setTreeSeed(String treeSeed) {
        TreeSeed = treeSeed;
    }

    public String getTreeSeedNumber() {
        return TreeSeedNumber;
    }

    public void setTreeSeedNumber(String treeSeedNumber) {
        TreeSeedNumber = treeSeedNumber;
    }
}
