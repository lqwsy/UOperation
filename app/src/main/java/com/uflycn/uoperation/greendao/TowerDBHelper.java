package com.uflycn.uoperation.greendao;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.util.LineUtil;
import com.uflycn.uoperation.util.LogUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 */
public class TowerDBHelper extends DbManager {
    private TowerDBHelper() {
    }


    //插入杆塔
    public void insertTower(Tower tower) {
        try {
            mDaoSession.getTowerDao().insert(tower);
        } catch (Exception e) {
            LogUtils.getInstance().e("插入数据库失败" + e.getMessage() + "tower" + tower.getSysTowerID(), 1);
        }
    }


    public void insertOrUpdateTower(Tower tower) {
        try {
            Tower current = getTower(tower.getSysTowerID());
            if (current == null) {
                mDaoSession.getTowerDao().insert(tower);
            } else {
                mDaoSession.getTowerDao().update(tower);
            }
        } catch (Exception e) {
            LogUtils.getInstance().e("插入数据失败" + e.getMessage(), 1);
        }
    }

    public void inserList(List<Tower> towers) {
      /*  for(Tower tower:towers){
            insertOrUpdateTower(tower);
        }*/
        mDaoSession.getTowerDao().insertInTx(towers);
    }


    //根据杆塔 id 获取 杆塔
    public Tower getTower(long towerid) {
        QueryBuilder<Tower> queryBuilder = mDaoSession.getTowerDao().queryBuilder().where(TowerDao.Properties.SysTowerID.eq(towerid), TowerDao.Properties.Deleted.eq(0));
        return queryBuilder.unique();
    }

    //    public Tower getTower(int lineId,String towerName){
    //        QueryBuilder<Tower> queryBuilder = mDaoSession.getTowerDao().queryBuilder().where(TowerDao.Properties.SysGridLineId)
    //    }
    //

    /**
     * 获取线路 的 所有杆塔
     */
    public List<Tower> getLineTowers(int lineid) {
        QueryBuilder<Tower> queryBuilder = mDaoSession.getTowerDao().queryBuilder().where(TowerDao.Properties.SysGridLineId.eq(lineid), TowerDao.Properties.Deleted.eq(0)).orderAsc();
        return queryBuilder.orderAsc(TowerDao.Properties.DisplayOrder).list();
    }

    /**
     * 根据线路id 和 displayorder 获取杆塔
     */
    public Tower getTower(int lineid, int displayOrder) {
        QueryBuilder<Tower> queryBuilder = mDaoSession.getTowerDao().queryBuilder().
                where(TowerDao.Properties.SysGridLineId.eq(lineid), TowerDao.Properties.DisplayOrder.eq(displayOrder), TowerDao.Properties.Deleted.eq(0));
        return queryBuilder.unique();
    }

    /**
     * 根据线路id 和 towerNo 获取杆塔
     */
    public Tower getTower(String Line_id, String Tower_No) {
        QueryBuilder<Tower> queryBuilder = mDaoSession.getTowerDao().queryBuilder().
                where(TowerDao.Properties.SysGridLineId.eq(Line_id), TowerDao.Properties.TowerNo.eq(Tower_No), TowerDao.Properties.Deleted.eq(0));
        return queryBuilder.unique();
    }

    public Tower getFirstTower(int lineId) {
        QueryBuilder<Tower> queryBuilder = mDaoSession.getTowerDao().queryBuilder().
                where(TowerDao.Properties.SysGridLineId.eq(lineId),
                        TowerDao.Properties.Deleted.eq(0));
        List<Tower> towers = queryBuilder.orderAsc(TowerDao.Properties.DisplayOrder).list();
        if (towers.size() == 0) {
            return null;
        }
        return towers.get(0);
    }

    public Tower getEndTower(int lineId) {
        QueryBuilder<Tower> queryBuilder = mDaoSession.getTowerDao().queryBuilder().
                where(TowerDao.Properties.SysGridLineId.eq(lineId),
                        TowerDao.Properties.Deleted.eq(0));
        List<Tower> towers = queryBuilder.orderDesc(TowerDao.Properties.DisplayOrder).list();
        if (towers.size() == 0) {
            return null;
        }
        return towers.get(0);
    }


