package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.UserRole;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2018/3/29.
 */
public class UserRoleDBHelp extends DbManager {
    public UserRoleDBHelp() {
    }
    public static UserRoleDBHelp getInstance(){
        return SingleTonHolder.sInstance;
    }

    public List<UserRole> getRoleIdByUserID(String userId){
        QueryBuilder<UserRole> queryBuilder = mDaoSession.getUserRoleDao().queryBuilder().where(UserRoleDao.Properties.UserId.eq(userId));
        return queryBuilder.build().list();
    }

    public void clearAll(){
        mDaoSession.getUserRoleDao().deleteAll();
    }

    public void insertList(List<UserRole> roles){
        mDaoSession.getUserRoleDao().insertInTx(roles);
    }

    private static class SingleTonHolder{
        private static UserRoleDBHelp sInstance = new UserRoleDBHelp();
    }
}
