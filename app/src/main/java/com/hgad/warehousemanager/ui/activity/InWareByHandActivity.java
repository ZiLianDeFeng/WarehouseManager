package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.ChangeWareRequest;
import com.hgad.warehousemanager.bean.request.CheckRequest;
import com.hgad.warehousemanager.bean.request.CheckStartRequest;
import com.hgad.warehousemanager.bean.request.ForUpRequest;
import com.hgad.warehousemanager.bean.request.InWareRequest;
import com.hgad.warehousemanager.bean.request.OutWareRequest;
import com.hgad.warehousemanager.bean.request.WareInfoRequest;
import com.hgad.warehousemanager.bean.response.ChangeWareResponse;
import com.hgad.warehousemanager.bean.response.CheckResponse;
import com.hgad.warehousemanager.bean.response.CheckStartResponse;
import com.hgad.warehousemanager.bean.response.ForUpResponse;
import com.hgad.warehousemanager.bean.response.InWareResponse;
import com.hgad.warehousemanager.bean.response.OutWareResponse;
import com.hgad.warehousemanager.bean.response.WareInfoResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.BottonPopupWindowView;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
public class InWareByHandActivity extends BaseActivity {

    private static final int FINISH = 100;
    private EditText et_markNum;
    private BottonPopupWindowView bottonPopupWindowView;
    private PopupWindow bottonPopupWindow;
    private String row;
    private String column;
    private String floor = "01";
    private String ware = "01";
    private Button btn_commit;
    private String address;
    private TextView tv_addressWare;
    private String type;
    private LinearLayout ll_address;
    private CustomProgressDialog customProgressDialog;
    private String realName;
    private List<WareInfo> data;
    private boolean isLast;
    private LinearLayout ll_markNum;
    private LinearLayout ll_detail;
    private TextView tv_markNum;
    private TextView tv_ware_name;
    String[] wareNums = new String[6];
    String[] rows = new String[15];
    String[] columns = new String[17];
    String[] floors = new String[27];
    private boolean isConnect;
    private String markNum;
    private String orderNum;
    private int id;
    private String model;
    private int orderId;
    private OrderInfo orderInfo;
    private String curAddress;
    private String proName;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH:
                    Intent intent = new Intent(type);
                    intent.putExtra(Constants.TYPE, type);
                    sendBroadcast(intent);
                    finish();
                    break;
            }
        }
    };
    private LinearLayout ll_number;
    private TextView tv_number;
    private String proType;
    private String checkTaskId;
    private TextView tv_weight;
    private int groupId;
    private boolean inWarePro;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_inware_byhand);
    }

    @Override
    protected void initData() {
        groupId = SPUtils.getInt(this, SPConstants.GROUP_ID);
        initWheelData();
        model = Constants.MODEL;
        realName = SPUtils.getString(this, SPConstants.REAL_NAME);
        Intent intent = getIntent();
        type = intent.getStringExtra(Constants.TYPE);
        checkTaskId = intent.getStringExtra(Constants.TASK_ID);
        data = (List<WareInfo>) intent.getSerializableExtra(Constants.LIST_DATA);
        orderInfo = (OrderInfo) intent.getSerializableExtra(Constants.ORDER_INFO);
        curAddress = intent.getStringExtra(Constants.ADDRESS);
        if (orderInfo != null) {
            orderNum = orderInfo.getOrderNum();
            orderId = orderInfo.getTaskId();
        }
        if (Constants.CHANGE_WARE.equals(type)) {
            initHeader("移位-手动移位");
        } else if (Constants.IN_WARE.equals(type)) {
            initHeader("入库-手动入库");
        } else if (Constants.CHECK.equals(type)) {
            initHeader("盘点-手动盘点");
//            ll_address.setVisibility(View.GONE);
        } else if (Constants.OUT_WARE.equals(type)) {
            initHeader("出库-手动出库");
//            ll_address.setVisibility(View.GONE);
        } else if (Constants.REVIEW_TYPE.equals(type)) {
            initHeader("复核-手动复核");
        }
    }

    private void initWheelData() {
        for (int i = 0; i < 6; i++) {
            wareNums[i] = "0" + (i + 1);
        }
        for (int i = 0; i < 15; i++) {
            if (i < 9) {
                rows[i] = "0" + (i + 1);
            } else {
                rows[i] = i + 1 + "";
            }
        }
        for (int i = 0; i < 17; i++) {
            if (i < 9) {
                columns[i] = "0" + (i + 1);
            } else {
                columns[i] = i + 1 + "";
            }
        }
        for (int i = 0; i < 27; i++) {
            if (i < 9) {
                floors[i] = "0" + (i + 1);
            } else if (i >= 9 && i < 18) {
                floors[i] = i + 2 + "";
            } else if (i >= 18) {
                floors[i] = i + 3 + "";
            }
        }
    }

    @Override
    protected void initView() {
        et_markNum = (EditText) findViewById(R.id.et_markNum);
        tv_addressWare = (TextView) findViewById(R.id.tv_addressWare);
        tv_addressWare.setOnClickListener(this);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        ll_markNum = (LinearLayout) findViewById(R.id.ll_markNum);
        ll_detail = (LinearLayout) findViewById(R.id.ll_detail);
        Button btn_find = (Button) findViewById(R.id.btn_find);
        btn_find.setOnClickListener(this);
        tv_markNum = (TextView) findViewById(R.id.tv_markNum);
        tv_ware_name = (TextView) findViewById(R.id.tv_ware_name);
        ll_number = (LinearLayout) findViewById(R.id.ll_number);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
    }