    private int getTowerId(String lineName, String towerNo) {
        //        QueryBuilder<Tower> queryBuilder = mDaoSession.getTowerDao().queryBuilder().
        //                where(TowerDao.Properties.TowerNo.equals(towerNo),TowerDao.Properties)

        return 0;
    }


    public List<Tower> getTowerInAreaList(double lat, double lon) {
        /**
         * select * from t_GridLine where _id = (
         select distinct SYS_GRID_LINE_ID from t_Tower where ABS(LATITUDE-23) <0.0045 and  ABS(LONGITUDE-113)<0.0048)
         */
        //        long date = System.currentTimeMillis();
        List<String> organizitions = LineUtil.getOrganizitionList();
        String sql;
        //        Log.d("nate", "计算权限:耗时" + (System.currentTimeMillis() - date) + "lat" + lat + "lng" + lon);
        if (organizitions != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < organizitions.size(); i++) {
                sb.append("'").append(organizitions.get(i)).append("'");
                sb.append(",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.lastIndexOf(","));
            }
            sql = "select * from t_Tower where ABS(LATITUDE-" + lat
                    + ") < 0.0009 and  ABS(LONGITUDE-" + lon + ") < 0.00093 and DELETED = 0   and DELETED = 0 " +
                    "and SYS_GRID_LINE_ID in (select distinct _id from t_GridLine where MAINTAIN_CLASS in " +
                    "(" + sb.toString() + "))";
        } else {
            sql = "select * from t_Tower where ABS(LATITUDE-" + lat
                    + ") < 0.0009 and  ABS(LONGITUDE-" + lon + ") < 0.00093 and DELETED = 0   and DELETED = 0 ";
        }
        return getTowers(sql);
    }


    public List<Tower> getTowerBetweenDisplaiOrder(String firstTowerId, String endTowerId, int lineId) {
        String sql = "select * from t_Tower where DISPLAY_ORDER >=" +
                " (select DISPLAY_ORDER from t_Tower where _id = " + firstTowerId + " and SYS_GRID_LINE_ID = " + lineId + ") and DISPLAY_ORDER <= " +
                "(select DISPLAY_ORDER from t_Tower where _id = " + endTowerId + " and SYS_GRID_LINE_ID = " + lineId + ") and SYS_GRID_LINE_ID = " + lineId + " and DELETED = 0";
        return getTowers(sql);
    }


    @NonNull
    private List<Tower> getTowers(String sql) {
        List<Tower> towers = new ArrayList<>();
        Cursor cursor = mDaoSession.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Tower tower = new Tower();
            tower.setSysTowerID(cursor.getLong(cursor.getColumnIndex("_id")));
            tower.setSysGridLineId(cursor.getInt(cursor.getColumnIndex("SYS_GRID_LINE_ID")));
            tower.setAltitude(cursor.getDouble(cursor.getColumnIndex("TOWER_ALTITUDE")));
            tower.setLatitude(cursor.getDouble(cursor.getColumnIndex("LATITUDE")));
            tower.setLongitude(cursor.getDouble(cursor.getColumnIndex("LONGITUDE")));
            tower.setDisplayOrder(cursor.getInt(cursor.getColumnIndex("DISPLAY_ORDER")));
            tower.setHeadAltitude(cursor.getString(cursor.getColumnIndex("HEAD_ALTITUDE")));
            tower.setTowerNo(cursor.getString(cursor.getColumnIndex("TOWER_NO")));
            towers.add(tower);
        }
        cursor.close();
        return towers;
    }

    public List<Tower> getTowerInList(List<Integer> ids) {
        QueryBuilder<Tower> queryBuilder = mDaoSession.getTowerDao().queryBuilder().where(TowerDao.Properties.SysTowerID.in(ids));
        return queryBuilder.list();
    }


    //根据线路 id 和 displayorder 获取杆塔
    public static TowerDBHelper getInstance() {
        return SingeletonHolder.sInstance;
    }

    public void clearAll() {
        mDaoSession.getTowerDao().deleteAll();
    }


    private static class SingeletonHolder {
        private static TowerDBHelper sInstance = new TowerDBHelper();
    }
}
