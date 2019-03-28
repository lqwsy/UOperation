package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.AssistRecord;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class AssistRecordDBHelper extends DbManager {

    private AssistRecordDBHelper() {
    }

    public static AssistRecordDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static AssistRecordDBHelper sInstance = new AssistRecordDBHelper();
    }

    public Long insert(AssistRecord assistRecord) {
        return mDaoSession.getAssistRecordDao().insert(assistRecord);
    }

    public List<AssistRecord> getNeedUpload() {
        QueryBuilder<AssistRecord> queryBuilder = mDaoSession.getAssistRecordDao().queryBuilder().where(AssistRecordDao.Properties.IsUpload.eq(false));
        return queryBuilder.list();
    }

    public void update(AssistRecord assistRecord) {
        mDaoSession.getAssistRecordDao().update(assistRecord);

    }

}
