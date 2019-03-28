package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/3/29.
 */
@Entity(nameInDb = "t_userrole")
public class UserRole {

    @Id(autoincrement = false)
    private String UserRoleId;

    private String UserId;

    private String RoleId;

    private String CreateDate;

    private String CreateUserId;

    private String CreateUserName;

    public String getCreateUserName() {
        return this.CreateUserName;
    }

    public void setCreateUserName(String CreateUserName) {
        this.CreateUserName = CreateUserName;
    }

    public String getCreateUserId() {
        return this.CreateUserId;
    }

    public void setCreateUserId(String CreateUserId) {
        this.CreateUserId = CreateUserId;
    }

    public String getCreateDate() {
        return this.CreateDate;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    public String getRoleId() {
        return this.RoleId;
    }

    public void setRoleId(String RoleId) {
        this.RoleId = RoleId;
    }

    public String getUserId() {
        return this.UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserRoleId() {
        return this.UserRoleId;
    }

    public void setUserRoleId(String UserRoleId) {
        this.UserRoleId = UserRoleId;
    }

    @Generated(hash = 131274637)
    public UserRole(String UserRoleId, String UserId, String RoleId,
            String CreateDate, String CreateUserId, String CreateUserName) {
        this.UserRoleId = UserRoleId;
        this.UserId = UserId;
        this.RoleId = RoleId;
        this.CreateDate = CreateDate;
        this.CreateUserId = CreateUserId;
        this.CreateUserName = CreateUserName;
    }

    @Generated(hash = 552541888)
    public UserRole() {
    }



}
