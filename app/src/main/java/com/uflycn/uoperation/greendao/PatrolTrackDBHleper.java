package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.PatrolTrack;
import com.uflycn.uoperation.util.StringUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */
public class PatrolTrackDBHleper extends DbManager {
    private PatrolTrackDBHleper() {
    }

    //获取所有的轨迹记录
    public List<PatrolTrack> getPatrolTrackRecords() {
        return mDaoSession.getPatrolTrackDao().queryBuilder().list();
    }

    //获取某一天内的轨迹记录

    /**
     * @param date 某一天的日期
     */
    public List<PatrolTrack> getgetPatrolTrackByDay(long date) {
        String sDate = "%" + date + "%";
        QueryBuilder<PatrolTrack> qb = mDaoSession.getPatrolTrackDao().queryBuilder().where(PatrolTrackDao.Properties.CreateDate.like(sDate));
        return qb.list();
    }


    public void insertPatrolTrack(PatrolTrack track) {
        mDaoSession.getPatrolTrackDao().insert(track);
    }

    public List<PatrolTrack> getNeedUploadList() {
        String currentDate = "%" + StringUtils.convertTimestampToYYYYMMDD(new Date().getTime()) + "%";
        String lastDate = "%" + StringUtils.getSpecifiedDayBefore(StringUtils.getNow2()) + "%";

        QueryBuilder<PatrolTrack> qb = mDaoSession.getPatrolTrackDao().queryBuilder()
                .where(PatrolTrackDao.Properties.UploadFlag.eq(0))
                .whereOr(PatrolTrackDao.Properties.CreateDate.like(currentDate), PatrolTrackDao.Properties.CreateDate.like(lastDate));
        return qb.orderDesc(PatrolTrackDao.Properties.Id).limit(1500).list();
    }

    public List<PatrolTrack> getLastestPatrolTack() {
        QueryBuilder<PatrolTrack> qb = mDaoSession.getPatrolTrackDao().queryBuilder()
                .where(PatrolTrackDao.Properties.UploadFlag.eq(0));
        return qb.orderDesc(PatrolTrackDao.Properties.Id).limit(1).list();
    }

    public void updateList(List<PatrolTrack> patrolTracks) {
        mDaoSession.getPatrolTrackDao().updateInTx(patrolTracks);
    }

    public static PatrolTrackDBHleper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static PatrolTrackDBHleper sInstance = new PatrolTrackDBHleper();
    }
}
