package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.ui.adapter.PhotosGridAdapter;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.LogUtils;
import com.uflycn.uoperation.util.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/7.
 */
public class PhotoGridActivity extends Activity implements AdapterView.OnItemClickListener {

    @BindView(R.id.gv_photos)
    GridView gv_photos;

    private PhotosGridAdapter photosGridAdapter;
    private String zipPath;
    private String photosPath;
    private List<String> zipFileNames = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_grid);
        ButterKnife.bind(this);
        initState();
        initData();
    }
    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void initData() {
        try {
            //压缩包的存放路径
            zipPath = getIntent().getStringExtra("path");
            File zioFile = new File(zipPath);
            if (zioFile.exists()) {
                photosPath = zipPath.substring(0, zipPath.lastIndexOf("."));
                //解压 ， 获取压缩包内部文件的名称List。 加上路径名放入adapter
                ZipUtils.upZipFile(zioFile, photosPath);
                zipFileNames = ZipUtils.getEntriesNames(zioFile);

                FileUtils.delFile(zipPath);
            } else {
                photosPath = zipPath.substring(0, zipPath.lastIndexOf("."));
                zipFileNames = ZipUtils.GetFileNames(photosPath);
            }
//            if (zipFileNames.size() > 0) {

                photosGridAdapter = new PhotosGridAdapter(zipFileNames, photosPath, this);
                gv_photos.setAdapter(photosGridAdapter);
//            }
        } catch (IOException e) {
            LogUtils.getInstance().e(e.getMessage(),1);
            e.printStackTrace();
        }
        gv_photos.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击图片时  判断图片是否已经存在，如果不存在 就进行下载，如果文件存在就直接打开
        Intent intent = new Intent(this, PhotoActivity.class);
        //大图的保存路径
        String path = photosPath.substring(0, photosPath.lastIndexOf(File.separator)) + File.separator + "pic_" + zipFileNames.get(position);
        File pic = new File(path);
        if (pic.exists()) {
            //图片的路径
            intent.putExtra("path", path);
        } else {
            int ID = Integer.valueOf(zipFileNames.get(position).substring(0, zipFileNames.get(position).lastIndexOf(".")));
            //图片的保存文件夹路径
            intent.putExtra("photosPath", photosPath);
            intent.putExtra("ID", ID);
        }
        startActivity(intent);
    }

    @OnClick({R.id.iv_back})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }


}
