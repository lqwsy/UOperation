package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2018/12/17
 * Describe  :
 */
@Entity(nameInDb = "operation_OptTensilePointTemperature")
public class OptTensilePointTemperature {
    @Id(autoincrement = true)
    private Long id;

    private Long InfraredTemperatureId;//这个是对于InfraredTemperature的id的引用
    private String Phase;//0-A相，1-B相，2-C相
    private String SideBar;//0-悬垂,1-小号侧，2-大号侧  中间接头都是悬垂
    private Integer BiasPoint;//0,1,2，3，4。赣西1代表上，2代表下，单导线全部是0
    private Double Temperature; //  填入的值
    private String Longitude;//中间接头必填
    private String Latitude;
    public String getLatitude() {
        return this.Latitude;
    }
    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }
    public String getLongitude() {
        return this.Longitude;
    }
    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }
    public Double getTemperature() {
        return this.Temperature;
    }
    public void setTemperature(Double Temperature) {
        this.Temperature = Temperature;
    }
    public Integer getBiasPoint() {
        return this.BiasPoint;
    }
    public void setBiasPoint(Integer BiasPoint) {
        this.BiasPoint = BiasPoint;
    }
    public String getSideBar() {
        return this.SideBar;
    }
    public void setSideBar(String SideBar) {
        this.SideBar = SideBar;
    }
    public String getPhase() {
        return this.Phase;
    }
    public void setPhase(String Phase) {
        this.Phase = Phase;
    }
    public Long getInfraredTemperatureId() {
        return this.InfraredTemperatureId;
    }
    public void setInfraredTemperatureId(Long InfraredTemperatureId) {
        this.InfraredTemperatureId = InfraredTemperatureId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 753412911)
    public OptTensilePointTemperature(Long id, Long InfraredTemperatureId,
            String Phase, String SideBar, Integer BiasPoint, Double Temperature,
            String Longitude, String Latitude) {
        this.id = id;
        this.InfraredTemperatureId = InfraredTemperatureId;
        this.Phase = Phase;
        this.SideBar = SideBar;
        this.BiasPoint = BiasPoint;
        this.Temperature = Temperature;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
    }
    @Generated(hash = 833726296)
    public OptTensilePointTemperature() {
    }
}
