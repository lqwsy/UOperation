package com.uflycn.uoperation.util;

import android.os.AsyncTask;

import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.greendao.ImagePathsDBHelper;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */
public class SaveImagePathsAsync extends AsyncTask<List<ImagePaths>,Void,Boolean>{
    @Override
    protected Boolean doInBackground(List<ImagePaths>... imagePaths) {
        ImagePathsDBHelper.getInstance().insert(imagePaths[0]);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(!aBoolean){
            ToastUtil.show("提交失败，数据库出错");
        }
    }
}
