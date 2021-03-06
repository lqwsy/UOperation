package com.uflycn.uoperation.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.uflycn.uoperation.bean.VirtualTower;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "operation_VirtualTower".
*/
public class VirtualTowerDao extends AbstractDao<VirtualTower, Long> {

    public static final String TABLENAME = "operation_VirtualTower";

    /**
     * Properties of entity VirtualTower.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TowerId = new Property(1, Long.class, "TowerId", false, "TOWER_ID");
        public final static Property TowerChangedID = new Property(2, Long.class, "TowerChangedID", false, "TOWER_CHANGED_ID");
        public final static Property LineId = new Property(3, int.class, "LineId", false, "LINE_ID");
        public final static Property PreTowerOrder = new Property(4, int.class, "PreTowerOrder", false, "PRE_TOWER_ORDER");
        public final static Property NextTowerOrder = new Property(5, int.class, "NextTowerOrder", false, "NEXT_TOWER_ORDER");
        public final static Property DisplayOrder = new Property(6, int.class, "DisplayOrder", false, "DISPLAY_ORDER");
        public final static Property Status = new Property(7, int.class, "Status", false, "STATUS");
        public final static Property Latitude = new Property(8, double.class, "Latitude", false, "LATITUDE");
        public final static Property Longitude = new Property(9, double.class, "Longitude", false, "LONGITUDE");
        public final static Property OriginalLatitude = new Property(10, double.class, "OriginalLatitude", false, "ORIGINAL_LATITUDE");
        public final static Property OriginalLongitude = new Property(11, double.class, "OriginalLongitude", false, "ORIGINAL_LONGITUDE");
        public final static Property Altitude = new Property(12, double.class, "Altitude", false, "ALTITUDE");
        public final static Property TowerName = new Property(13, String.class, "TowerName", false, "TOWER_NAME");
    }


    public VirtualTowerDao(DaoConfig config) {
        super(config);
    }
    
    public VirtualTowerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"operation_VirtualTower\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TOWER_ID\" INTEGER," + // 1: TowerId
                "\"TOWER_CHANGED_ID\" INTEGER," + // 2: TowerChangedID
                "\"LINE_ID\" INTEGER NOT NULL ," + // 3: LineId
                "\"PRE_TOWER_ORDER\" INTEGER NOT NULL ," + // 4: PreTowerOrder
                "\"NEXT_TOWER_ORDER\" INTEGER NOT NULL ," + // 5: NextTowerOrder
                "\"DISPLAY_ORDER\" INTEGER NOT NULL ," + // 6: DisplayOrder
                "\"STATUS\" INTEGER NOT NULL ," + // 7: Status
                "\"LATITUDE\" REAL NOT NULL ," + // 8: Latitude
                "\"LONGITUDE\" REAL NOT NULL ," + // 9: Longitude
                "\"ORIGINAL_LATITUDE\" REAL NOT NULL ," + // 10: OriginalLatitude
                "\"ORIGINAL_LONGITUDE\" REAL NOT NULL ," + // 11: OriginalLongitude
                "\"ALTITUDE\" REAL NOT NULL ," + // 12: Altitude
                "\"TOWER_NAME\" TEXT);"); // 13: TowerName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"operation_VirtualTower\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, VirtualTower entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long TowerId = entity.getTowerId();
        if (TowerId != null) {
            stmt.bindLong(2, TowerId);
        }
 
        Long TowerChangedID = entity.getTowerChangedID();
        if (TowerChangedID != null) {
            stmt.bindLong(3, TowerChangedID);
        }
        stmt.bindLong(4, entity.getLineId());
        stmt.bindLong(5, entity.getPreTowerOrder());
        stmt.bindLong(6, entity.getNextTowerOrder());
        stmt.bindLong(7, entity.getDisplayOrder());
        stmt.bindLong(8, entity.getStatus());
        stmt.bindDouble(9, entity.getLatitude());
        stmt.bindDouble(10, entity.getLongitude());
        stmt.bindDouble(11, entity.getOriginalLatitude());
        stmt.bindDouble(12, entity.getOriginalLongitude());
        stmt.bindDouble(13, entity.getAltitude());
 
        String TowerName = entity.getTowerName();
        if (TowerName != null) {
            stmt.bindString(14, TowerName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, VirtualTower entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long TowerId = entity.getTowerId();
        if (TowerId != null) {
            stmt.bindLong(2, TowerId);
        }
 
        Long TowerChangedID = entity.getTowerChangedID();
        if (TowerChangedID != null) {
            stmt.bindLong(3, TowerChangedID);
        }
        stmt.bindLong(4, entity.getLineId());
        stmt.bindLong(5, entity.getPreTowerOrder());
        stmt.bindLong(6, entity.getNextTowerOrder());
        stmt.bindLong(7, entity.getDisplayOrder());
        stmt.bindLong(8, entity.getStatus());
        stmt.bindDouble(9, entity.getLatitude());
        stmt.bindDouble(10, entity.getLongitude());
        stmt.bindDouble(11, entity.getOriginalLatitude());
        stmt.bindDouble(12, entity.getOriginalLongitude());
        stmt.bindDouble(13, entity.getAltitude());
 
        String TowerName = entity.getTowerName();
        if (TowerName != null) {
            stmt.bindString(14, TowerName);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public VirtualTower readEntity(Cursor cursor, int offset) {
        VirtualTower entity = new VirtualTower( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // TowerId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // TowerChangedID
            cursor.getInt(offset + 3), // LineId
            cursor.getInt(offset + 4), // PreTowerOrder
            cursor.getInt(offset + 5), // NextTowerOrder
            cursor.getInt(offset + 6), // DisplayOrder
            cursor.getInt(offset + 7), // Status
            cursor.getDouble(offset + 8), // Latitude
            cursor.getDouble(offset + 9), // Longitude
            cursor.getDouble(offset + 10), // OriginalLatitude
            cursor.getDouble(offset + 11), // OriginalLongitude
            cursor.getDouble(offset + 12), // Altitude
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13) // TowerName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, VirtualTower entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTowerId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTowerChangedID(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setLineId(cursor.getInt(offset + 3));
        entity.setPreTowerOrder(cursor.getInt(offset + 4));
        entity.setNextTowerOrder(cursor.getInt(offset + 5));
        entity.setDisplayOrder(cursor.getInt(offset + 6));
        entity.setStatus(cursor.getInt(offset + 7));
        entity.setLatitude(cursor.getDouble(offset + 8));
        entity.setLongitude(cursor.getDouble(offset + 9));
        entity.setOriginalLatitude(cursor.getDouble(offset + 10));
        entity.setOriginalLongitude(cursor.getDouble(offset + 11));
        entity.setAltitude(cursor.getDouble(offset + 12));
        entity.setTowerName(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(VirtualTower entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(VirtualTower entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(VirtualTower entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
