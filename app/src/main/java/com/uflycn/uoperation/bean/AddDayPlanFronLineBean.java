package com.uflycn.uoperation.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/27
 * Describe  :
 */
public class AddDayPlanFronLineBean extends LineBean {
    private String tourNums;//巡视杆塔号 ，分隔
    private ItemDetail typeOfWork;//工作类型
    private List<JobContent> jobContents;//工作内容，可以是多个
    private List<Tower> towers;//线路中要巡视塔的集合

    public String getTourNums() {
        return tourNums;
    }

    public void setTourNums(String tourNums) {
        this.tourNums = tourNums;
    }

    public ItemDetail getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(ItemDetail typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public List<JobContent> getJobContents() {
        return jobContents;
    }

    public void setJobContents(List<JobContent> jobContents) {
        this.jobContents = jobContents;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public void setTowers(List<Tower> towers) {
        this.towers = towers;
    }

    public static Creator<AddDayPlanFronLineBean> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.tourNums);
        dest.writeParcelable(this.typeOfWork, flags);
        dest.writeTypedList(this.jobContents);
        dest.writeTypedList(this.towers);
    }

    public AddDayPlanFronLineBean() {
    }

    protected AddDayPlanFronLineBean(Parcel in) {
        super(in);
        this.tourNums = in.readString();
        this.typeOfWork = in.readParcelable(ItemDetail.class.getClassLoader());
        this.jobContents = in.createTypedArrayList(JobContent.CREATOR);
        this.towers = in.createTypedArrayList(Tower.CREATOR);
    }

    public static final Creator<AddDayPlanFronLineBean> CREATOR = new Creator<AddDayPlanFronLineBean>() {
        @Override
        public AddDayPlanFronLineBean createFromParcel(Parcel source) {
            return new AddDayPlanFronLineBean(source);
        }

        @Override
        public AddDayPlanFronLineBean[] newArray(int size) {
            return new AddDayPlanFronLineBean[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        else {
            if (obj instanceof AddDayPlanFronLineBean) {
                AddDayPlanFronLineBean addDayPlanFronLineBean = (AddDayPlanFronLineBean) obj;
                if (addDayPlanFronLineBean.getMaintainClass()==this.getMaintainClass()) {
                    return true;
                }
            }
        }
        return false;
    }
}
