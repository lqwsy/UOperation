package com.uflycn.uoperation.ui.fragment.document.model;

import android.util.Log;

import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.Document;
import com.uflycn.uoperation.greendao.DocumentDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.document.DocumentListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by UF_PC on 2017/11/8.
 */
public class DocumentModelImpl implements DocumentModel {

    private Call<BaseCallBack<List<Document>>> mGetDocument;
    private Call<ResponseBody> mDown;

    @Override
    public void getDocument(final String fileName, final DocumentListener.loadDocumentListener listener) {

        mGetDocument = RetrofitManager.getInstance().getService(ApiService.class).getDocument(fileName);
        mGetDocument.enqueue(new Callback<BaseCallBack<List<Document>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<Document>>> call, Response<BaseCallBack<List<Document>>> response) {
                if (response == null || response.body() == null) {
                    listener.onFailed("网络请求出错");
                    listener.onSuccess(DocumentDBHelper.getInstance().getDocument(fileName));
                    return;
                }
                if (response.body().getCode() == 1) {
                    DocumentDBHelper.getInstance().insertDocument(response.body().getData());
                    listener.onSuccess(response.body().getData());
                } else {
                    listener.onSuccess(DocumentDBHelper.getInstance().getDocument(fileName));
                    listener.onFailed(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<Document>>> call, Throwable t) {
                if (t != null) {
                    Log.e("requestErr", "getDocument" + t.getMessage());
                    listener.onFailed(t.getMessage());

                }
                listener.onSuccess(DocumentDBHelper.getInstance().getDocument(fileName));

            }
        });
    }

    @Override
    public void openOrDownloadFIle(Document document, String filePath, DocumentListener.fileListener listener) {
        Document doc = DocumentDBHelper.getInstance().getDocument(document.getSysFileInfoId());
        if (doc != null && doc.getFilePath() != null) {
            File file = new File(doc.getFilePath());
            if (file.exists()) {
                //文件存在
                if (file.length() != Integer.valueOf(doc.getFileSize())) {
                    file.delete();
                    doc.setFilePath("");
                    DocumentDBHelper.getInstance().updateDocument(doc);
                    download(doc, filePath, listener);
                }
                listener.onOpenFile(doc, doc.getFilePath());
                //                openFile(document,filePath);
            } else {
                doc.setFilePath("");
                DocumentDBHelper.getInstance().updateDocument(doc);
                download(doc, filePath, listener);
            }
        } else {
            download(document, filePath, listener);
        }
    }

    @Override
    public void cancel() {
        mDown.cancel();
    }


    private void download(final Document document, final String filePath, final DocumentListener.fileListener listener) {
        mDown = RetrofitManager.getInstance().getService(ApiService.class).getFile(document.getSysFileInfoId());
        mDown.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response != null && response.body() != null) {
                    Observable.create(new Observable.OnSubscribe<String>() {

                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            String filepath = filePath + "/" + document.getFileName() + document.getExtensions();
                            File file1 = new File(filepath);
                            if (!file1.exists()) {
                                try {
                                    file1.createNewFile();
                                    inputstreamtofile(response.body().byteStream(), file1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    subscriber.onError(e);
                                }
                            }
                            subscriber.onNext(filepath);
                            subscriber.onCompleted();
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<String>() {
                                @Override
                                public void onCompleted() {
                                    //openFile(document, filePath);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e("err", "onError: " + e.getMessage().substring(0,34));
                                    String ErrorInfo = "No Activity found to handle Intent";//没有可以打开文件的应用
                                    String filepath = filePath + "/" + document.getFileName() + document.getExtensions();
                                    File file1 = new File(filepath);
                                    if (file1.exists()&&!ErrorInfo.equals(e.getMessage().substring(0,34))) {
                                        file1.delete();
                                        listener.onFailed("下载文件出错");
                                    }else if(ErrorInfo.equals(e.getMessage().substring(0,34))){
                                        listener.onFailed("打开文件出错");
                                    }else{
                                        file1.delete();
                                        listener.onFailed("未知文件出错");
                                    }
                                }

                                @Override
                                public void onNext(String s) {
                                    document.setFilePath(s);
                                    DocumentDBHelper.getInstance().updateDocument(document);
                                    listener.onOpenFile(document, s);

                                }
                            });

                }else {
                    listener.onFailed("下载文件出错");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t != null) {
                    listener.onFailed(t.getMessage());
                }
            }
        });

        //        Subscription subscription = RxDownload.getInstance()
        //                .maxThread(10)     //设置最大线程
        //                .maxRetryCount(10) //设置下载失败重试次数
        //                .retrofit(RetrofitManager.getInstance().getRetrofit())//若需要自己的retrofit客户端,可在这里指定
        //                .download(ContantUrl.BASE_URL + "/Common/DownloadFile", document.getFileName() + document.getExtensions(), filePath)
        //                .subscribeOn(Schedulers.io())
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe(new Subscriber<DownloadStatus>() {
        //                    @Override
        //                    public void onCompleted() {
        //                        //下载完成
        //                    }
        //
        //                    @Override
        //                    public void onError(Throwable e) {
        //                        //下载出错
        //                    }
        //
        //                    @Override
        //                    public void onNext(final DownloadStatus status) {
        //                        //下载状态
        //
        //                    }
        //                });
    }


    public void inputstreamtofile(InputStream ins, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        try {
            while ((bytesRead = ins.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        os.close();
        ins.close();
    }

}

