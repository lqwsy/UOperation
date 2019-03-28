package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "t_user")
public class User {

    /**
     * "UserId": "79d84045-6d7e-40ab-bf5a-59a31d014111",
     "Code": "1010106066",
     "Account": "zhangxw",
     "Password": "123456",
     "Secretkey": "201709221706242332",
     "RealName": "张小武",
     "Spell": "ZXW",
     "Alias": null,
     "DepartmentId": "1f5fa191-d979-463c-a3a5-28c87272bb23",
     "SortCode": 827,
     "DeleteMark": 1
     * */
    @Id(autoincrement = false)
    @Property(nameInDb = "UserId")
    private String UserId;
    private String Code;
    private String RealName;
    private String Spell;
    private String Alias;
    private String DepartmentId;
    private String Account;
    private String Password;
    private String Secretkey;
    private int SortCode;
    private int DeleteMark;

    @Generated(hash = 1000362107)
    public User(String UserId, String Code, String RealName, String Spell,
            String Alias, String DepartmentId, String Account, String Password,
            String Secretkey, int SortCode, int DeleteMark) {
        this.UserId = UserId;
        this.Code = Code;
        this.RealName = RealName;
        this.Spell = Spell;
        this.Alias = Alias;
        this.DepartmentId = DepartmentId;
        this.Account = Account;
        this.Password = Password;
        this.Secretkey = Secretkey;
        this.SortCode = SortCode;
        this.DeleteMark = DeleteMark;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getSpell() {
        return Spell;
    }

    public void setSpell(String spell) {
        Spell = spell;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getDepartmentId() {
        return DepartmentId;
    }

    public void setDepartmentId(String departmentId) {
        DepartmentId = departmentId;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSecretkey() {
        return Secretkey;
    }

    public void setSecretkey(String secretkey) {
        Secretkey = secretkey;
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
}
