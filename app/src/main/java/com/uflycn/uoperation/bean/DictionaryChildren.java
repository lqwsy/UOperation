package com.uflycn.uoperation.bean;

/**
 * Created by Administrator on 2017/9/8.
 */
public class DictionaryChildren {
    /*  "Id": "16cd5c63-15aa-4d9b-b44b-2b088cdb0d1d",
              "ItemCode": "1",
              "ItemName": "技术员级职称",
              "ItemValue": null,
              "SortCode": 1*/
    private String Id;
    private String ItemCode;
    private String ItemName;
    private String ItemValue;
    private String SortCode;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSortCode() {
        return SortCode;
    }

    public void setSortCode(String sortCode) {
        SortCode = sortCode;
    }

    public String getItemValue() {
        return ItemValue;
    }

    public void setItemValue(String itemValue) {
        ItemValue = itemValue;
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
}
