package com.uflycn.uoperation.ui.fragment.document.presenter;

import com.uflycn.uoperation.bean.Document;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.ui.fragment.document.DocumentListener;
import com.uflycn.uoperation.ui.fragment.document.model.DocumentModelImpl;
import com.uflycn.uoperation.ui.fragment.document.view.DocumentFragment;
import com.uflycn.uoperation.util.IOUtils;

import java.io.File;
import java.util.List;

/**
 * Created by UF_PC on 2017/11/8.
 */
public class DocumentPresentImpl implements DocumentPresent {

    private DocumentFragment mDocumentFragment;
    private DocumentModelImpl mDocumentModel;

    public DocumentPresentImpl(DocumentFragment documentFragment) {
        mDocumentFragment = documentFragment;
        mDocumentModel = new DocumentModelImpl();
    }


    @Override
    public void getDocumentList(String fileName) {
        mDocumentModel.getDocument(fileName, new DocumentListener.loadDocumentListener() {
            @Override
            public void onFailed(String message) {
                loadSuccess();
                mDocumentFragment.showMessage(message);
            }

            @Override
            public void onSuccess(List<Document> documents) {
                loadSuccess();
                mDocumentFragment.refeshDate(documents);
            }
        });
    }

    @Override
    public void openOrDownloadFile(Document document) {
        onLoading();
        mDocumentModel.openOrDownloadFIle(document, getFilePath(), new DocumentListener.fileListener() {

            @Override
            public void onFailed(String message) {
                mDocumentFragment.loadSuccess();
                mDocumentFragment.showMessage(message);
            }

            @Override
            public void onOpenFile(Document document, String filePath) {
                mDocumentFragment.loadSuccess();
                mDocumentFragment.openFile(document,filePath);
            }
        });
    }

    @Override
    public void onLoading() {
        mDocumentFragment.showLoading();
    }

    @Override
    public void loadSuccess() {
        mDocumentFragment.loadSuccess();
    }

    @Override
    public void cancel() {
        mDocumentModel.cancel();
    }


    private String getFilePath() {
        String picturePath = IOUtils.getRootStoragePath(mDocumentFragment.getContext()) +  AppConstant.DOCUMENT_PATH;
        File file = new File(picturePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return picturePath;
    }
}
