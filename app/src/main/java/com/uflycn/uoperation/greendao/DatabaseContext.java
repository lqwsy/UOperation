package com.uflycn.uoperation.greendao;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.util.IOUtils;

import java.io.File;

/**
 * Created by Ryan on 2016/8/3.
 */
public class DatabaseContext extends ContextWrapper {

    private Context mContext;

    public DatabaseContext(Context base) {
        super(base);
        mContext = base;
    }

    @Override
    public File getDatabasePath(String name) {
        String myPath = IOUtils.getRootStoragePath(mContext) + AppConstant.DB_DIR+ "/" + AppConstant.DB_NAME;
        File file = new File(myPath);
      if(!file.exists()){
          try{
              file.createNewFile();
          }catch (Exception  e){
          }
      }
        return file;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }

    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @see ContextWrapper#openOrCreateDatabase(String,
     *      int,
     *      SQLiteDatabase.CursorFactory,
     *      DatabaseErrorHandler)
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }
}
