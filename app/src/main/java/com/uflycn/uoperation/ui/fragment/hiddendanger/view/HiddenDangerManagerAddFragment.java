package com.uflycn.uoperation.ui.fragment.hiddendanger.view;

import android.content.Intent;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.util.AddPhotoUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 新建隐患fragment
 * 2019.3.21 lqw
 */
public class HiddenDangerManagerAddFragment extends DemoBaseFragment {

    @BindView(R.id.aigv_hdm_add_img)
    AddImageGridView tb_add_img;//图片列表
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private List<Map<String, Object>> datas;
    private final int MAX_IMAGE_LENGTH = 3;//可添加图片数量
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_hd_hdm_add;
    }

    @Override
    protected void initView() {
        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, getActivity());
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        tb_add_img.setAdapter(gridViewAddImgesAdpter);
        tb_add_img.getCodeResult(new AddImageGridView.AddCallBack() {
            @Override
            public void onClickButton(int code) {
                switch (code) {
                    case 1:
                        tempFile = AddPhotoUtils.camera(HiddenDangerManagerAddFragment.this);
                        break;
                    case 2:
                        AddPhotoUtils.gallery(HiddenDangerManagerAddFragment.this, gridViewAddImgesAdpter);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    protected void initData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        List<String> paths = (List<String>) data.getExtras().getSerializable("photos");//path是选择拍照或者图片的地址数组
                        //处理代码
                        for (String path : paths) {
                            datas.add(AddPhotoUtils.photoPath(HiddenDangerManagerAddFragment.this, path));
                            gridViewAddImgesAdpter.notifyDataSetChanged();
                        }
                    }
                    break;
                case PHOTO_REQUEST_CAREMA:
                    // 从相机返回的数据
                    if (AddPhotoUtils.hasSdcard()) {
                        if (tempFile != null) {
                            datas.add(AddPhotoUtils.photoPath(HiddenDangerManagerAddFragment.this, tempFile.getPath()));
                            gridViewAddImgesAdpter.notifyDataSetChanged();
                        } else {
                            ToastUtil.show("相机异常请稍后再试！");
                        }
                    } else {
                        ToastUtil.show("未找到存储卡，无法存储照片！");
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
