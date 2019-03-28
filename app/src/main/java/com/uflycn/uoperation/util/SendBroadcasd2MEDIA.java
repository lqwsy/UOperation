package com.uflycn.uoperation.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2018/3/7.
 */
public class SendBroadcasd2MEDIA {

    /**
     *发送广播  刷新图库
     * @param mContext
     * @param path
     */
    public static void sendBroadcasd(Context mContext,String path){
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
             Intent mediaScanIntent = new Intent(
                     Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
             Uri contentUri = Uri.fromFile(new File(path));
             mediaScanIntent.setData(contentUri);
             mContext.sendBroadcast(mediaScanIntent);

             //MediaScannerConnection.scanFile(mContext, new String[]{path}, null, null);
         } else {

             mContext.sendBroadcast(new Intent(
                     Intent.ACTION_MEDIA_MOUNTED,
                     Uri.parse("file://"
                             + Environment.getExternalStorageDirectory())));
         }
    }

}
