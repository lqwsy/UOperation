package com.uflycn.uoperation.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.uflycn.uoperation.bean.DayPlanDetail;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "t_dayPlanDetail".
*/
public class DayPlanDetailDao extends AbstractDao<DayPlanDetail, Long> {

    public static final String TABLENAME = "t_dayPlanDetail";

    /**
     * Properties of entity DayPlanDetail.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "Id", true, "_id");
        public final static Property SysDailyPlanSectionID = new Property(1, String.class, "sysDailyPlanSectionID", false, "SYS_DAILY_PLAN_SECTION_ID");
        public final static Property Status = new Property(2, int.class, "Status", false, "STATUS");
        public final static Property WorkNote = new Property(3, String.class, "WorkNote", false, "WORK_NOTE");
        public final static Property UploadFlag = new Property(4, boolean.class, "uploadFlag", false, "UPLOAD_FLAG");
    }


    public DayPlanDetailDao(DaoConfig config) {
        super(config);
    }
    
    public DayPlanDetailDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"t_dayPlanDetail\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: Id
                "\"SYS_DAILY_PLAN_SECTION_ID\" TEXT," + // 1: sysDailyPlanSectionID
                "\"STATUS\" INTEGER NOT NULL ," + // 2: Status
                "\"WORK_NOTE\" TEXT," + // 3: WorkNote
                "\"UPLOAD_FLAG\" INTEGER NOT NULL );"); // 4: uploadFlag
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"t_dayPlanDetail\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DayPlanDetail entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String sysDailyPlanSectionID = entity.getSysDailyPlanSectionID();
        if (sysDailyPlanSectionID != null) {
            stmt.bindString(2, sysDailyPlanSectionID);
        }
        stmt.bindLong(3, entity.getStatus());
 
        String WorkNote = entity.getWorkNote();
        if (WorkNote != null) {
            stmt.bindString(4, WorkNote);
        }
        stmt.bindLong(5, entity.getUploadFlag() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DayPlanDetail entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
 
        String sysDailyPlanSectionID = entity.getSysDailyPlanSectionID();
        if (sysDailyPlanSectionID != null) {
            stmt.bindString(2, sysDailyPlanSectionID);
        }
        stmt.bindLong(3, entity.getStatus());
 
        String WorkNote = entity.getWorkNote();
        if (WorkNote != null) {
            stmt.bindString(4, WorkNote);
        }
        stmt.bindLong(5, entity.getUploadFlag() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DayPlanDetail readEntity(Cursor cursor, int offset) {
        DayPlanDetail entity = new DayPlanDetail( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // sysDailyPlanSectionID
            cursor.getInt(offset + 2), // Status
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // WorkNote
            cursor.getShort(offset + 4) != 0 // uploadFlag
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DayPlanDetail entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSysDailyPlanSectionID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setStatus(cursor.getInt(offset + 2));
        entity.setWorkNote(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUploadFlag(cursor.getShort(offset + 4) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DayPlanDetail entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DayPlanDetail entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DayPlanDetail entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}