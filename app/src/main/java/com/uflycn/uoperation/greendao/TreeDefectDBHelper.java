package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.TreeDefectPointBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class TreeDefectDBHelper extends DbManager {
    private TreeDefectDBHelper() {
    }

    public static TreeDefectDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    public void updateList(List<TreeDefectPointBean> list) {
        deleteAllCache();//删除所有记录
        mDaoSession.getTreeDefectPointBeanDao().insertInTx(list);
    }

    public List<TreeDefectPointBean> getAllDefectByLineName(String lineName) {
        QueryBuilder<TreeDefectPointBean> queryBuilder = mDaoSession.getTreeDefectPointBeanDao().queryBuilder().where(
                TreeDefectPointBeanDao.Properties.LineName.eq(lineName)
        );
        return queryBuilder.list();
    }


    public void insert(TreeDefectPointBean treeDefectPointBean) {
        if (treeDefectPointBean==null)
            return;
        QueryBuilder<TreeDefectPointBean> queryBuilder = mDaoSession.getTreeDefectPointBeanDao().queryBuilder().where(TreeDefectPointBeanDao.Properties.SysTreeDefectPointID.eq(treeDefectPointBean.getSysTreeDefectPointID()));
        if (queryBuilder.build().unique() == null)
            mDaoSession.getTreeDefectPointBeanDao().insertOrReplace(treeDefectPointBean);
    }

    public List<TreeDefectPointBean> getDefectByTowerNum(String lineName, String towerNum) {
        QueryBuilder<TreeDefectPointBean> queryBuilder = mDaoSession.getTreeDefectPointBeanDao().queryBuilder().where(
                TreeDefectPointBeanDao.Properties.LineName.eq(lineName)
        );
        List<TreeDefectPointBean> result = new ArrayList<>();
        for (TreeDefectPointBean treeDefectPointBean : queryBuilder.list()) {
            if (treeDefectPointBean.getTowerA_Name().equals(towerNum) || treeDefectPointBean.getTowerB_Name().equals(towerNum)) {
                result.add(treeDefectPointBean);
            }
        }
        return result;
    }

    public TreeDefectPointBean getTreeDefectPointBeanById(int id){
        QueryBuilder<TreeDefectPointBean> queryBuilder = mDaoSession.getTreeDefectPointBeanDao().queryBuilder().where(TreeDefectPointBeanDao.Properties.SysTreeDefectPointID.eq(id));
        return queryBuilder.unique();
    }

    public void delete(int id) {
        QueryBuilder<TreeDefectPointBean> queryBuilder = mDaoSession.getTreeDefectPointBeanDao().queryBuilder().where(
                TreeDefectPointBeanDao.Properties.SysTreeDefectPointID.eq(id)
        );
        queryBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public List<TreeDefectPointBean> getTreeDefectPointBy2(String lineName, String towerNum){
        QueryBuilder<TreeDefectPointBean> queryBuilder = mDaoSession.getTreeDefectPointBeanDao().queryBuilder().where(
                TreeDefectPointBeanDao.Properties.LineName.eq(lineName)
        );
        List<TreeDefectPointBean> result = new ArrayList<>();
        for (TreeDefectPointBean treeDefectPointBean : queryBuilder.list()) {
            if (treeDefectPointBean.getTowerRegion().equals(towerNum)) {
                result.add(treeDefectPointBean);
            }
        }
        return result;
    }

    public void deleteTreePointDefect(TreeDefectPointBean treeDefectPointBean) {
        try {//可能数据库没有这条记录
            if (treeDefectPointBean.getSysTreeDefectPointID() != 0) {
                delete(treeDefectPointBean.getSysTreeDefectPointID().intValue());
            } else {
                mDaoSession.getTreeDefectPointBeanDao().delete(treeDefectPointBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTreePointDefectById(String id){
        TreeDefectPointBean treeDefectPointBean = getTreeDefectPointBeanById(Integer.valueOf(id));
        deleteTreePointDefect(treeDefectPointBean);
    }

    private void deleteAllCache() {
        mDaoSession.getTreeDefectPointBeanDao().deleteAll();
    }

    private static class SingletonHolder {
        private static TreeDefectDBHelper sInstance = new TreeDefectDBHelper();
    }


}
