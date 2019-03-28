package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.DayPlan;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DayPlanDBHelper extends DbManager {
    private DayPlanDBHelper() {
    }

    public static DayPlanDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }


    private static class SingletonHolder {
        private static DayPlanDBHelper sInstance = new DayPlanDBHelper();
    }

    public void insert(List<DayPlan> dayPlans) {
        clearAll();
        mDaoSession.getDayPlanDao().insertInTx(dayPlans);
    }

    public List<DayPlan> getList() {
        QueryBuilder<DayPlan> queryBuilder = mDaoSession.getDayPlanDao().queryBuilder().where(DayPlanDao.Properties.StatusString.eq("未完成"));
        return queryBuilder.list();
    }

    public void updateDayPlanToFinish(String dayPlanId) {
        DayPlan dayPlan = mDaoSession.getDayPlanDao().queryBuilder().where(DayPlanDao.Properties.SysDailyPlanSectionID.eq(dayPlanId)).unique();
        if (dayPlan!=null){
            dayPlan.setStatusString("已完成");
            mDaoSession.getDayPlanDao().update(dayPlan);
        }
    }

    public void clearAll() {
        mDaoSession.getDayPlanDao().deleteAll();
    }
}
