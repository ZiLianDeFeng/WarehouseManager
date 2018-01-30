package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
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
import com.hgad.warehousemanager.db.dao.BaseDaoImpl;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.BottonPopupWindowView;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
public class ScanResultActivity extends BaseActivity {
    private static final int FINISH = 100;
    private TextView tv_markNum;
    private TextView tv_type;
    private TextView tv_net_weight;
    //    private TextView tv_gross_weight;
    private TextView tv_test;
    private Button btn_commit;
    private String row = "01";
    private String column = "01";
    private String floor = "01";
    private String ware = "01";
    private TextView tv_addressWare;
    private String type;
    private String address;
    private LinearLayout ll_more;
    private PopupWindow morePopupWindow;
    private PopupWindow bottonPopupWindow;
    private BottonPopupWindowView bottonPopupWindowView;
    private ScrollView sv_main;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH:
                    setResult(Constants.RESULT_OK);
                    Intent intent = new Intent(type);
                    intent.putExtra(Constants.TYPE, type);
                    sendBroadcast(intent);
                    finish();
//                    toResult();
                    break;
            }
        }
    };
    private CustomProgressDialog customProgressDialog;
    private WareInfo wareInfo;
    private String realName;
    private boolean isLast;
    private String markNum;
    private boolean isConnect;
    private String[] wareNums = new String[6];
    private String[] floors = new String[27];
    private String outOrderNum;
    private String orderName;
    private String netW;
    private String spec;
    private int id;
    private String model;
    private int orderId;
    private String inOrderNum;
    private String steelGrade;
    private String curAddress;
    private String proName;
    private String state;
    private BaseDaoImpl<WareInfo, Integer> wareDao;
    private LinearLayout ll_spec;
    private LinearLayout ll_weight;
    private String checkTaskId;
    private View main_layout;
    private String count;
    private LinearLayout ll_number;
    private TextView tv_number;
    private String scanState;
    private String proType;
    private boolean inWarePro;
    private List<WareInfo> data;
    private int groupId;


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_scan_result);
    }

    @Override
    protected void initData() {
        groupId = SPUtils.getInt(this, SPConstants.GROUP_ID);
        model = Constants.MODEL;
        initWheelData();
        wareDao = new BaseDaoImpl<>(this, WareInfo.class);
        Intent intent = getIntent();
        type = intent.getStringExtra(Constants.TYPE);
        checkTaskId = intent.getStringExtra(Constants.TASK_ID);
        realName = SPUtils.getString(this, SPConstants.REAL_NAME);
        OrderInfo orderInfo = (OrderInfo) intent.getSerializableExtra(Constants.ORDER_INFO);
        if (orderInfo != null) {
            orderId = orderInfo.getTaskId();
            outOrderNum = orderInfo.getOrderNum();
        }
        initTypeHeader(intent);
        String resultStr = intent.getStringExtra(Constants.SCAN_RESULT);
        String codeType = intent.getStringExtra(Constants.CODE_TYPE);
        scanState = intent.getStringExtra(Constants.SCAN_STATE);
        getResultIfnotNull(resultStr, codeType, scanState);
        wareInfo = (WareInfo) intent.getSerializableExtra(Constants.WARE_INFO);
        getWareInfoIfnotNull();
        data = (List<WareInfo>) intent.getSerializableExtra(Constants.LIST_DATA);
        getWareListIfnotNull(data);
    }

    private void getWareListIfnotNull(List<WareInfo> data) {
        if (data != null) {
            boolean hava = false;
            for (WareInfo info : data) {
                if (info.getMarkNum().equals(markNum)) {
                    wareInfo = info;
                    address = wareInfo.getAddress();
                    proType = wareInfo.getType();
                    hava = true;
                }
            }
            if (hava) {
                int i = 0;
                for (WareInfo info : data) {
                    if ("0".equals(info.getState())) {
                        i++;
                    }
                    if (!Constants.REVIEW_TYPE.equals(type)) {
                        tv_addressWare.setText(address);
                    } else {
                        tv_addressWare.setText("无");
                        tv_addressWare.setOnClickListener(null);
                    }
                }
                if (i == 1) {
                    isLast = true;
                }
            } else {
                CommonUtils.showToast(this, "该货品不在该次任务中，请重新扫描！");
                tv_addressWare.setText("无");
                tv_addressWare.setOnClickListener(null);
                btn_commit.setVisibility(View.INVISIBLE);

            }
        }
    }

    private void getWareInfoIfnotNull() {
        if (wareInfo != null) {
            markNum = wareInfo.getMarkNum();
            outOrderNum = wareInfo.getOrderNum();
            id = wareInfo.getId();
            String count = wareInfo.getCount();
            if ("1".equals(wareInfo.getType())) {
                ll_number.setVisibility(View.VISIBLE);
                tv_number.setText(count);
            }
            tv_markNum.setText(markNum);
            tv_type.setText(wareInfo.getSpec());
//            tv_gross_weight.setText(wareInfo.getGrossWeight() + "kg");
            tv_net_weight.setText(wareInfo.getNetWeight() + "T");
            String wareInfoAddress = wareInfo.getAddress();
            if (!TextUtils.isEmpty(wareInfoAddress)) {
                ware = wareInfoAddress.substring(0, 2);
                row = wareInfoAddress.substring(2, 4);
                column = wareInfoAddress.substring(4, 6);
                floor = wareInfoAddress.substring(6, 8);
                address = ware + row + column + floor;
                String addressStr = ware + "仓" + row + "排" + column + "垛" + floor + "号";
                CommonUtils.stringInterceptionChangeLarge(tv_addressWare, addressStr, "仓", "排", "垛", "号");
            }
            state = wareInfo.getState();
            if (Constants.IN_TYPE.equals(type) && "1".equals(state)) {
                btn_commit.setVisibility(View.INVISIBLE);
                tv_addressWare.setOnClickListener(null);
            }
//            if (Constants.OUT_TYPE.equals(type) && !("0".equals(state))) {
//                btn_commit.setVisibility(View.INVISIBLE);
//            }
            if (Constants.OUT_TYPE.equals(type)) {
                btn_commit.setVisibility(View.INVISIBLE);
            }

//            if (Constants.REVIEW_TYPE.equals(type) && !"1".equals(state)) {
//                btn_commit.setVisibility(View.INVISIBLE);
//            }
            if (Constants.REVIEW_TYPE.equals(type)) {
                btn_commit.setVisibility(View.INVISIBLE);
            }
//            if (TextUtils.isEmpty(wareInfoAddress)) {
//                tv_addressWare.setText("无");
//            }
        }
    }

    private void getResultIfnotNull(String resultStr, String codeType, String scanState) {
        if (resultStr != null) {
            String encoding = CommonUtils.getEncoding(resultStr);
            String UTF_Str = "";
            String GB_Str = "";
            boolean is_cN = false;
            try {
                if (encoding.equals("GB2312")) {
                    UTF_Str = new String(resultStr.getBytes("GB2312"), "GB2312");
                } else if (encoding.equals("ISO-8859-1")) {
                    UTF_Str = new String(resultStr.getBytes("ISO-8859-1"), "UTF-8");
                }
                is_cN = CommonUtils.isChineseCharacter(UTF_Str);
                //防止有人特意使用乱码来生成二维码来判断的情况
                boolean b = CommonUtils.isSpecialCharacter(resultStr);
                if (b) {
                    is_cN = true;
                }
                if (!is_cN) {
                    GB_Str = new String(resultStr.getBytes("ISO-8859-1"), "GB2312");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String result;
            if (is_cN) {
                result = UTF_Str;
            } else {
                result = GB_Str;
            }
            if (Constants.IN_TYPE.equals(type)) {
                ll_spec.setVisibility(View.GONE);
                ll_weight.setVisibility(View.GONE);
            }
            if (type.equals(Constants.OUT_WARE) || type.equals(Constants.CHECK)) {
                tv_addressWare.setText("无");
                tv_addressWare.setOnClickListener(null);
            }
            if (Constants.NO_PLATES.equals(scanState)) {
                if (Constants.QR.equals(codeType)) {
                    if (result.contains("|") && result.contains(":")) {
                        if (!result.contains("标签号")) {
                            String markNumStr = result;
                            for (int i = 0; i < 3; i++) {
                                markNumStr = markNumStr.substring(markNumStr.indexOf(":") + 1);
                            }
                            markNum = subStringUnCh(markNumStr);
//                String netWStr = result;
//                for (int i = 0; i < 7; i++) {
//                    netWStr = netWStr.substring(netWStr.indexOf(":") + 1);
//                }
//                netW = subStringUnCh(netWStr);
                            tv_markNum.setText(markNum);
//                tv_net_weight.setText(netW);
                        } else {
                            String markNumStr = result.substring(result.indexOf("标签号"));
                            markNum = subStringInfo(markNumStr);
//                String specStr = result.substring(result.indexOf("规格"));
//                spec = subStringInfo(specStr);
//                String netWStr = result.substring(result.indexOf("净重"));
//                netW = subStringInfo(netWStr);
//                netW = formatterWeight(netW);
//                String orderNumStr = result.substring(result.indexOf("订单号"));
//                inOrderNum = subStringInfo(orderNumStr);
//                String orderNameStr = result.substring(result.indexOf("品名"));
//                orderName = subStringInfo(orderNameStr);
//                String steelGradeStr = result.substring(result.indexOf("牌号"));
//                steelGrade = steelGradeStr.substring(steelGradeStr.indexOf(";") + 1, steelGradeStr.indexOf("|")).trim();
//                steelGrade = steelGrade.replace("+", "%2B");
                            tv_markNum.setText(markNum);
//                            tv_type.setText(spec);
//                tv_gross_weight.setText(grossW);
//                            tv_net_weight.setText(netW);
                            String numberStr = result.substring(result.indexOf("张数"));
                            String pieces = subStringInfo(numberStr);
                            boolean matches = pieces.matches("[0-9]+");
                            if (matches) {
                                sv_main.setVisibility(View.GONE);
                                tv_test.setVisibility(View.VISIBLE);
                                tv_test.setText(result);
                                btn_commit.setVisibility(View.INVISIBLE);
                                CommonUtils.showToast(this, "请选择'钢板/板材'进行扫描");
                                return;
                            }
                        }
                        showProgressDialog(getString(R.string.info_check));
                        WareInfoRequest wareInfoRequest = new WareInfoRequest(markNum);
                        sendRequest(wareInfoRequest, WareInfoResponse.class);
                    } else {
                        sv_main.setVisibility(View.GONE);
                        tv_test.setVisibility(View.VISIBLE);
                        tv_test.setText(result);
                        btn_commit.setVisibility(View.INVISIBLE);
                        CommonUtils.showToast(this, "内容不匹配，请重新扫描");
                    }
                } else if (Constants.CODE.equals(codeType)) {
                    markNum = resultStr.trim();
                    tv_markNum.setText(markNum);
                    showProgressDialog(getString(R.string.info_check));
                    WareInfoRequest wareInfoRequest = new WareInfoRequest(markNum);
                    sendRequest(wareInfoRequest, WareInfoResponse.class);
                }
            } else if (Constants.PLATES.equals(scanState)) {
                if (Constants.CODE.equals(codeType)) {
                    CommonUtils.showToast(this, "钢板需扫二维码,请重新扫描二维码");
                    sv_main.setVisibility(View.GONE);
                    tv_test.setVisibility(View.VISIBLE);
                    tv_test.setText(result);
                    btn_commit.setVisibility(View.INVISIBLE);
                } else if (Constants.QR.equals(codeType)) {
                    if (!result.contains("标签号")) {
                        sv_main.setVisibility(View.GONE);
                        tv_test.setVisibility(View.VISIBLE);
                        tv_test.setText(result);
                        btn_commit.setVisibility(View.INVISIBLE);
                        CommonUtils.showToast(this, "内容不匹配，请重新扫描");
                    } else {
                        ll_number.setVisibility(View.VISIBLE);
                        String markNumStr = result.substring(result.indexOf("标签号"));
                        markNum = subStringInfo(markNumStr);
                        String numberStr = result.substring(result.indexOf("张数"));
                        count = subStringInfo(numberStr);
                        boolean matches = count.matches("[0-9]+");
                        if (!matches) {
                            sv_main.setVisibility(View.GONE);
                            tv_test.setVisibility(View.VISIBLE);
                            tv_test.setText(result);
                            btn_commit.setVisibility(View.INVISIBLE);
                            CommonUtils.showToast(this, "请选择'钢卷/钢轧'进行扫描");
                            return;
                        }
                        tv_markNum.setText(markNum);
                        tv_number.setText(count);
                        showProgressDialog(getString(R.string.info_check));
                        WareInfoRequest wareInfoRequest = new WareInfoRequest(markNum);
                        sendRequest(wareInfoRequest, WareInfoResponse.class);
                    }
                }
            }
        }
    }

    private void initTypeHeader(Intent intent) {
        if (Constants.IN_WARE.equals(type)) {
            initHeader("入库");
            isLast = intent.getBooleanExtra(Constants.IS_LAST, false);
        } else if (Constants.CHANGE_WARE.equals(type)) {
            initHeader("移位");
        } else if (Constants.CHECK.equals(type)) {
            initHeader("盘点");
            btn_commit.setText("确认");
            curAddress = intent.getStringExtra(Constants.ADDRESS);
            tv_addressWare.setOnClickListener(null);
        } else if (Constants.SCAN_RESULT.equals(type)) {
            initHeader("条码扫描");
            btn_commit.setVisibility(View.INVISIBLE);
            ll_more.setVisibility(View.VISIBLE);
            ll_more.setOnClickListener(this);
//            initMorePopupWindow();
        } else if (Constants.OUT_WARE.equals(type)) {
            initHeader("出库");
            isLast = intent.getBooleanExtra(Constants.IS_LAST, false);
            tv_addressWare.setOnClickListener(null);
        } else if (Constants.REVIEW_TYPE.equals(type)) {
            initHeader("复核");
            tv_addressWare.setOnClickListener(null);
        }
    }

    private String formatterWeight(String netW) {
        netW = netW.replace("kg", "");
        float weight = Integer.parseInt(netW.trim());
        return weight / 1000 + "";
    }

    private void initWheelData() {
        for (int i = 0; i < 6; i++) {
            wareNums[i] = "0" + (i + 1);
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

    private String subStringUnCh(String str) {
        if (!str.contains("|")) {
            return str.trim();
        }
        return str.substring(0, str.indexOf("|")).trim();
    }

    private String subStringInfo(String str) {
        if (!str.contains("|")) {
            return str.substring(str.indexOf(":") + 1).trim();
        }
        return str.substring(str.indexOf(":") + 1, str.indexOf("|")).trim();
    }

//    @Override
//    public void onBackPressed() {
//        backWarm();
//    }

    @Override
    protected void initView() {
        tv_markNum = (TextView) findViewById(R.id.tv_markNum);
        ll_spec = (LinearLayout) findViewById(R.id.ll_spec);
        ll_weight = (LinearLayout) findViewById(R.id.ll_weight);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_net_weight = (TextView) findViewById(R.id.tv_net_weight);
//        tv_gross_weight = (TextView) findViewById(R.id.tv_gross_weight);
        tv_addressWare = (TextView) findViewById(R.id.tv_addressWare);
        tv_addressWare.setOnClickListener(this);
        tv_test = (TextView) findViewById(R.id.tv_test);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        ImageView iv_more = (ImageView) findViewById(R.id.search);
        iv_more.setImageResource(R.mipmap.and);
        ll_more = (LinearLayout) findViewById(R.id.ll_search);
        sv_main = (ScrollView) findViewById(R.id.sv_main);
        main_layout = findViewById(R.id.main_layout);
        ll_number = (LinearLayout) findViewById(R.id.ll_number);
        tv_number = (TextView) findViewById(R.id.tv_number);
    }

    private void initMorePopupWindow() {
        View contentView = View.inflate(this, R.layout.popupwindow_more, null);
        morePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        morePopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000"))); //设置背景
        morePopupWindow.setFocusable(true); //设置获取焦点
//        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        morePopupWindow.setOutsideTouchable(true);
        morePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        contentView.findViewById(R.id.ll_in_ware).setOnClickListener(this);
        contentView.findViewById(R.id.ll_out_ware).setOnClickListener(this);
        contentView.findViewById(R.id.ll_change_ware).setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
        isConnect = true;
        if (request instanceof InWareRequest) {
            InWareResponse inWareResponse = (InWareResponse) response;
            if (inWareResponse != null) {
                if (inWareResponse.getResponseCode().getCode() == 200) {
                    if (Constants.REQUEST_SUCCESS.equals(inWareResponse.getErrorMsg())) {
                        handler.sendEmptyMessage(FINISH);
                    } else {
                        new AlertView("提示", inWareResponse.getErrorMsg(), null, new String[]{"确定"}, null, this, AlertView.Style.Alert, null).setCancelable(false).show();
                    }
                } else {
                    CommonUtils.showToast(ScanResultActivity.this, inWareResponse.getErrorMsg());
                }
            }
        } else if (request instanceof WareInfoRequest) {
            WareInfoResponse wareInfoResponse = (WareInfoResponse) response;
            if (wareInfoResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(wareInfoResponse.getErrorMsg())) {
                    WareInfoResponse.DataEntity entity = wareInfoResponse.getData();
                    if (entity != null) {
                        inWarePro = true;
                        wareInfo = new WareInfo();
                        wareInfo.setData(entity);
                        markNum = wareInfo.getMarkNum();
                        id = wareInfo.getId();
                        count = wareInfo.getCount();
                        proType = wareInfo.getType();
                        tv_markNum.setText(markNum);
                        if ("1".equals(proType)) {
                            ll_number.setVisibility(View.VISIBLE);
                            tv_number.setText(count);
                        }
                        if (!TextUtils.isEmpty(wareInfo.getSpec())) {
                            tv_type.setText(wareInfo.getSpec());
                        }
                        if (!TextUtils.isEmpty(wareInfo.getNetWeight())) {
                            tv_net_weight.setText(wareInfo.getNetWeight() + "T");
                        }
                        proName = wareInfo.getProName();
                        if (this.type.equals(Constants.IN_WARE)) {
                            tv_addressWare.setOnClickListener(null);
                            btn_commit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CommonUtils.showToast(ScanResultActivity.this, "该货品已入库，无法再次入库");
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
//                            if (type.equals(Constants.OUT_WARE) || type.equals(Constants.IN_WARE)||type.equals(Constants.CHECK)) {
//                                tv_addressWare.setText("无");
//                                tv_addressWare.setOnClickListener(null);
////                                btn_commit.setOnClickListener(new View.OnClickListener() {
////                                    @Override
////                                    public void onClick(View v) {
////                                        CommonUtils.showToast(ScanResultActivity.this, "该货品已出库，无法再次出库");
////                                    }
////                                });
//                            } else
                            if (this.type.equals(Constants.CHANGE_WARE)) {
                                tv_addressWare.setText("无");
                                tv_addressWare.setOnClickListener(null);
                                btn_commit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CommonUtils.showToast(ScanResultActivity.this, "该货品不在库中，无法移位");
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
                                    CommonUtils.showToast(ScanResultActivity.this, "该货品不在库中，无法移位");
                                }
                            });
                        }
                    }
                } else {
                    CommonUtils.showToast(this, wareInfoResponse.getErrorMsg());
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
                CommonUtils.showToast(ScanResultActivity.this, changeWareResponse.getErrorMsg());
            }
        } else if (request instanceof OutWareRequest) {
            OutWareResponse outWareResponse = (OutWareResponse) response;
            if (outWareResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(ScanResultActivity.this, outWareResponse.getErrorMsg());
                if (Constants.REQUEST_SUCCESS.equals(outWareResponse.getErrorMsg())) {
//                    if (isLast) {
////                        ForUpRequest forUpRequest = new ForUpRequest(orderId, "1");
////                        sendRequest(forUpRequest, ForUpResponse.class);
//                        new AlertView("提示", "所有货品已扫描完毕，请货品上车后进行复核", null, new String[]{"确认"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
//                            @Override
//                            public void onItemClick(Object o, int position) {
//                                switch (position) {
//                                    case 0:
                    Intent intent = new Intent(Constants.ORDER_REFRESH);
                    sendBroadcast(intent);
                    handler.sendEmptyMessage(FINISH);
//                                        break;
//                                }
//                            }
//                        }).setCancelable(false).show();
//                        isLast = false;
//                    } else {
//                    handler.sendEmptyMessage(FINISH);
//                    }
                }
            }
        } else if (request instanceof ForUpRequest) {
            ForUpResponse forUpResponse = (ForUpResponse) response;
            if (forUpResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(ScanResultActivity.this, forUpResponse.getErrorMsg());
                if (Constants.REQUEST_SUCCESS.equals(forUpResponse.getErrorMsg())) {

                }
            }
        } else if (request instanceof CheckRequest) {
            CheckResponse checkResponse = (CheckResponse) response;
            if (checkResponse.getResponseCode().getCode() == 200) {
//                if (Constants.REQUEST_SUCCESS.equals(checkResponse.getErrorMsg())) {
//                    CheckStartRequest checkStartRequest = new CheckStartRequest(curAddress, realName, Constants.MODEL, checkTaskId);
//                    sendRequest(checkStartRequest, CheckStartResponse.class);
//                } else {
//                    CommonUtils.showToast(this, checkResponse.getErrorMsg());
//                }
            }
        } else if (request instanceof CheckStartRequest) {
            CheckStartResponse checkStartResponse = (CheckStartResponse) response;
            if (checkStartResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(checkStartResponse.getErrorMsg())) {
                    CommonUtils.showToast(ScanResultActivity.this, "提交完成");
                    setResult(Constants.RESULT_OK);
                    handler.sendEmptyMessage(FINISH);
                } else {
                    new AlertView("提示", checkStartResponse.getErrorMsg(), null, new String[]{"确定"}, null, this, AlertView.Style.Alert, null
                    ).setCancelable(false).show();
                }
            }
        }
    }

    private void toResult() {
        Intent intent = new Intent(this, OperateResultActivity.class);
        intent.putExtra(Constants.TYPE, type);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_addressWare:
                chooseAddress();
                break;
            case R.id.pop_confirm:
                confirmAddress();
                break;
            case R.id.pop_cancle:
                bottonPopupWindow.dismiss();
                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void commit() {
        String trim = tv_addressWare.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            CommonUtils.showToast(this, getString(R.string.address_not_choose));
            return;
        }
        if (Constants.CHANGE_WARE.equals(type)) {
            changeCommit();
        } else if (Constants.IN_WARE.equals(type)) {
            inCommit();
        } else if (Constants.CHECK.equals(type)) {
            checkCommit();
        } else if (Constants.OUT_WARE.equals(type)) {
            outCommit();
        } else if (Constants.REVIEW_TYPE.equals(type)) {
            reviewCommit();
        }
//        chooseAddress();
    }

    private void reviewCommit() {
        address = "";
        showProgressDialog(getString(R.string.commit_data));
        OutWareRequest outWareRequest = new OutWareRequest(markNum, realName, outOrderNum, address, model, proType, CommonUtils.getCurrentTime().substring(0, 10), groupId + "");
        sendRequest(outWareRequest, OutWareResponse.class);
    }

    private void outCommit() {
        showProgressDialog(getString(R.string.commit_data));
//        if ("0".equals(state)) {
        OutWareRequest outWareRequest = new OutWareRequest(markNum, realName, outOrderNum, address, model, proType, CommonUtils.getCurrentTime().substring(0, 10), null);
        sendRequest(outWareRequest, OutWareResponse.class);
//        } else if ("1".equals(state)) {
//            OutWareRequest outWareRequest = new OutWareRequest(markNum, realName, outOrderNum, address, model, "1");
//            sendRequest(outWareRequest, OutWareResponse.class);
//        }
    }

    private void checkCommit() {
//        if (!inWarePro) {
//            CheckRequest checkRequest = new CheckRequest(markNum, proName, "", curAddress, "0");
//            sendRequest(checkRequest, CheckResponse.class);
//        } else if (!TextUtils.isEmpty(address) && !curAddress.equals(address) && inWarePro) {
//            CheckRequest checkRequest = new CheckRequest(markNum, proName, address, curAddress, "2");
//            sendRequest(checkRequest, CheckResponse.class);
//        } else {
//            showProgressDialog(getString(R.string.commit_data));
//            CheckStartRequest checkStartRequest = new CheckStartRequest(curAddress, realName, Constants.MODEL, checkTaskId);
//            sendRequest(checkStartRequest, CheckStartResponse.class);
//        }
        showProgressDialog(getString(R.string.commit_data));
        CheckStartRequest checkStartRequest = new CheckStartRequest(curAddress, realName, checkTaskId, markNum, inWarePro ? "0" : "1");
        sendRequest(checkStartRequest, CheckStartResponse.class);
    }

    private void inCommit() {
        showProgressDialog(getString(R.string.commit_data));
        boolean isNetWork = CommonUtils.checkNetWork(this);
        if (isNetWork) {
            InWareRequest inWareRequest = new InWareRequest(markNum, realName, inOrderNum, address, "1", netW, spec, orderName, "0", steelGrade, model, count, Constants.PLATES.equals(scanState) ? "1" : "0", groupId + "");
            sendRequest(inWareRequest, InWareResponse.class);
        } else {
//            WareInfo wareInfo = new WareInfo(markNum, address, spec, netW, inOrderNum, orderName, steelGrade, false);
//            try {
//                wareDao.saveOrUpdate(wareInfo);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            new AlertView("提示", getString(R.string.out_line_tip), null, new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
//                @Override
//                public void onItemClick(Object o, int position) {
//                    finish();
//                }
//            }).setCancelable(false).show();
            CommonUtils.showToast(this, "当前无网络信号");
        }
    }

    private void changeCommit() {
        if (TextUtils.isEmpty(address)) {
            CommonUtils.showToast(this, "库中无该货品，无法移位");
            return;
        }
        showProgressDialog(getString(R.string.commit_data));
        ChangeWareRequest changeWareRequest = new ChangeWareRequest(markNum, address, realName, model);
        sendRequest(changeWareRequest, ChangeWareResponse.class);
    }

    private void showProgressDialog(String content) {
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
                        CommonUtils.showToast(ScanResultActivity.this, getString(R.string.poor_signal) + "，请重新尝试");
                        btn_commit.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }, 5000);
    }

    private void showMore() {
        morePopupWindow.showAsDropDown(ll_more);
        backgroundAlpha(0.8f);
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
        if (Constants.CHECK.equals(type) || Constants.SCAN_RESULT.equals(type)) {
            Intent intent = new Intent(this, WareHouseActivity.class);
            intent.putExtra(Constants.TYPE, Constants.CHECK);
            intent.putExtra(Constants.ADDRESS, address);
            startActivity(intent);
        } else if (Constants.OUT_WARE.equals(type)) {
            Intent intent = new Intent(ScanResultActivity.this, WareHouseActivity.class);
            intent.putExtra(Constants.ADDRESS, address);
            intent.putExtra(Constants.TYPE, Constants.OUT_WARE);
            startActivity(intent);
        } else {
            bottonPopupWindowView = new BottonPopupWindowView(ware, floor, getString(R.string.choose_address), type);
            bottonPopupWindow = bottonPopupWindowView.creat(this, wareNums, floors, this);
            bottonPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
//            bottonPopupWindowView.initSeatTable(seatRows, seatColumns, unSeatRows, unSeatColums, unFullRows, unFullColums);
            bottonPopupWindowView.show(btn_commit, Gravity.BOTTOM, 0, 0, address);
            backgroundAlpha(0.8f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }
}
