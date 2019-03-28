package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.InPlace;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */
public class InPlaceDBHelper extends DbManager{
    private InPlaceDBHelper() {
    }
    public static InPlaceDBHelper getInstance(){
        return SingletonHolder.sInstance;
    }


    public InPlace getInplace(long id){
       return mDaoSession.getInPlaceDao().load(id);
    }
    /**获取所有需要上传的 到位登记*/
    public List<InPlace> getNeedUploadInplace(){
        QueryBuilder<InPlace> queryBuilder = mDaoSession.getInPlaceDao().queryBuilder().where(InPlaceDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }

    public void updateInplace(InPlace inPlace){
        mDaoSession.getInPlaceDao().update(inPlace);
    }
    public void insertInplace(InPlace inPlace){
        mDaoSession.getInPlaceDao().insert(inPlace);
    }
    private static class SingletonHolder{
        private static InPlaceDBHelper sInstance = new InPlaceDBHelper();
    }
}
