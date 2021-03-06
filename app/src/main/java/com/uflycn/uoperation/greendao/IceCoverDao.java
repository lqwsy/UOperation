package com.uflycn.uoperation.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.uflycn.uoperation.bean.IceCover;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "operation_IceCover".
*/
public class IceCoverDao extends AbstractDao<IceCover, Long> {

    public static final String TABLENAME = "operation_IceCover";

    /**
     * Properties of entity IceCover.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "Id", true, "_id");
        public final static Property SysUserId = new Property(1, String.class, "sysUserId", false, "SYS_USER_ID");
        public final static Property TowerId = new Property(2, int.class, "TowerId", false, "TOWER_ID");
        public final static Property IceCoverHeight = new Property(3, Double.class, "IceCoverHeight", false, "ICE_COVER_HEIGHT");
        public final static Property IceType = new Property(4, String.class, "IceType", false, "ICE_TYPE");
        public final static Property Temperature = new Property(5, Double.class, "Temperature", false, "TEMPERATURE");
        public final static Property Wetness = new Property(6, Double.class, "Wetness", false, "WETNESS");
        public final static Property CreateDate = new Property(7, String.class, "CreateDate", false, "CREATE_DATE");
        public final static Property UploadFlag = new Property(8, int.class, "UploadFlag", false, "UPLOAD_FLAG");
        public final static Property PlanDailyPlanSectionIDs = new Property(9, String.class, "PlanDailyPlanSectionIDs", false, "PLAN_DAILY_PLAN_SECTION_IDS");
        public final static Property SysPatrolExecutionID = new Property(10, String.class, "sysPatrolExecutionID", false, "SYS_PATROL_EXECUTION_ID");
    }


    public IceCoverDao(DaoConfig config) {
        super(config);
    }
    
    public IceCoverDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"operation_IceCover\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: Id
                "\"SYS_USER_ID\" TEXT," + // 1: sysUserId
                "\"TOWER_ID\" INTEGER NOT NULL ," + // 2: TowerId
                "\"ICE_COVER_HEIGHT\" REAL," + // 3: IceCoverHeight
                "\"ICE_TYPE\" TEXT," + // 4: IceType
                "\"TEMPERATURE\" REAL," + // 5: Temperature
                "\"WETNESS\" REAL," + // 6: Wetness
                "\"CREATE_DATE\" TEXT," + // 7: CreateDate
                "\"UPLOAD_FLAG\" INTEGER NOT NULL ," + // 8: UploadFlag
                "\"PLAN_DAILY_PLAN_SECTION_IDS\" TEXT," + // 9: PlanDailyPlanSectionIDs
                "\"SYS_PATROL_EXECUTION_ID\" TEXT);"); // 10: sysPatrolExecutionID
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"operation_IceCover\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, IceCover entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String sysUserId = entity.getSysUserId();
        if (sysUserId != null) {
            stmt.bindString(2, sysUserId);
        }
        stmt.bindLong(3, entity.getTowerId());
 
        Double IceCoverHeight = entity.getIceCoverHeight();
        if (IceCoverHeight != null) {
            stmt.bindDouble(4, IceCoverHeight);
        }
 
        String IceType = entity.getIceType();
        if (IceType != null) {
            stmt.bindString(5, IceType);
        }
 
        Double Temperature = entity.getTemperature();
        if (Temperature != null) {
            stmt.bindDouble(6, Temperature);
        }
 
        Double Wetness = entity.getWetness();
        if (Wetness != null) {
            stmt.bindDouble(7, Wetness);
        }
 
        String CreateDate = entity.getCreateDate();
        if (CreateDate != null) {
            stmt.bindString(8, CreateDate);
        }
        stmt.bindLong(9, entity.getUploadFlag());
 
        String PlanDailyPlanSectionIDs = entity.getPlanDailyPlanSectionIDs();
        if (PlanDailyPlanSectionIDs != null) {
            stmt.bindString(10, PlanDailyPlanSectionIDs);
        }
 
        String sysPatrolExecutionID = entity.getSysPatrolExecutionID();
        if (sysPatrolExecutionID != null) {
            stmt.bindString(11, sysPatrolExecutionID);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, IceCover entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String sysUserId = entity.getSysUserId();
        if (sysUserId != null) {
            stmt.bindString(2, sysUserId);
        }
        stmt.bindLong(3, entity.getTowerId());
 
        Double IceCoverHeight = entity.getIceCoverHeight();
        if (IceCoverHeight != null) {
            stmt.bindDouble(4, IceCoverHeight);
        }
 
        String IceType = entity.getIceType();
        if (IceType != null) {
            stmt.bindString(5, IceType);
        }
 
        Double Temperature = entity.getTemperature();
        if (Temperature != null) {
            stmt.bindDouble(6, Temperature);
        }
 
        Double Wetness = entity.getWetness();
        if (Wetness != null) {
            stmt.bindDouble(7, Wetness);
        }
 
        String CreateDate = entity.getCreateDate();
        if (CreateDate != null) {
            stmt.bindString(8, CreateDate);
        }
        stmt.bindLong(9, entity.getUploadFlag());
 
        String PlanDailyPlanSectionIDs = entity.getPlanDailyPlanSectionIDs();
        if (PlanDailyPlanSectionIDs != null) {
            stmt.bindString(10, PlanDailyPlanSectionIDs);
        }
 
        String sysPatrolExecutionID = entity.getSysPatrolExecutionID();
        if (sysPatrolExecutionID != null) {
            stmt.bindString(11, sysPatrolExecutionID);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public IceCover readEntity(Cursor cursor, int offset) {
        IceCover entity = new IceCover( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // sysUserId
            cursor.getInt(offset + 2), // TowerId
            cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3), // IceCoverHeight
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // IceType
            cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5), // Temperature
            cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6), // Wetness
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // CreateDate
            cursor.getInt(offset + 8), // UploadFlag
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // PlanDailyPlanSectionIDs
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // sysPatrolExecutionID
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, IceCover entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSysUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTowerId(cursor.getInt(offset + 2));
        entity.setIceCoverHeight(cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3));
        entity.setIceType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTemperature(cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5));
        entity.setWetness(cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6));
        entity.setCreateDate(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUploadFlag(cursor.getInt(offset + 8));
        entity.setPlanDailyPlanSectionIDs(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setSysPatrolExecutionID(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(IceCover entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(IceCover entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(IceCover entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
