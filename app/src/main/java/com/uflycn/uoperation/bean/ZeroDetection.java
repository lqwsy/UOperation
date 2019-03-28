package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 零值检测
 */
@Entity(nameInDb = "operation_ZeroDetection")
public class ZeroDetection {

    @Id(autoincrement = true)
    private Long Id;

    private String sysUserId;

    private String sysTowerId;

    //直线塔 耐张塔
    private String TowerType;

    //跳线类型
    private String JumperType;

    //绝缘串类型
    private String InsulatorType;

    //绝缘子片数
    private int InsulatorStringLength;

    //跳线片数
    private Integer JumperStringLength;


    private String PhaseALeft1; //A相左1

    private String PhaseALeft2; //A相左2

    private String PhaseARight1; //A相右1

    private String PhaseARight2; //A相右2

    private String PhaseBLeft1; //B相左1

    private String PhaseBLeft2; //B相左2

    private String PhaseBRight1; //B相右1

    private String PhaseBRight2; //B相右2

    private String PhaseCLeft1; //C相左1

    private String PhaseCLeft2; //C相左2

    private String PhaseCRight1; //C相右1

    private String PhaseCRight2; //C相右2

    private String JumperA1; //跳线A1

    private String JumperA2; //跳线A2

    private String JumperB1; //跳线B1

    private String JumperB2; //跳线B2

    private String JumperC1; //跳线C1

    private String JumperC2; //跳线C2

    private String CreateDate;
    private int UploadFlag;
    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;

