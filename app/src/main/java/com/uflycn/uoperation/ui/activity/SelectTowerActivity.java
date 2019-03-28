package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.SelectTowerEvent;
import com.uflycn.uoperation.widget.SeatTable;
import com.uflycn.uoperation.widget.SeatTowerView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectTowerActivity extends Activity {//进入选择杆塔的页面

    @BindView(R.id.seat_tower_view)
    SeatTowerView seatTowerViewView;
    @BindView(R.id.seat_table_view)
    SeatTable mSeatTable;

    int insulator;
    int jumpCount;
    /**
     * 跳线类型 0->单 1->双
     */
    int jumpType;
    private int[][] selectList;
    private Map<String, int[]> mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_tower);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int towerType = intent.getIntExtra(AppConstant.TOWER_TYPE, 0);
        int towerSubType = intent.getIntExtra(AppConstant.TOWER_SUB_TYPE, 0);

        insulator = intent.getIntExtra(AppConstant.INSULATOR_COUNT, 18);
        jumpCount = intent.getIntExtra(AppConstant.JUMP_COUNT, 18);
        jumpType = intent.getIntExtra(AppConstant.JUMP_TYPE, 1);

        Bundle bundle = intent.getExtras();
        if (bundle.getSerializable(AppConstant.SELECT_TOWER) != null) {
            if (towerType == 0) {
                //直线塔
                selectList = (int[][]) bundle.getSerializable(AppConstant.SELECT_TOWER);
            } else {
                mMap = (Map<String, int[]>) bundle.getSerializable(AppConstant.SELECT_TOWER);
            }
        }
        initSeat(towerType, towerSubType);
    }

    private void initSeat(int towertype, int towerSubType) {
        if (towertype == 1) {
            mSeatTable.setVisibility(View.GONE);
            seatTowerViewView.setVisibility(View.VISIBLE);
            initNaizhang(towerSubType);
        } else {
            mSeatTable.setVisibility(View.VISIBLE);
            seatTowerViewView.setVisibility(View.GONE);
            if (towerSubType == 0) {
                mSeatTable.setData(insulator, 3, 0, selectList);
                if (selectList == null)
                    selectList = new int[3][insulator];
            } else {
                mSeatTable.setData(insulator, 6, 1, selectList);
                if (selectList == null)
                    selectList = new int[6][insulator];

            }

            mSeatTable.setSeatChecker(new SeatTable.SeatChecker() {
                @Override
                public boolean isValidSeat(int row, int column) {
                    return true;
                }

                @Override
                public boolean isSold(int row, int column) {
                    return false;
                }

                @Override
                public void checked(int row, int column) {
                    selectList[column][row] = 1;


                }

                @Override
                public void unCheck(int row, int column) {
                    selectList[column][row] = 0;

                }

                @Override
                public String[] checkedSeatTxt(int row, int column) {
                    return new String[0];
                }
            });
        }
    }

    @OnClick({R.id.btn_submit, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (seatTowerViewView.getVisibility() == View.VISIBLE) {
                    EventBus.getDefault().post(new SelectTowerEvent(seatTowerViewView.getOverview(), mMap));
                } else {
                    EventBus.getDefault().post(new SelectTowerEvent(mSeatTable.getOverview(), selectList));
                }
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initNaizhang(final int towerSubType) {
        if (mMap == null)
            initMap();
        seatTowerViewView.setSeatChecker(new SeatTowerView.SeatChecker() {
            @Override
            public boolean isValidSeat(int row, int column) {
                if ((row == 0 || row == 1 || row == 4 || row == 5 || row == 8 || row == 9)) {
                    if (towerSubType == 0) {
                        if (row == 1 || row == 5 || row == 9) {
                            return false;
                        }
                    }
                    if (column < (20 - insulator) || column > (23 + insulator) || (column >= 20 && column <= 23))
                        return false;

                } else {
                    if (jumpType == 0) {
                        if (row == 3 || row == 7 || row == 11) {
                            return false;
                        }
                    }
                    if (((44 - jumpCount) / 2 > column) || (44 + jumpCount) / 2 <= column) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                return false;
            }

            @Override
            public void checked(int row, int column) {
                switch (row) {
                    case 0:
                        if (column < 20) {
                            mMap.get("PhaseALeft1")[19 - column] = 1;
                        } else {
                            mMap.get("PhaseARight1")[column - 24] = 1;
                        }
                        break;
                    case 1:
                        if (column < 20) {
                            mMap.get("PhaseALeft2")[19 - column] = 1;
                        } else {
                            mMap.get("PhaseARight2")[column - 24] = 1;
                        }
                        break;
                    case 2:
                        mMap.get("JumperA1")[column - ((44 - jumpCount) / 2)] = 1;
                        break;
                    case 3:
                        mMap.get("JumperA2")[column - ((44 - jumpCount) / 2)] = 1;
                        break;
                    case 4:
                        if (column < 20) {
                            mMap.get("PhaseBLeft1")[19 - column] = 1;
                        } else {
                            mMap.get("PhaseBRight1")[column - 24] = 1;
                        }
                        break;
                    case 5:
                        if (column < 20) {
                            mMap.get("PhaseBLeft2")[19 - column] = 1;
                        } else {
                            mMap.get("PhaseBRight2")[column - 24] = 1;
                        }
                        break;
                    case 6:
                        mMap.get("JumperB1")[column - ((44 - jumpCount) / 2)] = 1;
                        break;
                    case 7:
                        mMap.get("JumperB2")[column - ((44 - jumpCount) / 2)] = 1;
                        break;
                    case 8:
                        if (column < 20) {
                            mMap.get("PhaseCLeft1")[19 - column] = 1;
                        } else {
                            mMap.get("PhaseCRight1")[column - 24] = 1;
                        }
                        break;
                    case 9:
                        if (column < 20) {
                            mMap.get("PhaseCLeft2")[19 - column] = 1;
                        } else {
                            mMap.get("PhaseCRight2")[column - 24] = 1;
                        }
                        break;
                    case 10:
                        mMap.get("JumperC1")[column - ((44 - jumpCount) / 2)] = 1;
                        break;
                    case 11:
                        mMap.get("JumperC2")[column - ((44 - jumpCount) / 2)] = 1;
                        break;
                }
            }

            @Override
            public void unCheck(int row, int column) {
                switch (row) {
                    case 0:
                        if (column < 20) {
                            mMap.get("PhaseALeft1")[19 - column] = 0;
                        } else {
                            mMap.get("PhaseARight1")[column - 24] = 0;
                        }
                        break;
                    case 1:
                        if (column < 20) {
                            mMap.get("PhaseALeft2")[19 - column] = 0;
                        } else {
                            mMap.get("PhaseARight2")[column - 24] = 0;
                        }
                        break;
                    case 2:
                        mMap.get("JumperA1")[column - ((44 - jumpCount) / 2)] = 0;
                        break;
                    case 3:
                        mMap.get("JumperA2")[column - ((44 - jumpCount) / 2)] = 0;
                        break;
                    case 4:
                        if (column < 20) {
                            mMap.get("PhaseBLeft1")[19 - column] = 0;
                        } else {
                            mMap.get("PhaseBRight1")[column - 24] = 0;
                        }
                        break;
                    case 5:
                        if (column < 20) {
                            mMap.get("PhaseBLeft2")[19 - column] = 0;
                        } else {
                            mMap.get("PhaseBRight2")[column - 24] = 0;
                        }
                        break;
                    case 6:
                        mMap.get("JumperB1")[column - ((44 - jumpCount) / 2)] = 0;
                        break;
                    case 7:
                        mMap.get("JumperB2")[column - ((44 - jumpCount) / 2)] = 0;
                        break;
                    case 8:
                        if (column < 20) {
                            mMap.get("PhaseCLeft1")[19 - column] = 0;
                        } else {
                            mMap.get("PhaseCRight1")[column - 24] = 0;
                        }
                        break;
                    case 9:
                        if (column < 20) {
                            mMap.get("PhaseCLeft2")[19 - column] = 0;
                        } else {
                            mMap.get("PhaseCRight2")[column - 24] = 0;
                        }
                        break;
                    case 10:
                        mMap.get("JumperC1")[column - ((44 - jumpCount) / 2)] = 0;
                        break;
                    case 11:
                        mMap.get("JumperC2")[column - ((44 - jumpCount) / 2)] = 0;
                        break;
                }
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        seatTowerViewView.setData(12, 44, jumpCount, mMap);
    }

    private void initMap() {
        mMap = new HashMap<>();
        mMap.put("PhaseALeft1", new int[insulator]);
        mMap.put("PhaseALeft2", new int[insulator]);
        mMap.put("PhaseARight1", new int[insulator]);
        mMap.put("PhaseARight2", new int[insulator]);
        mMap.put("PhaseBLeft1", new int[insulator]);
        mMap.put("PhaseBLeft2", new int[insulator]);
        mMap.put("PhaseBRight1", new int[insulator]);
        mMap.put("PhaseBRight2", new int[insulator]);
        mMap.put("PhaseCLeft1", new int[insulator]);
        mMap.put("PhaseCLeft2", new int[insulator]);
        mMap.put("PhaseCRight1", new int[insulator]);
        mMap.put("PhaseCRight2", new int[insulator]);

        mMap.put("JumperA1", new int[jumpCount]);
        mMap.put("JumperA2", new int[jumpCount]);
        mMap.put("JumperB1", new int[jumpCount]);
        mMap.put("JumperB2", new int[jumpCount]);
        mMap.put("JumperC1", new int[jumpCount]);
        mMap.put("JumperC2", new int[jumpCount]);


    }


}
