package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.DefectDeleteRecord;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DefectDeleteDBHelper extends DbManager {
    private DefectDeleteDBHelper() {
    }

    public int insert(DefectDeleteRecord defectDeleteRecord) {
        if (!defectDeleteRecord.getSysDefectId().isEmpty()) {
            QueryBuilder<DefectDeleteRecord> queryBuilder = mDaoSession.getDefectDeleteRecordDao().queryBuilder().
                    where(DefectDeleteRecordDao.Properties.SysDefectId.eq(defectDeleteRecord.getSysDefectId()), DefectDeleteRecordDao.Properties.UploadFlag.eq(0));
            DefectDeleteRecord savedRecord = queryBuilder.unique();
            if (savedRecord != null) {
                return -1;
            }
        }
        return (int) mDaoSession.getDefectDeleteRecordDao().insert(defectDeleteRecord);
    }

    public void update(DefectDeleteRecord defectDeleteRecord) {
        mDaoSession.getDefectDeleteRecordDao().update(defectDeleteRecord);
    }

    public void delete(DefectDeleteRecord defectDeleteRecord) {
        mDaoSession.getDefectDeleteRecordDao().delete(defectDeleteRecord);
    }

    public List<DefectDeleteRecord> getUploadList() {
        QueryBuilder<DefectDeleteRecord> queryBuilder = mDaoSession.getDefectDeleteRecordDao().queryBuilder().where(DefectDeleteRecordDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }

    public DefectDeleteRecord getDefectDeleteRecord(int sysDefectId) {
        QueryBuilder<DefectDeleteRecord> queryBuilder = mDaoSession.getDefectDeleteRecordDao().
                queryBuilder().where(DefectDeleteRecordDao.Properties.SysDefectId.eq(sysDefectId));
        return queryBuilder.unique();
    }


    /**
     * 根据本地缺陷id查询 倒叙返回
     *
     * @param localDefectId
     * @return
     */
    public List<DefectDeleteRecord> getDefectDeleteRecordByLocalId(int localDefectId) {
        return mDaoSession.getDefectDeleteRecordDao().queryBuilder().
                where(DefectDeleteRecordDao.Properties.LocalDefectId.eq(localDefectId)).
                orderDesc(DefectDeleteRecordDao.Properties.Id).list();
    }

    public static DefectDeleteDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static DefectDeleteDBHelper sInstance = new DefectDeleteDBHelper();
    }
}
