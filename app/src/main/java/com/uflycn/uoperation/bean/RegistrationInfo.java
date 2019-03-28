package com.uflycn.uoperation.bean;

/**
 * Created by Ryan on 2017/4/1.
 */
public class RegistrationInfo {
    private long id ;

    private String name;

    private String company;

    private String deviceNo;

    private String productName;

    private String applicant;

    private String tel;

    private String email;


    private String regSerialNo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegSerialNo() {
        return regSerialNo;
    }

    public void setRegSerialNo(String regSerialNo) {
        this.regSerialNo = regSerialNo;
    }
}
