package com.uflycn.uoperation.ui.fragment.manageproject.view;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.eventbus.EditProjectEvent;
import com.uflycn.uoperation.eventbus.JumpEvent;
import com.uflycn.uoperation.eventbus.UpdateProjectListEvent;
import com.uflycn.uoperation.ui.adapter.ProjectListAdapter;
import com.uflycn.uoperation.ui.fragment.manageproject.presenter.ProjectPresenter;
import com.uflycn.uoperation.ui.fragment.manageproject.presenter.ProjectPresenterImpl;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.UIUtils;
import com.uflycn.uoperation.widget.VerticalRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectManageFragment extends Fragment implements ProjectView, TextView.OnEditorActionListener {//项目管理

    private ProjectListAdapter mProjectListAdapter;
    private Reference<Context> mContextRef;
    private ProjectPresenter mProjectPresenter;
    @BindView(R.id.project_list)
    SwipeMenuListView mListView;

    @BindView(R.id.swipe_refresh)
    VerticalRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.et_search_line_name)
    EditText mSearchLineEdit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_project_manage, container, false);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, contentView);
        mContextRef = new WeakReference<Context>(getActivity());
        mProjectPresenter = new ProjectPresenterImpl(this);
        mProjectPresenter.attach();
        initSearch();
        initDatas();
        initListMenuItem();
        initSwipRefreshLis();
        return contentView;
    }

    @OnClick({R.id.btn_create, R.id.iv_back, R.id.img_search_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create:
                EventBus.getDefault().post(new ChangePageEvent(6));
                break;
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.img_search_clear:
                mSearchLineEdit.setText("");
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mProjectPresenter.dettach();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseMainThreadEvent event) {
        if (event instanceof UpdateProjectListEvent) {
            handleUpdateList((UpdateProjectListEvent) event);
            if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    private void updateList(List<ProjectEntity> list) {
        if (mProjectListAdapter == null) {
            mProjectListAdapter = new ProjectListAdapter(list, mContextRef.get(), R.layout.item_project_list);
            mListView.setAdapter(mProjectListAdapter);
        } else {
            mProjectListAdapter.onDataChange(list);
        }

    }

    private void handleUpdateList(UpdateProjectListEvent event) {
        if (event.getMessage() == null || event.getMessage().equalsIgnoreCase("")) {//更新成功
            updateList(event.getProjectEntityList());
        } else {
            ToastUtil.show(event.getMessage());
        }
    }


    private void getProjectList() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        if (MyApplication.registeredTower == null) {
            mProjectPresenter.getAllProject(getLineListIds(), null, null, null);
        } else {
            mProjectPresenter.getAllProject(null, MyApplication.registeredTower.getSysTowerID() + "", null, null);
        }
    }

    private String getLineListIds() {
        StringBuilder sb = new StringBuilder();
        if (MyApplication.registeredTower == null) {
            if (MyApplication.mLineIdNamePairs.size() > 0) {
                for (Map.Entry entry : MyApplication.mLineIdNamePairs.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }


    private void initDatas() {
        getProjectList();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initDatas();
        }
    }


    private void initListMenuItem() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem uploadItem = new SwipeMenuItem(
                        mContextRef.get());
                // set item background
                uploadItem.setBackground(new ColorDrawable(Color.rgb(0xff, 0xcb,
                        0x5b)));
                // set item width
                uploadItem.setWidth(UIUtils.dp2px(90));
                // set a icon
                uploadItem.setTitle("巡视登记");
                uploadItem.setTitleSize(18);
                uploadItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(uploadItem);
                // create "edit" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        mContextRef.get());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0x73, 0xcc,
                        0xf5)));
                // set item width
                editItem.setWidth(UIUtils.dp2px(90));
                // set a icon
                editItem.setTitle("修改");
                editItem.setTitleSize(18);
                editItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(editItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        mContextRef.get());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xff, 0x00,
                        0xff)));
                deleteItem.setWidth(UIUtils.dp2px(90));
                deleteItem.setTitle("巡视记录");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);
        //    绑定左滑和删除事件
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                mListView.smoothCloseMenu();
                switch (index) {
                    case 0:
                        EventBus.getDefault().post(new ChangePageEvent(8));
                        break;
                    case 1:
                        EventBus.getDefault().post(new ChangePageEvent(7));
                        break;
                    case 2:
                        EventBus.getDefault().post(new ChangePageEvent(9));
                        break;
                }
                UIUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new EditProjectEvent(mProjectListAdapter.getItem(position)));
                    }
                }, 100);
                return false;
            }

        });
    }

    public void toActivity(final ProjectEntity mProjectEntity, final int flag){
        EventBus.getDefault().post(new ChangePageEvent(8));
        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new JumpEvent(flag ,mProjectEntity));
            }
        }, 100);
    }


    private void initSwipRefreshLis() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    initDatas();
            }
        });
    }


    private void initSearch() {
        mSearchLineEdit.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // 先隐藏键盘
            ((InputMethodManager) mContextRef.get()
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mSearchLineEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            // 搜索，进行自己要的操作...
            seachList();//这里是我要做的操作！
            return true;
        }
        return false;
    }


    private void seachList() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            //            mSwipeRefreshLayout.post(new Runnable() {
            //                @Override
            //                public void run() {
            mSwipeRefreshLayout.setRefreshing(true);
            //                }
            //            });
        }
        mProjectPresenter.getAllProject(getLineListIds(), null, mSearchLineEdit.getText().toString(), null);
    }

}
