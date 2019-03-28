package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.ProjectEntity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by UF_PC on 2017/10/20.
 */
public class ProjectEntityDbHelper extends DbManager {

    private ProjectEntityDbHelper() {
    }

    public static ProjectEntityDbHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static ProjectEntityDbHelper sInstance = new ProjectEntityDbHelper();
    }


    /**
     * 插入项目管理记录
     *
     * @param projectEntity 项目管理
     */
    public Long insertProjectEntity(ProjectEntity projectEntity) {
        return mDaoSession.getProjectEntityDao().insertOrReplace(projectEntity);
    }

    public void insertFromWsm(ProjectEntity projectEntity) {
        if (projectEntity == null)
            return;
        QueryBuilder<ProjectEntity> queryBuilder = mDaoSession.getProjectEntityDao().queryBuilder().where(ProjectEntityDao.Properties.PlatformId.eq(projectEntity.getPlatformId())
                , ProjectEntityDao.Properties.Deleted.eq(0));
        if (queryBuilder.list().size() == 0)
            mDaoSession.getProjectEntityDao().insertOrReplace(projectEntity);
    }

    /**
     * 更新项目管理数据
     *
     * @param projectEntity
     */
    public void updateProjectStatus(ProjectEntity projectEntity) {
        if (projectEntity.getPlatformId() != 0) {
            List<ProjectEntity> list = mDaoSession.getProjectEntityDao().queryBuilder().where(ProjectEntityDao.Properties.PlatformId.eq(projectEntity.getPlatformId())).list();
            mDaoSession.getProjectEntityDao().deleteInTx(list);
        }
        mDaoSession.getProjectEntityDao().insertOrReplace(projectEntity);
    }


    /**
     * 获取项目管理列表
     *
     * @return
     */
    public List<ProjectEntity> getProjectEntityList() {
        return mDaoSession.getProjectEntityDao().queryBuilder().orderDesc(ProjectEntityDao.Properties.PlatformId).list();
    }

    /**
     * 获取项目管理列表
     *
     * @param projectName 项目名
     * @return
     */
    public List<ProjectEntity> getProjectEntityList(String projectName) {
        if (projectName == null || projectName.equalsIgnoreCase("")) {
            return getProjectEntityList();
        }
        String like = "%" + projectName + "%";
        return mDaoSession.getProjectEntityDao().queryBuilder().where(ProjectEntityDao.Properties.ProjectName.like(like)).orderDesc(ProjectEntityDao.Properties.PlatformId).list();
    }


    /**
     * 更新数据库
     *
     * @param projectEntities
     */
    public void updateProjectEntityList(List<ProjectEntity> projectEntities) {
        //1.删除本地upload = 1 的数据
        List<ProjectEntity> deleteList = mDaoSession.getProjectEntityDao().queryBuilder().where(ProjectEntityDao.Properties.UploadFlag.eq(1)).list();
        mDaoSession.getProjectEntityDao().deleteInTx(deleteList);
        //2.修改项目记录为已上传
        for (ProjectEntity p : projectEntities) {
            p.setUploadFlag(1);

        }
        //3.插入或更新本地数据库
        mDaoSession.getProjectEntityDao().insertOrReplaceInTx(projectEntities);
    }

    public List<ProjectEntity> getNeedUploadList() {
        QueryBuilder<ProjectEntity> queryBuilder = mDaoSession.getProjectEntityDao()
                .queryBuilder().where(ProjectEntityDao.Properties.UploadFlag.eq(0), ProjectEntityDao.Properties.PlatformId.eq(0));
        return queryBuilder.orderDesc(ProjectEntityDao.Properties.PlatformId).list();
    }


    public List<ProjectEntity> getNeedUpdateList() {
        QueryBuilder<ProjectEntity> queryBuilder = mDaoSession.getProjectEntityDao()
                .queryBuilder().where(ProjectEntityDao.Properties.UploadFlag.eq(0), ProjectEntityDao.Properties.PlatformId.notEq(0));
        return queryBuilder.orderDesc(ProjectEntityDao.Properties.PlatformId).list();
    }

    public ProjectEntity getProjectEntityById(int id) {
        return mDaoSession.getProjectEntityDao().queryBuilder().where(ProjectEntityDao.Properties.SysBrokenDocumentId.eq(id)).unique();
    }


    public ProjectEntity getByPlatFormId(int platFormId) {
        return mDaoSession.getProjectEntityDao().queryBuilder().where(ProjectEntityDao.Properties.PlatformId.eq(platFormId)).unique();
    }

    public void delete(ProjectEntity projectEntity) {
        try {//本地可能不存在这个
            mDaoSession.getProjectEntityDao().delete(projectEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
