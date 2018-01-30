package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.InWareRequest;
import com.hgad.warehousemanager.bean.request.WareInfoRequest;
import com.hgad.warehousemanager.bean.response.InWareResponse;
import com.hgad.warehousemanager.bean.response.WareInfoResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.db.dao.BaseDaoImpl;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.BottonPopupWindowView;
import com.hgad.warehousemanager.view.CustomProgressDialog;

/**
 * Created by Administrator on 2017/8/8.
 */
public class HandOperateActivity extends BaseActivity {

    private EditText et_markNum;
    private EditText et_order_num;
    private EditText et_net_weight;
    private TextView tv_addressWare;
    private BottonPopupWindowView bottonPopupWindowView;
    private PopupWindow bottonPopupWindow;
    private String[] wareNums = new String[6];
    private String[] rows = new String[15];
    private String[] columns = new String[17];
    private String[] floors = new String[27];
    private String row = "1";
    private String column = "1";
    private String floor = "1";
    private String ware = "1";
    private Button btn_commit;
    private String address;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler();
    private String realName;
    private EditText et_order_name;
    private EditText et_order_spec;
    private String model;
    private EditText et_steel_grade;
    private BaseDaoImpl<WareInfo, Integer> wareDao;
    private View main_layout;
    private RadioGroup rg;
    private String proType;
    private EditText et_pieces;
    private int groupId;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_hand_operate);
    }

    @Override
    protected void initData() {
        groupId = SPUtils.getInt(this, SPConstants.GROUP_ID);
        model = Constants.MODEL;
        initWheelData();
        initHeader("入库-手动添加");
        realName = SPUtils.getString(this, SPConstants.REAL_NAME);
        wareDao = new BaseDaoImpl<>(this, WareInfo.class);
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
        et_order_num = (EditText) findViewById(R.id.et_order_num);
        et_order_name = (EditText) findViewById(R.id.et_order_name);
        et_order_spec = (EditText) findViewById(R.id.et_order_spec);
        et_net_weight = (EditText) findViewById(R.id.et_net_weight);
        et_steel_grade = (EditText) findViewById(R.id.et_steel_grade);
        et_pieces = (EditText) findViewById(R.id.et_pieces);
        tv_addressWare = (TextView) findViewById(R.id.tv_addressWare);
        tv_addressWare.setOnClickListener(this);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        main_layout = findViewById(R.id.main_layout);
        final LinearLayout ll_pieces = (LinearLayout) findViewById(R.id.ll_pieces);
        rg = (RadioGroup) findViewById(R.id.rg_type);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_tape:
                        proType = "0";
                        ll_pieces.setVisibility(View.GONE);
                        break;
                    case R.id.rb_plate:
                        proType = "1";
                        ll_pieces.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
        isConnect = true;
        if (request instanceof InWareRequest) {
            InWareResponse inWareResponse = (InWareResponse) response;
            if (inWareResponse.getResponseCode() != null) {
                if (inWareResponse.getResponseCode().getCode() == 200) {
                    if (Constants.REQUEST_SUCCESS.equals(inWareResponse.getErrorMsg())) {
                        Intent intent = new Intent(Constants.IN_WARE);
                        intent.putExtra(Constants.TYPE, Constants.IN_WARE);
                        sendBroadcast(intent);
                        finish();
                    } else {
                        new AlertView("提示", inWareResponse.getErrorMsg(), null, new String[]{"确定"}, null, this, AlertView.Style.Alert, null).setCancelable(false).show();
                    }
                } else {
                    CommonUtils.showToast(HandOperateActivity.this, inWareResponse.getErrorMsg());
                }
            }
        } else if (request instanceof WareInfoRequest) {
            WareInfoResponse wareInfoResponse = (WareInfoResponse) response;
            if (wareInfoResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(wareInfoResponse.getErrorMsg())) {
                    WareInfoResponse.DataEntity entity = wareInfoResponse.getData();
                    if (entity != null) {
                        tv_addressWare.setOnClickListener(null);
                        CommonUtils.showToast(HandOperateActivity.this, "该货品已入库，无法再次入库");
                    } else {
                        String markNum = et_markNum.getText().toString().trim();
                        showDialog(getString(R.string.commit_data));
                        InWareRequest inWareRequest = new InWareRequest(markNum, realName, null, address, "1", null, null, null, "0", null, model, null, proType, groupId + "");
                        sendRequest(inWareRequest, InWareResponse.class);
                    }
                } else {
                    CommonUtils.showToast(this, wareInfoResponse.getErrorMsg());
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commit();
                break;
            case R.id.tv_addressWare:
                chooseAddress();
                break;
            case R.id.pop_confirm:
                confirmAddress();
                break;
            case R.id.pop_cancle:
                bottonPopupWindow.dismiss();
                break;
        }
    }

//    private void changeChoose() {
//        bottonPopupWindowView.change();
//    }

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
//        Snackbar snackbar = Snackbar.make(main_layout, "选择的货位为" + address, Snackbar.LENGTH_LONG).setAction("提交", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                commit();
//            }
//        });
//        View snackbarview = snackbar.getView();
//        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarview;
//        //设置背景色
////        snackbarview.setBackgroundColor(0xffffffff);
//        //文字的颜色
////        ((TextView) snackbarview.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#FF0000"));
//        //按钮的颜色
//        ((Button) snackbarview.findViewById(R.id.snackbar_action)).setTextColor(Color.parseColor("#00ff00"));
//        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        p.gravity = Gravity.CENTER_VERTICAL;
//        snackbar.show();
    }

    private void chooseAddress() {
        bottonPopupWindowView = new BottonPopupWindowView(ware, floor, getString(R.string.choose_address), Constants.IN_TYPE);
        bottonPopupWindow = bottonPopupWindowView.creat(this, wareNums, floors, this);
        bottonPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.backgroundAlpha(1f, HandOperateActivity.this);
            }
        });
