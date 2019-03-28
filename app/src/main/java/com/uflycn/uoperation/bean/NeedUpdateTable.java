package com.uflycn.uoperation.bean;

/**
 * Created by Administrator on 2017/10/17.
 */
public class NeedUpdateTable {
    private int Category;
    private String LastSyncTime;
    private boolean IsUpdated;

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }

    public String getLastSyncTime() {
        return LastSyncTime;
    }

    public void setLastSyncTime(String lastSyncTime) {
        LastSyncTime = lastSyncTime;
    }

    public boolean isUpdated() {
        return IsUpdated;
    }

    public void setUpdated(boolean updated) {
        IsUpdated = updated;
    }
}
