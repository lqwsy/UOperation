package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "t_RecordSyncInfo")
public class RecordSyncInfo {
    @Id(autoincrement = false)
    private Long sysRecordSyncInfoId;
    private int Category;
    private String LastSyncTime;



    @Generated(hash = 1315635999)
    public RecordSyncInfo(Long sysRecordSyncInfoId, int Category,
            String LastSyncTime) {
        this.sysRecordSyncInfoId = sysRecordSyncInfoId;
        this.Category = Category;
        this.LastSyncTime = LastSyncTime;
    }

    @Generated(hash = 112795507)
    public RecordSyncInfo() {
    }



    public void setSysRecordSyncInfoId(Long sysRecordSyncInfoId) {
        this.sysRecordSyncInfoId = sysRecordSyncInfoId;
    }

    public Long getSysRecordSyncInfoId() {
        return sysRecordSyncInfoId;
    }

    public void setSysRecordSyncInfoId(long sysRecordSyncInfoId) {
        this.sysRecordSyncInfoId = sysRecordSyncInfoId;
    }



    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }

    public String getLastSyncTime() {
        return LastSyncTime;
    }

    public void setLastSyncTime(String lastSyncTime) {
        LastSyncTime = lastSyncTime;
    }
}