//            bottonPopupWindowView.initSeatTable(seatRows, seatColumns, unSeatRows, unSeatColums, unFullRows, unFullColums);
        bottonPopupWindowView.show(btn_commit, Gravity.BOTTOM, 0, 0, address);
        CommonUtils.backgroundAlpha(0.8f, this);
    }

    private boolean isConnect;

    private void commit() {
        String markNum = et_markNum.getText().toString().trim();
//        String orderNum = et_order_num.getText().toString().trim();
//        String netWeight = et_net_weight.getText().toString().trim();
//        String orderName = et_order_name.getText().toString().trim();
//        String orderSpec = et_order_spec.getText().toString().trim();
//        String steelGrade = et_steel_grade.getText().toString().trim();
        if (TextUtils.isEmpty(proType)) {
            CommonUtils.showToast(this, "未选择货品类型");
            return;
        }
        if (proType.equals("1")) {
            String pieces = et_pieces.getText().toString().trim();
            if (TextUtils.isEmpty(pieces)) {
                CommonUtils.showToast(this, "未输入张数");
                return;
            }
        }
        if (TextUtils.isEmpty(markNum)) {
            CommonUtils.showToast(this, "标签号不能为空");
            return;
        }

//        if (TextUtils.isEmpty(orderNum)) {
//            CommonUtils.showToast(this, "合同号不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(orderName)) {
//            CommonUtils.showToast(this, "品名不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(orderSpec)) {
//            CommonUtils.showToast(this, "规格不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(netWeight)) {
//            CommonUtils.showToast(this, "净重不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(steelGrade)) {
//            CommonUtils.showToast(this, "钢制牌号不能为空");
//            return;
//        }
        if (TextUtils.isEmpty(address)) {
            CommonUtils.showToast(this, "未选择仓库地址");
            return;
        }
        if (Constants.DEBUG) {
            showDialog(getString(R.string.commit_data));
        } else {
//            address = CommonUtils.formatAddressForUse(address);
//            steelGrade = steelGrade.replace("+", "%2B");
            boolean isNetWork = CommonUtils.checkNetWork(this);
            if (isNetWork) {
                showDialog(getString(R.string.commit_data));
                WareInfoRequest wareInfoRequest = new WareInfoRequest(markNum);
                sendRequest(wareInfoRequest, WareInfoResponse.class);
//                InWareRequest inWareRequest = new InWareRequest(markNum, realName, null, address, "1", null, null, null, "0", null, model, null, proType);
//                sendRequest(inWareRequest, InWareResponse.class);
            } else {
//                WareInfo wareInfo = new WareInfo(markNum, address, null, null, null, null, null, false);
//                try {
//                    wareDao.saveOrUpdate(wareInfo);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                new AlertView("提示", "当前网络无法连接到服务器，数据将会保存在本地，之后可进行提交", null, new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Object o, int position) {
//                        finish();
//                    }
//                }).setCancelable(false).show();
                CommonUtils.showToast(this, "当前无网络信号");
            }
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
                        CommonUtils.showToast(HandOperateActivity.this, getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }
}
