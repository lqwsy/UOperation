package com.uflycn.uoperation.bean;

public class GroundingLead {

    /**
     * FileName : s
     * Category : 1
     */

    private String FileName;
    private int Category;

    public GroundingLead() {
    }

    public GroundingLead(String fileName, int category) {
        FileName = fileName;
        Category = category;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public int getCategory() {
        return Category;
    }

    public void setCategory(int Category) {
        this.Category = Category;
    }
}
