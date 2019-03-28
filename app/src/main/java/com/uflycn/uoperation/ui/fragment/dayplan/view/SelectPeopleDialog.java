package com.uflycn.uoperation.ui.fragment.dayplan.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.AddDayPlanBean;
import com.uflycn.uoperation.bean.Organizition;
import com.uflycn.uoperation.bean.SelectPeopleBean;
import com.uflycn.uoperation.bean.SelectTower;
import com.uflycn.uoperation.bean.User;
import com.uflycn.uoperation.greendao.OrganizitionDbHelper;
import com.uflycn.uoperation.greendao.UserDBHellper;
import com.uflycn.uoperation.ui.fragment.dayplan.adapter.SelectPeopleDialogAdapter;
import com.uflycn.uoperation.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class SelectPeopleDialog extends Dialog {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_line_name)
    TextView mTvLineName;
    @BindView(R.id.tv_response_class)
    Spinner mSpResponseClass;
    @BindView(R.id.tv_response_people)
    Spinner mSpResponsePeople;

    private SelectTower data;
    private SelectPeopleDialogAdapter mAdapter;
    private List<SelectPeopleBean> mDatas;
    private List<Organizition> teamList;
    private List<List<User>> userList;
    private List<User> currentUsers;
    private User mSelectResponsePeople;
    private AddDayPlanBean mAddDayPlanBean;
    private Organizition mCurrentOrganizition;

    public SelectPeopleDialog(@NonNull Context context) {
        this(context, R.style.dialogWindowAnim);
        init();
    }

    public SelectPeopleDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public void init() {
        setContentView(R.layout.dialog_select_people);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);
        //设置adapter
        mDatas = new ArrayList<>();
        mAdapter = new SelectPeopleDialogAdapter(R.layout.item_close_day_plan_recycler, mDatas);
        View headView = View.inflate(getContext(), R.layout.item_close_day_plan_head, null);
        TextView leftName = (TextView) headView.findViewById(R.id.tv_left_name);
        leftName.setText("姓名");
        mAdapter.addHeaderView(headView);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recycler.setAdapter(mAdapter);
        //获取所有的班组和对应的用户
        //查找 t_Organizition 中的所有数据，可以获取到部门名
        teamList = OrganizitionDbHelper.getInstance().findAllTeam();
        userList = new ArrayList<>();
        //ListView的items
        Iterator<Organizition> iterator = teamList.iterator();
        while (iterator.hasNext()) {
            Organizition organizition = iterator.next();
            //各个部门的人的集合
            List<User> users = new ArrayList<>();
            //通过部门名称获取到部门中的人
            users.addAll(UserDBHellper.getInstance().getUserByDepartment(organizition.getOrganizationId()));
            if (users.size() == 0) {
                iterator.remove();
                continue;
            }
            userList.add(users);
        }
    }

    @OnClick({R.id.dlg_btn_left, R.id.dlg_btn_right})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.dlg_btn_left://关闭巡视
                //修改AddDayPlanBean的获取到的信息
//                if (mAddDayPlanBean == null) {
                    mAddDayPlanBean.setClassId(mCurrentOrganizition.getOrganizationId());
                    mAddDayPlanBean.setClassName(mCurrentOrganizition.getFullName());
                    mAddDayPlanBean.setResponseId(mSelectResponsePeople.getUserId());
                    mAddDayPlanBean.setResponseName(mSelectResponsePeople.getRealName());
                    List<SelectPeopleBean> selectData = getSelectData();
                    List<String> peopleIds = new ArrayList<>();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < selectData.size(); i++) {
                        SelectPeopleBean bean = selectData.get(i);
                        peopleIds.add(bean.getUserId());
                        sb.append(bean.getNum());
                        if (i == selectData.size() - 1) {

                        } else {
                            sb.append(",");
                        }
                    }
                    mAddDayPlanBean.setWorkPeopleIds(peopleIds);
                    mAddDayPlanBean.setWorkPeopleNames(sb.toString());
                    if (listener != null) {
                        listener.close(mAddDayPlanBean);
                    }
