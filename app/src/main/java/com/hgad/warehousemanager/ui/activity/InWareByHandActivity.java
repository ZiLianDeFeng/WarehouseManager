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

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.ChangeWareRequest;
import com.hgad.warehousemanager.bean.request.CheckRequest;
import com.hgad.warehousemanager.bean.request.InWareRequest;
import com.hgad.warehousemanager.bean.request.WareInfoRequest;
import com.hgad.warehousemanager.bean.response.ChangeWareResponse;
import com.hgad.warehousemanager.bean.response.CheckResponse;
import com.hgad.warehousemanager.bean.response.InWareResponse;
import com.hgad.warehousemanager.bean.response.WareInfoResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.BottonPopupWindowUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
public class InWareByHandActivity extends BaseActivity {

    private EditText et_markNum;
    private BottonPopupWindowUtils bottonPopupWindowUtils;
    private PopupWindow bottonPopupWindow;
    private String row;
    private String column;
    private String floor = "1";
    private String ware = "1";
    private Button btn_commit;
    private String address;
    private TextView tv_addressWare;
    private String type;
    private LinearLayout ll_address;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            }
        }
    };
    private String username;
    private List<WareInfo> data;
    private boolean isLast;
    private LinearLayout ll_markNum;
    private LinearLayout ll_detail;
    private TextView tv_markNum;
    private TextView tv_ware_name;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_inware_byhand);
    }

    @Override
    protected void initData() {
        username = SPUtils.getString(this, SPConstants.USER_NAME);
        Intent intent = getIntent();
        type = intent.getStringExtra(Constants.TYPE);
        data = (List<WareInfo>) intent.getSerializableExtra(Constants.LIST_DATA);
        if (Constants.CHANGE_WARE.equals(type)) {
            initHeader("移位-手动移位");
        } else if (Constants.IN_WARE.equals(type)) {
            initHeader("入库-手动添加");
        } else if (Constants.CHECK.equals(type)) {
            initHeader("盘点-手动盘点");
//            ll_address.setVisibility(View.GONE);
        } else if (Constants.OUT_WARE.equals(type)) {
            initHeader("出库-手动出库");
//            ll_address.setVisibility(View.GONE);
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
//        initBottonPopupWindow();
    }

    String[] wareNums = {"01", "02", "03", "04", "05", "06"};
    String[] rows = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};
    String[] columns = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};
    String[] floors = {"01", "02", "03"};

    //    private void initBottonPopupWindow() {
