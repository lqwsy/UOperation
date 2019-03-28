package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.ZeroDetection;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by UF_PC on 2017/11/3.
 */
public class ZeroDetectionDBHelper extends DbManager {

    public ZeroDetectionDBHelper() {
    }

    public static ZeroDetectionDBHelper getInstance() {
        return SingleTonHolder.sInstance;
    }

    private static class SingleTonHolder {
        private static ZeroDetectionDBHelper sInstance = new ZeroDetectionDBHelper();
    }

    public void insertZeroDetection(ZeroDetection zeroDetection) {
        mDaoSession.getZeroDetectionDao().insert(zeroDetection);
    }

    public void updateZeroDetction(ZeroDetection zeroDetection) {
        mDaoSession.getZeroDetectionDao().update(zeroDetection);
    }

    public List<ZeroDetection> getUploadList() {
        QueryBuilder<ZeroDetection> queryBuilder = mDaoSession.getZeroDetectionDao().queryBuilder().where(ZeroDetectionDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }

}
