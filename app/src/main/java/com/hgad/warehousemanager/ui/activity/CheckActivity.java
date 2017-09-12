package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.CheckTaskInfo;
import com.hgad.warehousemanager.bean.request.CheckStartRequest;
import com.hgad.warehousemanager.bean.request.WareHouseRequest;
import com.hgad.warehousemanager.bean.response.CheckStartResponse;
import com.hgad.warehousemanager.bean.response.WareHouseResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.AddAndSubText;
import com.hgad.warehousemanager.view.CustomProgressDialog;
import com.hgad.warehousemanager.zxing.activity.ScannerActivity;

/**
 * Created by Administrator on 2017/6/29.
 */
public class CheckActivity extends BaseActivity {
    private static final int SCAN = 110;
    private static final int NEXTCHECK = 111;
    public static final int FOR_REFRESH = 221;
    private static final int NOTIFY = 100;
    private String ware;
    private String row;
    private String column;
    private String floor;
    private String type = Constants.CHECK;
    private AddAndSubText at_row;
    private AddAndSubText at_column;
    private AddAndSubText at_floor;
    private String address;
    private String userName;
    private int curColumn;
    private int taskId;
    private String node;
    private int curFloor;
    private int curRow;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOTIFY:
                    Bundle data = msg.getData();
                    int rows = data.getInt("row");
                    int cols = data.getInt("col");
                    at_row.setMaxValue(rows);
                    at_column.setMaxValue(cols);
                    if (!TextUtils.isEmpty(node)) {
                        int startRow = Integer.parseInt(node.substring(2, 4));
                        at_row.setValue(startRow);
                        int rowValue = at_row.getValue();
                        int startColumn = Integer.parseInt(node.substring(4, 6));
                        at_column.setValue(startColumn);
                        int colValue = at_column.getValue();
                        int startFloor = Integer.parseInt(node.substring(6, 8));
                        if (startFloor == at_floor.getMaxValue()) {
                            if (colValue == at_column.getMaxValue()) {
                                at_row.setValue(rowValue + 1);
                                at_column.setValue(at_column.getMinValue());
                                at_floor.setValue(at_floor.getMinValue());
                            } else {
                                at_column.setValue(colValue + 1);
                                at_floor.setValue(at_floor.getMinValue());
                            }
                        } else {
                            if (startFloor == 9 || startFloor == 19) {
                                at_floor.setValue(startFloor + 2);
                            } else {
                                at_floor.setValue(startFloor + 1);
                            }
                        }
                        getRow(at_row.getValue());
                        curRow = at_row.getValue();
                        getColumn(at_column.getValue());
                        curColumn = at_column.getValue();
                        getFloor(at_floor.getValue());
                        curFloor = at_floor.getValue();
                    } else {
                        int rowValue = at_row.getValue();
                        getRow(rowValue);
                        curRow = rowValue;
                        int colValue = at_column.getValue();
                        getColumn(colValue);
                        curColumn = colValue;
                        int floorValue = at_floor.getValue();
                        getFloor(floorValue);
                        curFloor = floorValue;
                    }
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                    }
                    break;
            }
        }
    };
    private CustomProgressDialog customProgressDialog;
    private boolean isConnect;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_check);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        CheckTaskInfo checkTaskInfo = (CheckTaskInfo) intent.getSerializableExtra(Constants.CHECK_INFO);
        String taskName = checkTaskInfo.getName();
        node = checkTaskInfo.getNode();
        String wareNum = checkTaskInfo.getWareNo();
        taskId = checkTaskInfo.getTaskId();
        initHeader(taskName);
        ware = wareNum;
        userName = SPUtils.getString(this, SPConstants.USER_NAME);
        showDialog("数据同步中");
        WareHouseRequest wareHouseRequest = new WareHouseRequest(wareNum);
        sendRequest(wareHouseRequest, WareHouseResponse.class);

    }

    public void getRow(int rowValue) {
        if (rowValue < 10) {
            row = "0" + rowValue;
        } else {
            row = rowValue + "";
        }
    }

    public void getColumn(int colValue) {
        if (colValue < 10) {
            column = "0" + colValue;
        } else {
            column = colValue + "";
        }
    }

    public void getFloor(int floorValue) {
        if (floorValue < 10) {
            floor = "0" + floorValue;
        } else {
            floor = floorValue + "";
        }
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_start).setOnClickListener(this);
        at_row = (AddAndSubText) findViewById(R.id.at_row);
        at_row.setType("row");

        at_row.setOnNumberClickListener(new AddAndSubText.OnNumberClickListener() {
            @Override
            public void onButtonSub(int value) {

            }

            @Override
            public void onButtonAdd(int value) {
                at_column.setValue(at_column.getMinValue());
                at_floor.setValue(at_floor.getMinValue());
                curFloor = at_floor.getValue();
                curColumn = at_column.getValue();
                getRow(value);
                getColumn(curColumn);
                getFloor(curFloor);
                final String address = ware + row + column + floor;
                if (curRow == at_row.getMaxValue()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AlertView("提示", "当前仓库已全部扫完", null, new String[]{"确定"}, null, CheckActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    switch (position) {
                                        case 0:
//                                            CheckStartRequest checkStartRequest = new CheckStartRequest(address, "2", userName, Constants.MODEL, taskId + "");
//                                            sendRequest(checkStartRequest, CheckStartResponse.class);
                                            break;
                                    }
                                }
                            }).setCancelable(false).show();
                        }
                    }, 500);
                }
                curRow = value;
            }
        });
        at_column = (AddAndSubText) findViewById(R.id.at_column);
        at_column.setType("column");

        at_column.setOnNumberClickListener(new AddAndSubText.OnNumberClickListener() {
            @Override
            public void onButtonSub(int value) {

            }

            @Override
            public void onButtonAdd(int value) {
                at_floor.setValue(at_floor.getMinValue());
                curFloor = at_floor.getValue();
                getColumn(value);
                getFloor(curFloor);
                final String address = ware + row + column + floor;
                if (curColumn == at_column.getMaxValue()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AlertView("提示", "当前排位已全部扫完，\n即将切换到下一排继续进行盘点", null, new String[]{"确定"}, null, CheckActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    switch (position) {
                                        case 0:
//                                            CheckStartRequest checkStartRequest = new CheckStartRequest(address, "2", userName, Constants.MODEL, taskId + "");
//                                            sendRequest(checkStartRequest, CheckStartResponse.class);
                                            at_row.addNumber();
                                            break;
                                    }
                                }
                            }).setCancelable(false).show();
                        }
                    }, 500);
                }
                curColumn = value;
            }
        });
        at_floor = (AddAndSubText) findViewById(R.id.at_floor);
        at_floor.setType("floor");
        at_floor.setMaxValue(29);
        at_floor.setOnNumberClickListener(new AddAndSubText.OnNumberClickListener() {
            @Override
            public void onButtonSub(int value) {

            }

            @Override
            public void onButtonAdd(int value) {
                final String address = ware + row + column + floor;
                Log.d("address", "onButtonAdd: " + address);
                getFloor(value);
                if (curFloor == at_floor.getMaxValue()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new AlertView("提示", "当前垛位已全部扫完，\n即将切换到下一垛继续进行盘点", null, new String[]{"确定"}, null, CheckActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    switch (position) {
                                        case 0:
                                            CheckStartRequest checkStartRequest = new CheckStartRequest(address, "2", userName, Constants.MODEL, taskId + "");
                                            sendRequest(checkStartRequest, CheckStartResponse.class);
                                            at_column.addNumber();
                                            break;
                                    }
                                }
                            }).setCancelable(false).show();
                        }
                    }, 500);
                } else {
                    CheckStartRequest checkStartRequest = new CheckStartRequest(address, "1", userName, Constants.MODEL, taskId + "");
                    sendRequest(checkStartRequest, CheckStartResponse.class);
                }
                curFloor = value;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                Intent intent = new Intent(this, ScanResultActivity.class);
                intent.putExtra(Constants.SCAN_RESULT, result);
                intent.putExtra(Constants.TYPE, type);
                String address = ware + row + column + floor;
                intent.putExtra(Constants.ADDRESS, address);
                startActivityForResult(intent, NEXTCHECK);
            }
        } else if (resultCode == Constants.RESULT_OK && requestCode == NEXTCHECK) {
            int value = at_floor.getValue();
            address = ware + row + column + floor;
            if (value == at_floor.getMaxValue()) {
                CheckStartRequest checkStartRequest = new CheckStartRequest(address, "2", userName, Constants.MODEL, taskId + "");
                sendRequest(checkStartRequest, CheckStartResponse.class);
            } else {
                CheckStartRequest checkStartRequest = new CheckStartRequest(address, "1", userName, Constants.MODEL, taskId + "");
                sendRequest(checkStartRequest, CheckStartResponse.class);
            }
            if (value == 9 || value == 19) {
                at_floor.setValue(value + 2);
            } else if (value == 29) {
                new AlertView("提示", "当前垛位已全部扫完，\n即将切换到下一垛继续进行盘点", null, new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                if (at_column.getValue() != at_column.getMaxValue()) {
                                    at_column.addNumber();
                                } else {
//                                    CommonUtils.showToast(CheckActivity.this, "当前排位已全部扫完，");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertView("提示", "当前排位已全部扫完，\n即将切换到下一排继续进行盘点", null, new String[]{"确定"}, null, CheckActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                                                @Override
                                                public void onItemClick(Object o, int position) {
                                                    switch (position) {
                                                        case 0:
                                                            if (at_row.getValue() != at_row.getMaxValue()) {
                                                                at_row.addNumber();
                                                            } else {
                                                                CommonUtils.showToast(CheckActivity.this, "当前仓库已全部扫完，");
                                                            }
                                                            break;
                                                    }
                                                }
                                            }).setCancelable(false).show();
                                        }
                                    }, 500);
                                }
                                break;
                        }
                    }
                }).setCancelable(false).show();
            } else {
                at_floor.setValue(value + 1);
            }
            getFloor(at_floor.getValue());
        } else if (resultCode == Constants.HAND && requestCode == SCAN) {
            address = ware + row + column + floor;
            Log.d("address", "onActivityResult: " + address);
            Intent intent = new Intent(this, InWareByHandActivity.class);
            intent.putExtra(Constants.ADDRESS, address);
            intent.putExtra(Constants.TYPE, type);
            startActivityForResult(intent, NEXTCHECK);
        }
    }


    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        isConnect = true;
        if (request instanceof CheckStartRequest) {
            CheckStartResponse checkStartResponse = (CheckStartResponse) response;
            if (checkStartResponse.getResponseCode().getCode() == 200) {
                if (checkStartResponse.getErrorMsg().equals("请求成功")) {

                }
            } else {
                CommonUtils.showToast(this, checkStartResponse.getErrorMsg());
            }
        } else if (request instanceof WareHouseRequest) {
            WareHouseResponse wareHouseResponse = (WareHouseResponse) response;
            if (wareHouseResponse.getResponseCode().getCode() == 200) {
                if (wareHouseResponse.getErrorMsg().equals("请求成功")) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startScan();
                break;
        }
    }

    private void startScan() {
        Intent intent = new Intent(this, ScannerActivity.class);
        intent.putExtra(Constants.TYPE, type);
        startActivityForResult(intent, SCAN);
    }

    @Override
    public void onBackPressed() {
        setResult(FOR_REFRESH);
        finish();
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
                        CommonUtils.showToast(CheckActivity.this, getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }
}
