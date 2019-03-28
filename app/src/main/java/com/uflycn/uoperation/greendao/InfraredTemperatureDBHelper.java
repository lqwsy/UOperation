package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.OptTensilePointTemperature;
import com.uflycn.uoperation.bean.InfraredTemperature;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */
public class InfraredTemperatureDBHelper extends DbManager {
    private InfraredTemperatureDBHelper() {
    }

    public void insert(InfraredTemperature temperature) {
        mDaoSession.getInfraredTemperatureDao().insertOrReplace(temperature);
    }

    public void insert(InfraredTemperature temperature, List<OptTensilePointTemperature> list) {
        //把InfraredTemperature插入到数据库
        long id = mDaoSession.getInfraredTemperatureDao().insertOrReplace(temperature);
        if (list == null) {
            return;
        }
        //修改OptTensilePointTemperature的id
        for (OptTensilePointTemperature optTensilePointTemperature : list) {
            optTensilePointTemperature.setInfraredTemperatureId(id);
        }
        //把OptTensilePointTemperature保存
        //先清空原来表中数据
        mDaoSession.getOptTensilePointTemperatureDao().insertInTx(list);
    }
    public List<InfraredTemperature> getUploadList() {
        QueryBuilder<InfraredTemperature> queryBuilder = mDaoSession.getInfraredTemperatureDao().queryBuilder().where(InfraredTemperatureDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }


    public void updateInfraredTemperature(InfraredTemperature infraredTemperature) {
        mDaoSession.getInfraredTemperatureDao().update(infraredTemperature);
    }

    public static InfraredTemperatureDBHelper getInstance() {
        return SingeletonHolder.sInstance;
    }

    private static class SingeletonHolder {
        private static InfraredTemperatureDBHelper sInstance = new InfraredTemperatureDBHelper();
    }
}
