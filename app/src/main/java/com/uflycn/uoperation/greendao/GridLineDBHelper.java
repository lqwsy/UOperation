package com.uflycn.uoperation.greendao;

import android.util.Log;

import com.esri.android.map.Grid;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.util.LineUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class GridLineDBHelper extends DbManager {
    private GridLineDBHelper() {
    }

    public static GridLineDBHelper getInstance() {
        return SingleTonHolder.sInstance;
    }


    private static class SingleTonHolder {
        private static GridLineDBHelper sInstance = new GridLineDBHelper();
    }

    public void clearAll() {
        mDaoSession.getGridlineDao().deleteAll();
    }

    public void inertList(List<Gridline> lines) {
        for (Gridline detail : lines) {
            try {
                insertOrUpdateGridLine(detail);
            } catch (Exception e) {
                Log.e("insertList", "err" + detail.getLineName());
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 插入一条线路
     */
    public void insertOrUpdateGridLine(Gridline gridline) {
        Gridline current = getLine(gridline.getSysGridLineID());
        if (current == null) {//数据库里面不存在 插入
            mDaoSession.getGridlineDao().insert(gridline);
        } else {//存在 更新
            mDaoSession.getGridlineDao().update(gridline);
        }
    }

    /**
     * 根据线路id 获取一条线路
     */
    public Gridline getLine(long lineid) {
        QueryBuilder<Gridline> queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.SysGridLineID.eq(lineid),
                GridlineDao.Properties.Deleted.eq(0));
        return queryBuilder.unique();
    }

    /**
     * 获取杆塔数量
     *
     * @param lineId
     * @return
     */
    public int getTowerCount(long lineId) {
        return mDaoSession.getGridlineDao().queryBuilder().where(TowerDao.Properties.SysGridLineId.eq(lineId), GridlineDao.Properties.Deleted.eq(0)).unique().getTowerCount();
    }

    /**
     * 获取所有线路
     */
    public List<Gridline> getAllLines() {
        QueryBuilder<Gridline> queryBuilder = null;
        List<String> organizitions = LineUtil.getOrganizitionList();
        if (organizitions == null) {
            queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.Deleted.eq(0));
        } else {
            queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.MaintainClass.in(organizitions),
                    GridlineDao.Properties.Deleted.eq(0));
        }
        return queryBuilder.list();
    }

    /**
     * 获取所有线路 20个一页
     */
    public List<Gridline> getAllLines(int page) {
        QueryBuilder<Gridline> queryBuilder = null;
        List<String> organizitions =  LineUtil.getOrganizitionList();
        if (organizitions == null) {
            queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.Deleted.eq(0));
        } else {
            queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.MaintainClass.in(organizitions),
                    GridlineDao.Properties.Deleted.eq(0));
        }
        return queryBuilder.offset(20 * (page - 1)).limit(20).list();
    }

    /**
     * 根据线路名搜索
     *
     * @param lineName
     * @return
     */
    public Gridline getLine(String lineName) {
        QueryBuilder<Gridline> queryBuilder = null;
        List<String> organizitions =  LineUtil.getOrganizitionList();
        if (organizitions == null) {
            queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.LineName.like(lineName), GridlineDao.Properties.Deleted.eq(0));
        } else {
            queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.LineName.like(lineName),
                    GridlineDao.Properties.MaintainClass.in(organizitions),
                    GridlineDao.Properties.Deleted.eq(0));
        }
        return queryBuilder.list().get(0);
    }

    /**
     * 根据电压等级集合搜索线路
     */
    public List<Gridline> getLineListByVols(List<ItemDetail> Vols) {
        if (Vols.isEmpty()) {
            return getAllLines();
        }
        QueryBuilder<Gridline> queryBuilder = null;
        List<String> organizitions = LineUtil.getOrganizitionList();
        List<Gridline> gridlines = new ArrayList<>();
        for (ItemDetail item : Vols) {
            if (organizitions == null) {
                queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.VoltageClass.eq(item.getItemCode()), GridlineDao.Properties.Deleted.eq(0));
            } else {
                queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.VoltageClass.eq(item.getItemCode()),
                        GridlineDao.Properties.MaintainClass.in(organizitions),
                        GridlineDao.Properties.Deleted.eq(0));
            }
            gridlines.addAll(queryBuilder.list());
        }

        return gridlines;
    }


    /**
     * 根据线路名搜索  20个一页
     *
     * @param lineName
     * @param page     页数
     * @return
     */
    public List<Gridline> getLineList(String lineName, int page) {
        if (lineName.equalsIgnoreCase("")) {
            return getAllLines(page);
        }
        String like = "%" + lineName + "%";
        QueryBuilder<Gridline> queryBuilder = null;
        List<String> organizitions = LineUtil.getOrganizitionList();
        if (organizitions == null) {
            queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.LineName.like(like), GridlineDao.Properties.Deleted.eq(0));
        } else {
            queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.LineName.like(like),
                    GridlineDao.Properties.MaintainClass.in(organizitions),
                    GridlineDao.Properties.Deleted.eq(0));
        }
        return queryBuilder.offset(20 * (page - 1)).limit(20).list();
    }


    /**
     * 根据线路名搜索
     *
     * @param lineName
     * @return
     */
    public List<Gridline> getLineList(String lineName) {
        if (lineName.equalsIgnoreCase("")) {
            return getAllLines();
        }
        String like = "%" + lineName + "%";
        QueryBuilder<Gridline> queryBuilder = null;
        List<String> organizitions = LineUtil.getOrganizitionList();
        if (organizitions == null) {
            queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.LineName.like(like), GridlineDao.Properties.Deleted.eq(0));
        } else {
            queryBuilder = mDaoSession.getGridlineDao().queryBuilder().where(GridlineDao.Properties.LineName.like(like),
                    GridlineDao.Properties.MaintainClass.in(organizitions),
                    GridlineDao.Properties.Deleted.eq(0));
        }
        return queryBuilder.build().list();
    }

}