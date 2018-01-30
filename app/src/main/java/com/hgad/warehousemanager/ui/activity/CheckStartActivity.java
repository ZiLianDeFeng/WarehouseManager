package com.hgad.warehousemanager.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.CheckTaskInfo;
import com.hgad.warehousemanager.bean.request.WareHouseRequest;
import com.hgad.warehousemanager.bean.response.WareHouseResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;
import com.hgad.warehousemanager.view.WheelView;
import com.hgad.warehousemanager.zxing.activity.QrScanActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */
public class CheckStartActivity extends BaseActivity {

    private static final int SCAN = 100;
    private static final int NEXTCHECK = 110;
    public static final int FOR_REFRESH = 221;
    private static final int NOTIFY = 100;
    private WheelView wl_ware;
    private WheelView wl_floor;
    private WheelView wl_row;
    private WheelView wl_column;
    String[] rows = new String[15];
    String[] columns = new String[17];
    String[] floors = new String[27];
    private String ware;
    private String row;
    private String column;
    private String floor;
    private int curPosition;
    private String type = Constants.CHECK;
    private int taskId;
    private String userName;
    private CustomProgressDialog customProgressDialog;
    private boolean isConnect;
    private List<String> rowList = new ArrayList<>();
    private List<String> colList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOTIFY:
                    Bundle data = msg.getData();
                    int rows = data.getInt("row");
                    int cols = data.getInt("col");
                    rowList.clear();
                    colList.clear();
                    for (int i = 1; i < rows + 1; i++) {
                        if (i < 10) {
                            rowList.add("0" + i);
                        } else {
                            rowList.add(i + "");
                        }
                    }
                    for (int i = 1; i < cols + 1; i++) {
                        if (i < 10) {
                            colList.add("0" + i);
                        } else {
                            colList.add(i + "");
                        }
                    }
                    wl_row.setItems(rowList);
                    wl_row.setSeletion(0);
                    wl_column.setItems(colList);
                    wl_column.setSeletion(0);
                    wl_floor.setSeletion(0);
                    row = wl_row.getSeletedItem();
                    column = wl_column.getSeletedItem();
//                    ware = wl_ware.getSeletedItem();
                    floor = wl_floor.getSeletedItem();
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                    }
                    break;
            }
        }
    };
    private String address;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.CHECK)) {
                String type = intent.getStringExtra(Constants.TYPE);
                toResult(type);
            } else if (action.equals(Constants.NEXT)) {
                toNextScan = true;
            } else if (action.equals(Constants.OVER)) {
                toNextScan = false;
            }
        }
    };
    private Button btn_start;
    private String state;
    private boolean toNextScan;

    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.CHECK);
        intentFilter.addAction(Constants.NEXT);
        intentFilter.addAction(Constants.OVER);
        registerReceiver(receiver, intentFilter);
    }

    private void toResult(String type) {
        Intent intent = new Intent(this, OperateResultActivity.class);
        intent.putExtra(Constants.TYPE, type);
        intent.putExtra(Constants.ADDRESS, address);
        startActivity(intent);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_check_start);
        registBroadcastReceiver(receiver);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        CheckTaskInfo checkTaskInfo = (CheckTaskInfo) intent.getSerializableExtra(Constants.CHECK_INFO);
        String taskName = checkTaskInfo.getName();
