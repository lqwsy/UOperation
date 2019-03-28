package com.uflycn.uoperation.bean;

public class TowerBrand {
    private String FileName;

    public TowerBrand() {
    }

    public TowerBrand(String fileName) {
        FileName = fileName;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }
}
