package com.uflycn.uoperation.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/26
 * Describe  : 线路
 */
public class LineBean implements Parcelable {

    /**
     * lineName : 庄钤线
     * VoltageClass : 35kV
     * sysGridLineId : 185
     * MaintainClass : 07137e0b-4221-4bca-90b3-c5c43ddcdffc
     * InspectTowerIds :
     * InspectTowerNos :
     * SelectMonth :
     * MaintainClassName : 运检公司
     * DangerDefectCount : null
     * SeriousDefectCount : null
     * NormalDefectCount : null
     */

    private String lineName;
    private int sysGridLineId;
    private String VoltageClass;
    private String MaintainClass;
    private String InspectTowerIds;
    private String InspectTowerNos;
    private String SelectMonth;
    private String MaintainClassName;
    private String DangerDefectCount;
    private String SeriousDefectCount;
    private String NormalDefectCount;

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String voltageClass) {
        VoltageClass = voltageClass;
    }

    public int getSysGridLineId() {
        return sysGridLineId;
    }

    public void setSysGridLineId(int sysGridLineId) {
        this.sysGridLineId = sysGridLineId;
    }

    public String getMaintainClass() {
        return MaintainClass;
    }

    public void setMaintainClass(String maintainClass) {
        MaintainClass = maintainClass;
    }

    public String getInspectTowerIds() {
        return InspectTowerIds;
    }

    public void setInspectTowerIds(String inspectTowerIds) {
        InspectTowerIds = inspectTowerIds;
    }

    public String getInspectTowerNos() {
        return InspectTowerNos;
    }

    public void setInspectTowerNos(String inspectTowerNos) {
        InspectTowerNos = inspectTowerNos;
    }

    public String getSelectMonth() {
        return SelectMonth;
    }

    public void setSelectMonth(String selectMonth) {
        SelectMonth = selectMonth;
    }

    public String getMaintainClassName() {
        return MaintainClassName;
    }

    public void setMaintainClassName(String maintainClassName) {
        MaintainClassName = maintainClassName;
    }

    public String getDangerDefectCount() {
        return DangerDefectCount;
    }

    public void setDangerDefectCount(String dangerDefectCount) {
        DangerDefectCount = dangerDefectCount;
    }

    public String getSeriousDefectCount() {
        return SeriousDefectCount;
    }

    public void setSeriousDefectCount(String seriousDefectCount) {
        SeriousDefectCount = seriousDefectCount;
    }

    public String getNormalDefectCount() {
        return NormalDefectCount;
    }

    public void setNormalDefectCount(String normalDefectCount) {
        NormalDefectCount = normalDefectCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lineName);
        dest.writeString(this.VoltageClass);
        dest.writeInt(this.sysGridLineId);
        dest.writeString(this.MaintainClass);
        dest.writeString(this.InspectTowerIds);
        dest.writeString(this.InspectTowerNos);
        dest.writeString(this.SelectMonth);
        dest.writeString(this.MaintainClassName);
        dest.writeString(this.DangerDefectCount);
        dest.writeString(this.SeriousDefectCount);
        dest.writeString(this.NormalDefectCount);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    public LineBean() {
    }

    protected LineBean(Parcel in) {
        this.lineName = in.readString();
        this.VoltageClass = in.readString();
        this.sysGridLineId = in.readInt();
        this.MaintainClass = in.readString();
        this.InspectTowerIds = in.readString();
        this.InspectTowerNos = in.readString();
        this.SelectMonth = in.readString();
        this.MaintainClassName = in.readString();
        this.DangerDefectCount = in.readString();
        this.SeriousDefectCount = in.readString();
        this.NormalDefectCount = in.readString();
        this.isSelect = in.readByte() != 0;
    }

    public static final Parcelable.Creator<LineBean> CREATOR = new Parcelable.Creator<LineBean>() {
        @Override
        public LineBean createFromParcel(Parcel source) {
            return new LineBean(source);
        }

        @Override
        public LineBean[] newArray(int size) {
            return new LineBean[size];
        }
    };
}
