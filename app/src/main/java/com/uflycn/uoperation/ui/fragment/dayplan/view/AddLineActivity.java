package com.uflycn.uoperation.ui.fragment.dayplan.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.MyBaseActivity;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.LineBean;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.dayplan.adapter.AddLineAdapter;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 添加线路-线路列表
 */
public class AddLineActivity extends MyBaseActivity {

    public static final String KEY_ADD_LINE_RESULT_LIST = "key_add_line_result_list";
    public static final int ADD_LINE_RESULT = 0x0011;

    @BindView(R.id.sp_voltage_class)
    Spinner spVoltageClass;//电压等级
    @BindView(R.id.et_line_name)
    EditText etLineName;//线路名
    @BindView(R.id.btn_filter)
    Button btnFilter;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    //    @BindView(R.id.btn_select_all)
//    Button btnSelectAll;
    @BindView(R.id.btn_select_add)
    Button btnSelectAdd;
    @BindView(R.id.btn_select_close)
    Button btnSelectClose;
    private Unbinder unbinder;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private boolean isSelectAll = false;
    //筛选条件
    public String voltage = "";
    public String lineName = "";
    private AddLineAdapter mAdapter;
    private List<Integer> selectItems;

    public static Intent newInstance(Context context) {
        return new Intent(context, AddLineActivity.class);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_add_line;
    }

    @Override
    public void initData() {
        super.initData();
        //请求网络
        showRefresh();
        //这里没有用mvp模式
        getLineDataByNet(voltage, lineName);
    }

    private void getLineDataByNet(String voltage, String lineName) {
        RetrofitManager.getInstance()
                .getService(ApiService.class)
                .getLineBeans(voltage, lineName)
                .enqueue(new Callback<BaseCallBack<List<LineBean>>>() {
                    @Override
                    public void onResponse(Call<BaseCallBack<List<LineBean>>> call, Response<BaseCallBack<List<LineBean>>> response) {
                        dismissRefresh();
                        if (response != null && response.body() != null && response.body().getCode() == 1) {
                            List<LineBean> data = response.body().getData();//获取到线路信息
                            refreshLines(data);
                        } else {
                            ToastUtil.show("获取列表失败!");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseCallBack<List<LineBean>>> call, Throwable t) {
                        ToastUtil.show("网络连接失败");
                        dismissRefresh();
                    }
                });
    }

    //<editor-fold desc="刷新线路列表">

    /**
     * 刷新线路列表
     */
    private void refreshLines(List<LineBean> data) {
        if (data != null && data.size() <= 0) {
            mAdapter.setNewData(new ArrayList<LineBean>());
        } else {
            mAdapter.setNewData(data);
        }
    }
    //</editor-fold>

    @Override
    public void initView() {
        super.initView();
        //初始化spinner
        List<ItemDetail> itemDetails = ItemDetailDBHelper.getInstance().getItem("电压等级");
        final List<String> itemStrings = new ArrayList<>();
        itemStrings.add("所有电压");
        for (ItemDetail itemDetail : itemDetails) {
            itemStrings.add(itemDetail.getItemsName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemStrings);
        spVoltageClass.setAdapter(adapter);
        spVoltageClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    voltage = "";
                } else {
                    voltage = itemStrings.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //处理输入框的输入
        etLineName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lineName = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //下拉刷新
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();//网络请求
            }
        });
        //处理Recycler
        recycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AddLineAdapter(R.layout.item_add_lin, new ArrayList<LineBean>(), new CheckBoxChangeListener() {
            @Override
            public void onSelectChanged(LineBean lineBean) {
                handlerSelectOne(lineBean);//单选路线
            }
        });//数据表
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler.setAdapter(mAdapter);
        //将头部的列表名添加道头部
        View headView = View.inflate(this, R.layout.item_add_line_head, null);//标题
        mAdapter.addHeaderView(headView);
        //设置adapter的监听
        selectItems = new ArrayList<>();
        mAdapter.setAddLineListener(new AddLineAdapter.AddLineAdapterListener() {
            @Override
            public void selectLine(LineBean getline) {
                boolean result = false;
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    LineBean lineBean = mAdapter.getData().get(i);
                    if (getline.getSysGridLineId() == lineBean.getSysGridLineId()) {
                        result = getline.isSelect();
                    }
                    lineBean.setSelect(result);
                    result=false;
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.iv_open_close_drawer, R.id.btn_filter, R.id.btn_select_add, R.id.btn_select_close})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_open_close_drawer:
                finish();
                break;
            case R.id.btn_filter:
                handlerFilter();//筛选
                break;
//            case R.id.btn_select_all:
//                isSelectAll = !isSelectAll;
//                handlerSelectAll(isSelectAll);
//                break;
            case R.id.btn_select_add:
                handlerSureAdd();
                break;
            case R.id.btn_select_close:
                finish();
                break;
            default:
                break;
        }
    }

    private void handlerFilter() {
        initData();//网络请求
    }

    /**
     * 确认添加
     */
    private void handlerSureAdd() {
        ArrayList<LineBean> selectLine = mAdapter.getSelectLine();
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(KEY_ADD_LINE_RESULT_LIST, selectLine);
        setResult(ADD_LINE_RESULT, intent);
        finish();
    }

    private void handlerSelectOne(LineBean one) {
        boolean result = false;
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            LineBean lineBean = mAdapter.getData().get(i);
            if (one.getSysGridLineId() == lineBean.getSysGridLineId()) {
                result = one.isSelect();
            }
            lineBean.setSelect(result);
            result = false;
        }
        mAdapter.notifyDataSetChanged();
    }

    public void showRefresh() {
        if (mSwipeRefresh != null && !mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(true);
        }
    }

    public void dismissRefresh() {
        if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    public interface CheckBoxChangeListener {
        void onSelectChanged(LineBean lineBean);
    }

}
