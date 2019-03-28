package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectRemark;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DefectRemarkDBHelper extends DbManager {
    private DefectRemarkDBHelper() {
    }

    public static DefectRemarkDBHelper getInstance(){
        return SingletonHolder.sInstance;
    }

    public void deleteAllByDefectId(int defectId){
        QueryBuilder<DefectRemark> queryBuilder = mDaoSession.getDefectRemarkDao().
                queryBuilder().where(DefectRemarkDao.Properties.DefectId.eq(defectId),
                DefectRemarkDao.Properties.UploadFlag.eq(1)
                );
       List<DefectRemark> list =  queryBuilder.list();
        for(DefectRemark remark:list){
            mDaoSession.getDefectRemarkDao().delete(remark);
        }
    }
    public void insertDefectRemark(DefectRemark remark){
        mDaoSession.getDefectRemarkDao().insert(remark);
    }
    public void update(DefectRemark defectRemark){
        mDaoSession.getDefectRemarkDao().update(defectRemark);
    }
    public void insertList(List<DefectRemark> list){
        mDaoSession.getDefectRemarkDao().insertInTx(list);
    }

    public List<DefectRemark> getAllListByDefectId(int id){
        QueryBuilder<DefectRemark> queryBuilder = mDaoSession.getDefectRemarkDao().queryBuilder().where(DefectRemarkDao.Properties.DefectId.eq(id));
        return queryBuilder.orderDesc(DefectRemarkDao.Properties.DefectId).list();
    }

    public List<DefectRemark> getAllListByDefectId(DefectBean defectBean) {
        if (defectBean.getSysTowerDefectId() != 0) {
            QueryBuilder<DefectRemark> queryBuilder = mDaoSession.getDefectRemarkDao().queryBuilder().where(DefectRemarkDao.Properties.DefectId.eq(defectBean.getSysTowerDefectId()));
            return queryBuilder.orderDesc(DefectRemarkDao.Properties.DefectId).list();
        } else {
            QueryBuilder<DefectRemark> queryBuilder = mDaoSession.getDefectRemarkDao().queryBuilder().where(DefectRemarkDao.Properties.LocalDefectId.eq(defectBean.getId()));
            return queryBuilder.orderDesc(DefectRemarkDao.Properties.LocalDefectId).list();
        }
    }

    public List<DefectRemark> getNeedUploadList(){
        QueryBuilder<DefectRemark> queryBuilder = mDaoSession.getDefectRemarkDao().queryBuilder().where(DefectRemarkDao.Properties.UploadFlag.eq(0));
        return queryBuilder.orderDesc(DefectRemarkDao.Properties.DefectId).list();
    }

    public List<DefectRemark> getListByLocalDefectId(int localDefectId){
        QueryBuilder<DefectRemark> queryBuilder = mDaoSession.getDefectRemarkDao().queryBuilder().where(DefectRemarkDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }

    private static class SingletonHolder {
        private static DefectRemarkDBHelper sInstance = new DefectRemarkDBHelper();
    }
}
