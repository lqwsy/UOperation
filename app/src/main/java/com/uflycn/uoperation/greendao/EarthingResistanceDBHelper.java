package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.EarthingResistance;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */
public class EarthingResistanceDBHelper extends DbManager {
    private EarthingResistanceDBHelper() {
    }

    public void insert(EarthingResistance earthingResistance){
        mDaoSession.getEarthingResistanceDao().insert(earthingResistance);
    }

    public List<EarthingResistance> getUploadList(){
        QueryBuilder<EarthingResistance> queryBuilder = mDaoSession.getEarthingResistanceDao().queryBuilder().where(EarthingResistanceDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }

    public void updateById(EarthingResistance earthingResistance){
        mDaoSession.getEarthingResistanceDao().update(earthingResistance);
    }

    public void updateList(List<EarthingResistance> list){
        mDaoSession.getEarthingResistanceDao().updateInTx(list);
    }
    public static EarthingResistanceDBHelper getInstance(){
        return SingletonHolder.sInstance;
    }
    private static class SingletonHolder{
        private static EarthingResistanceDBHelper sInstance = new EarthingResistanceDBHelper();
    }
}
