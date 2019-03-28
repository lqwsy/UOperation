package com.uflycn.uoperation.ui.fragment.document.model;

import com.uflycn.uoperation.bean.Document;
import com.uflycn.uoperation.ui.fragment.document.DocumentListener;

/**
 * Created by UF_PC on 2017/11/8.
 */
public interface DocumentModel {

   void getDocument(String fileName, DocumentListener.loadDocumentListener listener);

   void openOrDownloadFIle(Document document, String filePath, DocumentListener.fileListener listener);

   void cancel();
}
