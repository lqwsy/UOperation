package com.uflycn.uoperation.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TowerList {

    /**
     * sysTowerID : 10846
     * TypeOfWork : [{"TypeOfWork":"cb0506c0-cc24-4bad-b489-20584d614473","TypeOfWorkString":"人工巡视"}]
     */

    private int sysTowerID;
    @SerializedName("TypeOfWorkList")
    private List<TypeOfWork> TypeOfWork;

    public int getSysTowerID() {
        return sysTowerID;
    }

    public void setSysTowerID(int sysTowerID) {
        this.sysTowerID = sysTowerID;
    }

    public List<TypeOfWork> getTypeOfWork() {
        return TypeOfWork;
    }

    public void setTypeOfWork(List<TypeOfWork> TypeOfWork) {
        this.TypeOfWork = TypeOfWork;
    }

}
