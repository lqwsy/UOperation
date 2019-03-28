package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 接地电阻
 */
@Entity(nameInDb = "operation_EarthingResistance")
public class EarthingResistance {

    @Id(autoincrement = true)
    private Long Id;
    private String sysUserId;

    private int TowerId;

    private Double ResistanceA; //接地电阻A

    private Double ResistanceB; //接地电阻B

    private Double ResistanceC; //接地电阻C

    private Double ResistanceD; //接地电阻D

    private String CreateDate;
    private int UploadFlag;
    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;


    @Generated(hash = 333007036)
    public EarthingResistance(Long Id, String sysUserId, int TowerId,
            Double ResistanceA, Double ResistanceB, Double ResistanceC,
            Double ResistanceD, String CreateDate, int UploadFlag,
            String PlanDailyPlanSectionIDs, String sysPatrolExecutionID) {
        this.Id = Id;
        this.sysUserId = sysUserId;
        this.TowerId = TowerId;
        this.ResistanceA = ResistanceA;
        this.ResistanceB = ResistanceB;
        this.ResistanceC = ResistanceC;
        this.ResistanceD = ResistanceD;
        this.CreateDate = CreateDate;
        this.UploadFlag = UploadFlag;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 1182502230)
    public EarthingResistance() {
    }

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
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

    public int getTowerId() {
        return TowerId;
    }

    public void setTowerId(int towerId) {
        TowerId = towerId;
    }

    public Double getResistanceA() {
        return ResistanceA;
    }

    public void setResistanceA(Double resistanceA) {
        ResistanceA = resistanceA;
    }

    public Double getResistanceB() {
        return ResistanceB;
    }

    public void setResistanceB(Double resistanceB) {
        ResistanceB = resistanceB;
    }

    public Double getResistanceC() {
        return ResistanceC;
    }

    public void setResistanceC(Double resistanceC) {
        ResistanceC = resistanceC;
    }

    public Double getResistanceD() {
        return ResistanceD;
    }

    public void setResistanceD(Double resistanceD) {
        ResistanceD = resistanceD;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public int getUploadFlag() {
        return UploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        UploadFlag = uploadFlag;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }
}
