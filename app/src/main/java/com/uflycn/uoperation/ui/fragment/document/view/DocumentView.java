package com.uflycn.uoperation.ui.fragment.document.view;

import com.uflycn.uoperation.bean.Document;

import java.util.List;

/**
 * Created by UF_PC on 2017/11/8.
 */
public interface DocumentView {

    void refeshDate(List<Document> documents);

    void showMessage(String message);

    void showLoading();

    void loadSuccess();

    void openFile(Document document,String filaPath);
}
