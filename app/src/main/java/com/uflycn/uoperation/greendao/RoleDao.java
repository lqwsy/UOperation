package com.uflycn.uoperation.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.uflycn.uoperation.bean.Role;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "t_role".
*/
public class RoleDao extends AbstractDao<Role, String> {

    public static final String TABLENAME = "t_role";

    /**
     * Properties of entity Role.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property RoleId = new Property(0, String.class, "RoleId", true, "ROLE_ID");
        public final static Property Code = new Property(1, String.class, "Code", false, "CODE");
        public final static Property FullName = new Property(2, String.class, "FullName", false, "FULL_NAME");
        public final static Property Category = new Property(3, String.class, "Category", false, "CATEGORY");
        public final static Property Description = new Property(4, String.class, "Description", false, "DESCRIPTION");
        public final static Property Enabled = new Property(5, int.class, "Enabled", false, "ENABLED");
        public final static Property DeleteMark = new Property(6, int.class, "DeleteMark", false, "DELETE_MARK");
    }


    public RoleDao(DaoConfig config) {
        super(config);
    }
    
    public RoleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"t_role\" (" + //
                "\"ROLE_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: RoleId
                "\"CODE\" TEXT," + // 1: Code
                "\"FULL_NAME\" TEXT," + // 2: FullName
                "\"CATEGORY\" TEXT," + // 3: Category
                "\"DESCRIPTION\" TEXT," + // 4: Description
                "\"ENABLED\" INTEGER NOT NULL ," + // 5: Enabled
                "\"DELETE_MARK\" INTEGER NOT NULL );"); // 6: DeleteMark
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"t_role\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Role entity) {
        stmt.clearBindings();
 
        String RoleId = entity.getRoleId();
        if (RoleId != null) {
            stmt.bindString(1, RoleId);
        }
 
        String Code = entity.getCode();
        if (Code != null) {
            stmt.bindString(2, Code);
        }
 
        String FullName = entity.getFullName();
        if (FullName != null) {
            stmt.bindString(3, FullName);
        }
 
        String Category = entity.getCategory();
        if (Category != null) {
            stmt.bindString(4, Category);
        }
 
        String Description = entity.getDescription();
        if (Description != null) {
            stmt.bindString(5, Description);
        }
        stmt.bindLong(6, entity.getEnabled());
        stmt.bindLong(7, entity.getDeleteMark());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Role entity) {
        stmt.clearBindings();
 
        String RoleId = entity.getRoleId();
        if (RoleId != null) {
            stmt.bindString(1, RoleId);
        }
 
        String Code = entity.getCode();
        if (Code != null) {
            stmt.bindString(2, Code);
        }
 
        String FullName = entity.getFullName();
        if (FullName != null) {
            stmt.bindString(3, FullName);
        }
 
        String Category = entity.getCategory();
        if (Category != null) {
            stmt.bindString(4, Category);
        }
 
        String Description = entity.getDescription();
        if (Description != null) {
            stmt.bindString(5, Description);
        }
        stmt.bindLong(6, entity.getEnabled());
        stmt.bindLong(7, entity.getDeleteMark());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public Role readEntity(Cursor cursor, int offset) {
        Role entity = new Role( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // RoleId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // FullName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Category
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Description
            cursor.getInt(offset + 5), // Enabled
            cursor.getInt(offset + 6) // DeleteMark
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Role entity, int offset) {
        entity.setRoleId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFullName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCategory(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDescription(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEnabled(cursor.getInt(offset + 5));
        entity.setDeleteMark(cursor.getInt(offset + 6));
     }
    
    @Override
    protected final String updateKeyAfterInsert(Role entity, long rowId) {
        return entity.getRoleId();
    }
    
    @Override
    public String getKey(Role entity) {
        if(entity != null) {
            return entity.getRoleId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Role entity) {
        return entity.getRoleId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
