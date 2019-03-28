package com.uflycn.uoperation.bean;

import com.google.gson.annotations.SerializedName;
import com.uflycn.uoperation.greendao.DaoSession;
import com.uflycn.uoperation.greendao.InfraredTemperatureDao;
import com.uflycn.uoperation.greendao.OptTensilePointTemperatureDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * 红外测温
 */
@Entity(nameInDb = "operation_InfraredTemperature")
public class InfraredTemperature {
    @Id(autoincrement = true)
    private Long Id;

    private String sysUserId;

    private int sysTowerId;
    private Integer NearTowerId;
    private double Wireway; //导线温度
    private double EnvTemperature; //环境温度
    private boolean IsThreeCross;
    private Integer JointType; // 接头类型，0-耐张接头，1-中间接头
    //新加的
    private Integer WirewayType; //0代表0-单导线，1-双导线，2-四分裂导线   四分裂只有宁波才有

    private String CreateDate;

    private String LoopType;

    private int Qualified;

    private int UploadFlag;
    private String PlanDailyPlanSectionIDs;

    private String sysPatrolExecutionID;

    //多个输入框中的值
    @SerializedName("TensilePointTemperatureList")
    @ToMany(referencedJoinProperty = "InfraredTemperatureId")
    private List<OptTensilePointTemperature> OptTensilePointTemperatureList;

    @SerializedName("OptTensilePointTemperatureList")
    @Transient
    private List<OptTensilePointTemperature> TensilePointTemperatureList;

    public void setTensilePointTemperatureList(List<OptTensilePointTemperature> tensilePointTemperatureList) {
        TensilePointTemperatureList = tensilePointTemperatureList;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 529077541)
    public synchronized void resetOptTensilePointTemperatureList() {
        OptTensilePointTemperatureList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1815257998)
    public List<OptTensilePointTemperature> getOptTensilePointTemperatureList() {
        if (OptTensilePointTemperatureList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OptTensilePointTemperatureDao targetDao = daoSession.getOptTensilePointTemperatureDao();
            List<OptTensilePointTemperature> OptTensilePointTemperatureListNew = targetDao._queryInfraredTemperature_OptTensilePointTemperatureList(Id);
            synchronized (this) {
                if (OptTensilePointTemperatureList == null) {
                    OptTensilePointTemperatureList = OptTensilePointTemperatureListNew;
                }
            }
        }
        return OptTensilePointTemperatureList;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 149358626)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInfraredTemperatureDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1178843439)
    private transient InfraredTemperatureDao myDao;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public int getUploadFlag() {
        return this.UploadFlag;
    }

    public void setUploadFlag(int UploadFlag) {
        this.UploadFlag = UploadFlag;
    }

    public int getQualified() {
        return this.Qualified;
    }

    public void setQualified(int Qualified) {
        this.Qualified = Qualified;
    }

    public String getLoopType() {
        return this.LoopType;
    }

    public void setLoopType(String LoopType) {
        this.LoopType = LoopType;
    }

    public String getCreateDate() {
        return this.CreateDate;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    public Integer getWirewayType() {
        return this.WirewayType;
    }

    public void setWirewayType(Integer WirewayType) {
        this.WirewayType = WirewayType;
    }

    public Integer getJointType() {
        return this.JointType;
    }

    public void setJointType(Integer JointType) {
        this.JointType = JointType;
    }

    public boolean getIsThreeCross() {
        return this.IsThreeCross;
    }

    public void setIsThreeCross(boolean IsThreeCross) {
        this.IsThreeCross = IsThreeCross;
    }

    public double getEnvTemperature() {
        return this.EnvTemperature;
    }

    public void setEnvTemperature(double EnvTemperature) {
        this.EnvTemperature = EnvTemperature;
    }

    public double getWireway() {
        return this.Wireway;
    }

    public void setWireway(double Wireway) {
        this.Wireway = Wireway;
    }

    public Integer getNearTowerId() {
        return this.NearTowerId;
    }

    public void setNearTowerId(Integer NearTowerId) {
        this.NearTowerId = NearTowerId;
    }

    public int getSysTowerId() {
        return this.sysTowerId;
    }

    public void setSysTowerId(int sysTowerId) {
        this.sysTowerId = sysTowerId;
    }

    public String getSysUserId() {
        return this.sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 117930112)
    public InfraredTemperature(Long Id, String sysUserId, int sysTowerId, Integer NearTowerId, double Wireway, double EnvTemperature, boolean IsThreeCross,
            Integer JointType, Integer WirewayType, String CreateDate, String LoopType, int Qualified, int UploadFlag, String PlanDailyPlanSectionIDs,
            String sysPatrolExecutionID) {
        this.Id = Id;
        this.sysUserId = sysUserId;
        this.sysTowerId = sysTowerId;
        this.NearTowerId = NearTowerId;
        this.Wireway = Wireway;
        this.EnvTemperature = EnvTemperature;
        this.IsThreeCross = IsThreeCross;
        this.JointType = JointType;
        this.WirewayType = WirewayType;
        this.CreateDate = CreateDate;
        this.LoopType = LoopType;
        this.Qualified = Qualified;
        this.UploadFlag = UploadFlag;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 1764025754)
    public InfraredTemperature() {
    }
}
