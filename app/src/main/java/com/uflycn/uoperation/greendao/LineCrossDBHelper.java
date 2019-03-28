package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.LineCrossEntity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */
public class LineCrossDBHelper extends DbManager {
    private LineCrossDBHelper() {
    }

    public void clearAll() {
        mDaoSession.getLineCrossEntityDao().deleteAll();
    }

    //插入缓存数据
    public void insertList(List<LineCrossEntity> lineCrossEntities) {
        mDaoSession.getLineCrossEntityDao().insertInTx(lineCrossEntities);
    }

    //获取数据库里面的所有数据
    private List<LineCrossEntity> getAllCache(String lineName) {
        QueryBuilder<LineCrossEntity> queryBuilder = mDaoSession.getLineCrossEntityDao().queryBuilder().where(LineCrossEntityDao.Properties.LineName.eq(lineName));

        return queryBuilder.orderDesc(LineCrossEntityDao.Properties.Id).list();
    }

    public List<LineCrossEntity> getCacheByLine(List<String> lineNames) {
        List<LineCrossEntity> lineCrossEntities = new ArrayList<>();
        for (String linename : lineNames) {
            List<LineCrossEntity> lineCrossEntityList = getAllCache(linename);
            for (LineCrossEntity line : lineCrossEntityList) {
                if (!line.getVoltageClass().contains("kV"))
                    line.setVoltageClass(ItemDetailDBHelper.getInstance().getItem("电压等级", line.getVoltageClass()));
            }
            lineCrossEntities.addAll(lineCrossEntityList);
        }
        return lineCrossEntities;
    }

    //获取需要上传的数据
    public List<LineCrossEntity> getNeedUploadList() {
        QueryBuilder<LineCrossEntity> queryBuilder = mDaoSession.getLineCrossEntityDao().queryBuilder().where(LineCrossEntityDao.Properties.UploadFlag.eq(0),
                LineCrossEntityDao.Properties.PlatformId.eq(0));
        return queryBuilder.orderDesc(LineCrossEntityDao.Properties.PlatformId).list();
    }

