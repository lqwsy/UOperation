package com.uflycn.uoperation.greendao;

import com.uflycn.uoperation.bean.Document;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by UF_PC on 2017/11/8.
 */
public class DocumentDBHelper extends DbManager {

    private DocumentDBHelper() {
    }

    public static DocumentDBHelper getInstance() {
        return SingletonHolder.sInstance;
    }

    public static class SingletonHolder {
        private static DocumentDBHelper sInstance = new DocumentDBHelper();
    }


    public void insertDocument(final List<Document> documents) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (Document document : documents) {
                    Document doc = mDaoSession.getDocumentDao().queryBuilder().where(DocumentDao.Properties.SysFileInfoId.eq(document.getSysFileInfoId())).unique();
                    if (doc == null)
                        mDaoSession.getDocumentDao().insertOrReplace(document);
                }
            }
        }.start();

    }

    public void updateDocument(Document document) {
        mDaoSession.getDocumentDao().update(document);
    }

    public List<Document> getAllDocument() {
        return mDaoSession.getDocumentDao().queryBuilder().list();
    }

    public List<Document> getDocument(String name) {
        if (name.equalsIgnoreCase("")) {
            return getAllDocument();
        }
        String like = "%" + name + "%";
        QueryBuilder<Document> queryBuilder = mDaoSession.getDocumentDao().queryBuilder().where(DocumentDao.Properties.FileName.like(like));
        return queryBuilder.list();
    }

    public Document getDocument(int sysFileInfoId) {
        return mDaoSession.getDocumentDao().queryBuilder().where(DocumentDao.Properties.SysFileInfoId.eq(sysFileInfoId)).unique();
    }


}
