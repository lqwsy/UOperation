package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.BrokenDocument;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */
public class BrokenDocumentDBHelper extends DbManager {
    private BrokenDocumentDBHelper() {
    }

    //插入多条数据 交跨修改 需要一张新的表 来登记
    public void insertList(List<BrokenDocument> list) {
        mDaoSession.getBrokenDocumentDao().insertInTx(list);
    }

    public List<BrokenDocument> getAllCache() {
        QueryBuilder<BrokenDocument> queryBuilder = mDaoSession.getBrokenDocumentDao().queryBuilder();
        return queryBuilder.list();
    }

    public List<BrokenDocument> getAllByLineNames(List<String> lineNames) {
        List<BrokenDocument> list = new ArrayList<>();
        for (String name : lineNames) {
            list.addAll(getAllListByLineName(name));
        }
        return list;
    }

    public List<BrokenDocument> getAllListByLineName(String lineName) {
        QueryBuilder<BrokenDocument> queryBuilder = mDaoSession.getBrokenDocumentDao().queryBuilder().
                where(BrokenDocumentDao.Properties.LineName.eq(lineName));
        return queryBuilder.list();
    }


    //本地有平台没有的 数据不动，平台有本地没有的插入， 平台有本地有的更新
    public void updateList(List<BrokenDocument> list) {
        deleteInvalidList();//删除无效的数据
        for (BrokenDocument brokenDocument : list) {
            try {
                update(brokenDocument);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public List<BrokenDocument> getUploadList() {
        QueryBuilder<BrokenDocument> queryBuilder = mDaoSession.getBrokenDocumentDao()
                .queryBuilder().where(BrokenDocumentDao.Properties.UploadFlag.eq(0), BrokenDocumentDao.Properties.PlatformId.eq(0));
        return queryBuilder.orderDesc(BrokenDocumentDao.Properties.PlatformId).list();
    }

    //获取需要修改的数据
    public List<BrokenDocument> getNeedUpdateList() {
        QueryBuilder<BrokenDocument> queryBuilder = mDaoSession.getBrokenDocumentDao()
                .queryBuilder().where(BrokenDocumentDao.Properties.UploadFlag.eq(0), BrokenDocumentDao.Properties.PlatformId.notEq(0));
        return queryBuilder.orderDesc(BrokenDocumentDao.Properties.PlatformId).list();
    }

    public List<BrokenDocument> getAllByTowerId(int towerId) {
        QueryBuilder<BrokenDocument> queryBuilder = mDaoSession.getBrokenDocumentDao().queryBuilder().where(BrokenDocumentDao.Properties.StartTowerId.eq(towerId));
        return queryBuilder.orderDesc(BrokenDocumentDao.Properties.PlatformId).list();
    }

    private void deleteInvalidList() {
        List<BrokenDocument> list = mDaoSession.getBrokenDocumentDao().queryBuilder().where(BrokenDocumentDao.Properties.PlatformId.eq(0),
                BrokenDocumentDao.Properties.UploadFlag.eq(1)).list();
        mDaoSession.getBrokenDocumentDao().deleteInTx(list);
    }

    //更新外破列表的时候删除那些 已上传但是 PlatformId =0 的数据
    public void update(BrokenDocument brokenDocument) {
        BrokenDocument brokenDocument1 = getBrokenDocument(brokenDocument.getPlatformId());
        if (brokenDocument1 != null) {
            brokenDocument.setId(brokenDocument1.getId());//增删改查 都是根据这个Id 来
            mDaoSession.getBrokenDocumentDao().update(brokenDocument);
        } else {
            mDaoSession.getBrokenDocumentDao().insert(brokenDocument);
        }
    }


    public void updateById(BrokenDocument brokenDocument) {
        try {
            if (brokenDocument.getPlatformId() != 0) {
                BrokenDocument brokenDocument1 = getBrokenDocument(brokenDocument.getPlatformId());
                if (brokenDocument1 != null) {
                    brokenDocument.setId(brokenDocument1.getId());//增删改查 都是根据这个Id 来
                    mDaoSession.getBrokenDocumentDao().update(brokenDocument);
                } else {
                    mDaoSession.getBrokenDocumentDao().insertOrReplace(brokenDocument);
                }
            } else {

                mDaoSession.getBrokenDocumentDao().insertOrReplace(brokenDocument);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BrokenDocument getBrokenDocument(int platformId) {
        QueryBuilder<BrokenDocument> queryBuilder = mDaoSession.getBrokenDocumentDao().queryBuilder().where(BrokenDocumentDao.Properties.PlatformId.eq(platformId));
        return queryBuilder.unique();
    }

    public Long insertBroken(BrokenDocument brokenDocument) {
        return mDaoSession.getBrokenDocumentDao().insert(brokenDocument);
    }

    public void insertFromWsm(BrokenDocument brokenDocument) {
        if (brokenDocument == null)
            return;
        QueryBuilder<BrokenDocument> queryBuilder = mDaoSession.getBrokenDocumentDao().queryBuilder().where(BrokenDocumentDao.Properties.PlatformId.eq(brokenDocument.getPlatformId()));
        if (queryBuilder.list().size() == 0)
            mDaoSession.getBrokenDocumentDao().insertOrReplace(brokenDocument);
    }


    public void delete(BrokenDocument brokenDocument) {
        try {
            if (brokenDocument.getPlatformId() == 0) {
                mDaoSession.getBrokenDocumentDao().delete(brokenDocument);
            } else {
                BrokenDocument delete = mDaoSession.getBrokenDocumentDao().queryBuilder().where(BrokenDocumentDao.Properties.PlatformId.eq(brokenDocument.getPlatformId())).unique();
                if (delete != null) {
                    mDaoSession.getBrokenDocumentDao().delete(delete);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearAll() {
        mDaoSession.getBrokenDocumentDao().deleteAll();
    }

    public static BrokenDocumentDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static BrokenDocumentDBHelper sInstance = new BrokenDocumentDBHelper();
    }
}
