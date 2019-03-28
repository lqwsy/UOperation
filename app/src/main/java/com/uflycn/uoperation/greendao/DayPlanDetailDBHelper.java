package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.DayPlanDetail;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DayPlanDetailDBHelper extends DbManager {
    private DayPlanDetailDBHelper() {
    }

    public static DayPlanDetailDBHelper getInstance() {
        return DayPlanDetailDBHelper.SingletonHolder.sInstance;
    }


    private static class SingletonHolder {
        private static DayPlanDetailDBHelper sInstance = new DayPlanDetailDBHelper();
    }


    public List<DayPlanDetail> getNeedUploadList() {
        QueryBuilder<DayPlanDetail> queryBuilder = mDaoSession.getDayPlanDetailDao().queryBuilder().where(DayPlanDetailDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }


    public long insert(DayPlanDetail dayPlanDetail) {
        return mDaoSession.getDayPlanDetailDao().insert(dayPlanDetail);
    }

    public void update(DayPlanDetail dayPlanDetail) {
        mDaoSession.getDayPlanDetailDao().update(dayPlanDetail);
    }


}