//
//    }
    final int[] seatRows = new int[]{4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6};
    final int[] seatColumns = new int[]{4, 5, 6, 7, 8, 9, 10, 4, 5, 6, 7, 8, 9, 10, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    final int[] unSeatRows = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4};
    final int[] unSeatColums = new int[]{1, 2, 3, 4, 12, 13, 14, 15, 1, 2, 3, 13, 14, 15, 1, 2, 14, 15, 1, 15};
    final int[] unFullRows = new int[]{7, 7, 7, 8, 8, 8};
    final int[] unFullColums = new int[]{14, 12, 13, 13, 14, 12};

    @Override
    public void onBackPressed() {
        backWarm();
    }


    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
        if (request instanceof InWareRequest) {
            InWareResponse inWareResponse = (InWareResponse) response;
            if (inWareResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(InWareByHandActivity.this, inWareResponse.getErrorMsg());
                if ("请求成功".equals(inWareResponse.getErrorMsg())) {
                    setResult(Constants.RESULT_OK);
                    finish();
                }
            }
        } else if (request instanceof ChangeWareRequest) {
            ChangeWareResponse changeWareResponse = (ChangeWareResponse) response;
            if (changeWareResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(InWareByHandActivity.this, changeWareResponse.getErrorMsg());
                if ("请求成功".equals(changeWareResponse.getErrorMsg())) {
                    finish();
                }
            }
        } else if (request instanceof WareInfoRequest) {
            WareInfoResponse wareInfoResponse = (WareInfoResponse) response;
            if (wareInfoResponse.getResponseCode().getCode() == 200) {
                if ("请求成功".equals(wareInfoResponse.getErrorMsg())) {
                    WareInfoResponse.DataEntity entity = wareInfoResponse.getData();
                    if (entity != null) {
                        WareInfo wareInfo = new WareInfo();
                        wareInfo.setData(entity);
                        ll_markNum.setVisibility(View.GONE);
                        ll_detail.setVisibility(View.VISIBLE);
                        String markNum = wareInfo.getMarkNum();
                        tv_markNum.setText(markNum);
                        tv_ware_name.setText(wareInfo.getProName());
                        if (!TextUtils.isEmpty(entity.getPositionCode())) {
                            address = wareInfo.getAddress();
                            StringBuffer str = new StringBuffer(this.address);
                            str.insert(2, "仓");
                            str.insert(5, "排");
                            str.insert(8, "垛");
                            str.insert(11, "层");
                            address = str.toString();
                            tv_addressWare.setText(address);
                            if (type.equals(Constants.IN_WARE)) {
                                tv_addressWare.setOnClickListener(null);
                                btn_commit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CommonUtils.showToast(InWareByHandActivity.this, "该货品已入库，无法再次入库");
                                    }
                                });
                            } else if (type.equals(Constants.OUT_WARE)) {
                                tv_addressWare.setOnClickListener(null);
                            }
                        } else {
                            if (type.equals(Constants.OUT_WARE)) {
                                tv_addressWare.setText("无");
                                tv_addressWare.setOnClickListener(null);
                                btn_commit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CommonUtils.showToast(InWareByHandActivity.this, "该货品已出库，无法再次出库");
                                    }
                                });
                            }else if (type.equals(Constants.CHANGE_WARE)){
                                tv_addressWare.setText("无");
                                tv_addressWare.setOnClickListener(null);
                                btn_commit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CommonUtils.showToast(InWareByHandActivity.this,"该货品不在库中，无法移位");
                                    }
                                });
                            }
                        }
                    } else {
                        CommonUtils.showToast(this, "服务器中查询不到该货品");
                    }
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
                String text = bottonPopupWindowUtils.getConfirmText();
                if ("下一步".equals(text)) {
                    changeChoose();
                } else if ("确定".equals(text)) {
                    confirmAddress();
                }
                break;
            case R.id.pop_cancle:
                bottonPopupWindow.dismiss();
                break;
            case R.id.pop_switch:
                changeChoose();
                break;
            case R.id.btn_find:
                search();
                break;
        }
    }

    private void search() {
        String markNum = et_markNum.getText().toString().trim();
        boolean checkNetWork = CommonUtils.checkNetWork(this);
        if (checkNetWork) {
            customProgressDialog = new CustomProgressDialog(this, "数据校验中");
            customProgressDialog.setCancelable(false);
            customProgressDialog.setCanceledOnTouchOutside(false);
            customProgressDialog.show();
            WareInfoRequest wareInfoRequest = new WareInfoRequest(markNum);
            sendRequest(wareInfoRequest, WareInfoResponse.class);
            return;
        } else {
            CommonUtils.showToast(this, "请检查网络");
        }
    }

    private void changeChoose() {
        bottonPopupWindowUtils.change();
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
        } else if (Constants.IN_WARE.equals(type)) {
            inCommit();
        } else if (Constants.CHECK.equals(type)) {
            checkCommit();
        }
    }

    private void outCommit() {
        String markNum = et_markNum.getText().toString().trim();
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
                int i = 0;
                for (WareInfo info : data) {
                    if ("0".equals(info.getState())) {
                        i++;
                    }
//                    tv_addressWare.setText(address);
                }
                if (i == 1) {
                    isLast = true;
                }
                customProgressDialog = new CustomProgressDialog(this, "数据提交中");
//        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                customProgressDialog.setCancelable(false);
                customProgressDialog.setCanceledOnTouchOutside(false);
                customProgressDialog.show();
                InWareRequest inWareRequest = new InWareRequest(wareInfo.getMarkNum(), username, isLast ? wareInfo.getTaskId() : null, "1", address);
                sendRequest(inWareRequest, InWareResponse.class);
            } else {
                CommonUtils.showToast(this, "该货品不在该次任务中，请重新扫描！");
//                tv_addressWare.setText("无");
//                tv_addressWare.setOnClickListener(null);
            }
        }
    }

    private void checkCommit() {
        CheckRequest checkRequest = new CheckRequest();
        sendRequest(checkRequest, CheckResponse.class);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                }
                CommonUtils.showToast(InWareByHandActivity.this, "信息正确");
                finish();
            }
        }, 2000);
        customProgressDialog = new CustomProgressDialog(this, "信息校验中");
