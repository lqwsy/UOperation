package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.DefectType;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DefectTypeDBHelper extends DbManager {
    private DefectTypeDBHelper() {
    }

    public DefectType getDefectType(long defectTypeId) {
        QueryBuilder<DefectType> queryBuilder = mDaoSession.getDefectTypeDao().queryBuilder().where(DefectTypeDao.Properties.SysDefectTypeID.eq(defectTypeId));
        return queryBuilder.unique();
    }

    public void inserOrUpdate(DefectType defectType) {
        DefectType current = getDefectType(defectType.getSysDefectTypeID());
        if (current == null) {
            mDaoSession.getDefectTypeDao().insert(defectType);
        } else {
            mDaoSession.getDefectTypeDao().update(defectType);
        }
    }

    public void insertList(List<DefectType> defectTypes) {
        mDaoSession.getDefectTypeDao().insertInTx(defectTypes);
    }

    public void clearAll() {
        mDaoSession.getDefectTypeDao().deleteAll();
    }

    public List<DefectType> getChilds(int parentId) {
        QueryBuilder<DefectType> queryBuilder = mDaoSession.getDefectTypeDao().queryBuilder().where(DefectTypeDao.Properties.DefectParentId.eq(parentId),
                DefectTypeDao.Properties.Deleted.eq(0));
        return queryBuilder.list();
    }


    public List<DefectType> getDefectType(int category) {
        QueryBuilder<DefectType> queryBuilder = mDaoSession.getDefectTypeDao().queryBuilder().where(DefectTypeDao.Properties.DefectParentId.eq(0),
                DefectTypeDao.Properties.DefectCategory.eq(category), DefectTypeDao.Properties.Deleted.eq(0));
        return queryBuilder.list();
    }



    public DefectType getParentDefect(int category) {
        DefectType defectType = mDaoSession.getDefectTypeDao().queryBuilder().where(DefectTypeDao.Properties.SysDefectTypeID.eq(category)).unique();
        DefectType parent = mDaoSession.getDefectTypeDao().queryBuilder().where(DefectTypeDao.Properties.SysDefectTypeID.eq(defectType.getDefectParentId())).unique();

        return parent;
    }


    public static DefectTypeDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }


    private static class SingletonHolder {
        private static DefectTypeDBHelper sInstance = new DefectTypeDBHelper();
    }
}
