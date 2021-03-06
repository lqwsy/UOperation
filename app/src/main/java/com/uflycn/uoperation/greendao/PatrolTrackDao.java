package com.uflycn.uoperation.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.uflycn.uoperation.bean.PatrolTrack;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "operation_PatrolTrack".
*/
public class PatrolTrackDao extends AbstractDao<PatrolTrack, Long> {

    public static final String TABLENAME = "operation_PatrolTrack";

    /**
     * Properties of entity PatrolTrack.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "Id", true, "sysPatrolTrackId");
        public final static Property SysUserId = new Property(1, String.class, "sysUserId", false, "SYS_USER_ID");
        public final static Property Longitude = new Property(2, double.class, "Longitude", false, "LONGITUDE");
        public final static Property Latitude = new Property(3, double.class, "Latitude", false, "LATITUDE");
        public final static Property Altitude = new Property(4, double.class, "Altitude", false, "ALTITUDE");
        public final static Property CreateDate = new Property(5, String.class, "CreateDate", false, "CREATE_DATE");
        public final static Property UploadFlag = new Property(6, int.class, "UploadFlag", false, "UPLOAD_FLAG");
        public final static Property PatrolTypeId = new Property(7, String.class, "PatrolTypeId", false, "PATROL_TYPE_ID");
        public final static Property SysTowerId = new Property(8, int.class, "sysTowerId", false, "SYS_TOWER_ID");
        public final static Property NearTowerId = new Property(9, int.class, "NearTowerId", false, "NEAR_TOWER_ID");
        public final static Property PatrolType = new Property(10, String.class, "PatrolType", false, "PATROL_TYPE");
        public final static Property SysDailyPlanSectionID = new Property(11, String.class, "sysDailyPlanSectionID", false, "SYS_DAILY_PLAN_SECTION_ID");
        public final static Property SysPatrolExecutionID = new Property(12, String.class, "sysPatrolExecutionID", false, "SYS_PATROL_EXECUTION_ID");
        public final static Property DailyPlanTimeSpanIDs = new Property(13, String.class, "DailyPlanTimeSpanIDs", false, "DAILY_PLAN_TIME_SPAN_IDS");
    }


    public PatrolTrackDao(DaoConfig config) {
        super(config);
    }
    
    public PatrolTrackDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"operation_PatrolTrack\" (" + //
                "\"sysPatrolTrackId\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: Id
                "\"SYS_USER_ID\" TEXT," + // 1: sysUserId
                "\"LONGITUDE\" REAL NOT NULL ," + // 2: Longitude
                "\"LATITUDE\" REAL NOT NULL ," + // 3: Latitude
                "\"ALTITUDE\" REAL NOT NULL ," + // 4: Altitude
                "\"CREATE_DATE\" TEXT," + // 5: CreateDate
                "\"UPLOAD_FLAG\" INTEGER NOT NULL ," + // 6: UploadFlag
                "\"PATROL_TYPE_ID\" TEXT," + // 7: PatrolTypeId
                "\"SYS_TOWER_ID\" INTEGER NOT NULL ," + // 8: sysTowerId
                "\"NEAR_TOWER_ID\" INTEGER NOT NULL ," + // 9: NearTowerId
                "\"PATROL_TYPE\" TEXT," + // 10: PatrolType
                "\"SYS_DAILY_PLAN_SECTION_ID\" TEXT," + // 11: sysDailyPlanSectionID
                "\"SYS_PATROL_EXECUTION_ID\" TEXT," + // 12: sysPatrolExecutionID
                "\"DAILY_PLAN_TIME_SPAN_IDS\" TEXT);"); // 13: DailyPlanTimeSpanIDs
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"operation_PatrolTrack\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PatrolTrack entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String sysUserId = entity.getSysUserId();
        if (sysUserId != null) {
            stmt.bindString(2, sysUserId);
        }
        stmt.bindDouble(3, entity.getLongitude());
        stmt.bindDouble(4, entity.getLatitude());
        stmt.bindDouble(5, entity.getAltitude());
 
        String CreateDate = entity.getCreateDate();
        if (CreateDate != null) {
            stmt.bindString(6, CreateDate);
        }
        stmt.bindLong(7, entity.getUploadFlag());
 
        String PatrolTypeId = entity.getPatrolTypeId();
        if (PatrolTypeId != null) {
            stmt.bindString(8, PatrolTypeId);
        }
        stmt.bindLong(9, entity.getSysTowerId());
        stmt.bindLong(10, entity.getNearTowerId());
 
        String PatrolType = entity.getPatrolType();
        if (PatrolType != null) {
            stmt.bindString(11, PatrolType);
        }
 
        String sysDailyPlanSectionID = entity.getSysDailyPlanSectionID();
        if (sysDailyPlanSectionID != null) {
            stmt.bindString(12, sysDailyPlanSectionID);
        }
 
        String sysPatrolExecutionID = entity.getSysPatrolExecutionID();
        if (sysPatrolExecutionID != null) {
            stmt.bindString(13, sysPatrolExecutionID);
        }
 
        String DailyPlanTimeSpanIDs = entity.getDailyPlanTimeSpanIDs();
        if (DailyPlanTimeSpanIDs != null) {
            stmt.bindString(14, DailyPlanTimeSpanIDs);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PatrolTrack entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String sysUserId = entity.getSysUserId();
        if (sysUserId != null) {
            stmt.bindString(2, sysUserId);
        }
        stmt.bindDouble(3, entity.getLongitude());
        stmt.bindDouble(4, entity.getLatitude());
        stmt.bindDouble(5, entity.getAltitude());
 
        String CreateDate = entity.getCreateDate();
        if (CreateDate != null) {
            stmt.bindString(6, CreateDate);
        }
        stmt.bindLong(7, entity.getUploadFlag());
 
        String PatrolTypeId = entity.getPatrolTypeId();
        if (PatrolTypeId != null) {
            stmt.bindString(8, PatrolTypeId);
        }
        stmt.bindLong(9, entity.getSysTowerId());
        stmt.bindLong(10, entity.getNearTowerId());
 
        String PatrolType = entity.getPatrolType();
        if (PatrolType != null) {
            stmt.bindString(11, PatrolType);
        }
 
        String sysDailyPlanSectionID = entity.getSysDailyPlanSectionID();
        if (sysDailyPlanSectionID != null) {
            stmt.bindString(12, sysDailyPlanSectionID);
        }
 
        String sysPatrolExecutionID = entity.getSysPatrolExecutionID();
        if (sysPatrolExecutionID != null) {
            stmt.bindString(13, sysPatrolExecutionID);
        }
 
        String DailyPlanTimeSpanIDs = entity.getDailyPlanTimeSpanIDs();
        if (DailyPlanTimeSpanIDs != null) {
            stmt.bindString(14, DailyPlanTimeSpanIDs);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public PatrolTrack readEntity(Cursor cursor, int offset) {
        PatrolTrack entity = new PatrolTrack( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // sysUserId
            cursor.getDouble(offset + 2), // Longitude
            cursor.getDouble(offset + 3), // Latitude
            cursor.getDouble(offset + 4), // Altitude
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // CreateDate
            cursor.getInt(offset + 6), // UploadFlag
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // PatrolTypeId
            cursor.getInt(offset + 8), // sysTowerId
            cursor.getInt(offset + 9), // NearTowerId
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // PatrolType
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // sysDailyPlanSectionID
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // sysPatrolExecutionID
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13) // DailyPlanTimeSpanIDs
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PatrolTrack entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSysUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLongitude(cursor.getDouble(offset + 2));
        entity.setLatitude(cursor.getDouble(offset + 3));
        entity.setAltitude(cursor.getDouble(offset + 4));
        entity.setCreateDate(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUploadFlag(cursor.getInt(offset + 6));
        entity.setPatrolTypeId(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSysTowerId(cursor.getInt(offset + 8));
        entity.setNearTowerId(cursor.getInt(offset + 9));
        entity.setPatrolType(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setSysDailyPlanSectionID(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setSysPatrolExecutionID(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setDailyPlanTimeSpanIDs(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PatrolTrack entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PatrolTrack entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PatrolTrack entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