//        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
    }

    private void inCommit() {
        String markNum = et_markNum.getText().toString().trim();
        WareInfo wareInfo = null;
        if (data != null) {
            boolean hava = false;
            for (WareInfo info : data) {
                if (info.getMarkNum().equals(markNum)) {
                    wareInfo = info;
//                    address = wareInfo.getAddress();
                    hava = true;
                }
            }
            if (hava) {
                int i = 0;
                for (WareInfo info : data) {
                    if ("0".equals(info.getState())) {
                        i++;
                    }
//                    tv_addressWare.setText(address);
                }
                if (i == 1) {
                    isLast = true;
                }
                address = address.replace("仓", "");
                address = address.replace("排", "");
                address = address.replace("垛", "");
                address = address.replace("层", "");
                InWareRequest inWareRequest = new InWareRequest(markNum, username, isLast ? wareInfo.getTaskId() : null, "0", address);
                sendRequest(inWareRequest, InWareResponse.class);
                customProgressDialog = new CustomProgressDialog(this, "数据提交中");
//        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                customProgressDialog.setCancelable(false);
                customProgressDialog.setCanceledOnTouchOutside(false);
                customProgressDialog.show();
            } else {
                CommonUtils.showToast(this, "该货品不在该次任务中，请重新扫描！");
//                tv_addressWare.setText("无");
//                tv_addressWare.setOnClickListener(null);
            }
        }
    }

    private void changeCommit() {
        String markNum = et_markNum.getText().toString().trim();
        address = address.replace("仓", "");
        address = address.replace("排", "");
        address = address.replace("垛", "");
        address = address.replace("层", "");
        ChangeWareRequest changeWareRequset = new ChangeWareRequest(markNum, address, username);
        sendRequest(changeWareRequset, ChangeWareResponse.class);
        customProgressDialog = new CustomProgressDialog(this, "数据提交中");
//        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
    }

    private void confirmAddress() {
        if (bottonPopupWindowUtils.getRow() == null
                || bottonPopupWindowUtils.getColumn() == null) {
            CommonUtils.showToast(this, getString(R.string.address_not_choose));
            return;
        }
        ware = bottonPopupWindowUtils.getWare();
        row = bottonPopupWindowUtils.getRow();
        column = bottonPopupWindowUtils.getColumn();
        floor = bottonPopupWindowUtils.getFloor();
        address = ware + "仓" + row + "排" + column + "垛" + floor + "层";
        CommonUtils.stringInterceptionChangeLarge(tv_addressWare, address, new String[]{ware, row, column, floor}, "仓", "排", "垛", "层");
        bottonPopupWindow.dismiss();
    }

    private void chooseAddress() {
        bottonPopupWindowUtils = new BottonPopupWindowUtils(ware, floor, getResources().getString(R.string.choose_address));
        bottonPopupWindow = bottonPopupWindowUtils.creat(this, wareNums, rows, columns, floors, this);
        bottonPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.backgroundAlpha(1f, InWareByHandActivity.this);
            }
        });
//        bottonPopupWindowUtils.initSeatTable(seatRows, seatColumns, unSeatRows, unSeatColums, unFullRows, unFullColums);
        bottonPopupWindowUtils.show(btn_commit, Gravity.BOTTOM, 0, 0);
        CommonUtils.backgroundAlpha(0.8f, this);
    }
}
