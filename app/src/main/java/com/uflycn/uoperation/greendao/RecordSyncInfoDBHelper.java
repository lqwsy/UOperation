package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.RecordSyncInfo;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */
public class RecordSyncInfoDBHelper extends DbManager {
    private RecordSyncInfoDBHelper() {
    }

    public List<RecordSyncInfo> getList() {
        QueryBuilder<RecordSyncInfo> queryBuilder = mDaoSession.getRecordSyncInfoDao().queryBuilder();
        return queryBuilder.list();
    }

    public void insert(RecordSyncInfo recordSyncInfo) {
        mDaoSession.getRecordSyncInfoDao().insert(recordSyncInfo);
    }

    public void update(int catogary, RecordSyncInfo recordSyncInfo) {
        QueryBuilder<RecordSyncInfo> queryBuilder = mDaoSession.getRecordSyncInfoDao().queryBuilder().where(RecordSyncInfoDao.Properties.Category.eq(catogary));
        RecordSyncInfo info = queryBuilder.unique();
        info.setLastSyncTime(recordSyncInfo.getLastSyncTime());
        mDaoSession.getRecordSyncInfoDao().update(info);
    }


    public void clearDatas() {
        mDaoSession.getRecordSyncInfoDao().deleteAll();
    }

    public static RecordSyncInfoDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static RecordSyncInfoDBHelper sInstance = new RecordSyncInfoDBHelper();
    }
}
