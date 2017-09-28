package com.hgad.warehousemanager.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
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
import com.hgad.warehousemanager.bean.request.ForUpRequest;
import com.hgad.warehousemanager.bean.request.InWareRequest;
import com.hgad.warehousemanager.bean.request.OutWareRequest;
import com.hgad.warehousemanager.bean.request.WareInfoRequest;
import com.hgad.warehousemanager.bean.response.ChangeWareResponse;
import com.hgad.warehousemanager.bean.response.CheckResponse;
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
import java.sql.SQLException;
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
    private String row = "1";
    private String column = "1";
    private String floor = "1";
    private String ware = "1";
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
    private String username;
    private boolean isLast;
    private String markNum;
    private boolean isConnect;
    String[] wareNums = new String[6];
    String[] floors = new String[27];
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
    private MediaPlayer mediaPlayer;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_scan_result);
    }

    @Override
    protected void initData() {
        model = Constants.MODEL;
        initWheelData();
        wareDao = new BaseDaoImpl<>(this, WareInfo.class);
        Intent intent = getIntent();
        type = intent.getStringExtra(Constants.TYPE);
        username = SPUtils.getString(this, SPConstants.USER_NAME);
        OrderInfo orderInfo = (OrderInfo) intent.getSerializableExtra(Constants.ORDER_INFO);
        if (orderInfo != null) {
            orderId = orderInfo.getTaskId();
            outOrderNum = orderInfo.getOrderNum();
        }
        initTypeHeader(intent);
        String resultStr = intent.getStringExtra(Constants.SCAN_RESULT);
        getResultIfnotNull(resultStr);
        wareInfo = (WareInfo) intent.getSerializableExtra(Constants.WARE_INFO);
        getWareInfoIfnotNull();
        List<WareInfo> data = (List<WareInfo>) intent.getSerializableExtra(Constants.LIST_DATA);
        getWareListIfnotNull(data);
    }

    private void getWareListIfnotNull(List<WareInfo> data) {
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
                    tv_addressWare.setText(address);
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
                address = ware + "仓" + row + "排" + column + "垛" + floor + "号";
                CommonUtils.stringInterceptionChangeLarge(tv_addressWare, address, "仓", "排", "垛", "号");
            }
            state = wareInfo.getState();
            if (Constants.IN_TYPE.equals(type) && "1".equals(state)) {
                btn_commit.setVisibility(View.INVISIBLE);
                tv_addressWare.setOnClickListener(null);
            }
            if (Constants.OUT_TYPE.equals(type) && !("0".equals(state))) {
                btn_commit.setVisibility(View.INVISIBLE);
            }
            if (Constants.REVIEW_TYPE.equals(type) && !"1".equals(state)) {
                btn_commit.setVisibility(View.INVISIBLE);
            }
//            if (TextUtils.isEmpty(wareInfoAddress)) {
//                tv_addressWare.setText("无");
//            }
        }
    }

    private void getResultIfnotNull(String resultStr) {
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
            if (!result.contains("标签号")) {
                sv_main.setVisibility(View.GONE);
                tv_test.setText(result);
                btn_commit.setVisibility(View.INVISIBLE);
            } else {
                String markNumStr = result.substring(result.indexOf("标签号"));
                markNum = subStringInfo(markNumStr);
                String specStr = result.substring(result.indexOf("规格"));
                spec = subStringInfo(specStr);
                String netWStr = result.substring(result.indexOf("净重"));
                netW = subStringInfo(netWStr);
                netW = formatterWeight(netW);
                String orderNumStr = result.substring(result.indexOf("订单号"));
                inOrderNum = subStringInfo(orderNumStr);
                String orderNameStr = result.substring(result.indexOf("品名"));
                orderName = subStringInfo(orderNameStr);
                String steelGradeStr = result.substring(result.indexOf("牌号"));
                steelGrade = steelGradeStr.substring(steelGradeStr.indexOf(";") + 1, steelGradeStr.indexOf("|")).trim();
                steelGrade = steelGrade.replace("+", "%2B");
                tv_markNum.setText(markNum);
                tv_type.setText(spec);
//                tv_gross_weight.setText(grossW);
                tv_net_weight.setText(netW);
                if (type.equals(Constants.OUT_WARE)) {
                    tv_addressWare.setText("无");
                    tv_addressWare.setOnClickListener(null);
                }
//                boolean checkNetWork = CommonUtils.checkNetWork(this);
//                if (checkNetWork) {
                showDialog(getString(R.string.info_check));
                WareInfoRequest wareInfoRequest = new WareInfoRequest(markNum);
                sendRequest(wareInfoRequest, WareInfoResponse.class);
//                    return;
//                } else {
//                    CommonUtils.showToast(this, getString(R.string.check_net));
//                }
            }
        }
    }

