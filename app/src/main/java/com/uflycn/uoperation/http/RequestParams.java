package com.uflycn.uoperation.http;

import android.content.ContentValues;

import org.apache.http.NameValuePair;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/9/11.
 */
public class RequestParams {
    public static final MediaType MEDIA_TYPE_FORM = MediaType.parse("multipart/form-data");
    public static final MediaType MEDIA_TYPE_PICTURE = MediaType.parse("image/jpeg");


    public static Map<String, RequestBody> initFormParas(Map<String,String> params,MediaType mediaType){
        Map<String,RequestBody> map = new HashMap<>();
        for(Map.Entry<String,String> entry:params.entrySet()){
            RequestBody startTowerId = RequestBody.create(MEDIA_TYPE_FORM, entry.getValue());
            map.put(entry.getKey(), startTowerId);
        }
        return map;
    }

    public static MultipartBody.Part initPostPiture(String filePath,String key){
        File file = new File(filePath);
        RequestBody imgFile = RequestBody.create(MEDIA_TYPE_PICTURE, file);
        MultipartBody.Part requestImgPart =
                MultipartBody.Part.createFormData("BrokenImage", file.getName(), imgFile);
        return requestImgPart;
    }


}
