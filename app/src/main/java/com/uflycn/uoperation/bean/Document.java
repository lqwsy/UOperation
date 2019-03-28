package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by UF_PC on 2017/11/8.
 */
@Entity(nameInDb = "operation_TransmissionDocument")
public class Document {
    @Id(autoincrement = false)
    private Long sysTechnicalFileId;
    private String FileName;
    private String Introduction;
    private String FileSize;
    private String Extensions;
    private String CreatedTime;
    private String CreatedBy;
    private int sysFileInfoId;
    private String FilePath;
    private String PlanDailyPlanSectionIDs;


    @Generated(hash = 1973824372)
    public Document(Long sysTechnicalFileId, String FileName, String Introduction,
            String FileSize, String Extensions, String CreatedTime,
            String CreatedBy, int sysFileInfoId, String FilePath,
            String PlanDailyPlanSectionIDs) {
        this.sysTechnicalFileId = sysTechnicalFileId;
        this.FileName = FileName;
        this.Introduction = Introduction;
        this.FileSize = FileSize;
        this.Extensions = Extensions;
        this.CreatedTime = CreatedTime;
        this.CreatedBy = CreatedBy;
        this.sysFileInfoId = sysFileInfoId;
        this.FilePath = FilePath;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }

    @Generated(hash = 91234483)
    public Document() {
    }


    public Long getSysTechnicalFileId() {
        return sysTechnicalFileId;
    }

    public void setSysTechnicalFileId(Long sysTechnicalFileId) {
        this.sysTechnicalFileId = sysTechnicalFileId;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public void setIntroduction(String introduction) {
        Introduction = introduction;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getExtensions() {
        return Extensions;
    }

    public void setExtensions(String extensions) {
        Extensions = extensions;
    }

    public String getCreatedTime() {
        if (CreatedTime.lastIndexOf(":") != -1){
            return CreatedTime.substring(0,CreatedTime.lastIndexOf(":"));
        }else{
            return CreatedTime;
        }
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public int getSysFileInfoId() {
        return sysFileInfoId;
    }

    public void setSysFileInfoId(int sysFileInfoId) {
        this.sysFileInfoId = sysFileInfoId;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }
}
