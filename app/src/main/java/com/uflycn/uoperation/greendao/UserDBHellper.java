package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.User;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 */
public class UserDBHellper extends DbManager {
    public UserDBHellper() {
    }
    public static UserDBHellper getInstance(){
        return SingleTonHolder.sInstance;
    }

    //根据账户名称查询  账户
    public User getUserByAccount(String account){
        QueryBuilder<User> userQueryBuilder = mDaoSession.getUserDao().queryBuilder().where(UserDao.Properties.Account.eq(account));
        return userQueryBuilder.unique();
    }

    public User getUserByRealName(String realName){
        QueryBuilder<User> userQueryBuilder = mDaoSession.getUserDao().queryBuilder().where(UserDao.Properties.RealName.eq(realName),UserDao.Properties.DeleteMark.notEq(0));
        return userQueryBuilder.unique();
    }
    public List<User> getUserByDepartment(String departmentId){
        QueryBuilder<User> userQueryBuilder = mDaoSession.getUserDao().queryBuilder().where(UserDao.Properties.DepartmentId.eq(departmentId),UserDao.Properties.DeleteMark.notEq(0));
        return userQueryBuilder.list();
    }

    /**判断数据库 是否存在用来验证 登录*/
    public User getUser(String account,String password){
        QueryBuilder<User> userQueryBuilder = mDaoSession.getUserDao().queryBuilder().where(UserDao.Properties.Account.eq(account),UserDao.Properties.Password.eq(password));
        return userQueryBuilder.unique();
    }

    public User getUser(String id){
        QueryBuilder<User> userQueryBuilder = mDaoSession.getUserDao().queryBuilder().where(UserDao.Properties.UserId.eq(id));
        return userQueryBuilder.unique();
    }

    public void insertOrUpdate(User user){
        User current = getUser(user.getUserId());
        if(current == null){
         mDaoSession.getUserDao().insert(user);
        }else{
            mDaoSession.getUserDao().update(user);
        }
    }

    public void clearAll(){
        mDaoSession.getUserDao().deleteAll();
    }

    public void insertList(List<User> users){
        mDaoSession.getUserDao().insertInTx(users);
    }

    private static class SingleTonHolder{
        private static UserDBHellper sInstance = new UserDBHellper();
    }
}
