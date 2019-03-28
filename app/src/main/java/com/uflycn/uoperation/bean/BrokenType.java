package com.uflycn.uoperation.bean;

/**
 * Created by Administrator on 2017/9/8.
 */
public class BrokenType {
    /**
     * "ItemDetailsId": "c9d194f3-b7e7-4051-be85-8cd3b16e7f18",
     "ItemsId": "f6120286-bfc5-4e55-8712-a514d9822bff",
     "ParentId": null,
     "ItemName": "外破二",
     "ItemCode": "02",
     "ItemValue": " ",
     "Description": " ",
     "AllowEdit": null,
     "AllowDelete": null,
     "Enabled": 1,
     "SortCode": 146,
     "DeleteMark": 0,
     "CreateDate": "2017-09-05T16:38:04.643",
     "CreateUserId": "3cd1cd06-3d77-4efb-a78d-ad9a9cea3d80",
     "CreateUserName": "admin",
     "ModifyDate": null,
     "ModifyUserId": null,
     "ModifyUserName": null
     */
    private String ItemDetailsId;
    private String ItemsId;
    private String ItemName;
    private String ItemCode;
    private String ItemValue;
    private String Description;
    private int DeleteMark;

    public String getItemDetailsId() {
        return ItemDetailsId;
    }

    public void setItemDetailsId(String itemDetailsId) {
        ItemDetailsId = itemDetailsId;
    }

    public String getItemsId() {
        return ItemsId;
    }

    public void setItemsId(String itemsId) {
        ItemsId = itemsId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItemValue() {
        return ItemValue;
    }

    public void setItemValue(String itemValue) {
        ItemValue = itemValue;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getDeleteMark() {
        return DeleteMark;
    }

    public void setDeleteMark(int deleteMark) {
        DeleteMark = deleteMark;
    }
}
