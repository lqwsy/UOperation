package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

@Entity(nameInDb = "t_dayPlan")
public class DayPlan extends DayPlanBaseBean implements Serializable {
    @org.greenrobot.greendao.annotation.Id
    private Long Id;
    /**
     * sysDailyPlanSectionID  : f7cf59b0-24ed-4fcc-bac9-bd7152aa1661
     * StartDateString  : 2019-01-14  时间
     * TypeOfWorkString  :   任务类型
     * ResponsiblePersonName  : 1  负责人
     * OfficeHolderNames  : 38   工作人员
     * //暂时还缺一个工作内容
     * Remark  : null  备注
     */
    private String sysDailyPlanSectionID;
    private String StartDateString;
    private String StatusString;
    private String TypeOfWorkString;
    private String ResponsiblePersonName;
    private String ResponsiblePersonID;//负责人id
    private String OfficeHolderNames;
    private String JobContent;//工作内容现在还没有返回，后面后台添加
    private String Remark;
    @Transient //这个现在没有用了，
    private int isFromTopType;//是上面的 日 周，月，
    @Transient
    private int type;//0代表着自己的类型， 1代表着所有的类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsFromTopType() {
        return isFromTopType;
    }

    public void setIsFromTopType(int isFromTopType) {
        this.isFromTopType = isFromTopType;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getJobContent() {
        return this.JobContent;
    }

    public void setJobContent(String JobContent) {
        this.JobContent = JobContent;
    }

    public String getOfficeHolderNames() {
        return this.OfficeHolderNames;
    }

    public void setOfficeHolderNames(String OfficeHolderNames) {
        this.OfficeHolderNames = OfficeHolderNames;
    }

    public String getResponsiblePersonID() {
        return this.ResponsiblePersonID;
    }

    public void setResponsiblePersonID(String ResponsiblePersonID) {
        this.ResponsiblePersonID = ResponsiblePersonID;
    }

    public String getResponsiblePersonName() {
        return this.ResponsiblePersonName;
    }

    public void setResponsiblePersonName(String ResponsiblePersonName) {
        this.ResponsiblePersonName = ResponsiblePersonName;
    }

    public String getTypeOfWorkString() {
        return this.TypeOfWorkString;
    }

    public void setTypeOfWorkString(String TypeOfWorkString) {
        this.TypeOfWorkString = TypeOfWorkString;
    }

    public String getStatusString() {
        return this.StatusString;
    }

    public void setStatusString(String StatusString) {
        this.StatusString = StatusString;
    }

    public String getStartDateString() {
        return this.StartDateString;
    }

    public void setStartDateString(String StartDateString) {
        this.StartDateString = StartDateString;
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

    @Generated(hash = 554231890)
    public DayPlan(Long Id, String sysDailyPlanSectionID, String StartDateString,
                   String StatusString, String TypeOfWorkString,
                   String ResponsiblePersonName, String ResponsiblePersonID,
                   String OfficeHolderNames, String JobContent, String Remark) {
        this.Id = Id;
        this.sysDailyPlanSectionID = sysDailyPlanSectionID;
        this.StartDateString = StartDateString;
        this.StatusString = StatusString;
        this.TypeOfWorkString = TypeOfWorkString;
        this.ResponsiblePersonName = ResponsiblePersonName;
        this.ResponsiblePersonID = ResponsiblePersonID;
        this.OfficeHolderNames = OfficeHolderNames;
        this.JobContent = JobContent;
        this.Remark = Remark;
    }

    @Generated(hash = 1444131124)
    public DayPlan() {
    }


}
