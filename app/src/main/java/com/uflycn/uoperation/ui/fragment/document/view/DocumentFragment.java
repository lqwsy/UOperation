package com.uflycn.uoperation.ui.fragment.document.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.Document;
import com.uflycn.uoperation.ui.adapter.DocumentAdapter;
import com.uflycn.uoperation.ui.fragment.document.presenter.DocumentPresentImpl;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.ToastUtil;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by UF_PC on 2017/11/3.
 */
public class DocumentFragment extends Fragment implements DocumentView, BaseQuickAdapter.OnRecyclerViewItemClickListener {
    private Reference<Activity> mRef;
    private DocumentPresentImpl mDocumentPresent;
    private DocumentAdapter mDocumentAdapter;
    private List<Document> mDocuments;
    private ProgressDialog mProgressDialog;
    private ProgressDialog loadGrogressDialog;

    @BindView(R.id.lv_document)
    ListView lv_document;

    @BindView(R.id.et_search_line)
    EditText etSearchLine;

    @BindView(R.id.btn_search_line)
    Button btnSearch;

    @BindView(R.id.srl_document)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_document, container, false);
        ButterKnife.bind(this, view);
        mRef = new WeakReference<Activity>(getActivity());
        mDocumentPresent = new DocumentPresentImpl(this);

        mDocuments = new ArrayList<>();
        mDocumentAdapter = new DocumentAdapter(R.layout.item_document, this.getContext(),mDocuments);
        lv_document.setAdapter(mDocumentAdapter);
        lv_document.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDocumentPresent.openOrDownloadFile(mDocumentAdapter.getItem(position));
            }
        });

        initData();
        loadGrogressDialog = new ProgressDialog(mRef.get());
        loadGrogressDialog.setMessage("正在加载文件，请稍等...");
        loadGrogressDialog.setCanceledOnTouchOutside(false);
        loadGrogressDialog.show();

        mProgressDialog = new ProgressDialog(mRef.get());
        mProgressDialog.setMessage("文件正在下载，请稍等...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        return view;
    }

    private void initData() {
        mDocumentPresent.getDocumentList("");
    }

    @OnClick({R.id.iv_open_close_drawer, R.id.btn_search_line})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_close_drawer:
                ((MainActivity) mRef.get()).openLeftMenu();
                break;
            case R.id.btn_search_line:
                mDocumentPresent.getDocumentList(etSearchLine.getText().toString());
                break;
        }
    }


    @Override
    public void onItemClick(View view, int i) {
        mDocumentPresent.openOrDownloadFile(mDocumentAdapter.getItem(i));
    }

    @Override
    public void refeshDate(List<Document> documents) {
        if (mDocumentAdapter != null){
            mDocumentAdapter.onDataChange(documents);
        }
        mDocumentAdapter.notifyDataSetChanged();
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
        loadGrogressDialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        if (lv_document != null){
            mDocumentAdapter.notifyDataSetChanged();
        }
        ToastUtil.show(message);
    }

    @Override
    public void showLoading() {
        if (mProgressDialog != null){
            mProgressDialog.show();
        }

    }

    @Override
    public void loadSuccess() {
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void openFile(Document document, String filaPath) {
        Intent intent = null;
        if (document.getExtensions().equals(".xlsx") || document.getExtensions().equals(".xls")) {
            intent = FileUtils.getExcelFileIntent(filaPath);
        } else if (document.getExtensions().equals(".docx") || document.getExtensions().equals(".doc")) {
            intent = FileUtils.getWordFileIntent(filaPath);
        } else if (document.getExtensions().equals(".pptx") || document.getExtensions().equals(".ppt")) {
            intent = FileUtils.getPptFileIntent(filaPath);
        } else if (document.getExtensions().equals(".png") || document.getExtensions().equals(".jpeg")|| document.getExtensions().equals(".jpg")) {
            intent = FileUtils.getImageFileIntent(filaPath);
        } else if (document.getExtensions().equals(".txt")) {
            intent = FileUtils.getTextFileIntent(filaPath, false);
        } else if (document.getExtensions().equals(".pdf")) {
            intent = FileUtils.getPdfFileIntent(filaPath);
        } else {
            showMessage("文件格式不支持");
        }
        if (intent != null){
//            Intent i = new Intent(this.getActivity(), WebViewActivity.class);
//            startActivity(i);
            try{
                startActivity(intent);
            }catch (Exception e){
                ToastUtil.show("没有应用可以打开此文件");
            }
        }
    }

}