//    /**
//     * 初始化 报警音频
//     */
//    private void initBeepSound() {
//        if (mediaPlayer == null) {
//            // 在stream_system音量不可调的，用户发现它太大声，所以我们现在播放的音乐流。
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//            // 初始化 媒体播放器
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setOnCompletionListener(null);
//            AssetFileDescriptor file = getResources().openRawResourceFd(
//                    R.raw.jentlemo_kanon);
//            try {
//                mediaPlayer.setDataSource(file.getFileDescriptor(),
//                        file.getStartOffset(), file.getLength());
//
//                // 关闭 资源文件管理器
//                file.close();
//                mediaPlayer.setVolume(30f, 30f);
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                mediaPlayer = null; // 异常 释放播放器对象
//            }
//        }
//    }

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
            initMorePopupWindow();
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

    private String subStringInfo(String str) {
        if (!str.contains("|")) {
            return str.substring(str.indexOf(":") + 1).trim();
        }
        return str.substring(str.indexOf(":") + 1, str.indexOf("|")).trim();
    }

    @Override
    public void onBackPressed() {
        backWarm();
    }

    @Override
    protected void initView() {
        tv_markNum = (TextView) findViewById(R.id.tv_markNum);
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
//        initBeepSound();
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
                    CommonUtils.showToast(ScanResultActivity.this, inWareResponse.getErrorMsg());
                    if (Constants.REQUEST_SUCCESS.equals(inWareResponse.getErrorMsg())) {
                        handler.sendEmptyMessage(FINISH);
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
                        wareInfo = new WareInfo();
                        wareInfo.setData(entity);
                        markNum = wareInfo.getMarkNum();
                        id = wareInfo.getId();
                        tv_markNum.setText(markNum);
                        tv_type.setText(wareInfo.getSpec());
                        tv_net_weight.setText(wareInfo.getNetWeight() + "T");
                        proName = wareInfo.getProName();
                        if (type.equals(Constants.IN_WARE)) {
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
                            String addressStr = CommonUtils.formatAddress(address);
                            CommonUtils.stringInterceptionChangeLarge(tv_addressWare, addressStr, "仓", "排", "垛", "号");
                            if (type.equals(Constants.OUT_WARE)) {
                                tv_addressWare.setOnClickListener(null);
                            }
                        } else {
                            if (type.equals(Constants.OUT_WARE) || type.equals(Constants.IN_WARE)) {
                                tv_addressWare.setText("无");
                                tv_addressWare.setOnClickListener(null);
//                                btn_commit.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        CommonUtils.showToast(ScanResultActivity.this, "该货品已出库，无法再次出库");
//                                    }
//                                });
                            } else if (type.equals(Constants.CHANGE_WARE)) {
                                tv_addressWare.setText("无");
                                tv_addressWare.setOnClickListener(null);
                                btn_commit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CommonUtils.showToast(ScanResultActivity.this, "该货品不在库中，无法移位");
                                    }
                                });
                            } else if (Constants.REVIEW_TYPE.equals(type)) {
                                tv_addressWare.setText("无");
                                tv_addressWare.setOnClickListener(null);
                            }
                        }
                    } else {
                        CommonUtils.showToast(this, wareInfoResponse.getErrorMsg());
                    }
                }
            }
        } else if (request instanceof ChangeWareRequest) {
            ChangeWareResponse changeWareResponse = (ChangeWareResponse) response;
            if (changeWareResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(ScanResultActivity.this, changeWareResponse.getErrorMsg());
                if (Constants.REQUEST_SUCCESS.equals(changeWareResponse.getErrorMsg())) {
                    handler.sendEmptyMessage(FINISH);
                }
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
                CommonUtils.showToast(ScanResultActivity.this, "提交完成");
                setResult(Constants.RESULT_OK);
//                toResult();
                handler.sendEmptyMessage(FINISH);
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
            case R.id.ll_search:
                showMore();
                break;
            case R.id.ll_in_ware:
                inWare();
                break;
            case R.id.ll_out_ware:
                outWare();
                break;
            case R.id.ll_change_ware:
                changeWare();
                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void changeWare() {
        morePopupWindow.dismiss();
        Intent intent = new Intent(this, ScanResultActivity.class);
        intent.putExtra(Constants.TYPE, Constants.CHANGE_WARE);
        startActivity(intent);
    }

    private void inWare() {
        morePopupWindow.dismiss();
        Intent intent = new Intent(this, ScanResultActivity.class);
        intent.putExtra(Constants.TYPE, Constants.IN_WARE);
        startActivity(intent);
    }

    private void outWare() {
        morePopupWindow.dismiss();
        Intent intent = new Intent(this, ScanResultActivity.class);
        intent.putExtra(Constants.TYPE, Constants.OUT_WARE);
        startActivity(intent);
    }

    private void changeChoose() {
//        bottonPopupWindowView.change();
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
    }

    private void reviewCommit() {
        address = CommonUtils.formatAddressForUse(address);
        showDialog(getString(R.string.commit_data));
        OutWareRequest outWareRequest = new OutWareRequest(markNum, username, outOrderNum, address, model, CommonUtils.getCurrentTime().substring(0, 10));
        sendRequest(outWareRequest, OutWareResponse.class);
    }

    private void outCommit() {
        address = CommonUtils.formatAddressForUse(address);
        showDialog(getString(R.string.commit_data));
//        if ("0".equals(state)) {
        OutWareRequest outWareRequest = new OutWareRequest(markNum, username, outOrderNum, address, model, CommonUtils.getCurrentTime().substring(0, 10));
        sendRequest(outWareRequest, OutWareResponse.class);
//        } else if ("1".equals(state)) {
//            OutWareRequest outWareRequest = new OutWareRequest(markNum, username, outOrderNum, address, model, "1");
//            sendRequest(outWareRequest, OutWareResponse.class);
//        }
    }

    private void checkCommit() {

        showDialog(getString(R.string.commit_data));
        if (!curAddress.equals(address)) {
            CheckRequest checkRequest = new CheckRequest(markNum, proName, address, curAddress);
            sendRequest(checkRequest, CheckResponse.class);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    customProgressDialog.dismiss();
                    setResult(Constants.RESULT_OK);
                    finish();
                }
            }, 1000);
        }
    }

    private void inCommit() {
        address = CommonUtils.formatAddressForUse(address);
        showDialog(getString(R.string.commit_data));
        boolean isNetWork = CommonUtils.checkNetWork(this);
        if (isNetWork) {
            InWareRequest inWareRequest = new InWareRequest(markNum, username, inOrderNum, address, "1", netW, spec, orderName, "0", steelGrade, model);
            sendRequest(inWareRequest, InWareResponse.class);
        } else {
            WareInfo wareInfo = new WareInfo(markNum, address, spec, netW, inOrderNum, orderName, steelGrade, false);
            try {
                wareDao.saveOrUpdate(wareInfo);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            new AlertView("提示", "当前网络无法连接到服务器，数据将会保存在本地，之后可进行提交", null, new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    finish();
                }
            }).setCancelable(false).show();
        }
    }

    private void changeCommit() {
        address = CommonUtils.formatAddressForUse(address);
        showDialog(getString(R.string.commit_data));
        ChangeWareRequest changeWareRequest = new ChangeWareRequest(markNum, address, username, model);
        sendRequest(changeWareRequest, ChangeWareResponse.class);
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
                        CommonUtils.showToast(ScanResultActivity.this, getString(R.string.poor_signal));
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
        address = ware + "仓" + row + "排" + column + "垛" + floor + "号";
        new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT).setTitle("提示")
                .setMessage("选择的货位为" + address)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.stringInterceptionChangeLarge(tv_addressWare, address, "仓", "排", "垛", "号");
                        bottonPopupWindow.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .setCancelable(false)
                .show();
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
            bottonPopupWindowView.show(btn_commit, Gravity.BOTTOM, 0, 0);
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