//    @Override
//    public void onBackPressed() {
//        backWarm();
//    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        isConnect = true;
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
        if (request instanceof InWareRequest) {
            InWareResponse inWareResponse = (InWareResponse) response;
            if (inWareResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(InWareByHandActivity.this, inWareResponse.getErrorMsg());
                if (Constants.REQUEST_SUCCESS.equals(inWareResponse.getErrorMsg())) {
                    handler.sendEmptyMessage(FINISH);
                }
            }
        } else if (request instanceof ChangeWareRequest) {
            ChangeWareResponse changeWareResponse = (ChangeWareResponse) response;
            if (changeWareResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(changeWareResponse.getErrorMsg())) {
                    handler.sendEmptyMessage(FINISH);
                } else {
                    new AlertView("提示", changeWareResponse.getErrorMsg(), null, new String[]{"确定"}, null, this, AlertView.Style.Alert, null).setCancelable(false).show();
                }
            } else {
                CommonUtils.showToast(InWareByHandActivity.this, changeWareResponse.getErrorMsg());
            }
        } else if (request instanceof WareInfoRequest) {
            WareInfoResponse wareInfoResponse = (WareInfoResponse) response;
            if (wareInfoResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(wareInfoResponse.getErrorMsg())) {
                    WareInfoResponse.DataEntity entity = wareInfoResponse.getData();
                    if (entity != null) {
                        inWarePro = true;
                        WareInfo wareInfo = new WareInfo();
                        wareInfo.setData(entity);
                        ll_markNum.setVisibility(View.GONE);
                        ll_detail.setVisibility(View.VISIBLE);
                        markNum = wareInfo.getMarkNum();
                        id = wareInfo.getId();
                        String count = wareInfo.getCount();
                        proType = wareInfo.getType();
                        tv_markNum.setText(markNum);
                        tv_weight.setText(wareInfo.getNetWeight());
                        if ("1".equals(proType)) {
                            ll_number.setVisibility(View.VISIBLE);
                            tv_number.setText(count);
                        }
                        proName = wareInfo.getProName();
                        tv_ware_name.setText(proName);
                        if (type.equals(Constants.IN_WARE)) {
                            tv_addressWare.setOnClickListener(null);
                            btn_commit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CommonUtils.showToast(InWareByHandActivity.this, "该货品已入过库，无法再次入库");
                                }
                            });
                        }
                        if (!TextUtils.isEmpty(wareInfoResponse.getData().getPositionCode().trim())) {
                            address = wareInfo.getAddress();
                            ware = address.substring(0, 2);
                            String addressStr = CommonUtils.formatAddress(address);
                            CommonUtils.stringInterceptionChangeLarge(tv_addressWare, addressStr, "仓", "排", "垛", "号");
                            if (this.type.equals(Constants.OUT_WARE)) {
                                tv_addressWare.setOnClickListener(null);
                            }
                            if (Constants.REVIEW_TYPE.equals(this.type)) {
                                btn_commit.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            if (this.type.equals(Constants.CHANGE_WARE)) {
                                tv_addressWare.setText("无");
                                tv_addressWare.setOnClickListener(null);
                                btn_commit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CommonUtils.showToast(InWareByHandActivity.this, "该货品不在库中，无法移位");
                                    }
                                });
                            } else {
                                tv_addressWare.setText("无");
                                tv_addressWare.setOnClickListener(null);
                                if (Constants.OUT_TYPE.equals(this.type)) {
                                    btn_commit.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    } else {
                        inWarePro = false;
                        CommonUtils.showToast(this, "库中无该货品");
                        if (this.type.equals(Constants.CHANGE_WARE)) {
                            tv_addressWare.setText("无");
                            tv_addressWare.setOnClickListener(null);
                            btn_commit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CommonUtils.showToast(InWareByHandActivity.this, "该货品不在库中，无法移位");
                                }
                            });
                        }
                    }
                } else {
                    CommonUtils.showToast(this, wareInfoResponse.getErrorMsg());
                }
            }
        } else if (request instanceof OutWareRequest) {
            OutWareResponse outWareResponse = (OutWareResponse) response;
            if (outWareResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(InWareByHandActivity.this, outWareResponse.getErrorMsg());
                if (Constants.REQUEST_SUCCESS.equals(outWareResponse.getErrorMsg())) {
                    Intent intent = new Intent(Constants.REFRESH);
                    sendBroadcast(intent);
                    handler.sendEmptyMessage(FINISH);
                }
            }
        } else if (request instanceof ForUpRequest) {
            ForUpResponse forUpResponse = (ForUpResponse) response;
            if (forUpResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(InWareByHandActivity.this, forUpResponse.getErrorMsg());
                if (Constants.REQUEST_SUCCESS.equals(forUpResponse.getErrorMsg())) {
                    new AlertView("提示", "所有货品已扫描完毕，订单将进入复核状态，请货品上车后进行复核", null, new String[]{"确认"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            switch (position) {
                                case 0:
                                    Intent intent = new Intent(Constants.REFRESH);
                                    sendBroadcast(intent);
                                    Intent orderIntent = new Intent(Constants.ORDER_REFRESH);
                                    sendBroadcast(orderIntent);
                                    finish();
                                    break;
                            }
                        }
                    }).setCancelable(false).show();
                }
            }
        } else if (request instanceof CheckRequest) {
            CheckResponse checkResponse = (CheckResponse) response;
            if (checkResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(checkResponse.getErrorMsg())) {
//                    CheckStartRequest checkStartRequest = new CheckStartRequest(curAddress, realName, Constants.MODEL, checkTaskId);
//                    sendRequest(checkStartRequest, CheckStartResponse.class);
                } else {
                    CommonUtils.showToast(this, checkResponse.getErrorMsg());
                }
            }
        } else if (request instanceof CheckStartRequest) {
            CheckStartResponse checkStartResponse = (CheckStartResponse) response;
            if (checkStartResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(this, checkStartResponse.getErrorMsg());
                if (Constants.REQUEST_SUCCESS.equals(checkStartResponse.getErrorMsg())) {
                    setResult(Constants.RESULT_OK);
                    handler.sendEmptyMessage(FINISH);
                } else {
                    CommonUtils.showToast(this, checkStartResponse.getErrorMsg());
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_addressWare:
                chooseAddress();
                break;
            case R.id.btn_commit:
                commit();
                break;
            case R.id.pop_confirm:
                String text = bottonPopupWindowView.getConfirmText();
//                if ("下一步".equals(text)) {
//                    changeChoose();
//                } else if ("确定".equals(text)) {
                confirmAddress();
//                }
                break;
            case R.id.pop_cancle:
                bottonPopupWindow.dismiss();
                break;
            case R.id.pop_switch:
//                changeChoose();
                break;
            case R.id.btn_find:
                search();
                break;
        }
    }

    private void search() {
        String markNum = et_markNum.getText().toString().trim();
        if (TextUtils.isEmpty(markNum)) {
            CommonUtils.showToast(this, "还未填写标签号");
            return;
        }
        if (!type.equals(Constants.REVIEW_TYPE)) {
            showDialog(getString(R.string.info_check));
            WareInfoRequest wareInfoRequest = new WareInfoRequest(markNum);
            sendRequest(wareInfoRequest, WareInfoResponse.class);
        } else {
            WareInfo wareInfo = null;
            if (data != null) {
                boolean hava = false;
                for (WareInfo info : data) {
                    if (info.getMarkNum().equals(markNum)) {
                        wareInfo = info;
                        address = wareInfo.getAddress();
                        hava = true;
                    }
                }
                if (hava) {
                    ll_markNum.setVisibility(View.GONE);
                    ll_detail.setVisibility(View.VISIBLE);
                    this.markNum = wareInfo.getMarkNum();
                    id = wareInfo.getId();
                    String count = wareInfo.getCount();
                    proType = wareInfo.getType();
                    tv_markNum.setText(markNum);
                    tv_addressWare.setText("无");
                    tv_addressWare.setOnClickListener(null);
//                    int i = 0;
//                    for (WareInfo info : data) {
//                        if ("0".equals(info.getState())) {
//                            i++;
//                        }
////                    tv_addressWare.setText(address);
//                    }
//                    if (i == 1) {
//                        isLast = true;
//                    }
//                    showDialog(getString(R.string.commit_data));
//                    OutWareRequest outWareRequest = new OutWareRequest(markNum, realName, orderNum + "", address, model, proType, CommonUtils.getCurrentTime().substring(0, 10));
//                    sendRequest(outWareRequest, OutWareResponse.class);
                } else {
                    CommonUtils.showToast(this, "该货品不在该次任务中，请重新扫描！");
//                tv_addressWare.setText("无");
//                tv_addressWare.setOnClickListener(null);
                }
            }
        }
    }

    private void commit() {
        if (Constants.OUT_WARE.equals(type)) {
            outCommit();
            return;
        }
        if (!Constants.CHECK.equals(type)) {
            if (TextUtils.isEmpty(tv_addressWare.getText().toString().trim())) {
                CommonUtils.showToast(this, getString(R.string.address_not_choose));
                return;
            }
        }
        if (Constants.CHANGE_WARE.equals(type)) {
            changeCommit();
//        } else if (Constants.IN_WARE.equals(type)) {
//            inCommit();
            //Z3Q7142131
        } else if (Constants.CHECK.equals(type)) {
            checkCommit();
        } else if (Constants.REVIEW_TYPE.equals(type)) {
            reviewCommit();
        }
    }

    private void reviewCommit() {
        WareInfo wareInfo = null;
        if (data != null) {
            boolean hava = false;
            for (WareInfo info : data) {
                if (info.getMarkNum().equals(markNum)) {
                    wareInfo = info;
                    hava = true;
                }
            }
            if (hava) {
                address = "";
                proType = wareInfo.getType();
                showDialog(getString(R.string.commit_data));
                OutWareRequest outWareRequest = new OutWareRequest(markNum, realName, orderNum + "", address, model, proType, CommonUtils.getCurrentTime().substring(0, 10), groupId + "");
                sendRequest(outWareRequest, OutWareResponse.class);
            } else {
                CommonUtils.showToast(this, "该货品不在该次任务中，请重新扫描！");
//                tv_addressWare.setText("无");
//                tv_addressWare.setOnClickListener(null);
            }
        }
    }

    private void outCommit() {
        WareInfo wareInfo = null;
        if (data != null) {
            boolean hava = false;
            for (WareInfo info : data) {
                if (info.getMarkNum().equals(markNum)) {
                    wareInfo = info;
                    address = wareInfo.getAddress();
                    hava = true;
                }
            }
            if (hava) {
                showDialog(getString(R.string.commit_data));
                OutWareRequest outWareRequest = new OutWareRequest(markNum, realName, orderNum, address, model, proType, CommonUtils.getCurrentTime().substring(0, 10), null);
                sendRequest(outWareRequest, OutWareResponse.class);
            } else {
                CommonUtils.showToast(this, "该货品不在该次任务中，请重新扫描！");
//                tv_addressWare.setText("无");
//                tv_addressWare.setOnClickListener(null);
            }
        }
    }

    private void checkCommit() {
//        if (TextUtils.isEmpty(address)) {
//            CheckRequest checkRequest = new CheckRequest(markNum, proName, "", curAddress, "0");
//            sendRequest(checkRequest, CheckResponse.class);
//        } else if (!curAddress.equals(address)) {
//            CheckRequest checkRequest = new CheckRequest(markNum, proName, address, curAddress, "2");
//            sendRequest(checkRequest, CheckResponse.class);
//        } else {
//            showDialog(getString(R.string.commit_data));
//            CheckStartRequest checkStartRequest = new CheckStartRequest(curAddress, realName, Constants.MODEL, checkTaskId);
//            sendRequest(checkStartRequest, CheckStartResponse.class);
//        }
        CheckStartRequest checkStartRequest = new CheckStartRequest(curAddress, realName, checkTaskId, markNum, inWarePro ? "0" : "1");
        sendRequest(checkStartRequest, CheckStartResponse.class);
    }

    private void changeCommit() {
//        String markNum = et_markNum.getText().toString().trim();
//        address = CommonUtils.formatAddressForUse(address);
        ChangeWareRequest changeWareRequset = new ChangeWareRequest(markNum, address, realName, model);
        sendRequest(changeWareRequset, ChangeWareResponse.class);
        showDialog(getString(R.string.commit_data));
    }

    private void confirmAddress() {
        if (bottonPopupWindowView.getRow() == null
                || bottonPopupWindowView.getColumn() == null) {
            CommonUtils.showToast(this, getString(R.string.address_not_choose));
            return;
        }
        ware = bottonPopupWindowView.getWare();
        row = bottonPopupWindowView.getRow();
        column = bottonPopupWindowView.getColumn();
        floor = bottonPopupWindowView.getFloor();
        if (TextUtils.isEmpty(ware) || TextUtils.isEmpty(row) || TextUtils.isEmpty(column) || TextUtils.isEmpty(floor)) {
            CommonUtils.showToast(this, "信息有误,请重新选择货位");
            return;
        }
        bottonPopupWindow.dismiss();
        address = ware + row + column + floor;
        final String addressStr = ware + "仓" + row + "排" + column + "垛" + floor + "号";
        new AlertView("提示", "选择的货位为" + addressStr, "取消", new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        CommonUtils.stringInterceptionChangeLarge(tv_addressWare, addressStr, "仓", "排", "垛", "号");

                        break;
                }
            }
        }).setCancelable(false).show();
    }

    private void chooseAddress() {
        bottonPopupWindowView = new BottonPopupWindowView(ware, floor, getString(R.string.choose_address), type);
        bottonPopupWindow = bottonPopupWindowView.creat(this, wareNums, floors, this);
        bottonPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.backgroundAlpha(1f, InWareByHandActivity.this);
            }
        });
//        bottonPopupWindowView.initSeatTable(seatRows, seatColumns, unSeatRows, unSeatColums, unFullRows, unFullColums);
        bottonPopupWindowView.show(btn_commit, Gravity.BOTTOM, 0, 0, address);
        CommonUtils.backgroundAlpha(0.8f, this);
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
                        CommonUtils.showToast(InWareByHandActivity.this, getString(R.string.poor_signal) + "，请重新尝试");
                        btn_commit.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }, 5000);
    }
}
