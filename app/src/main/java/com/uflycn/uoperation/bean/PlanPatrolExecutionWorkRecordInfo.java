package com.uflycn.uoperation.bean;

import java.util.ArrayList;
import java.util.List;

public class PlanPatrolExecutionWorkRecordInfo {


    /**
     * sysPatrolExecutionID : aa85ce2f-d155-4fe1-8000-dcd519563ee2
     * TaskTypeString : 巡视
     * sysGridLineID : 2319
     * LineName : h测试线路
     * TypeOfWork : cb0506c0-cc24-4bad-b489-20584d614473
     * TypeOfWorkString : 人工巡视
     * TowerList : [{"sysTowerID":16694,"TowerNo":"2","DisplayOrder":2},{"sysTowerID":16695,"TowerNo":"3","DisplayOrder":3},{"sysTowerID":16696,"TowerNo":"4","DisplayOrder":4},{"sysTowerID":16697,"TowerNo":"5","DisplayOrder":5}]
     */

    private String sysPatrolExecutionID;
    private String TaskTypeString;
    private int sysGridLineID;
    private String LineName;
    private String TypeOfWork;
    private String TypeOfWorkString;
    private List<Tower> TowerList;

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public String getTaskTypeString() {
        return TaskTypeString;
    }

    public void setTaskTypeString(String TaskTypeString) {
        this.TaskTypeString = TaskTypeString;
    }

    public int getSysGridLineID() {
        return sysGridLineID;
    }

    public void setSysGridLineID(int sysGridLineID) {
        this.sysGridLineID = sysGridLineID;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String LineName) {
        this.LineName = LineName;
    }

    public String getTypeOfWork() {
        return TypeOfWork;
    }

    public void setTypeOfWork(String TypeOfWork) {
        this.TypeOfWork = TypeOfWork;
    }

    public String getTypeOfWorkString() {
        return TypeOfWorkString;
    }

    public void setTypeOfWorkString(String TypeOfWorkString) {
        this.TypeOfWorkString = TypeOfWorkString;
    }

    public List<Tower> getTowerList() {
        return TowerList;
    }

    public void setTowerList(List<Tower> TowerList) {
        this.TowerList = TowerList;
    }

    /**
     * Created by Administrator on 2019/3/25.
     * <p>
     * Description:树障列表
     */
    public static class TreeBarrier {


        /**
         * sysTreeDefectPointID : 1072
         * sysGridLineID : 371
         * LineName : 陵惠线
         * VoltageClass : 500kV
         * TowerA_Id : 108661
         * TowerA_Name : 20
         * TowerB_Name : 21
         * TowerRegion : 20 - 21
         * DistanceFromTower : 5
         * DistanceFromLine : 12
         * DistanceFromLineH : 14
         * DistanceFromLineV : 15
         * Latitude : 37.40172
         * Longitude : 116.666542
         * LatitudeString : 37.40172
         * LongitudeString : 116.666542
         * DefectStateString : 待处理
         * FoundedTime : 2019-03-22 11:09:02
         * Type : 1
         * TreeDefectType : 树障
         * DefectLevel : 1
         * TreeDefectPointType : 一般
         * OriginalString : 人巡
         * TreeSeed : 梧桐树
         * TreeSeedNumber : 5
         * Remark : 500kV陵惠线20-21杆段内，距小号侧距离5米，有梧桐树数量为5棵，净空距离12.00米
         * ImageCategory : TreeDefect
         */

        private int sysTreeDefectPointID;
        private int sysGridLineID;
        private String LineName;
        private String VoltageClass;
        private int TowerA_Id;
        private String TowerA_Name;
        private String TowerB_Name;
        private String TowerRegion;
        private int DistanceFromTower;
        private int DistanceFromLine;
        private int DistanceFromLineH;
        private int DistanceFromLineV;
        private double Latitude;
        private double Longitude;
        private double LatitudeString;
        private double LongitudeString;
        private String DefectStateString;
        private String FoundedTime;
        private int Type;
        private String TreeDefectType;
        private int DefectLevel;
        private String TreeDefectPointType;
        private String OriginalString;
        private String TreeSeed;
        private int TreeSeedNumber;
        private String Remark;
        private String ImageCategory;

        public int getSysTreeDefectPointID() {
            return sysTreeDefectPointID;
        }

        public void setSysTreeDefectPointID(int sysTreeDefectPointID) {
            this.sysTreeDefectPointID = sysTreeDefectPointID;
        }

        public int getSysGridLineID() {
            return sysGridLineID;
        }

        public void setSysGridLineID(int sysGridLineID) {
            this.sysGridLineID = sysGridLineID;
        }

        public String getLineName() {
            return LineName;
        }

