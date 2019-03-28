package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.RoleMenuData;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2018/3/29.
 */
public class RoleMenuDataDBHelp extends DbManager {
    public RoleMenuDataDBHelp() {
    }

    public static RoleMenuDataDBHelp getInstance() {
        return SingleTonHolder.sInstance;
    }

    public Integer getAccessScopeByRoleId(String RoleId) {
        QueryBuilder<RoleMenuData> queryBuilder = mDaoSession.getRoleMenuDataDao().queryBuilder().where(RoleMenuDataDao.Properties.RoleId.eq(RoleId));
        if (queryBuilder.build().unique() == null) {
            return null;
        } else {
            return queryBuilder.build().unique().getAccessScope();
        }
    }

    public String getOrganizationByRoleId(String RoleId, String menuId) {
        QueryBuilder<RoleMenuData> queryBuilder = mDaoSession.getRoleMenuDataDao().queryBuilder().where(RoleMenuDataDao.Properties.RoleId.eq(RoleId),
                RoleMenuDataDao.Properties.MenuId.eq(menuId),
                RoleMenuDataDao.Properties.AccessScope.eq(8));
        if (queryBuilder.unique() == null)
            return "";
        return queryBuilder.unique().getOrganizations();

    }

    public void clearAll() {
        mDaoSession.getRoleMenuDataDao().deleteAll();
    }

    public void insertList(List<RoleMenuData> roles) {
        mDaoSession.getRoleMenuDataDao().insertInTx(roles);
    }

    private static class SingleTonHolder {
        private static RoleMenuDataDBHelp sInstance = new RoleMenuDataDBHelp();
    }

}
