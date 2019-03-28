package com.uflycn.uoperation.bean;

/**
 * Created by Administrator on 2017/10/11.
 */
public class ProjectRecord {
    /**
     * "rowNum": 1,
     "sysProjectInspectionId": 2,
     "sysProjectId": 1006,
     "VoltageClass": "220kV",
     "GridLineName": "大仙Ⅰ线",
     "StartTowerId": "6",
     "EndTowerId": "82",
     "ProjectName": "大仙测试15",
     "State": 0,
     "StateString": "未处理",
     "Remark": "1245885",
     "CheckedImage": "Project/20171011/d0231f7558f94c7a8b2a2af55c46aaca.jpg",
     "CheckerId": "admin",
     "CheckedTime": "2017-10-11T17:48:09.967",
     "CheckedTimeString": "2017-10-11 17:48"
     * */
    private String CheckedTimeString;
    private String StateString;
    private String Remark;

    public String getCheckedTimeString() {
        return CheckedTimeString;
    }

    public void setCheckedTimeString(String checkedTimeString) {
        CheckedTimeString = checkedTimeString;
    }

    public String getStateString() {
        return StateString;
    }

    public void setStateString(String stateString) {
        StateString = stateString;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
