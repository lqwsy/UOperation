package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.ImagePaths;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/6.
 */
public class ImagePathsDBHelper extends DbManager {
    private ImagePathsDBHelper() {
    }

    public void insert(List<ImagePaths> imagePaths) {
        mDaoSession.getImagePathsDao().insertInTx(imagePaths);
    }

    public void updateImagePaths(List<ImagePaths> imagePaths) {
        mDaoSession.getImagePathsDao().updateInTx(imagePaths);
    }

    public List<Map<String, Object>> getImagePaths(int Category, String fatherPlatformId, long LocalId) {
        QueryBuilder<ImagePaths> queryBuilder = null;
        if (fatherPlatformId.isEmpty() || fatherPlatformId.equals("0")) {
            queryBuilder = mDaoSession.getImagePathsDao().queryBuilder()
                    .where(ImagePathsDao.Properties.LocalId.eq(LocalId),
                            ImagePathsDao.Properties.Category.eq(Category));
        } else {
            queryBuilder = mDaoSession.getImagePathsDao().queryBuilder()
                    .where(ImagePathsDao.Properties.FatherPlatformId.eq(fatherPlatformId),
                            ImagePathsDao.Properties.Category.eq(Category));
        }
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < queryBuilder.list().size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("path", queryBuilder.list().get(i).getPath());
            data.add(map);
        }
        return data;
    }

    public void deleteImagePaths(List<ImagePaths> imagePaths) {
        mDaoSession.getImagePathsDao().deleteInTx(imagePaths);
    }

    public static ImagePathsDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static ImagePathsDBHelper sInstance = new ImagePathsDBHelper();
    }

}