    @Generated(hash = 871247233)
    public ZeroDetection(Long Id, String sysUserId, String sysTowerId,
            String TowerType, String JumperType, String InsulatorType,
            int InsulatorStringLength, Integer JumperStringLength,
            String PhaseALeft1, String PhaseALeft2, String PhaseARight1,
            String PhaseARight2, String PhaseBLeft1, String PhaseBLeft2,
            String PhaseBRight1, String PhaseBRight2, String PhaseCLeft1,
            String PhaseCLeft2, String PhaseCRight1, String PhaseCRight2,
            String JumperA1, String JumperA2, String JumperB1, String JumperB2,
            String JumperC1, String JumperC2, String CreateDate, int UploadFlag,
            String PlanDailyPlanSectionIDs, String sysPatrolExecutionID) {
        this.Id = Id;
        this.sysUserId = sysUserId;
        this.sysTowerId = sysTowerId;
        this.TowerType = TowerType;
        this.JumperType = JumperType;
        this.InsulatorType = InsulatorType;
        this.InsulatorStringLength = InsulatorStringLength;
        this.JumperStringLength = JumperStringLength;
        this.PhaseALeft1 = PhaseALeft1;
        this.PhaseALeft2 = PhaseALeft2;
        this.PhaseARight1 = PhaseARight1;
        this.PhaseARight2 = PhaseARight2;
        this.PhaseBLeft1 = PhaseBLeft1;
        this.PhaseBLeft2 = PhaseBLeft2;
        this.PhaseBRight1 = PhaseBRight1;
        this.PhaseBRight2 = PhaseBRight2;
        this.PhaseCLeft1 = PhaseCLeft1;
        this.PhaseCLeft2 = PhaseCLeft2;
        this.PhaseCRight1 = PhaseCRight1;
        this.PhaseCRight2 = PhaseCRight2;
        this.JumperA1 = JumperA1;
        this.JumperA2 = JumperA2;
        this.JumperB1 = JumperB1;
        this.JumperB2 = JumperB2;
        this.JumperC1 = JumperC1;
        this.JumperC2 = JumperC2;
        this.CreateDate = CreateDate;
        this.UploadFlag = UploadFlag;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 997612755)
    public ZeroDetection() {
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

    public String getSysTowerId() {
        return sysTowerId;
    }

    public void setSysTowerId(String sysTowerId) {
        this.sysTowerId = sysTowerId;
    }

    public String getTowerType() {
        return TowerType;
    }

    public void setTowerType(String towerType) {
        TowerType = towerType;
    }

    public String getJumperType() {
        return JumperType;
    }

    public void setJumperType(String jumperType) {
        JumperType = jumperType;
    }

    public String getInsulatorType() {
        return InsulatorType;
    }

    public void setInsulatorType(String insulatorType) {
        InsulatorType = insulatorType;
    }

    public int getInsulatorStringLength() {
        return InsulatorStringLength;
    }

    public void setInsulatorStringLength(int insulatorStringLength) {
        InsulatorStringLength = insulatorStringLength;
    }

    public Integer getJumperStringLength() {
        return JumperStringLength;
    }

    public void setJumperStringLength(Integer jumperStringLength) {
        JumperStringLength = jumperStringLength;
    }

    public String getPhaseALeft1() {
        return PhaseALeft1;
    }

    public void setPhaseALeft1(String phaseALeft1) {
        PhaseALeft1 = phaseALeft1;
    }

    public String getPhaseALeft2() {
        return PhaseALeft2;
    }

    public void setPhaseALeft2(String phaseALeft2) {
        PhaseALeft2 = phaseALeft2;
    }

    public String getPhaseARight1() {
        return PhaseARight1;
    }

    public void setPhaseARight1(String phaseARight1) {
        PhaseARight1 = phaseARight1;
    }

    public String getPhaseARight2() {
        return PhaseARight2;
    }

    public void setPhaseARight2(String phaseARight2) {
        PhaseARight2 = phaseARight2;
    }

    public String getPhaseBLeft1() {
        return PhaseBLeft1;
    }

    public void setPhaseBLeft1(String phaseBLeft1) {
        PhaseBLeft1 = phaseBLeft1;
    }

    public String getPhaseBLeft2() {
        return PhaseBLeft2;
    }

    public void setPhaseBLeft2(String phaseBLeft2) {
        PhaseBLeft2 = phaseBLeft2;
    }

    public String getPhaseBRight1() {
        return PhaseBRight1;
    }

    public void setPhaseBRight1(String phaseBRight1) {
        PhaseBRight1 = phaseBRight1;
    }

    public String getPhaseBRight2() {
        return PhaseBRight2;
    }

    public void setPhaseBRight2(String phaseBRight2) {
        PhaseBRight2 = phaseBRight2;
    }

    public String getPhaseCLeft1() {
        return PhaseCLeft1;
    }

    public void setPhaseCLeft1(String phaseCLeft1) {
        PhaseCLeft1 = phaseCLeft1;
    }

    public String getPhaseCLeft2() {
        return PhaseCLeft2;
    }

    public void setPhaseCLeft2(String phaseCLeft2) {
        PhaseCLeft2 = phaseCLeft2;
    }

    public String getPhaseCRight1() {
        return PhaseCRight1;
    }

    public void setPhaseCRight1(String phaseCRight1) {
        PhaseCRight1 = phaseCRight1;
    }

    public String getPhaseCRight2() {
        return PhaseCRight2;
    }

    public void setPhaseCRight2(String phaseCRight2) {
        PhaseCRight2 = phaseCRight2;
    }

    public String getJumperA1() {
        return JumperA1;
    }

    public void setJumperA1(String jumperA1) {
        JumperA1 = jumperA1;
    }

    public String getJumperA2() {
        return JumperA2;
    }

    public void setJumperA2(String jumperA2) {
        JumperA2 = jumperA2;
    }

    public String getJumperB1() {
        return JumperB1;
    }

    public void setJumperB1(String jumperB1) {
        JumperB1 = jumperB1;
    }

    public String getJumperB2() {
        return JumperB2;
    }

    public void setJumperB2(String jumperB2) {
        JumperB2 = jumperB2;
    }

    public String getJumperC1() {
        return JumperC1;
    }

    public void setJumperC1(String jumperC1) {
        JumperC1 = jumperC1;
    }

    public String getJumperC2() {
        return JumperC2;
    }

    public void setJumperC2(String jumperC2) {
        JumperC2 = jumperC2;
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
