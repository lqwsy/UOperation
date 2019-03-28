package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by Administrator on 2017/9/21.
 */
public class BrokenPatrolDetail {
    /**
     * "sysBrokenPatrolDetailId": 6,
     "PatrolImage": "BrokenDocument/20170921/2cd8195676104b91b1740f390b2bcc3b.jpg",
     "Remark": "测试",
     "CreateDate": "2017-09-21T10:09:33.563",
     "BrokenStatus": "未消除"
     * */
    @Id(autoincrement = true)
    private long sysBrokenPatrolDetailId;
    private int sysBrokenDocumentId;
    private String sysUserId;
    private int BrokenStatus;
    private String PatrolImage;
    private String Remark;
    private String CreateDate;
    private int UploadFlag;
    @Transient
    private String BrokenStatusName;

    private String PlanDailyPlanSectionIDs;


    public long getSysBrokenPatrolDetailId() {
        return sysBrokenPatrolDetailId;
    }

    public void setSysBrokenPatrolDetailId(long sysBrokenPatrolDetailId) {
        this.sysBrokenPatrolDetailId = sysBrokenPatrolDetailId;
    }

    public int getSysBrokenDocumentId() {
        return sysBrokenDocumentId;
    }

    public void setSysBrokenDocumentId(int sysBrokenDocumentId) {
        this.sysBrokenDocumentId = sysBrokenDocumentId;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public int getBrokenStatus() {
        return BrokenStatus;
    }

    public void setBrokenStatus(int brokenStatus) {
        BrokenStatus = brokenStatus;
    }

    public String getPatrolImage() {
        return PatrolImage;
    }

    public void setPatrolImage(String patrolImage) {
        PatrolImage = patrolImage;
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

    public int getUploadFlag() {
        return UploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        UploadFlag = uploadFlag;
    }

    public String getBrokenStatusName() {
        if(BrokenStatus == 0){
            BrokenStatusName = "未消除";
        }else{
            BrokenStatusName = "已消除";
        }
        return BrokenStatusName;
    }
}
