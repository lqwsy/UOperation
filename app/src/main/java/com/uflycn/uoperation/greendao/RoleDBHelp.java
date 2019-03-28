package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.Role;

import java.util.List;

/**
 * Created by Administrator on 2018/3/29.
 */
public class RoleDBHelp extends DbManager {
    public RoleDBHelp() {
    }
    public static RoleDBHelp getInstance(){
        return SingleTonHolder.sInstance;
    }



    public void clearAll(){
        mDaoSession.getRoleDao().deleteAll();
    }

    public void insertList(List<Role> roles){
        mDaoSession.getRoleDao().insertInTx(roles);
    }

    private static class SingleTonHolder{
        private static RoleDBHelp sInstance = new RoleDBHelp();
    }
}
