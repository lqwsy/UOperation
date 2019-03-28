package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/3/29.
 */
@Entity(nameInDb = "t_role")
public class Role {

    @Id(autoincrement = false)
    private String RoleId; //角色主键

//    1 系统管理员
//    2 班员
//    3 仓管
//    4 管理人员
//    5 分管领导
    private String Code;//角色编号

    private String FullName;//角色名称

    private String Category;//角色分类

    private String Description;//描述

    private int Enabled; //是否有效：1-有效，0-无效

    private int DeleteMark; //删除标记:1-正常，0-删除'

    public int getDeleteMark() {
        return this.DeleteMark;
    }

    public void setDeleteMark(int DeleteMark) {
        this.DeleteMark = DeleteMark;
    }

    public int getEnabled() {
        return this.Enabled;
    }

    public void setEnabled(int Enabled) {
        this.Enabled = Enabled;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getCategory() {
        return this.Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getFullName() {
        return this.FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public String getCode() {
        return this.Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getRoleId() {
        return this.RoleId;
    }

    public void setRoleId(String RoleId) {
        this.RoleId = RoleId;
    }

    @Generated(hash = 734794332)
    public Role(String RoleId, String Code, String FullName, String Category,
            String Description, int Enabled, int DeleteMark) {
        this.RoleId = RoleId;
        this.Code = Code;
        this.FullName = FullName;
        this.Category = Category;
        this.Description = Description;
        this.Enabled = Enabled;
        this.DeleteMark = DeleteMark;
    }

    @Generated(hash = 844947497)
    public Role() {
    }

}
