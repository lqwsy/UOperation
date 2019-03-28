package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.IceCover;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */
public class IceCoverDBHelper extends DbManager {
    private IceCoverDBHelper() {
    }

    public void insert(IceCover iceCover){
        mDaoSession.getIceCoverDao().insert(iceCover);
    }

    public List<IceCover> getUploadList(){
        QueryBuilder<IceCover> queryBuilder = mDaoSession.getIceCoverDao().queryBuilder().where(IceCoverDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }

    public void updateById(IceCover iceCover){
        mDaoSession.getIceCoverDao().update(iceCover);
    }

    public void updateList(List<IceCover> list){
        mDaoSession.getIceCoverDao().updateInTx(list);
    }
    public static IceCoverDBHelper getInstance(){
        return SingletonHolder.sInstance;
    }
    private static class SingletonHolder{
        private static IceCoverDBHelper sInstance = new IceCoverDBHelper();
    }
}
