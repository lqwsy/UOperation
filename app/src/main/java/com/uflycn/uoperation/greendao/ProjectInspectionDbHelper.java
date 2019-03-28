package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.ProjectInspection;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by UF_PC on 2017/10/20.
 */
public class ProjectInspectionDbHelper extends DbManager {

    private ProjectInspectionDbHelper() {
    }

    public static ProjectInspectionDbHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static ProjectInspectionDbHelper sInstance = new ProjectInspectionDbHelper();
    }


    /**
     * 插入巡视登记记录
     *
     * @param projectInspection 项目管理
     */
    public Long insertProjectInspection(ProjectInspection projectInspection) {
        return mDaoSession.getProjectInspectionDao().insert(projectInspection);
    }


    /**
     * 更新巡视登记记录数据
     *
     * @param projectInspection
     */
    public void updateProjectInspection(ProjectInspection projectInspection) {
        mDaoSession.getProjectInspectionDao().update(projectInspection);
    }


    /**
     * 更新巡视登记表记录
     *
     * @param projectInspections
     */
    public void updateProjectInspection(List<ProjectInspection> projectInspections) {
        //1、获取数据库中已上传的数据集合
        List<ProjectInspection> projectInspectionList = mDaoSession.getProjectInspectionDao().queryBuilder().where(ProjectInspectionDao.Properties.UploadFlag.eq(1)).list();
        //2、删除本地已上传数据
        mDaoSession.getProjectInspectionDao().deleteInTx(projectInspectionList);
        //3、插入平台上传回来的数据
        mDaoSession.getProjectInspectionDao().insertOrReplaceInTx(projectInspections);
    }

    /**
     * 获取巡视记录
     */
    public List<ProjectInspection> getProjectInspectionById(int projectId) {
        return mDaoSession.getProjectInspectionDao().queryBuilder().where(ProjectInspectionDao.Properties.LocalProjectId.eq(projectId)).list();
    }

    /**
     * 根据平台Id获取巡视记录
     */
    public List<ProjectInspection> getProjectInspectionByPlatformId(int platFormId) {
        return mDaoSession.getProjectInspectionDao().queryBuilder().where(ProjectInspectionDao.Properties.SysProjectId.eq(platFormId)).list();
    }

    public List<ProjectInspection> getNeedUploadList() {
        QueryBuilder<ProjectInspection> queryBuilder = mDaoSession.getProjectInspectionDao().queryBuilder().
                where(ProjectInspectionDao.Properties.UploadFlag.eq(0));
        return queryBuilder.list();
    }

    public void updateList(List<ProjectInspection> projectInspections) {
        mDaoSession.getProjectInspectionDao().updateInTx(projectInspections);
    }
}