//                }
                dismiss();
                break;
            case R.id.dlg_btn_right://取消
                dismiss();
                break;
            default:
                break;
        }
    }

    public void setData(final AddDayPlanBean bean) {
        this.mAddDayPlanBean = bean;
        //将数据设置到对话框中
        mTvLineName.setText(bean.getLineName());

        //负责班组
        List<String> items = new ArrayList<>();
        for (Organizition organizition : teamList) {
            items.add(organizition.getFullName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        mSpResponseClass.setAdapter(adapter);
        mSpResponseClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //先清除以前员工的状态
//                for (User user : currentUsers) {
//
//                }
                //更新班组
                mCurrentOrganizition = teamList.get(position);
                //更新对于的员工
                currentUsers = userList.get(position);
                updateResponseName(bean);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //负责人
        //默认负责人
        if (!StringUtils.isEmptyOrNull(bean.getClassId())) {
            for (int i = 0; i < teamList.size(); i++) {
                if (teamList.get(i).getOrganizationId().equals(bean.getClassId())) {
                    //找到默认选择的班组
                    mSpResponseClass.setSelection(i);
                    //更新班组
                    mCurrentOrganizition = teamList.get(i);
//                    更新对于的员工
                    currentUsers = userList.get(i);
                    updateResponseName(bean);
                }
            }
        } else {
            mCurrentOrganizition = teamList.get(0);
            currentUsers = userList.get(0);
            //更新负责人和工作人员列表
            updateResponseName(bean);
        }
    }

    private void updateResponseName(AddDayPlanBean bean) {
        //更新负责人
        List<String> itemUserNames = new ArrayList<>();
        for (User currentUser : currentUsers) {
            itemUserNames.add(currentUser.getRealName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, itemUserNames);
        mSpResponsePeople.setAdapter(adapter);
        mSpResponsePeople.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mSelectResponsePeople = currentUsers.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //设置选中的默认负责人
        if (!StringUtils.isEmptyOrNull(bean.getResponseId())) {
            for (int i = 0; i < currentUsers.size(); i++) {
                if (currentUsers.get(i).getUserId().equals(bean.getResponseId())) {
                    //找到负责人
                    mSelectResponsePeople = currentUsers.get(i);
                    mSpResponsePeople.setSelection(i);
                }
            }
        } else {
            mSelectResponsePeople = currentUsers.get(0);
        }
        //更新工作人员
        //获取所有的人员
        List<SelectPeopleBean> SelectPeopleBeans = new ArrayList<>();
        List<String> workPeopleIds = bean.getWorkPeopleIds();
        if (workPeopleIds != null && workPeopleIds.size() > 0) {
            for (User currentUser : currentUsers) {
                SelectPeopleBean peopleBean = new SelectPeopleBean();
                peopleBean.setNum(currentUser.getRealName());
                peopleBean.setUserId(currentUser.getUserId());
                peopleBean.setChecked(workPeopleIds.contains(currentUser.getUserId()));
                SelectPeopleBeans.add(peopleBean);
            }
        } else {
            for (User currentUser : currentUsers) {
                SelectPeopleBean peopleBean = new SelectPeopleBean();
                peopleBean.setNum(currentUser.getRealName());
                peopleBean.setUserId(currentUser.getUserId());
//            peopleBean.setChecked(workPeopleIds.contains(currentUser.getUserId()));
                SelectPeopleBeans.add(peopleBean);
            }
        }
        mAdapter.setNewData(SelectPeopleBeans);
        mAdapter.notifyDataSetChanged();
    }

    public List<SelectPeopleBean> getSelectData() {
        final List<SelectPeopleBean> selectTowers = new ArrayList<>();
        List<SelectPeopleBean> data = mAdapter.getData();
        Observable.from(data)
                .filter(new Func1<SelectPeopleBean, Boolean>() {
                    @Override
                    public Boolean call(SelectPeopleBean SelectPeopleBean) {
                        return SelectPeopleBean.isChecked();
                    }
                }).subscribe(new Action1<SelectPeopleBean>() {
            @Override
            public void call(SelectPeopleBean SelectPeopleBean) {
                selectTowers.add(SelectPeopleBean);
            }
        });
        return selectTowers;
    }

    private OnCloseListener listener;

    public interface OnCloseListener {
        void close(AddDayPlanBean bean);
    }

    public void setOnCloseListener(OnCloseListener listener) {
        this.listener = listener;
    }
}
