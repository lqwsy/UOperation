package com.uflycn.uoperation.bean;

public class TowerHead {
    private String FileName;

    public TowerHead() {
    }

    public TowerHead(String fileName) {
        FileName = fileName;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }
}
