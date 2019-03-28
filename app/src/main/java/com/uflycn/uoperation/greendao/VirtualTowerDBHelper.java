package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.VirtualTower;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class VirtualTowerDBHelper extends DbManager {

    public static VirtualTowerDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static VirtualTowerDBHelper sInstance = new VirtualTowerDBHelper();
    }

    public void insertOrUpdate(VirtualTower tower) {
        VirtualTower virtualTower = getVirtualTower(tower.getTowerId());
        Tower towerEx = TowerDBHelper.getInstance().getTower(tower.getTowerId());
        if (towerEx != null) {
            tower.setPreTowerOrder(towerEx.getDisplayOrder() - 1);
            tower.setNextTowerOrder(towerEx.getDisplayOrder() + 1);
            tower.setDisplayOrder(towerEx.getDisplayOrder());
            tower.setLineId(towerEx.getSysGridLineId());
            tower.setTowerName(towerEx.getTowerNo() + "");

        }
        if (virtualTower == null) {
            mDaoSession.getVirtualTowerDao().insert(tower);
        } else {
            virtualTower.setLatitude(tower.getLatitude());
            virtualTower.setLongitude(tower.getLongitude());
            virtualTower.setAltitude(tower.getAltitude());
            virtualTower.setPreTowerOrder(tower.getPreTowerOrder());
            virtualTower.setNextTowerOrder(tower.getNextTowerOrder());
            virtualTower.setTowerName(tower.getTowerName());
            virtualTower.setLineId(tower.getLineId());
            virtualTower.setDisplayOrder(tower.getDisplayOrder());
            virtualTower.setStatus(tower.getStatus());

            mDaoSession.getVirtualTowerDao().update(virtualTower);
        }
    }

    public VirtualTower getVirtualTower(long towerId) {
        QueryBuilder<VirtualTower> builder = mDaoSession.getVirtualTowerDao().queryBuilder().where(VirtualTowerDao.Properties.TowerId.eq(towerId));
        return builder.unique();
    }

    public VirtualTower getVirtualTower(int lineId, int displayOrder) {
        QueryBuilder<VirtualTower> builder = mDaoSession.getVirtualTowerDao().queryBuilder()
                .where(VirtualTowerDao.Properties.LineId.eq(lineId), VirtualTowerDao.Properties.DisplayOrder.eq(displayOrder));
        return builder.unique();
    }

    public List<VirtualTower> getVirtualTowers(int lineId) {
        QueryBuilder<VirtualTower> builder = mDaoSession.getVirtualTowerDao().queryBuilder().where(VirtualTowerDao.Properties.LineId.eq(lineId));
        return builder.orderAsc(VirtualTowerDao.Properties.DisplayOrder).list();
    }

    public void delete(VirtualTower tower) {
        mDaoSession.getVirtualTowerDao().delete(tower);
    }

}
