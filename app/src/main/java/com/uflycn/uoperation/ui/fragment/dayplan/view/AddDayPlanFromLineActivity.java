package com.uflycn.uoperation.ui.fragment.dayplan.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.AddDayPlanFronLineBean;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.JobContent;
import com.uflycn.uoperation.bean.LineBean;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.dayplan.adapter.AddDayPlanFromLineAdapter;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 从线路中添加
 */
public class AddDayPlanFromLineActivity extends Activity {

    public static final int REQUEST_CODE = 0x0013;
    public static final String KEY_RESULT = "key_result";
    public static final int RESULT_CODE = 0x0021;
    private Unbinder unbinder;
    @BindView(R.id.sp_work_type)
    Spinner mSpWorkType;
    @BindView(R.id.tv_work_content)
    TextView mTvWorkContent;
    private ProgressDialog mProgressDialog;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.btn_save)
    Button mBtnSave;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    private AddDayPlanFromLineAdapter mAdapter;
    ArrayList<AddDayPlanFronLineBean> mDatas;//线路
    //当前的缺陷类型和工作内容
    private List<JobContent> mJobContents;//工作内容的集合
    private ItemDetail mCurrentItemDetail;//当前的工作类型字典表

    /**
     * 实例化AddDayPlanActivity
     */
    public static Intent newInstance(Context context) {
        return new Intent(context, AddDayPlanFromLineActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_day_plan_from_line);
        unbinder = ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        //加载工作类型
        //工作内容集合
        mJobContents = new ArrayList<>();
        final List<ItemDetail> itemDetails = ItemDetailDBHelper.getInstance().getItem("工作类型");
        List<String> jobTypes = new ArrayList<>();
        for (ItemDetail itemDetail : itemDetails) {
            jobTypes.add(itemDetail.getItemsName());
        }
        ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, jobTypes);
        mSpWorkType.setAdapter(jobTypeAdapter);
        if (mCurrentItemDetail==null){
            mCurrentItemDetail = itemDetails.get(0);//设置默认的
            mSpWorkType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long position) {
                    mCurrentItemDetail = itemDetails.get(i);
                    mJobContents.clear();
                    mTvWorkContent.setText("");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        //初始化recycler
        mDatas = new ArrayList<>();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AddDayPlanFromLineAdapter(R.layout.item_add_day_plan_from_line, mDatas);
        View headView = View.inflate(this, R.layout.item_add_day_plan_from_line_head, null);
        mAdapter.addHeaderView(headView);
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter.setOnSelectOrRemoveListener(new AddDayPlanFromLineAdapter.OnSelectOrRemoveListener() {
            @Override
            public void onSelect(AddDayPlanFronLineBean bean) {
                handleSelect(bean);
            }

            @Override
            public void remove(AddDayPlanFronLineBean bean) {
                //移除当前的item
                mDatas.remove(bean);
                mAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * 处理选择事件
     */
    private void handleSelect(final AddDayPlanFronLineBean bean) {
        //所有塔tower，里面有为选中的和选中的
        final List<Tower> resultTowers = new ArrayList<>();
        //数据库中所有的tower，都是未选中的
        final List<Tower> towers = TowerDBHelper.getInstance().getLineTowers(bean.getSysGridLineId());
        //bean中被选中的tower
        final List<Tower> selectTower = bean.getTowers();
        //代表着第一次线路中返回过来
        if (selectTower == null || selectTower.size() == 0) {
            Observable.from(towers)
                    .subscribe(new Action1<Tower>() {
                        @Override
                        public void call(Tower tower) {
                            resultTowers.add(tower);
                        }
                    });
        } else {
            //如果选中的塔中有数据
            Observable.from(towers)
                    .map(new Func1<Tower, Tower>() {
                        @Override
                        public Tower call(Tower tower) {
                            if (isContain(selectTower, tower)) {
                                tower.setChecked(true);
                            } else {
                                tower.setChecked(false);
                            }
                            return tower;
                        }
                    }).subscribe(new Action1<Tower>() {
                @Override
                public void call(Tower tower) {
                    resultTowers.add(tower);
                }
            });
        }
        final FromLineTowerNumsDialog dialog = new FromLineTowerNumsDialog(this);
        dialog.setData(resultTowers, bean.getLineName());//由线路选择塔号弹窗
        dialog.show();
        //监听对话框的确定事件
        dialog.setOnSureTowerListener(new FromLineTowerNumsDialog.OnSureTowerNumsListener() {
            @Override
            public void onSureTowerNums(List<Tower> selectDatas) {
                //获取选中的塔
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < selectDatas.size(); i++) {
                    Tower tower = selectDatas.get(i);
                    sb.append(tower.getTowerNo());
                    if (i == selectDatas.size() - 1) {
                    } else {
                        sb.append(",");
                    }
                }
                //更新显示的towernos
                bean.setTourNums(sb.toString());
                //更新选中的towers
                bean.setTowers(selectDatas);
                //刷新页面
                mAdapter.notifyDataSetChanged();
                //消失对话框
                dialog.dismiss();
            }
        });
    }

    private boolean isContain(List<Tower> selectTower, Tower tower) {
        boolean isContain = false;
        for (Tower tower1 : selectTower) {
            if (tower1.getTowerNo().equals(tower.getTowerNo())) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    /**
     * 通过工作类型id获取工作内容
     */
    private void getJobContentFromNet(String jobTypeId) {
        showDialog("加载中...");
        RetrofitManager.getInstance().getService(ApiService.class)
                .getJobContent(AppConstant.TOKEN, jobTypeId)
                .enqueue(new Callback<BaseCallBack<List<JobContent>>>() {
                    @Override
                    public void onResponse(Call<BaseCallBack<List<JobContent>>> call, Response<BaseCallBack<List<JobContent>>> response) {
                        missDialog();
                        if (response != null && response.body() != null && response.body().getCode() == 1) {
                            refreshJobContent(response.body().getData());
                        } else {
                            ToastUtil.show("获取列表失败!");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseCallBack<List<JobContent>>> call, Throwable t) {
                        ToastUtil.show("网络连接失败!");
                        missDialog();
                    }
                });
    }

    /**
     * 刷新对于的工作内容
     */
    private void refreshJobContent(final List<JobContent> data) {
        if (data != null && data.size() > 0) {
            final List<JobContent> resultJobContent = new ArrayList<>();
            String[] jobContentStrs = new String[data.size()];
            boolean[] choessItems = new boolean[data.size()];
            for (int i = 0; i < data.size(); i++) {
                JobContent jobContent = data.get(i);
                if (mJobContents.size() > 0) {
                    if (mJobContents.contains(jobContent)) {
                        choessItems[i] = true;
                    } else {
                        choessItems[i] = false;
                    }
                } else {
                    choessItems[i] = false;
                }
                jobContentStrs[i] = jobContent.getWorkContent();
            }
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("选择工作内容")
                    .setMultiChoiceItems(jobContentStrs, choessItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                            if (isChecked) {
                                if (!resultJobContent.contains(data.get(i))) {
                                    //如果不包含
                                    resultJobContent.add(data.get(i));
                                }
                            } else {
                                if (resultJobContent.contains(data.get(i))) {
                                    //如果包含
                                    resultJobContent.remove(data.get(i));
                                }
                            }
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int pos) {
                            //设置 工作内容的值
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < resultJobContent.size(); i++) {
                                sb.append(resultJobContent.get(i).getWorkContent());
                                if (i != resultJobContent.size() - 1) {
                                    sb.append(",");
                                }
                            }
                            mTvWorkContent.setText(sb.toString());
                            //更新当前的值
                            mJobContents.clear();
                            for (JobContent jobContent : resultJobContent) {
                                mJobContents.add(jobContent);
                            }
                            //关闭对话框
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            dialog.show();
        }
    }

    @OnClick({R.id.iv_open_close_drawer, R.id.btn_select_line,
            R.id.btn_save, R.id.btn_cancel,
            R.id.btn_select_job_content})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_open_close_drawer:
                finish();
                break;
            case R.id.btn_select_line:
                handleAddLine();
                break;
            case R.id.btn_save:
                handleSaveLine();//保存
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_select_job_content:
                handleSelectJobContent();//工作内容选择
                break;
            default:
                break;
        }
    }

    /**
     * 选中工作内容点击事件
     */
    private void handleSelectJobContent() {
        //联网获取到数据
        getJobContentFromNet(mCurrentItemDetail.getItemDetailsId());
    }

    /**
     * 保存这些线路
     */
    private void handleSaveLine() {
        //将当前的集合中的数据全部添加工作类型和工作内容
        for (AddDayPlanFronLineBean data : mDatas) {
            if (data.getTourNums() == null || data.getTowers() == null || data.getTowers().size() == 0) {
                ToastUtil.show("巡视塔号不能为空!");
                return;
            }
            if (mJobContents.size() == 0) {
                ToastUtil.show("工作内容不能为空!");
                return;
            }
            //将工作类型和工作内容设置到集合中
            data.setTypeOfWork(mCurrentItemDetail);
            data.setJobContents(mJobContents);
        }
        //将页面关闭并将数据返回
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(KEY_RESULT, mDatas);
        setResult(RESULT_CODE, intent);
        finish();
    }

    /**
     * 添加线路
     */
    private void handleAddLine() {
        Intent intent = AddLineActivity.newInstance(this);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == AddLineActivity.ADD_LINE_RESULT) {
            //从AddLineActivity中获取到添加的线路
            ArrayList<LineBean> listData = data.getParcelableArrayListExtra(AddLineActivity.KEY_ADD_LINE_RESULT_LIST);
            //将数据封装到列表中
            for (LineBean lineBean : listData) {
                AddDayPlanFronLineBean bean = new AddDayPlanFronLineBean();
                bean.setDangerDefectCount(lineBean.getDangerDefectCount());
                bean.setInspectTowerIds(lineBean.getInspectTowerIds());
                bean.setInspectTowerNos(lineBean.getInspectTowerNos());
                bean.setLineName(lineBean.getLineName());
                bean.setMaintainClass(lineBean.getMaintainClass());
                bean.setMaintainClassName(lineBean.getMaintainClassName());
                bean.setNormalDefectCount(lineBean.getNormalDefectCount());
                bean.setSeriousDefectCount(lineBean.getSeriousDefectCount());
                bean.setSelectMonth(lineBean.getSelectMonth());
                bean.setSysGridLineId(lineBean.getSysGridLineId());
                bean.setVoltageClass(lineBean.getVoltageClass());
                mDatas.add(bean);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public void showDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
        } else {
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }
    }

    public void missDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }
}
