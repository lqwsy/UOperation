package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "t_assist_record")
public class AssistRecord {


    /**
     * sysTowerID : 1
     * IsAgreement : false
     * Topography : 8900988080
     * GroundingLeadServerityLevel : 1
     * GroundingLeadIsInsulate : 1
     * PullLineServerityLevel : 1
     * PullLineIsInsulate : false
     * Assess : 1
     * Remark : 测试
     * fileList : {"TowerHead":[{"FileName":"s"},{"FileName":"s"}],"TowerBrand":[{"FileName":"s"},{"FileName":"s"}],"GroundingLead":[{"FileName":"s","Category":1},{"FileName":"s","Category":2}],"PullLine":[{"FileName":"s","Category":1},{"FileName":"s","Category":2}]}
     */
    @Id
    private Long Id;
    private int sysTowerID;
    private boolean IsAgreement;
    private String Topography;
    private int GroundingLeadServerityLevel;
    private boolean GroundingLeadIsInsulate;
    private int PullLineServerityLevel;
    private boolean PullLineIsInsulate;
    private int Assess;
    private String Remark;
    @Transient
    private FileList fileList;
    private String fileListJson;
    private boolean isUpload;

    public String getFileListJson() {
        return this.fileListJson;
    }

    public void setFileListJson(String fileListJson) {
        this.fileListJson = fileListJson;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public int getAssess() {
        return this.Assess;
    }

    public void setAssess(int Assess) {
        this.Assess = Assess;
    }

    public boolean getPullLineIsInsulate() {
        return this.PullLineIsInsulate;
    }

    public void setPullLineIsInsulate(boolean PullLineIsInsulate) {
        this.PullLineIsInsulate = PullLineIsInsulate;
    }

    public int getPullLineServerityLevel() {
        return this.PullLineServerityLevel;
    }

    public void setPullLineServerityLevel(int PullLineServerityLevel) {
        this.PullLineServerityLevel = PullLineServerityLevel;
    }

    public boolean getGroundingLeadIsInsulate() {
        return this.GroundingLeadIsInsulate;
    }

    public void setGroundingLeadIsInsulate(boolean GroundingLeadIsInsulate) {
        this.GroundingLeadIsInsulate = GroundingLeadIsInsulate;
    }

    public int getGroundingLeadServerityLevel() {
        return this.GroundingLeadServerityLevel;
    }

    public void setGroundingLeadServerityLevel(int GroundingLeadServerityLevel) {
        this.GroundingLeadServerityLevel = GroundingLeadServerityLevel;
    }

    public String getTopography() {
        return this.Topography;
    }

    public void setTopography(String Topography) {
        this.Topography = Topography;
    }

    public boolean getIsAgreement() {
        return this.IsAgreement;
    }

    public void setIsAgreement(boolean IsAgreement) {
        this.IsAgreement = IsAgreement;
    }

    public int getSysTowerID() {
        return this.sysTowerID;
    }

    public void setSysTowerID(int sysTowerID) {
        this.sysTowerID = sysTowerID;
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public FileList getFileList() {
        return fileList;
    }

    public void setFileList(FileList fileList) {
        this.fileList = fileList;
    }

    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    @Generated(hash = 1618283859)
    public AssistRecord(Long Id, int sysTowerID, boolean IsAgreement, String Topography, int GroundingLeadServerityLevel, boolean GroundingLeadIsInsulate, int PullLineServerityLevel, boolean PullLineIsInsulate, int Assess, String Remark, String fileListJson,
            boolean isUpload) {
        this.Id = Id;
        this.sysTowerID = sysTowerID;
        this.IsAgreement = IsAgreement;
        this.Topography = Topography;
        this.GroundingLeadServerityLevel = GroundingLeadServerityLevel;
        this.GroundingLeadIsInsulate = GroundingLeadIsInsulate;
        this.PullLineServerityLevel = PullLineServerityLevel;
        this.PullLineIsInsulate = PullLineIsInsulate;
        this.Assess = Assess;
        this.Remark = Remark;
        this.fileListJson = fileListJson;
        this.isUpload = isUpload;
    }

    @Generated(hash = 795948696)
    public AssistRecord() {
    }


}
