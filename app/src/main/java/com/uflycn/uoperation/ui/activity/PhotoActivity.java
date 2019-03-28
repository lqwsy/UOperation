package com.uflycn.uoperation.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.util.ToastUtil;
import com.xflyer.utils.ThreadPoolUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PhotoActivity extends Activity {

    private ImageView image;
    private ImageView ivBack;
    private String mDefectPicturePath;
    private PicHandler mHandler;
    private Call<ResponseBody> mGetImageRequest;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo);
        image = (ImageView) findViewById(R.id.iv_image);
        ivBack = (ImageView) findViewById(R.id.iv_back);

        String picturePath = getIntent().getStringExtra("path");

        int defectId = getIntent().getIntExtra("ID", -1);
        String photosPath = getIntent().getStringExtra("photosPath");
        initState();
        mHandler = new PicHandler(this);
        if (picturePath == null) {
            downDefectPicture(photosPath,defectId);
        } else {
            Message message = Message.obtain();
            message.obj = picturePath;
            message.what = 0;
            mHandler.sendMessageDelayed(message, 500);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private void downDefectPicture(String photosPath, final int defectId) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载图片中...");
            mProgressDialog.setCanceledOnTouchOutside(true);
        }
        mProgressDialog.show();


        mDefectPicturePath = photosPath.substring(0,photosPath.lastIndexOf(File.separator));
        File file = new File(mDefectPicturePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        mDefectPicturePath = mDefectPicturePath + File.separator +"pic_" +defectId + ".jpg";
        final File mFile = new File(mDefectPicturePath);
        mGetImageRequest = RetrofitManager.getInstance().getService(ApiService.class).getImageById(defectId);
        mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if(mFile.exists()){
                        mFile.delete();
                    }
                }
                return false;
            }
        });
        mGetImageRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,final Response<ResponseBody> response) {
                if (response != null) {


                        ThreadPoolUtils.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    if(!mFile.exists()){
                                        mFile.createNewFile();
                                    }
                                    InputStream ins = response.body().byteStream();
                                    OutputStream os = new FileOutputStream(mFile);
                                    int bytesRead = 0;
                                    byte[] buffer = new byte[1024];
                                    try{
                                        while ((bytesRead = ins.read(buffer)) != -1) {
                                            os.write(buffer, 0, bytesRead);
                                            os.flush();
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    os.close();
                                    ins.close();
                                    Message message = Message.obtain();
                                    message.obj = mDefectPicturePath;
                                    message.what = 1;
                                    mHandler.sendMessageDelayed(message, 500);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                    if(mFile.exists()){
                                        mFile.delete();
                                    }
                                    image.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                                    ToastUtil.show(getString(R.string.tip_no_picture));
                                } finally {

                                }
                            }
                        });

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if(mFile.exists()){
                    mFile.delete();
                }
                ToastUtil.show(getString(R.string.tip_no_net));
            }
        });
    }


    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setPicImage(String picturePath) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        String path = "file://"+picturePath;
//        ImageSize imageSize = new ImageSize(AppConstant.ScreenWidth,AppConstant.ScreenHeight);
//        ImageLoader.getInstance().displayImage(path, image,imageSize);
        if (!this.isDestroyed()) {
            Glide.with(this).load(path).into(image);
        }else{
            ToastUtil.show("图片加载失败");

        }
    }

    private static class PicHandler extends Handler {
        private Reference<PhotoActivity> mReference;

        public PicHandler(PhotoActivity activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            String path = (String) msg.obj;
            if(path != null){
                mReference.get().setPicImage(path);
            }else{
                ToastUtil.show("图片路径错误");
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Glide.with(this).onDestroy();
        if (mGetImageRequest != null && !mGetImageRequest.isCanceled()) {
            mGetImageRequest.cancel();
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
