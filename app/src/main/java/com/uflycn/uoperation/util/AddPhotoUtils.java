package com.uflycn.uoperation.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AddPhotoUtils {

    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final

    /**
     * 拍照
     */
    public static File camera(Fragment fragment) {
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            String IMAGE_DIR = IOUtils.getRootStoragePath(fragment.getActivity()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "交跨";
            File dir = new File(IMAGE_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String fileName = IMAGE_DIR + File.separator + System.currentTimeMillis() + ".jpg";
            File tempFile = new File(fileName);
            //从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            Intent intent = new Intent();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            fragment.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
            return tempFile;
        } else {
            ToastUtil.show("未找到存储卡，无法拍照！");
        }
        return null;
    }

    /**
     * 拍照
     */
    public static File camera(Activity activity) {
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            String IMAGE_DIR = IOUtils.getRootStoragePath(activity) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "交跨";
            File dir = new File(IMAGE_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String fileName = IMAGE_DIR + File.separator + System.currentTimeMillis() + ".jpg";
            File tempFile = new File(fileName);
            //从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            Intent intent = new Intent();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            activity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
            return tempFile;
        } else {
            ToastUtil.show("未找到存储卡，无法拍照！");
        }
        return null;
    }

    /**
     * 判断sdcard是否被挂载
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 从相册获取2
     */
    public static void gallery(Fragment fragment, GridViewAddImgesAdpter gridViewAddImgesAdpter) {
        Intent intent = new Intent(fragment.getActivity(), PhotoSelectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        int nowNeed = gridViewAddImgesAdpter.getMaxImages() - gridViewAddImgesAdpter.getCount() + 1;
        intent.putExtra("limit", nowNeed);//number是选择图片的数量
        fragment.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 对于文件进行剪切。展示
     *
     * @param path
     */
    public static Map<String,Object> photoPath(Fragment fragment,String path) {
        String IMAGE_DIR = IOUtils.getRootStoragePath(fragment.getActivity()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "树障";
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = IMAGE_DIR + File.separator + System.currentTimeMillis() + ".jpg";
        FileUtils.changeBitmapPath(path, fileName);
        SendBroadcasd2MEDIA.sendBroadcasd(fragment.getActivity(), IMAGE_DIR);
        Map<String, Object> map = new HashMap<>();
        map.put("path", fileName);
        return map;
    }
    /**
     * 从相册获取2
     */
    public static void gallery(Activity activity, GridViewAddImgesAdpter gridViewAddImgesAdpter) {
        Intent intent = new Intent(activity, PhotoSelectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        int nowNeed = gridViewAddImgesAdpter.getMaxImages() - gridViewAddImgesAdpter.getCount() + 1;
        intent.putExtra("limit", nowNeed);//number是选择图片的数量
        activity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 对于文件进行剪切。展示
     *
     * @param path
     */
    public static Map<String,Object> photoPath(Activity activity,String path) {
        String IMAGE_DIR = IOUtils.getRootStoragePath(activity) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "树障";
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = IMAGE_DIR + File.separator + System.currentTimeMillis() + ".jpg";
        FileUtils.changeBitmapPath(path, fileName);
        SendBroadcasd2MEDIA.sendBroadcasd(activity, IMAGE_DIR);
        Map<String, Object> map = new HashMap<>();
        map.put("path", fileName);
        return map;
    }
}
