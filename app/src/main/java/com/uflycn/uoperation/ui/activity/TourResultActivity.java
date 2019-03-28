package com.uflycn.uoperation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.ui.fragment.assistRecord.view.AssistRecordFragment;
import com.uflycn.uoperation.ui.fragment.breakdocument.BreakDocumentFragment;
import com.uflycn.uoperation.ui.fragment.checkresult.CrossRegisterFragment;
import com.uflycn.uoperation.ui.fragment.checkresult.CrossclearFragment;
import com.uflycn.uoperation.ui.fragment.checkresult.EditCrossFragment;
import com.uflycn.uoperation.ui.fragment.checkresult.RegisterDefectFragment;
import com.uflycn.uoperation.ui.fragment.checkresult.TourResultFragment;
import com.uflycn.uoperation.ui.fragment.cleardefectlist.view.ClearDefectListFragment;
import com.uflycn.uoperation.ui.fragment.manageproject.view.CreateProjectFragment;
import com.uflycn.uoperation.ui.fragment.manageproject.view.EditProjectFragment;
import com.uflycn.uoperation.ui.fragment.manageproject.view.InspectRecordFragment;
import com.uflycn.uoperation.ui.fragment.manageproject.view.ProjectManageFragment;
import com.uflycn.uoperation.ui.fragment.manageproject.view.ProjectRegisterFragment;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class TourResultActivity extends FragmentActivity {

    private RadioGroup mRadioGroup;
    private Fragment previousFragment;
    public ArrayList<Fragment> mFragmentList;
    private int position;
    private int mIntExtra;
    private int mTowerId;

    private ProjectEntity projectEntity;
    private LineCrossEntity lineCrossEntity;
    private int FLAG = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_tour_result);

        mIntExtra = getIntent().getIntExtra(AppConstant.POSITION, 0);
        mTowerId = getIntent().getIntExtra(AppConstant.TOWER_ID_KEY, -1);
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mRadioGroup.setVisibility(View.GONE);
        initContent();
        EventBus.getDefault().register(this);

        Object object = getIntent().getSerializableExtra("OptProject");
        int flag = getIntent().getIntExtra("flag", -1);
        FLAG = getIntent().getIntExtra("FLAG", -1);
        if (object instanceof ProjectEntity) {
            projectEntity = (ProjectEntity) object;
            ((ProjectManageFragment) mFragmentList.get(1)).toActivity(projectEntity, FLAG);
        } else if (object instanceof LineCrossEntity) {
            lineCrossEntity = (LineCrossEntity) object;
            if (flag == 0) {
                changeToEditCross(lineCrossEntity, FLAG);
            } else if (flag == 1) {
                changeToDeleteCross(lineCrossEntity, FLAG);
            } else {
                ToastUtil.show("未知错误");
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (FLAG == 1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("toValue", AppConstant.WORK_SHEET);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void initContent() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new TourResultFragment());    //巡视结果主界面 0
        mFragmentList.add(new ProjectManageFragment()); //项目管理主界面 1
        mFragmentList.add(new BreakDocumentFragment()); //外破管理主界面 2
        mFragmentList.add(new CrossRegisterFragment()); //交跨登记      3
        mFragmentList.add(new EditCrossFragment());     //修改交跨      4
        mFragmentList.add(new RegisterDefectFragment());//缺陷登记      5
        mFragmentList.add(new CreateProjectFragment()); //创建项目      6
        mFragmentList.add(new EditProjectFragment());   //修改项目      7
        mFragmentList.add(new ProjectRegisterFragment());//项目登记     8
        mFragmentList.add(new InspectRecordFragment());//项目特训       9
        mFragmentList.add(new CrossclearFragment());   //消除交跨       10
        mFragmentList.add(new ClearDefectListFragment());//清障工单     11
        mFragmentList.add(new AssistRecordFragment());//辅助记录     12
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_tour_result:
                        position = 0;
                        break;
                    case R.id.rb_dis_defect:
                        position = 1;
                        break;
                    case R.id.rb_break_document:
                        position = 2;
                        break;
                    case R.id.rb_defect_list:
                        position = 11;
                        break;
//                    case R.id.rb_assist_record:
//                        position = 12;
//                        break;
                }

                Fragment currentFragment = mFragmentList.get(position);
                switchFragment(previousFragment, currentFragment);
            }
        });
        //
        if (mIntExtra == 0) {
            mRadioGroup.check(R.id.rb_tour_result);
        } else if (mIntExtra == 1) {
            mRadioGroup.check(R.id.rb_dis_defect);
        } else if (mIntExtra == 2) {
            mRadioGroup.check(R.id.rb_break_document);
        } else if (mIntExtra == 11) {
            mRadioGroup.check(R.id.rb_defect_list);
        }
//        else if (mIntExtra == 12) {
//            mRadioGroup.check(R.id.rb_assist_record);
//        }

    }

    /**
     * 切换fragment
     *
     * @param from
     * @param to
     */
    public void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            previousFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    transaction.hide(from);
                }
                if (to != null) {
                    transaction.add(R.id.fl_content, to).commit();
                }
            } else {
                if (from != null) {
                    transaction.hide(from);
                }
                if (to != null) {
                    transaction.show(to).commit();
                }
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("ActionBar", "OnKey事件");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (previousFragment instanceof EditCrossFragment) {
                EditCrossFragment.onKeyDown(keyCode, event);
            } else if (previousFragment instanceof CrossclearFragment) {
                CrossclearFragment.onKeyDown(keyCode, event);
            } else if (previousFragment instanceof ProjectManageFragment
                    || previousFragment instanceof BreakDocumentFragment
                    || previousFragment instanceof TourResultFragment
                    || previousFragment instanceof ClearDefectListFragment) {
                finish();
            } else if (previousFragment instanceof CreateProjectFragment
                    || previousFragment instanceof EditProjectFragment
                    || previousFragment instanceof ProjectRegisterFragment
                    || previousFragment instanceof InspectRecordFragment) {
                EventBus.getDefault().post(new ChangePageEvent(1));
            } else if (previousFragment instanceof RegisterDefectFragment
                    || previousFragment instanceof CrossRegisterFragment) {
                EventBus.getDefault().post(new ChangePageEvent(0));
            }
        }

        //        if(keyCode == KeyEvent.KEYCODE_BACK){
        //            if(previousFragment instanceof CrossRegisterFragment){
        //                Fragment currentFragment = mFragmentList.get(0);
        //                switchFragment(previousFragment, currentFragment);
        //                return true;
        //            }else if(previousFragment instanceof EditCrossFragment) {
        //                Fragment currentFragment = mFragmentList.get(0);
        //                switchFragment(previousFragment, currentFragment);
        //                return true;
        //            }
        //        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(BaseMainThreadEvent event) {
        if (event instanceof ChangePageEvent) {
            Fragment currentFragment = mFragmentList.get(((ChangePageEvent) event).getChangeToPos());
            switchFragment(previousFragment, currentFragment);
        }
    }

    public void changeToEditCross(LineCrossEntity entity, int flag_jump) {
        switchFragment(previousFragment, mFragmentList.get(4));
        ((EditCrossFragment) mFragmentList.get(4)).setCurrentLineCrossEntity(entity, flag_jump);
    }

    public void changeToDeleteCross(LineCrossEntity entity, int flag_jump) {
        switchFragment(previousFragment, mFragmentList.get(10));
        ((CrossclearFragment) mFragmentList.get(10)).setCurrentLineCrossEntity(entity, flag_jump);
    }

    public int getTowerId() {
        return mTowerId;
    }
}