        public void setLineName(String LineName) {
            this.LineName = LineName;
        }

        public String getVoltageClass() {
            return VoltageClass;
        }

        public void setVoltageClass(String VoltageClass) {
            this.VoltageClass = VoltageClass;
        }

        public int getTowerA_Id() {
            return TowerA_Id;
        }

        public void setTowerA_Id(int TowerA_Id) {
            this.TowerA_Id = TowerA_Id;
        }

        public String getTowerA_Name() {
            return TowerA_Name;
        }

        public void setTowerA_Name(String TowerA_Name) {
            this.TowerA_Name = TowerA_Name;
        }

        public String getTowerB_Name() {
            return TowerB_Name;
        }

        public void setTowerB_Name(String TowerB_Name) {
            this.TowerB_Name = TowerB_Name;
        }

        public String getTowerRegion() {
            return TowerRegion;
        }

        public void setTowerRegion(String TowerRegion) {
            this.TowerRegion = TowerRegion;
        }

        public int getDistanceFromTower() {
            return DistanceFromTower;
        }

        public void setDistanceFromTower(int DistanceFromTower) {
            this.DistanceFromTower = DistanceFromTower;
        }

        public int getDistanceFromLine() {
            return DistanceFromLine;
        }

        public void setDistanceFromLine(int DistanceFromLine) {
            this.DistanceFromLine = DistanceFromLine;
        }

        public int getDistanceFromLineH() {
            return DistanceFromLineH;
        }

        public void setDistanceFromLineH(int DistanceFromLineH) {
            this.DistanceFromLineH = DistanceFromLineH;
        }

        public int getDistanceFromLineV() {
            return DistanceFromLineV;
        }

        public void setDistanceFromLineV(int DistanceFromLineV) {
            this.DistanceFromLineV = DistanceFromLineV;
        }

        public double getLatitude() {
            return Latitude;
        }

        public void setLatitude(double Latitude) {
            this.Latitude = Latitude;
        }

        public double getLongitude() {
            return Longitude;
        }

        public void setLongitude(double Longitude) {
            this.Longitude = Longitude;
        }

        public double getLatitudeString() {
            return LatitudeString;
        }

        public void setLatitudeString(double LatitudeString) {
            this.LatitudeString = LatitudeString;
        }

        public double getLongitudeString() {
            return LongitudeString;
        }

        public void setLongitudeString(double LongitudeString) {
            this.LongitudeString = LongitudeString;
        }

        public String getDefectStateString() {
            return DefectStateString;
        }

        public void setDefectStateString(String DefectStateString) {
            this.DefectStateString = DefectStateString;
        }

        public String getFoundedTime() {
            return FoundedTime;
        }

        public void setFoundedTime(String FoundedTime) {
            this.FoundedTime = FoundedTime;
        }

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public String getTreeDefectType() {
            return TreeDefectType;
        }

        public void setTreeDefectType(String TreeDefectType) {
            this.TreeDefectType = TreeDefectType;
        }

        public int getDefectLevel() {
            return DefectLevel;
        }

        public void setDefectLevel(int DefectLevel) {
            this.DefectLevel = DefectLevel;
        }

        public String getTreeDefectPointType() {
            return TreeDefectPointType;
        }

        public void setTreeDefectPointType(String TreeDefectPointType) {
            this.TreeDefectPointType = TreeDefectPointType;
        }

        public String getOriginalString() {
            return OriginalString;
        }

        public void setOriginalString(String OriginalString) {
            this.OriginalString = OriginalString;
        }

        public String getTreeSeed() {
            return TreeSeed;
        }

        public void setTreeSeed(String TreeSeed) {
            this.TreeSeed = TreeSeed;
        }

        public int getTreeSeedNumber() {
            return TreeSeedNumber;
        }

        public void setTreeSeedNumber(int TreeSeedNumber) {
            this.TreeSeedNumber = TreeSeedNumber;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getImageCategory() {
            return ImageCategory;
        }

        public void setImageCategory(String ImageCategory) {
            this.ImageCategory = ImageCategory;
        }
    }

    /**
     * 树障请求
     */
    public static class TestBean {

        private String lineName;
        private String descript;

        public String getLineName() {
            return lineName;
        }

        public void setLineName(String lineName) {
            this.lineName = lineName;
        }

        public String getDescript() {
            return descript;
        }

        public void setDescript(String descript) {
            this.descript = descript;
        }

        //测试数据
        public static List<TestBean> initTestData(){
            List<TestBean> testBeans = new ArrayList<>();
            for(int i=0;i<3;i++){
                TestBean testBean = new TestBean();
                testBean.setLineName("线路"+i);
                testBean.setDescript("测试描述"+i);
                testBeans.add(testBean);
            }
            return testBeans;
        }

    }
}
