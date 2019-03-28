package com.uflycn.uoperation.ui.fragment.document;

import com.uflycn.uoperation.bean.Document;

import java.util.List;

/**
 * Created by UF_PC on 2017/11/8.
 */
public interface DocumentListener {

    interface BaseListener{
        void onFailed(String message);
    }


    interface loadDocumentListener extends BaseListener{
        void onSuccess(List<Document> documents);
    }

    interface fileListener extends BaseListener{
        void onOpenFile(Document document,String filePath);

    }
//    void onSuccess(List<Document> documents);
//
//    void onFailed(String message);
//
//    void onOpenFile(Document document,String filePath);
}
