package com.uflycn.uoperation.bean;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/20
 * Describe  :
 */
public class SelectTower {

    /**
     * LineName : 123
     * VoltageClass : 123
     * TowerList : [{"sysTowerID":"123","TowerNo":"123","sysDailyPlanPatrolTowerID":"123","IsCheck":"true"}]
     */

    private String LineName;
    private String VoltageClass;
    private List<TowerListBean> TowerList;

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String voltageClass) {
        VoltageClass = voltageClass;
    }

    public List<TowerListBean> getTowerList() {
        return TowerList;
    }

    public void setTowerList(List<TowerListBean> towerList) {
        TowerList = towerList;
    }

    public static class TowerListBean {
        /**
         * sysTowerID : 123
         * TowerNo : 123
         * sysDailyPlanPatrolTowerID : 123
         * IsCheck : true
         */

        private String sysTowerID;
        private String TowerNo;
        private String sysDailyPlanPatrolTowerID;
        private Boolean IsCheck;

        public String getSysTowerID() {
            return sysTowerID;
        }

        public void setSysTowerID(String sysTowerID) {
            this.sysTowerID = sysTowerID;
        }

        public String getTowerNo() {
            return TowerNo;
        }

        public void setTowerNo(String TowerNo) {
            this.TowerNo = TowerNo;
        }

        public String getSysDailyPlanPatrolTowerID() {
            return sysDailyPlanPatrolTowerID;
        }

        public void setSysDailyPlanPatrolTowerID(String sysDailyPlanPatrolTowerID) {
            this.sysDailyPlanPatrolTowerID = sysDailyPlanPatrolTowerID;
        }

        public Boolean getCheck() {
            return IsCheck;
        }

        public void setCheck(Boolean check) {
            IsCheck = check;
        }
    }
}
