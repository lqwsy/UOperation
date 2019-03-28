package com.uflycn.uoperation.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by Administrator on 2017/9/11.
 */
@Entity(nameInDb = "t_Tower")
public class Tower implements Parcelable {
  /**
   * "sysTowerID": 122,
   "sysGridLineId": 5,
   "TowerNo": "1",
   "TowerType": null,
   "TowerAssetsCode": "1",
   "TowerAltitude": 18,
   "Latitude": 28.220409,
   "Longitude": 115.414587,
   "HeadAltitude": 14,
   "Altitude": 44,
   "DisplayOrder": 1,
   "Deleted": true
   * */
    @Id(autoincrement = false)
    private Long sysTowerID;
    private int sysGridLineId;
    private String TowerType;
    private String TowerNo;
    private String TowerAssetsCode;
    private String TowerAltitude;
    private double Latitude;
    private double Longitude;
    private String HeadAltitude;
    private double Altitude;
    private int DisplayOrder;
    private boolean Deleted;
    @Transient//这个属性是为了从线路中选择日计划的，对话框中的选择是否选中
    private boolean isChecked;
    @Transient//这个属性为了添加线路 选择线路是否被巡视用的
    private boolean isTour;

    public boolean isTour() {
        return isTour;
    }

    public void setTour(boolean tour) {
        isTour = tour;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Generated(hash = 189129997)
    public Tower(Long sysTowerID, int sysGridLineId, String TowerType,
            String TowerNo, String TowerAssetsCode, String TowerAltitude,
            double Latitude, double Longitude, String HeadAltitude,
            double Altitude, int DisplayOrder, boolean Deleted) {
        this.sysTowerID = sysTowerID;
        this.sysGridLineId = sysGridLineId;
        this.TowerType = TowerType;
        this.TowerNo = TowerNo;
        this.TowerAssetsCode = TowerAssetsCode;
        this.TowerAltitude = TowerAltitude;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.HeadAltitude = HeadAltitude;
        this.Altitude = Altitude;
        this.DisplayOrder = DisplayOrder;
        this.Deleted = Deleted;
    }

    @Generated(hash = 1554454725)
    public Tower() {
    }


    public Long getSysTowerID() {
        return sysTowerID;
    }

    public void setSysTowerID(Long sysTowerID) {
        this.sysTowerID = sysTowerID;
    }

    public void setSysGridLineId(int sysGridLineId) {
        this.sysGridLineId = sysGridLineId;
    }

    public String getTowerType() {
        return TowerType;
    }

    public void setTowerType(String towerType) {
        TowerType = towerType;
    }

    public String getTowerNo() {
        return TowerNo;
    }

    public void setTowerNo(String towerNo) {
        TowerNo = towerNo;
    }

    public String getTowerAssetsCode() {
        return TowerAssetsCode;
    }

    public void setTowerAssetsCode(String towerAssetsCode) {
        TowerAssetsCode = towerAssetsCode;
    }

    public String getTowerAltitude() {
        return TowerAltitude;
    }

    public void setTowerAltitude(String towerAltitude) {
        TowerAltitude = towerAltitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getHeadAltitude() {
        return HeadAltitude;
    }

    public void setHeadAltitude(String headAltitude) {
        HeadAltitude = headAltitude;
    }

    public double getAltitude() {
        return Altitude;
    }

    public void setAltitude(double altitude) {
        Altitude = altitude;
    }

    public int getDisplayOrder() {
        return DisplayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        DisplayOrder = displayOrder;
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

    public int getSysGridLineId() {
        return this.sysGridLineId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.sysTowerID);
        dest.writeInt(this.sysGridLineId);
        dest.writeString(this.TowerType);
        dest.writeString(this.TowerNo);
        dest.writeString(this.TowerAssetsCode);
        dest.writeString(this.TowerAltitude);
        dest.writeDouble(this.Latitude);
        dest.writeDouble(this.Longitude);
        dest.writeString(this.HeadAltitude);
        dest.writeDouble(this.Altitude);
        dest.writeInt(this.DisplayOrder);
        dest.writeByte(this.Deleted ? (byte) 1 : (byte) 0);
    }

    protected Tower(Parcel in) {
        this.sysTowerID = (Long) in.readValue(Long.class.getClassLoader());
        this.sysGridLineId = in.readInt();
        this.TowerType = in.readString();
        this.TowerNo = in.readString();
        this.TowerAssetsCode = in.readString();
        this.TowerAltitude = in.readString();
        this.Latitude = in.readDouble();
        this.Longitude = in.readDouble();
        this.HeadAltitude = in.readString();
        this.Altitude = in.readDouble();
        this.DisplayOrder = in.readInt();
        this.Deleted = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Tower> CREATOR = new Parcelable.Creator<Tower>() {
        @Override
        public Tower createFromParcel(Parcel source) {
            return new Tower(source);
        }

        @Override
        public Tower[] newArray(int size) {
            return new Tower[size];
        }
    };
}
