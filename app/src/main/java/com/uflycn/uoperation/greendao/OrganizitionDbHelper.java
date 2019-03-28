package com.uflycn.uoperation.greendao;

import android.database.Cursor;

import com.uflycn.uoperation.bean.Organizition;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 * 班组
 */
public class OrganizitionDbHelper extends DbManager{
    public static OrganizitionDbHelper getInstance(){
        return SingletonHolder.sInstance;
    }

    /**
     * 递归遍历本部门及子部门
     */
    public List<String> getSelfAndSubOrg(String id) {
        String sql = "with hgo as( select *,0 as rank from t_Organizition t where t.OrganizationId='"+ id +"' " +
                "union all select h.*,h1.rank+1 from t_Organizition h join hgo h1 on h.Parent_Id=h1.OrganizationId) " +
                "select * from hgo where DELETE_MARK=1";
        Cursor cursor = mDaoSession.getDatabase().rawQuery(sql,null);

        List<String> organizationIdList = new ArrayList<String>();
        while(cursor.moveToNext()) {
            organizationIdList.add(cursor.getString(cursor.getColumnIndex("OrganizationId")));
        }
        return organizationIdList;
    }

    public List<Organizition> findAllTeam(){
//        QueryBuilder<Organizition> queryBuilder = mDaoSession.getOrganizitionDao().queryBuilder().where(
//                OrganizitionDao.Properties.DeleteMark.eq(1),
//                OrganizitionDao.Properties.ParentId.notEq(0)
//        );
        QueryBuilder<Organizition> queryBuilder = mDaoSession.getOrganizitionDao().queryBuilder().where(
                OrganizitionDao.Properties.DeleteMark.eq(1)
        );
        List<Organizition> ids = new ArrayList<>();
        ids.addAll(queryBuilder.list());
//        if (queryBuilder.list() != null || queryBuilder.list().size() !=0){
//            for (Organizition organizition : queryBuilder.list()){
//                ids.add(organizition.getFullName());
//            }
//        }
        return ids;
    }

    public Organizition getOrganizition(String id){
        QueryBuilder<Organizition> queryBuilder = mDaoSession.getOrganizitionDao().queryBuilder().where(OrganizitionDao.Properties.OrganizationId.eq(id));
        return queryBuilder.unique();
    }

    public void insertOrUpdate(Organizition organizition){
        Organizition current = getOrganizition(organizition.getOrganizationId());
        if(current == null){
            mDaoSession.getOrganizitionDao().insert(organizition);
        }else{
            mDaoSession.getOrganizitionDao().update(organizition);
        }
    }

    public List<String> findManagerOrg(String Uid){
        QueryBuilder<Organizition> queryBuilder = mDaoSession.getOrganizitionDao().queryBuilder().whereOr(OrganizitionDao.Properties.Manager.eq(Uid),
                OrganizitionDao.Properties.AssistantManager.eq(Uid));
        List<String> ids = new ArrayList<>();
        if (queryBuilder.list() != null || queryBuilder.list().size() !=0){
            for (Organizition organizition : queryBuilder.list()){
                ids.add(organizition.getOrganizationId());
            }
        }
        return ids;
    }

    public void clearAll(){
        mDaoSession.getOrganizitionDao().deleteAll();
    }

    public void insertList(List<Organizition> organizitions){
        mDaoSession.getOrganizitionDao().insertInTx(organizitions);
    }

    private OrganizitionDbHelper(){}
    private static class SingletonHolder{
        private static OrganizitionDbHelper sInstance = new OrganizitionDbHelper();
    }
}