    //本地有平台没有的 数据不动，平台有本地没有的插入， 平台有本地有的更新
    public void updateList(List<LineCrossEntity> list) {
        deleteInvalidList();
        for (LineCrossEntity lineCrossEntity : list) {
            try {
                update(lineCrossEntity);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void update(LineCrossEntity lineCrossEntity) {
        LineCrossEntity lineCross = getLineCross(lineCrossEntity.getPlatformId());
        if (lineCross != null) {
            lineCrossEntity.setId(lineCross.getId());
            mDaoSession.getLineCrossEntityDao().update(lineCrossEntity);
        } else {
            mDaoSession.getLineCrossEntityDao().insert(lineCrossEntity);
        }
    }

    public void updateById(LineCrossEntity entity) {
        //        List<LineCrossEntity> crossEntityList = mDaoSession.getLineCrossEntityDao().queryBuilder().where(LineCrossEntityDao.Properties.PlatformId.eq(0),
        //                LineCrossEntityDao.Properties.PlatformId.eq(1)).list();
        //        mDaoSession.getLineCrossEntityDao().deleteInTx(crossEntityList);

        List<LineCrossEntity> list = mDaoSession.getLineCrossEntityDao().queryBuilder().
                where(LineCrossEntityDao.Properties.Id.eq(entity.getId())).list();
        mDaoSession.getLineCrossEntityDao().deleteInTx(list);
        mDaoSession.getLineCrossEntityDao().insertOrReplace(entity);
    }

    public void updateByPlatFormId(LineCrossEntity entity, int upDateFlag) {
        if (entity.getPlatformId() == 0) {
            updateById(entity);
        } else {
            List<LineCrossEntity> list = mDaoSession.getLineCrossEntityDao().queryBuilder().
                    where(LineCrossEntityDao.Properties.PlatformId.eq(entity.getPlatformId())).list();
            mDaoSession.getLineCrossEntityDao().deleteInTx(list);
            entity.setUploadFlag(upDateFlag);
            mDaoSession.getLineCrossEntityDao().insertOrReplace(entity);
        }

    }


    private void deleteInvalidList() {
        QueryBuilder<LineCrossEntity> queryBuilder = mDaoSession.getLineCrossEntityDao().queryBuilder().where(LineCrossEntityDao.Properties.PlatformId.eq(0),
                LineCrossEntityDao.Properties.UploadFlag.eq(1));
        queryBuilder.buildDelete();
    }


    public void insert(LineCrossEntity lineCrossEntity) {
        mDaoSession.getLineCrossEntityDao().insertOrReplace(lineCrossEntity);
    }

    public void insertFromWsm(LineCrossEntity lineCrossEntity) {
        if (lineCrossEntity==null)
            return;
        QueryBuilder<LineCrossEntity> queryBuilder = mDaoSession.getLineCrossEntityDao().queryBuilder().where(LineCrossEntityDao.Properties.PlatformId.eq(lineCrossEntity.getPlatformId()));
        if (queryBuilder.list() == null)
            mDaoSession.getLineCrossEntityDao().insertOrReplace(lineCrossEntity);
    }

    public LineCrossEntity getLineCross(int platformId) {
        QueryBuilder<LineCrossEntity> queryBuilder = mDaoSession.getLineCrossEntityDao().queryBuilder().where(LineCrossEntityDao.Properties.PlatformId.eq(platformId));
        if (queryBuilder.list().size()>1){
            return queryBuilder.list().get(0);
        }else{
            return queryBuilder.unique();
        }
    }

    public List<LineCrossEntity> getLineCrossByTowerId(int towerId, int endTowerId) {
        QueryBuilder<LineCrossEntity> queryBuilder = mDaoSession.getLineCrossEntityDao().queryBuilder().whereOr(LineCrossEntityDao.Properties.StartTowerId.eq(towerId), LineCrossEntityDao.Properties.EndTowerId.eq(endTowerId));
        List<LineCrossEntity> list = queryBuilder.orderDesc(LineCrossEntityDao.Properties.Id).list();
        for (LineCrossEntity entity : list) {
            if (!entity.getVoltageClass().contains("kV"))
                entity.setVoltageClass(ItemDetailDBHelper.getInstance().getItem("电压等级", entity.getVoltageClass()));
        }
        return list;
    }

    public List<LineCrossEntity> getLineCrossByLineName(String lineName) {
        QueryBuilder<LineCrossEntity> queryBuilder = mDaoSession.getLineCrossEntityDao().queryBuilder().where(LineCrossEntityDao.Properties.LineName.eq(lineName));
        List<LineCrossEntity> list = queryBuilder.orderDesc(LineCrossEntityDao.Properties.Id).list();
        return list;
    }

    public List<LineCrossEntity> getTowerCrossByTowerId(String towerId) {
        QueryBuilder<LineCrossEntity> queryBuilder = mDaoSession.getLineCrossEntityDao().queryBuilder().where(LineCrossEntityDao.Properties.StartTowerId.eq(towerId));
        List<LineCrossEntity> list = queryBuilder.orderDesc(LineCrossEntityDao.Properties.Id).list();
        for (LineCrossEntity entity : list) {

        }
        return list;
    }

    /**
     * 更新交跨线路
     *
     * @param lineNames
     * @param crossEntityList
     */
    public void updateLineCross(List<String> lineNames, List<LineCrossEntity> crossEntityList) {
        for (String lineName : lineNames) {
            deleteDateByName(lineName);
        }
        mDaoSession.getLineCrossEntityDao().insertInTx(crossEntityList);
    }

    private void deleteDateByName(String lineName) {
        QueryBuilder<LineCrossEntity> queryBuilder = mDaoSession.getLineCrossEntityDao().queryBuilder().where(LineCrossEntityDao.Properties.LineName.eq(lineName),
                LineCrossEntityDao.Properties.UploadFlag.eq(1));

        mDaoSession.getLineCrossEntityDao().deleteInTx(queryBuilder.list());
    }


    public List<LineCrossEntity> getNeedUpdateList() {
        QueryBuilder<LineCrossEntity> queryBuilder = mDaoSession.getLineCrossEntityDao().queryBuilder().where(LineCrossEntityDao.Properties.UploadFlag.eq(0),
                LineCrossEntityDao.Properties.PlatformId.notEq(0));
        return queryBuilder.orderDesc(LineCrossEntityDao.Properties.PlatformId).list();
    }

    public static LineCrossDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static LineCrossDBHelper sInstance = new LineCrossDBHelper();
    }
}
