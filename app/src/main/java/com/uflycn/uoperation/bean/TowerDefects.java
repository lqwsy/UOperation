package com.uflycn.uoperation.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */
public class TowerDefects {
    /**
     *   "Code": 1,
     "Message": "",
     "TowerInfo": {
     "sysTowerID": 1876,
     "TowerType": null,
     "TowerNo": "51",
     "TowerAssetsCode": "51",
     "TowerAltitude": 21,
     "Latitude": 28.138289,
     "Longitude": 115.160392,
     "HeadAltitude": 7,
     "Altitude": 51,
     "DisplayOrder": 51,
     "sysGridLineID": 37,
     "Deleted": false,
     "CreatedBy": "4345BDC8-8AE8-473F-B43B-5ECE4EAAE7D6",
     "CreatedTime": "2017-08-19T14:05:29.203",
     "UpdatedBy": "3cd1cd06-3d77-4efb-a78d-ad9a9cea3d80",
     "UpdatedTime": "2017-09-14T16:31:42.077"
     },
     "ChannelDefect": [],
     "PropertyDefect": [],
     "TreeDefectPoint": [],
     "PersonalDefect": []
     * */
    List<DefectBean> PersonalDefect;
    List<DefectBean> ChannelDefect;
    List<DefectBean> PropertyDefect;
    List<TreeDefectPointBean> TreeDefectPoint;
    private int Code;
    private String Message;
    private Tower TowerInfo;

    public List<DefectBean> getPersonalDefect() {
        return PersonalDefect;
    }

    public void setPersonalDefect(List<DefectBean> personalDefect) {
        PersonalDefect = personalDefect;
    }

    public List<DefectBean> getChannelDefect() {
        return ChannelDefect;
    }

    public void setChannelDefect(List<DefectBean> channelDefect) {
        ChannelDefect = channelDefect;
    }

    public List<DefectBean> getPropertyDefect() {
        return PropertyDefect;
    }

    public void setPropertyDefect(List<DefectBean> propertyDefect) {
        PropertyDefect = propertyDefect;
    }

    public List<TreeDefectPointBean> getTreeDefectPoint() {
        return TreeDefectPoint;
    }

    public void setTreeDefectPoint(List<TreeDefectPointBean> treeDefectPoint) {
        TreeDefectPoint = treeDefectPoint;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Tower getTowerInfo() {
        return TowerInfo;
    }

    public void setTowerInfo(Tower towerInfo) {
        TowerInfo = towerInfo;
    }
}