//        node = checkTaskInfo.getNode();
        String wareNum = checkTaskInfo.getWareNo();
        state = checkTaskInfo.getState();

        taskId = checkTaskInfo.getTaskId();
        initHeader(taskName);
        ware = wareNum;
        userName = SPUtils.getString(this, SPConstants.USER_NAME);
        showDialog("数据同步中");
        WareHouseRequest wareHouseRequest = new WareHouseRequest(wareNum);
        sendRequest(wareHouseRequest, WareHouseResponse.class);
    }

    @Override
    protected void initView() {
        for (int i = 0; i < 27; i++) {
            if (i < 9) {
                floors[i] = "0" + (i + 1);
            } else if (i >= 9 && i < 18) {
                floors[i] = i + 2 + "";
            } else if (i >= 18) {
                floors[i] = i + 3 + "";
            }
        }
        wl_floor = (WheelView) findViewById(R.id.wl_floor);
        wl_floor.setOffset(1);
        wl_floor.setItems(Arrays.asList(floors));
        wl_floor.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                floor = item;
            }
        });
        wl_row = (WheelView) findViewById(R.id.wl_row);
        wl_row.setOffset(1);
        wl_row.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                row = item;
                wl_column.setSeletion(0);
                wl_floor.setSeletion(0);
            }
        });
        wl_column = (WheelView) findViewById(R.id.wl_column);
        wl_column.setOffset(1);
        wl_column.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                column = item;
                wl_floor.setSeletion(0);
            }
        });
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);
        TextView btnConfirm = (TextView) findViewById(R.id.btn_confirm);
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setText("记录");
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        isConnect = true;
        if (request instanceof WareHouseRequest) {
            WareHouseResponse wareHouseResponse = (WareHouseResponse) response;
            if (wareHouseResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(wareHouseResponse.getErrorMsg())) {
                    WareHouseResponse.DataEntity dataEntity = wareHouseResponse.getData().get(0);
                    int rows = dataEntity.getRows();
                    int cols = dataEntity.getCols();
                    Message message = new Message();
                    message.what = NOTIFY;
                    Bundle bundle = new Bundle();
                    bundle.putInt("row", rows);
                    bundle.putInt("col", cols);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString(Constants.RESULT);
                Intent intent = new Intent(this, ScanResultActivity.class);
                String codeType = bundle.getString(Constants.CODE_TYPE);
                String scanState = bundle.getString(Constants.SCAN_STATE);
                intent.putExtra(Constants.CODE_TYPE, codeType);
                intent.putExtra(Constants.SCAN_RESULT, result);
                intent.putExtra(Constants.TYPE, type);
                intent.putExtra(Constants.SCAN_STATE, scanState);
                address = ware + row + column + floor;
                intent.putExtra(Constants.ADDRESS, address);
                intent.putExtra(Constants.TASK_ID, taskId + "");
                startActivityForResult(intent, NEXTCHECK);
            }
        } else if (resultCode == Constants.RESULT_OK && requestCode == NEXTCHECK) {
            int curIndex = wl_floor.getSeletedIndex();
            if (curIndex == floors.length - 1) {
                int colIndex = wl_column.getSeletedIndex();
                if (colIndex != colList.size() - 1) {
                    wl_column.setSeletion(colIndex + 1);
                    new AlertView("提示", "当前垛位已全部扫完，\n即将切换到下一垛继续进行盘点", null, new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            switch (position) {
                                case 0:

                                    break;
                            }
                        }
                    }).setCancelable(false).show();
                } else {
                    int rowIndex = wl_row.getSeletedIndex();
                    if (rowIndex != rowList.size() - 1) {
                        wl_row.setSeletion(rowIndex + 1);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new AlertView("提示", "当前排位已全部扫完，\n即将切换到下一排继续进行盘点", null, new String[]{"确定"}, null, CheckStartActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(Object o, int position) {
                                        switch (position) {
                                            case 0:

                                                break;
                                        }
                                    }
                                }).setCancelable(false).show();
                            }
                        }, 500);
                    } else {
                        CommonUtils.showToast(CheckStartActivity.this, "已扫完最后一排");
                        toNextScan = false;
                    }
                }
            } else {
                wl_floor.setSeletion(curIndex + 1);
            }
            address = ware + row + column + floor;
            if (toNextScan) {
                startScan();
            }
        } else if (resultCode == Constants.HAND && requestCode == SCAN) {
            address = ware + row + column + floor;
            Intent intent = new Intent(this, InWareByHandActivity.class);
            intent.putExtra(Constants.ADDRESS, address);
            intent.putExtra(Constants.TYPE, type);
            startActivityForResult(intent, NEXTCHECK);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(FOR_REFRESH);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startScan();
                break;
            case R.id.btn_confirm:
                toRecord();
                break;
        }
    }

    private void toRecord() {
        Intent intent = new Intent(this, CheckDetailActivity.class);
        startActivity(intent);
    }

    private void startScan() {
        if (!"0".equals(state)) {
            new AlertView("提示", "当前盘点任务已关闭，无法盘点", null, new String[]{"确定"}, null, this, AlertView.Style.Alert, null).show();
        } else {
            address = ware + row + column + floor;
            Intent intent = new Intent(this, QrScanActivity.class);
            intent.putExtra(Constants.TYPE, type);
            intent.putExtra(Constants.ADDRESS, address);
            startActivityForResult(intent, SCAN);
        }
    }

    private void showDialog(String content) {
        customProgressDialog = new CustomProgressDialog(this, content);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        isConnect = false;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isConnect) {
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                        CommonUtils.showToast(CheckStartActivity.this, getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }
}
