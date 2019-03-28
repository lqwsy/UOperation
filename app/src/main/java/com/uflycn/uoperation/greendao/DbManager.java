package com.uflycn.uoperation.greendao;

import android.database.sqlite.SQLiteDatabase;

import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.constant.AppConstant;

public class DbManager {
    private DaoMaster.DevOpenHelper openHelper;

    protected DaoMaster mDaoMaster;

    protected DaoSession mDaoSession;

    protected DbManager() {
        openHelper = new DaoMaster.DevOpenHelper(new DatabaseContext(MyApplication.getContext()), AppConstant.DB_NAME,null);
        mDaoMaster = new DaoMaster(openHelper.getWritableDb());
        mDaoSession = mDaoMaster.newSession();
    }

    /**
     * 获取可读数据库
     */
    protected SQLiteDatabase getReadableDatabase() {
        return openHelper.getReadableDatabase();
    }


    /**
     * 获取可写数据库
     */
    protected SQLiteDatabase getWritableDatabase() {
        return openHelper.getWritableDatabase();
    }


    public static DbManager getInstance(){
        return DBSingleton.mInstance;
    }
    private static class DBSingleton{
        private static  DbManager mInstance = new DbManager();
    }

}
