package com.uflycn.uoperation.ui.fragment.hiddendanger.model;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class ProgressRequestBody extends RequestBody {
    private boolean isEffective = true;
    //实际待包装的请求体
    private final RequestBody requestBody;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            //开始包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        if (!isEffective) {
            bufferedSink.close();
            return;
        }
        //写入
        requestBody.writeTo(bufferedSink);
        if (!isEffective) {
            bufferedSink.close();
            return;
        }
        bufferedSink.flush();
    }

    public void close() {
        isEffective = false;
        if (bufferedSink != null) {
            try {
                bufferedSink.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写入，回调进度接口
     */

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long byteWriteed = 0L;
            //总得字节数
            long contentBytes = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                //                    if(mHandler != null && mListener != null){
                //                        if(contentBytes == 0L){
                //                            contentBytes = contentLength();
                //                        }
                //                        byteWriteed += byteCount;
                //                        mListener.onProgress(byteWriteed, contentBytes, byteWriteed == contentBytes);
                //                    }
            }
        };
    }
}