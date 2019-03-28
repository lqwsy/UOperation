package com.uflycn.uoperation.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/10/16.
 */
@Entity(nameInDb = "t_ItemDetails")
public class ItemDetail implements Parcelable {//字典表

    //"ItemDetailsId":"16cd5c63-15aa-4d9b-b44b-2b088cdb0d1d",
    // "ItemsName":"技术员级职称","ParentId":null,"
    // ItemName":"职称等级","ItemCode":"1",
    // "Description":null,"SortCode":1
    // ,"DeleteMark":0
    @Id(autoincrement = false)
    private String ItemDetailsId;
    private String ItemsName;//数据字典名称
    private String ParentId;//父节点 主键
    private String ItemName;//名称
    private String ItemCode;
    private String Description;
    private int SortCode;
    private int DeleteMark;
    private String ItemValue;//电压类型


    @Generated(hash = 1384439465)
    public ItemDetail(String ItemDetailsId, String ItemsName, String ParentId, String ItemName,
            String ItemCode, String Description, int SortCode, int DeleteMark, String ItemValue) {
        this.ItemDetailsId = ItemDetailsId;
        this.ItemsName = ItemsName;
        this.ParentId = ParentId;
        this.ItemName = ItemName;
        this.ItemCode = ItemCode;
        this.Description = Description;
        this.SortCode = SortCode;
        this.DeleteMark = DeleteMark;
        this.ItemValue = ItemValue;
    }

    @Generated(hash = 796116746)
    public ItemDetail() {
    }


    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }
    public String getItemDetailsId() {
        return ItemDetailsId;
    }

    public void setItemDetailsId(String itemDetailsId) {
        ItemDetailsId = itemDetailsId;
    }

    public String getItemsName() {
        return ItemsName;
    }

    public void setItemsName(String itemsName) {
        ItemsName = itemsName;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ItemDetailsId);
        dest.writeString(this.ItemsName);
        dest.writeString(this.ParentId);
        dest.writeString(this.ItemName);
        dest.writeString(this.ItemCode);
        dest.writeString(this.Description);
        dest.writeInt(this.SortCode);
        dest.writeInt(this.DeleteMark);
    }

    public String getItemValue() {
        return this.ItemValue;
    }

    public void setItemValue(String ItemValue) {
        this.ItemValue = ItemValue;
    }

    protected ItemDetail(Parcel in) {
        this.ItemDetailsId = in.readString();
        this.ItemsName = in.readString();
        this.ParentId = in.readString();
        this.ItemName = in.readString();
        this.ItemCode = in.readString();
        this.Description = in.readString();
        this.SortCode = in.readInt();
        this.DeleteMark = in.readInt();
    }

    public static final Parcelable.Creator<ItemDetail> CREATOR = new Parcelable.Creator<ItemDetail>() {
        @Override
        public ItemDetail createFromParcel(Parcel source) {
            return new ItemDetail(source);
        }

        @Override
        public ItemDetail[] newArray(int size) {
            return new ItemDetail[size];
        }
    };
}
