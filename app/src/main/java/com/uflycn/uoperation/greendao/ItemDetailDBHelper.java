package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.ItemDetail;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 * 数据库操作类
 */
public class ItemDetailDBHelper extends DbManager {
    private ItemDetailDBHelper() {
    }


    public ItemDetail getItemDetail(String itemId) {
        QueryBuilder<ItemDetail> queryBuilder = mDaoSession.getItemDetailDao().queryBuilder().where(ItemDetailDao.Properties.ItemDetailsId.eq(itemId),
                ItemDetailDao.Properties.DeleteMark.eq(0));
        return queryBuilder.unique();
    }

    public ItemDetail getItemDetailByItemsName(String itemsName) {
        QueryBuilder<ItemDetail> queryBuilder = mDaoSession.getItemDetailDao().queryBuilder().where(ItemDetailDao.Properties.ItemsName.eq(itemsName),
                ItemDetailDao.Properties.DeleteMark.eq(0));
        return queryBuilder.unique();
    }

    public ItemDetail getTourItemDetail(String itemsName) {
        QueryBuilder<ItemDetail> queryBuilder = mDaoSession.getItemDetailDao().queryBuilder().where(ItemDetailDao.Properties.ItemsName.eq(itemsName),
                ItemDetailDao.Properties.DeleteMark.eq(0),
                ItemDetailDao.Properties.ItemName.eq("巡视性质"));
        return queryBuilder.unique();
    }

    public ItemDetail getHiddenType(String itemHiddenType){
        QueryBuilder<ItemDetail> queryBuilder=mDaoSession.getItemDetailDao().queryBuilder().where(ItemDetailDao.Properties.ItemsName.eq(itemHiddenType),
                ItemDetailDao.Properties.DeleteMark.eq(0),
                ItemDetailDao.Properties.ItemName.eq("棚膜隐患类型"));
        return queryBuilder.unique();
    }

    /**
     * 根据电压得到相别
     *
     * @param itemsName
     * @param itemValue
     * @return
     */
    public List<ItemDetail> getTourPhaseTypemDetail(String itemsName, String itemValue) {
        QueryBuilder<ItemDetail> queryBuilder = mDaoSession.getItemDetailDao().queryBuilder().where(
                ItemDetailDao.Properties.DeleteMark.eq(0),
                ItemDetailDao.Properties.ItemName.eq(itemsName)
                , ItemDetailDao.Properties.ItemValue.eq(itemValue));
        return queryBuilder.list();

    }

    public List<ItemDetail> getItem(String itemName) {
        QueryBuilder<ItemDetail> queryBuilder = mDaoSession.getItemDetailDao().queryBuilder().where(
                ItemDetailDao.Properties.ItemName.eq(itemName),
                ItemDetailDao.Properties.DeleteMark.eq(0));
        return queryBuilder.orderDesc(ItemDetailDao.Properties.ParentId).list();
    }

    public void insertOrupdate(ItemDetail itemDetail) {
        ItemDetail current = getItemDetail(itemDetail.getItemDetailsId());
        if (current == null) {
            mDaoSession.getItemDetailDao().insert(itemDetail);
        } else {
            mDaoSession.getItemDetailDao().update(itemDetail);
        }
    }


    public void insertList(List<ItemDetail> itemDetails) {
        mDaoSession.getItemDetailDao().insertInTx(itemDetails);//要么一个都不完成 要么都完成
    }


    /**
     * 根据parentid 获取 子类list
     */
    public List<ItemDetail> getChilds(String parentId) {
        QueryBuilder<ItemDetail> queryBuilder = mDaoSession.getItemDetailDao().queryBuilder().where(ItemDetailDao.Properties.ParentId.eq(parentId));
        return queryBuilder.orderDesc(ItemDetailDao.Properties.ParentId).list();
    }


    public String getItem(String itemName, String itemCode) {
        ItemDetail itemDetail = mDaoSession.getItemDetailDao().queryBuilder().
                where(ItemDetailDao.Properties.ItemName.eq(itemName),
                        ItemDetailDao.Properties.ItemCode.eq(itemCode)).unique();
        if (itemDetail == null) {
            return "";
        }
        return itemDetail.getItemsName();
    }

    public ItemDetail getItemByCode(String itemName, String itemCode) {
        return mDaoSession.getItemDetailDao().queryBuilder().
                where(ItemDetailDao.Properties.ItemName.eq(itemName),
                        ItemDetailDao.Properties.ItemCode.eq(itemCode)).unique();
    }

    public void clearAll() {
        mDaoSession.getItemDetailDao().deleteAll();
    }

    public static ItemDetailDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static ItemDetailDBHelper sInstance = new ItemDetailDBHelper();
    }
}
