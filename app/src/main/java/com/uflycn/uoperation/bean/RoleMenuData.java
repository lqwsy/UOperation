package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/3/29.
 */
@Entity(nameInDb = "t_rolemenudata")
public class RoleMenuData {

    @Id(autoincrement = false)
    private String RoleMenuDataId;

    private String RoleId; //角色id

    private String MenuId; //菜单id

    private Integer AccessScope; //权限级别

    private String Organizations; //班组

    public String getOrganizations() {
        return this.Organizations;
    }

    public void setOrganizations(String Organizations) {
        this.Organizations = Organizations;
    }

    public int getAccessScope() {
        return this.AccessScope;
    }

    public void setAccessScope(int AccessScope) {
        this.AccessScope = AccessScope;
    }

    public String getMenuId() {
        return this.MenuId;
    }

    public void setMenuId(String MenuId) {
        this.MenuId = MenuId;
    }

    public String getRoleId() {
        return this.RoleId;
    }

    public void setRoleId(String RoleId) {
        this.RoleId = RoleId;
    }

    public String getRoleMenuDataId() {
        return this.RoleMenuDataId;
    }

    public void setRoleMenuDataId(String RoleMenuDataId) {
        this.RoleMenuDataId = RoleMenuDataId;
    }

    public void setAccessScope(Integer AccessScope) {
        this.AccessScope = AccessScope;
    }

    @Generated(hash = 1199891618)
    public RoleMenuData(String RoleMenuDataId, String RoleId, String MenuId,
            Integer AccessScope, String Organizations) {
        this.RoleMenuDataId = RoleMenuDataId;
        this.RoleId = RoleId;
        this.MenuId = MenuId;
        this.AccessScope = AccessScope;
        this.Organizations = Organizations;
    }

    @Generated(hash = 1581339400)
    public RoleMenuData() {
    }
}

