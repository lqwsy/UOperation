package com.uflycn.uoperation.ui.fragment.document.presenter;

import com.uflycn.uoperation.bean.Document;

/**
 * Created by UF_PC on 2017/11/8.
 */
public interface DocumentPresent {

    void getDocumentList(String fileName);

    void openOrDownloadFile(Document document);

    void onLoading();

    void loadSuccess();

    void cancel();
}
