package com.uflycn.uoperation.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.uflycn.uoperation.bean.LineCrossEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "operation_LineCross".
*/
public class LineCrossEntityDao extends AbstractDao<LineCrossEntity, Long> {

    public static final String TABLENAME = "operation_LineCross";

    /**
     * Properties of entity LineCrossEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PlatformId = new Property(1, int.class, "PlatformId", false, "PLATFORM_ID");
        public final static Property StartTowerId = new Property(2, int.class, "StartTowerId", false, "START_TOWER_ID");
        public final static Property EndTowerId = new Property(3, int.class, "EndTowerId", false, "END_TOWER_ID");
        public final static Property CrossType = new Property(4, int.class, "CrossType", false, "CROSS_TYPE");
        public final static Property DtoSmartTower = new Property(5, double.class, "DtoSmartTower", false, "DTO_SMART_TOWER");
        public final static Property Clearance = new Property(6, double.class, "Clearance", false, "CLEARANCE");
        public final static Property CrossAngle = new Property(7, double.class, "CrossAngle", false, "CROSS_ANGLE");
        public final static Property CrossImage = new Property(8, String.class, "CrossImage", false, "CROSS_IMAGE");
        public final static Property CrossLatitude = new Property(9, String.class, "CrossLatitude", false, "CROSS_LATITUDE");
        public final static Property CrossLongitude = new Property(10, String.class, "CrossLongitude", false, "CROSS_LONGITUDE");
        public final static Property Remark = new Property(11, String.class, "Remark", false, "REMARK");
        public final static Property CreatedTime = new Property(12, String.class, "CreatedTime", false, "CREATED_TIME");
        public final static Property UpdatedTime = new Property(13, String.class, "UpdatedTime", false, "UPDATED_TIME");
        public final static Property UploadFlag = new Property(14, int.class, "UploadFlag", false, "UPLOAD_FLAG");
        public final static Property LineName = new Property(15, String.class, "LineName", false, "LINE_NAME");
        public final static Property VoltageClass = new Property(16, String.class, "VoltageClass", false, "VOLTAGE_CLASS");
        public final static Property StartTower = new Property(17, String.class, "StartTower", false, "START_TOWER");
        public final static Property EndTower = new Property(18, String.class, "EndTower", false, "END_TOWER");
        public final static Property CrossStatus = new Property(19, String.class, "CrossStatus", false, "CROSS_STATUS");
        public final static Property CrossTypeName = new Property(20, String.class, "CrossTypeName", false, "CROSS_TYPE_NAME");
        public final static Property CreatedBy = new Property(21, String.class, "CreatedBy", false, "CREATED_BY");
        public final static Property PlanDailyPlanSectionIDs = new Property(22, String.class, "PlanDailyPlanSectionIDs", false, "PLAN_DAILY_PLAN_SECTION_IDS");
        public final static Property SysPatrolExecutionID = new Property(23, String.class, "sysPatrolExecutionID", false, "SYS_PATROL_EXECUTION_ID");
    }


    public LineCrossEntityDao(DaoConfig config) {
        super(config);
    }
    
    public LineCrossEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"operation_LineCross\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"PLATFORM_ID\" INTEGER NOT NULL ," + // 1: PlatformId
                "\"START_TOWER_ID\" INTEGER NOT NULL ," + // 2: StartTowerId
                "\"END_TOWER_ID\" INTEGER NOT NULL ," + // 3: EndTowerId
                "\"CROSS_TYPE\" INTEGER NOT NULL ," + // 4: CrossType
                "\"DTO_SMART_TOWER\" REAL NOT NULL ," + // 5: DtoSmartTower
                "\"CLEARANCE\" REAL NOT NULL ," + // 6: Clearance
                "\"CROSS_ANGLE\" REAL NOT NULL ," + // 7: CrossAngle
                "\"CROSS_IMAGE\" TEXT," + // 8: CrossImage
                "\"CROSS_LATITUDE\" TEXT," + // 9: CrossLatitude
                "\"CROSS_LONGITUDE\" TEXT," + // 10: CrossLongitude
                "\"REMARK\" TEXT," + // 11: Remark
                "\"CREATED_TIME\" TEXT," + // 12: CreatedTime
                "\"UPDATED_TIME\" TEXT," + // 13: UpdatedTime
                "\"UPLOAD_FLAG\" INTEGER NOT NULL ," + // 14: UploadFlag
                "\"LINE_NAME\" TEXT," + // 15: LineName
                "\"VOLTAGE_CLASS\" TEXT," + // 16: VoltageClass
                "\"START_TOWER\" TEXT," + // 17: StartTower
                "\"END_TOWER\" TEXT," + // 18: EndTower
                "\"CROSS_STATUS\" TEXT," + // 19: CrossStatus
                "\"CROSS_TYPE_NAME\" TEXT," + // 20: CrossTypeName
                "\"CREATED_BY\" TEXT," + // 21: CreatedBy
                "\"PLAN_DAILY_PLAN_SECTION_IDS\" TEXT," + // 22: PlanDailyPlanSectionIDs
                "\"SYS_PATROL_EXECUTION_ID\" TEXT);"); // 23: sysPatrolExecutionID
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"operation_LineCross\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, LineCrossEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getPlatformId());
        stmt.bindLong(3, entity.getStartTowerId());
        stmt.bindLong(4, entity.getEndTowerId());
        stmt.bindLong(5, entity.getCrossType());
        stmt.bindDouble(6, entity.getDtoSmartTower());
        stmt.bindDouble(7, entity.getClearance());
        stmt.bindDouble(8, entity.getCrossAngle());
 
        String CrossImage = entity.getCrossImage();
        if (CrossImage != null) {
            stmt.bindString(9, CrossImage);
        }
 
        String CrossLatitude = entity.getCrossLatitude();
        if (CrossLatitude != null) {
            stmt.bindString(10, CrossLatitude);
        }
 
        String CrossLongitude = entity.getCrossLongitude();
        if (CrossLongitude != null) {
            stmt.bindString(11, CrossLongitude);
        }
 
        String Remark = entity.getRemark();
        if (Remark != null) {
            stmt.bindString(12, Remark);
        }
 
        String CreatedTime = entity.getCreatedTime();
        if (CreatedTime != null) {
            stmt.bindString(13, CreatedTime);
        }
 
        String UpdatedTime = entity.getUpdatedTime();
        if (UpdatedTime != null) {
            stmt.bindString(14, UpdatedTime);
        }
        stmt.bindLong(15, entity.getUploadFlag());
 
        String LineName = entity.getLineName();
        if (LineName != null) {
            stmt.bindString(16, LineName);
        }
 
        String VoltageClass = entity.getVoltageClass();
        if (VoltageClass != null) {
            stmt.bindString(17, VoltageClass);
        }
 
        String StartTower = entity.getStartTower();
        if (StartTower != null) {
            stmt.bindString(18, StartTower);
        }
 
        String EndTower = entity.getEndTower();
        if (EndTower != null) {
            stmt.bindString(19, EndTower);
        }
 
        String CrossStatus = entity.getCrossStatus();
        if (CrossStatus != null) {
            stmt.bindString(20, CrossStatus);
        }
 
        String CrossTypeName = entity.getCrossTypeName();
        if (CrossTypeName != null) {
            stmt.bindString(21, CrossTypeName);
        }
 
        String CreatedBy = entity.getCreatedBy();
        if (CreatedBy != null) {
            stmt.bindString(22, CreatedBy);
        }
 
        String PlanDailyPlanSectionIDs = entity.getPlanDailyPlanSectionIDs();
        if (PlanDailyPlanSectionIDs != null) {
            stmt.bindString(23, PlanDailyPlanSectionIDs);
        }
 
        String sysPatrolExecutionID = entity.getSysPatrolExecutionID();
        if (sysPatrolExecutionID != null) {
            stmt.bindString(24, sysPatrolExecutionID);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, LineCrossEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getPlatformId());
        stmt.bindLong(3, entity.getStartTowerId());
        stmt.bindLong(4, entity.getEndTowerId());
        stmt.bindLong(5, entity.getCrossType());
        stmt.bindDouble(6, entity.getDtoSmartTower());
        stmt.bindDouble(7, entity.getClearance());
        stmt.bindDouble(8, entity.getCrossAngle());
 
        String CrossImage = entity.getCrossImage();
        if (CrossImage != null) {
            stmt.bindString(9, CrossImage);
        }
 
        String CrossLatitude = entity.getCrossLatitude();
        if (CrossLatitude != null) {
            stmt.bindString(10, CrossLatitude);
        }
 
        String CrossLongitude = entity.getCrossLongitude();
        if (CrossLongitude != null) {
            stmt.bindString(11, CrossLongitude);
        }
 
        String Remark = entity.getRemark();
        if (Remark != null) {
            stmt.bindString(12, Remark);
        }
 
        String CreatedTime = entity.getCreatedTime();
        if (CreatedTime != null) {
            stmt.bindString(13, CreatedTime);
        }
 
        String UpdatedTime = entity.getUpdatedTime();
        if (UpdatedTime != null) {
            stmt.bindString(14, UpdatedTime);
        }
        stmt.bindLong(15, entity.getUploadFlag());
 
        String LineName = entity.getLineName();
        if (LineName != null) {
            stmt.bindString(16, LineName);
        }
 
        String VoltageClass = entity.getVoltageClass();
        if (VoltageClass != null) {
            stmt.bindString(17, VoltageClass);
        }
 
        String StartTower = entity.getStartTower();
        if (StartTower != null) {
            stmt.bindString(18, StartTower);
        }
 
        String EndTower = entity.getEndTower();
        if (EndTower != null) {
            stmt.bindString(19, EndTower);
        }
 
        String CrossStatus = entity.getCrossStatus();
        if (CrossStatus != null) {
            stmt.bindString(20, CrossStatus);
        }
 
        String CrossTypeName = entity.getCrossTypeName();
        if (CrossTypeName != null) {
            stmt.bindString(21, CrossTypeName);
        }
 
        String CreatedBy = entity.getCreatedBy();
        if (CreatedBy != null) {
            stmt.bindString(22, CreatedBy);
        }
 
        String PlanDailyPlanSectionIDs = entity.getPlanDailyPlanSectionIDs();
        if (PlanDailyPlanSectionIDs != null) {
            stmt.bindString(23, PlanDailyPlanSectionIDs);
        }
 
        String sysPatrolExecutionID = entity.getSysPatrolExecutionID();
        if (sysPatrolExecutionID != null) {
            stmt.bindString(24, sysPatrolExecutionID);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public LineCrossEntity readEntity(Cursor cursor, int offset) {
        LineCrossEntity entity = new LineCrossEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // PlatformId
            cursor.getInt(offset + 2), // StartTowerId
            cursor.getInt(offset + 3), // EndTowerId
            cursor.getInt(offset + 4), // CrossType
            cursor.getDouble(offset + 5), // DtoSmartTower
            cursor.getDouble(offset + 6), // Clearance
            cursor.getDouble(offset + 7), // CrossAngle
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // CrossImage
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // CrossLatitude
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // CrossLongitude
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // Remark
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // CreatedTime
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // UpdatedTime
            cursor.getInt(offset + 14), // UploadFlag
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // LineName
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // VoltageClass
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // StartTower
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // EndTower
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // CrossStatus
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // CrossTypeName
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // CreatedBy
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // PlanDailyPlanSectionIDs
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23) // sysPatrolExecutionID
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, LineCrossEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPlatformId(cursor.getInt(offset + 1));
        entity.setStartTowerId(cursor.getInt(offset + 2));
        entity.setEndTowerId(cursor.getInt(offset + 3));
        entity.setCrossType(cursor.getInt(offset + 4));
        entity.setDtoSmartTower(cursor.getDouble(offset + 5));
        entity.setClearance(cursor.getDouble(offset + 6));
        entity.setCrossAngle(cursor.getDouble(offset + 7));
        entity.setCrossImage(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCrossLatitude(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCrossLongitude(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setRemark(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCreatedTime(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setUpdatedTime(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setUploadFlag(cursor.getInt(offset + 14));
        entity.setLineName(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setVoltageClass(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setStartTower(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setEndTower(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setCrossStatus(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setCrossTypeName(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setCreatedBy(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setPlanDailyPlanSectionIDs(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setSysPatrolExecutionID(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(LineCrossEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(LineCrossEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(LineCrossEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
