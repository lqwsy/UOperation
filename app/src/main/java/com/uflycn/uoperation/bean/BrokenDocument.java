package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**
 * 外破建档
 */
@Entity(nameInDb = "operation_BrokenDocument")
public class BrokenDocument implements Serializable {

    //外破建档系统id
    @Id(autoincrement = true)
    private Long Id;
    //登陆用户id
    private String sysUserId;
    private int sysTaskId;//工单id
    private int PlatformId;
    //起始塔Id
    @Property(nameInDb = "StartTowerId")
    private int StartTowerId;
    //终止塔Id
    @Property(nameInDb = "EndTowerId")
    private int EndTowerId;
    //外破类型
    @Property(nameInDb = "BrokenType")
    private String BrokenType;
    //专档编号
    @Property(nameInDb = "DocumentNo")
    private String DocmentNo;
    //对方单位
    @Property(nameInDb = "Company")
    private String Company;
    //联系人
    @Property(nameInDb = "ContactPerson")
    private String ContactPerson;
    //电话
    @Property(nameInDb = "PhoneNo")
    private String PhoneNo;
    //外破图片
    @Property(nameInDb = "BrokenImage")
    private String BrokenImage;

    private String VoltageClass;

    private int Status;
    //备注
    private String Remark;
    //创建时间
    private String CreateDate;

    private String UpdateTime;
    private String LineName;
    //起始杆塔编号

    private String StartTowerNo;
    //终止杆塔编号

    private String EndTowerNo;

    private String BrokenTypeName;

    private int UploadFlag = 1;

    private String PlanDailyPlanSectionIDs;

    private String sysPatrolExecutionID;

    @Generated(hash = 94556827)
    public BrokenDocument(Long Id, String sysUserId, int sysTaskId, int PlatformId,
            int StartTowerId, int EndTowerId, String BrokenType, String DocmentNo,
            String Company, String ContactPerson, String PhoneNo,
            String BrokenImage, String VoltageClass, int Status, String Remark,
            String CreateDate, String UpdateTime, String LineName,
            String StartTowerNo, String EndTowerNo, String BrokenTypeName,
            int UploadFlag, String PlanDailyPlanSectionIDs,
            String sysPatrolExecutionID) {
        this.Id = Id;
        this.sysUserId = sysUserId;
        this.sysTaskId = sysTaskId;
        this.PlatformId = PlatformId;
        this.StartTowerId = StartTowerId;
        this.EndTowerId = EndTowerId;
        this.BrokenType = BrokenType;
        this.DocmentNo = DocmentNo;
        this.Company = Company;
        this.ContactPerson = ContactPerson;
        this.PhoneNo = PhoneNo;
        this.BrokenImage = BrokenImage;
        this.VoltageClass = VoltageClass;
        this.Status = Status;
        this.Remark = Remark;
        this.CreateDate = CreateDate;
        this.UpdateTime = UpdateTime;
        this.LineName = LineName;
        this.StartTowerNo = StartTowerNo;
        this.EndTowerNo = EndTowerNo;
        this.BrokenTypeName = BrokenTypeName;
        this.UploadFlag = UploadFlag;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 375855824)
    public BrokenDocument() {
    }



    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public int getPlatformId() {
        return PlatformId;
    }

    public void setPlatformId(int platformId) {
        PlatformId = platformId;
    }

    public int getStartTowerId() {
        return StartTowerId;
    }

    public void setStartTowerId(int startTowerId) {
        StartTowerId = startTowerId;
    }

    public int getEndTowerId() {
        return EndTowerId;
    }

    public void setEndTowerId(int endTowerId) {
        EndTowerId = endTowerId;
    }

    public String getBrokenType() {
        return BrokenType;
    }

    public void setBrokenType(String brokenType) {
        BrokenType = brokenType;
    }

    public String getDocmentNo() {
        return DocmentNo;
    }

    public void setDocmentNo(String docmentNo) {
        DocmentNo = docmentNo;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getBrokenImage() {
        return BrokenImage;
    }

    public void setBrokenImage(String brokenImage) {
        BrokenImage = brokenImage;
    }

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String voltageClass) {
        VoltageClass = voltageClass;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getStartTowerNo() {
        return StartTowerNo;
    }

    public void setStartTowerNo(String startTowerNo) {
        StartTowerNo = startTowerNo;
    }

    public String getEndTowerNo() {
        return EndTowerNo;
    }

    public void setEndTowerNo(String endTowerNo) {
        EndTowerNo = endTowerNo;
    }

    public String getBrokenTypeName() {
        return BrokenTypeName;
    }

    public void setBrokenTypeName(String brokenTypeName) {
        BrokenTypeName = brokenTypeName;
    }

    public int getUploadFlag() {
        return UploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        UploadFlag = uploadFlag;
    }

    public int getSysTaskId() {
        return this.sysTaskId;
    }

    public void setSysTaskId(int sysTaskId) {
        this.sysTaskId = sysTaskId;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }
}
