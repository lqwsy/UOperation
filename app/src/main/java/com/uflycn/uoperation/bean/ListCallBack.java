package com.uflycn.uoperation.bean;

public class ListCallBack<T> {
    private int Index;
    private String Error;

    private int PlatformId;
    private T RecordData;

    public int getPlatformId() {
        return PlatformId;
    }

    public void setPlatformId(int platformId) {
        PlatformId = platformId;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public T getRecordData() {
        return RecordData;
    }

    public void setRecordData(T recordData) {
        RecordData = recordData;
    }
}
