package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.BrokenInspectRecord;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class BrokenInspectRecordDBHelper extends DbManager {
    private BrokenInspectRecordDBHelper() {
    }

    public static BrokenInspectRecordDBHelper getInstance() {
        return SingletonHodler.sInstance;
    }


    public long insert(BrokenInspectRecord record) {
        return mDaoSession.getBrokenInspectRecordDao().insert(record);
    }


    public void insertCacheList(List<BrokenInspectRecord> list) {
        deleteInvalidList();//删除所有已上传但是 Platformid = 0 的数据
        for (BrokenInspectRecord record : list) {
            try {
                insertOrUpdate(record);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void deleteInvalidList() {
        QueryBuilder<BrokenInspectRecord> queryBuilder = mDaoSession.getBrokenInspectRecordDao().queryBuilder().where(
                BrokenInspectRecordDao.Properties.SysBrokenPatrolDetailId.eq(0), BrokenInspectRecordDao.Properties.UploadFlag.eq(1)
        );
        queryBuilder.buildDelete();
       /* List<BrokenInspectRecord> list = queryBuilder.list();
        for (BrokenInspectRecord record : list) {
            mDaoSession.getBrokenInspectRecordDao().delete(record);
        }*/
    }


    private void insertOrUpdate(BrokenInspectRecord record) {
        BrokenInspectRecord origin = getInspectRecord(record.getSysBrokenPatrolDetailId());
        if (origin != null) {
            record.setSysBrokenInspectRecordId(origin.getSysBrokenInspectRecordId());
            mDaoSession.getBrokenInspectRecordDao().update(record);
        } else {
            mDaoSession.getBrokenInspectRecordDao().insert(record);
        }
    }

    public void update(BrokenInspectRecord record) {
        mDaoSession.getBrokenInspectRecordDao().update(record);
    }

    public void updateList(List<BrokenInspectRecord> list) {
        mDaoSession.getBrokenInspectRecordDao().updateInTx(list);
    }

    public BrokenInspectRecord getInspectRecord(int platFormId) {
        QueryBuilder<BrokenInspectRecord> recordQueryBuilder = mDaoSession.getBrokenInspectRecordDao().
                queryBuilder().where(BrokenInspectRecordDao.Properties.SysBrokenPatrolDetailId.eq(platFormId));
        return recordQueryBuilder.unique();

    }


    //根据外破的 平台ID 来获取
    private List<BrokenInspectRecord> getAllCacheByDocumentPlatformId(int brokenDocId) {
        //先查数据库中 DocumentPlatformId
        QueryBuilder<BrokenInspectRecord> recordQueryBuilder = mDaoSession.getBrokenInspectRecordDao().
                queryBuilder().where(BrokenInspectRecordDao.Properties.DocumentPlatformId.eq(brokenDocId));
        return recordQueryBuilder.list();
    }

    //根据外破的 本地 ID 来获取 平台ID为0 的时候
    private List<BrokenInspectRecord> getAllCacheByDocumentID(int brokenDocId) {
        //先查数据库中 DocumentPlatformId
        QueryBuilder<BrokenInspectRecord> recordQueryBuilder = mDaoSession.getBrokenInspectRecordDao().
                queryBuilder().where(BrokenInspectRecordDao.Properties.BrokenDocumentId.eq(brokenDocId));
        return recordQueryBuilder.list();
    }


    //根据brokendocument 的 id 来查询相关的特巡
    public List<BrokenInspectRecord> getAllCacheById(int brokenDocId) {
        List<BrokenInspectRecord> plat = getAllCacheByDocumentPlatformId(brokenDocId);
        if (plat == null || plat.size() == 0) {
            return getAllCacheByDocumentID(brokenDocId);
        }
        return plat;
    }

    public List<BrokenInspectRecord> getNeedUploadList() {
        QueryBuilder<BrokenInspectRecord> recordQueryBuilder = mDaoSession.getBrokenInspectRecordDao().
                queryBuilder().where(BrokenInspectRecordDao.Properties.UploadFlag.eq(0));
        return recordQueryBuilder.orderDesc(BrokenInspectRecordDao.Properties.DocumentPlatformId).list();
    }

    public void deleteAllList(){
        QueryBuilder<BrokenInspectRecord> recordQueryBuilder = mDaoSession.getBrokenInspectRecordDao().
                queryBuilder().where(BrokenInspectRecordDao.Properties.UploadFlag.eq(1));
        mDaoSession.getBrokenInspectRecordDao().deleteInTx(recordQueryBuilder.list());
    }

    private static class SingletonHodler {
        private static BrokenInspectRecordDBHelper sInstance = new BrokenInspectRecordDBHelper();
    }
}
