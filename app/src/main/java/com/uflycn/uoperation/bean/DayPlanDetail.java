package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "t_dayPlanDetail")
public class DayPlanDetail {
    @org.greenrobot.greendao.annotation.Id
    private Long Id;

    private String sysDailyPlanSectionID;

    private int Status;

    private String WorkNote;

    private boolean uploadFlag;

    public boolean getUploadFlag() {
        return this.uploadFlag;
    }

    public void setUploadFlag(boolean uploadFlag) {
        this.uploadFlag = uploadFlag;
    }

    public String getWorkNote() {
        return this.WorkNote;
    }

    public void setWorkNote(String WorkNote) {
        this.WorkNote = WorkNote;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getSysDailyPlanSectionID() {
        return this.sysDailyPlanSectionID;
    }

    public void setSysDailyPlanSectionID(String sysDailyPlanSectionID) {
        this.sysDailyPlanSectionID = sysDailyPlanSectionID;
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    @Generated(hash = 1206776897)
    public DayPlanDetail(Long Id, String sysDailyPlanSectionID, int Status,
            String WorkNote, boolean uploadFlag) {
        this.Id = Id;
        this.sysDailyPlanSectionID = sysDailyPlanSectionID;
        this.Status = Status;
        this.WorkNote = WorkNote;
        this.uploadFlag = uploadFlag;
    }

    @Generated(hash = 1917686932)
    public DayPlanDetail() {
    }

}
