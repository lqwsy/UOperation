package com.uflycn.uoperation.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * 班组
 */
@Entity(nameInDb = "t_Organizition")
public class Organizition {
    @Id(autoincrement = false)
    @Property(nameInDb = "OrganizationId")
    private String OrganizationId;
    private String ParentId;
    private String Code;
    private String ShortName;
    private String FullName;
    private String Description;
    private int Enabled;
    private int SortCode;
    private int DeleteMark;
    private String Manager;
    private String ManagerName;
    private String AssistantManager;


    @Generated(hash = 1544611274)
    public Organizition(String OrganizationId, String ParentId, String Code,
            String ShortName, String FullName, String Description, int Enabled,
            int SortCode, int DeleteMark, String Manager, String ManagerName,
            String AssistantManager) {
        this.OrganizationId = OrganizationId;
        this.ParentId = ParentId;
        this.Code = Code;
        this.ShortName = ShortName;
        this.FullName = FullName;
        this.Description = Description;
        this.Enabled = Enabled;
        this.SortCode = SortCode;
        this.DeleteMark = DeleteMark;
        this.Manager = Manager;
        this.ManagerName = ManagerName;
        this.AssistantManager = AssistantManager;
    }

    @Generated(hash = 692689418)
    public Organizition() {
    }


    public String getOrganizationId() {
        return OrganizationId;
    }

    public void setOrganizationId(String organizationId) {
        OrganizationId = organizationId;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getEnabled() {
        return Enabled;
    }

    public void setEnabled(int enabled) {
        Enabled = enabled;
    }

    public int getSortCode() {
        return SortCode;
    }

    public void setSortCode(int sortCode) {
        SortCode = sortCode;
    }

    public int getDeleteMark() {
        return DeleteMark;
    }

    public void setDeleteMark(int deleteMark) {
        DeleteMark = deleteMark;
    }

    public String getManager() {
        return Manager;
    }

    public void setManager(String manager) {
        Manager = manager;
    }

    public String getManagerName() {
        return ManagerName;
    }

    public void setManagerName(String managerName) {
        ManagerName = managerName;
    }

    public String getAssistantManager() {
        return AssistantManager;
    }

    public void setAssistantManager(String assistantManager) {
        AssistantManager = assistantManager;
    }
}
