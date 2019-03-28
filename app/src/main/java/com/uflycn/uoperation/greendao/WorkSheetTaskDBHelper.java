package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.WorkSheetTask;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by UF_PC on 2017/11/3.
 */
public class WorkSheetTaskDBHelper extends DbManager {

    public WorkSheetTaskDBHelper() {
    }

    public static WorkSheetTaskDBHelper getInstance() {
        return SingleTonHolder.sInstance;
    }

    private static class SingleTonHolder {
        private static WorkSheetTaskDBHelper sInstance = new WorkSheetTaskDBHelper();
    }

    /**
     * 查询工单编号 如果不存在 则返回-1
     *
     * @return
     */
    public int queryWorkSheetTaskId(int BusinessId, int BusinessType) {
        QueryBuilder<WorkSheetTask> queryBuilder = mDaoSession.getWorkSheetTaskDao().queryBuilder().where(WorkSheetTaskDao.Properties.BusinessId.eq(BusinessId),
                WorkSheetTaskDao.Properties.BusinessType.eq(BusinessType),
                WorkSheetTaskDao.Properties.Deleted.eq(0));
        List<WorkSheetTask> workSheetTasks = queryBuilder.list();
        if (workSheetTasks == null||workSheetTasks.size() == 0){
            return -1;
        }else{
            return Integer.valueOf(workSheetTasks.get(0).getSysTaskId()+"");
        }
    }

    public int queryWorkSheetTaskId(String BusinessId, int BusinessType) {
        QueryBuilder<WorkSheetTask> queryBuilder = mDaoSession.getWorkSheetTaskDao().queryBuilder().where(WorkSheetTaskDao.Properties.BusinessId.eq(BusinessId),
                WorkSheetTaskDao.Properties.BusinessType.le(BusinessType),
                WorkSheetTaskDao.Properties.Deleted.eq(0));
        List<WorkSheetTask> workSheetTasks = queryBuilder.list();
        if (workSheetTasks == null||workSheetTasks.size() == 0){
            return -1;
        }else{
            return Integer.valueOf(workSheetTasks.get(0).getSysTaskId()+"");
        }
    }

    public WorkSheetTask queryWorkSheet(int id){
        QueryBuilder<WorkSheetTask> queryBuilder = mDaoSession.getWorkSheetTaskDao().queryBuilder().where(WorkSheetTaskDao.Properties.SysTaskId.eq(id));
        return queryBuilder.build().unique();
    }


    public void insertWorkSheetTask(WorkSheetTask workSheetTask) {
        mDaoSession.getWorkSheetTaskDao().insert(workSheetTask);
    }

    public void updateWorkSheetTask(WorkSheetTask workSheetTask) {
        mDaoSession.getWorkSheetTaskDao().updateInTx(workSheetTask);
    }

    public void insertAll(List<WorkSheetTask> list) {
        WorkSheetTask workSheetTask;
        for (int i = 0; i < list.size(); i++) {
            workSheetTask = mDaoSession.getWorkSheetTaskDao().queryBuilder().where(WorkSheetTaskDao.Properties.SysTaskId.eq(list.get(i).getSysTaskId()))
                    .build().unique();
            if (workSheetTask == null || workSheetTask.getStatus() != 2) {
                mDaoSession.insertOrReplace(list.get(i));
            }
        }
    }

    public void deleteAll() {
        mDaoSession.getWorkSheetTaskDao().deleteAll();
    }

    public void delete(WorkSheetTask workSheetTask) {
        mDaoSession.getWorkSheetTaskDao().delete(workSheetTask);
    }

    public void delete(int workSheetTaskid) {
        delete(queryWorkSheet(workSheetTaskid));
    }

    public void update(WorkSheetTask workSheetTask) {
        mDaoSession.getWorkSheetTaskDao().update(workSheetTask);
    }


    public List<WorkSheetTask> getUploadList() {
        QueryBuilder<WorkSheetTask> queryBuilder = mDaoSession.getWorkSheetTaskDao().queryBuilder().where(WorkSheetTaskDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }

}
