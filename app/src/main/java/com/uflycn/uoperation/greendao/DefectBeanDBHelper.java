package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.DefectBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DefectBeanDBHelper extends DbManager {
    private DefectBeanDBHelper() {
    }

    public static DefectBeanDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    public void deleteAllByLineId(String lineName) {
        QueryBuilder<DefectBean> queryBuilder = mDaoSession.getDefectBeanDao().queryBuilder().where(
                DefectBeanDao.Properties.LineName.eq(lineName)
        );
        List<DefectBean> defectBeanList = queryBuilder.list();
        mDaoSession.getDefectBeanDao().deleteInTx(defectBeanList);
    }

    public List<DefectBean> getAllDefectByLineId(String lineName) {
        QueryBuilder<DefectBean> queryBuilder = mDaoSession.getDefectBeanDao().queryBuilder().where(
                DefectBeanDao.Properties.LineName.eq(lineName)
        );
        return queryBuilder.orderDesc(DefectBeanDao.Properties.Id).list();
    }

    public List<DefectBean> getDefectByTowerNum(String lineName, String towerNum){
        QueryBuilder<DefectBean> queryBuilder = mDaoSession.getDefectBeanDao().queryBuilder().where(
                DefectBeanDao.Properties.LineName.eq(lineName), DefectBeanDao.Properties.SysTowerID.eq(towerNum)
        );
//        List<DefectBean> result =  new ArrayList<>();
//        for(DefectBean defectBean : queryBuilder.list()){
//            if(defectBean.getTowerNo().equals(towerNum)||defectBean.getNearTowerNo().equals(towerNum)){
//                result.add(defectBean);
//            }
//        }
        return queryBuilder.list();
    }



    public void deleteDefectBean(DefectBean defectBean) {
        try {//可能数据库没有这条记录
            if (defectBean.getSysTowerDefectId() != 0) {
                delete(defectBean.getSysTowerDefectId());
            } else {
                mDaoSession.getDefectBeanDao().delete(defectBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAllByLineName(List<String> lineNames) {
        for (String lineName : lineNames) {
            deleteByLineName(lineName);
        }
    }
    private void deleteByLineName(String lineName) {
        QueryBuilder<DefectBean> queryBuilder = mDaoSession.getDefectBeanDao().queryBuilder().where(
                DefectBeanDao.Properties.UploadFlag.eq(1), DefectBeanDao.Properties.LineName.eq(lineName));
        mDaoSession.getDefectBeanDao().deleteInTx(queryBuilder.list());
    }

    public void delete(int id) {
        QueryBuilder<DefectBean> queryBuilder = mDaoSession.getDefectBeanDao().queryBuilder().where(DefectBeanDao.Properties.SysTowerDefectId.eq(id));
        DefectBean defectBean = queryBuilder.unique();
        mDaoSession.getDefectBeanDao().delete(defectBean);
    }

    public void insertLineDefectList(List<DefectBean> list) {
        try {
            mDaoSession.getDefectBeanDao().insertOrReplaceInTx(list);//插入本条线路的所有记录
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DefectBean getDefectBeanById(int id){
        QueryBuilder<DefectBean> queryBuilder = mDaoSession.getDefectBeanDao().queryBuilder().where(DefectBeanDao.Properties.SysTowerDefectId.eq(id));
        return queryBuilder.unique();
    }

    public Long insert(DefectBean defectBean) {
        return mDaoSession.getDefectBeanDao().insertOrReplace(defectBean);
    }

    public void insertFromWsm(DefectBean defectBean){
        if(defectBean==null)
            return;
        QueryBuilder<DefectBean> queryBuilder = mDaoSession.getDefectBeanDao().queryBuilder().where(DefectBeanDao.Properties.SysTowerDefectId.eq(defectBean.getSysTowerDefectId()));
        if (queryBuilder.list().size() == 0)
            mDaoSession.getDefectBeanDao().insertOrReplace(defectBean);
    }

    public void updateById(DefectBean defectBean) {
        mDaoSession.getDefectBeanDao().update(defectBean);
    }


    public List<DefectBean> getNeedUploadList() {
        QueryBuilder<DefectBean> queryBuilder = mDaoSession.getDefectBeanDao().queryBuilder().where(DefectBeanDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }

    private static class SingletonHolder {
        private static DefectBeanDBHelper sInstance = new DefectBeanDBHelper();
    }
}
