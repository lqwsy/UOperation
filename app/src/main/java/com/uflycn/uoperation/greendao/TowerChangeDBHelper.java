package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.TowerChange;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */
public class TowerChangeDBHelper extends DbManager {//杆塔变更登记

    private TowerChangeDBHelper() {
    }

    public TowerChange getTowerChange(long id) {
        return mDaoSession.getTowerChangeDao().load(id);
    }

    public List<TowerChange> getUploadList() {
        QueryBuilder<TowerChange> queryBuilder = mDaoSession.getTowerChangeDao().queryBuilder().where(TowerChangeDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }

    public void insert(TowerChange towerChange) {
        try{//可能已经存进去了
            mDaoSession.getTowerChangeDao().insert(towerChange);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateTowerChange(TowerChange towerChange) {
        mDaoSession.getTowerChangeDao().update(towerChange);
    }

    public static TowerChangeDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static TowerChangeDBHelper sInstance = new TowerChangeDBHelper();
    }
}
