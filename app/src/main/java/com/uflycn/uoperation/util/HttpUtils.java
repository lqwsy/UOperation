package com.uflycn.uoperation.util;


import android.util.Log;

import com.uflycn.uoperation.bean.BaseEntity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Ryan on 2016/5/18.
 */
public class HttpUtils<T> {


    private Call mCall;
    private final int SUCCESS = 1;
    public static final int NETWORK_ERROR = -1;
    public static final int SERVER_RESPONSE_ERROR = -2;
    private final String TAG = "response";
    private LogUtils mLogUtils = LogUtils.getInstance();

    public HttpUtils(Call call) {
        mCall = call;
    }

    public void handleResponse(final ResponseListener listener) {
        mCall.enqueue(new Callback<BaseEntity<T>>() {
            @Override
            public void onResponse(Call<BaseEntity<T>> call, Response<BaseEntity<T>> response) {
                if (response.isSuccessful() && response.errorBody() == null) {
                    if (response.body().getCode() == SUCCESS) {
                        if (listener != null) {
                            listener.onSuccess((T) response.body().getData());
                        }
                    } else {
                        mLogUtils.e("response code != 1",1);
      //                  Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        if (listener != null) {
                            listener.onFail(response.body().getCode(),response.body().getMessage());
                        }
                    }
                } else {
                    mLogUtils.e("response has error " + response.message(),1);
                    Log.d(TAG, "error code:" + response.code());
                    Log.d(TAG, "error message:" + response.message());

     //               Toast.makeText(mContext, "网络请求返回异常！", Toast.LENGTH_LONG).show();
                    if (listener != null) {
                        listener.onFail(0,"网络请求返回异常！");
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseEntity<T>> call, Throwable t) {
                Log.d(TAG, "error:" + t.getMessage());
          //      mLogUtils.e("response onFailure " + t.getMessage());
         //       Toast.makeText(mContext, "网络请求出现异常！", Toast.LENGTH_LONG).show();
                if (listener != null) {
                    listener.onFail(NETWORK_ERROR,"网络请求出现异常");
                }
            }
        });
    }

    public interface ResponseListener<T> {
        void onSuccess(T t);

        void onFail(int code, String message);
    }


    public interface DownloadListener{
        /**
         * @param total        总字节数
         * @param done         是否完成
         */
        void onProgress(long bytesRead, long total, boolean done);
    }



    public Call handleDownload(final String filePath,final ResponseListener listener){
        mCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    if (listener != null) {
                        listener.onSuccess(null);
                    }

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            IOUtils.writeFile(filePath,response.body().byteStream());
                        }
                    });

                    thread.start();
                //    boolean writtenToDisk = IOUtils.writeFile(filePath,response.body().byteStream());

                    Log.d(TAG, "file download was a success? " + "");
                } else {
                    Log.d(TAG, "server contact failed");
                    if (listener != null) {
                        System.out.println("失败:server contact failed");
                        listener.onFail(SERVER_RESPONSE_ERROR,"server contact failed!");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
                System.out.println("失败:" + t.toString());
                listener.onFail(NETWORK_ERROR,"网络请求出现异常");
            }
        });

        return mCall;
    }

    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
    }


}
