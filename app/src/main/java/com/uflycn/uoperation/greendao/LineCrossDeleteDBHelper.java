package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.LineCrossDelete;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class LineCrossDeleteDBHelper extends DbManager {
    private LineCrossDeleteDBHelper() {
    }

    public void insert(LineCrossDelete lineCrossDelete){
        mDaoSession.getLineCrossDeleteDao().insert(lineCrossDelete);
    }

    public void update(LineCrossDelete lineCrossDelete){
        mDaoSession.getLineCrossDeleteDao().update(lineCrossDelete);
    }

    public List<LineCrossDelete> getNeedUploadList(){
        QueryBuilder<LineCrossDelete> queryBuilder = mDaoSession.getLineCrossDeleteDao().queryBuilder().where(LineCrossDeleteDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }



    public static LineCrossDeleteDBHelper getInstance(){
        return SingletonHolder.sInstance;
    }
    private static class SingletonHolder{
        private static LineCrossDeleteDBHelper sInstance = new LineCrossDeleteDBHelper();

    }
}
