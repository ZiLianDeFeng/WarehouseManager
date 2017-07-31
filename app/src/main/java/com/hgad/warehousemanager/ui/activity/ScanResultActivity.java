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

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.ChangeWareRequest;
import com.hgad.warehousemanager.bean.request.InWareRequest;
import com.hgad.warehousemanager.bean.request.WareInfoRequest;
import com.hgad.warehousemanager.bean.response.ChangeWareResponse;
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
    private TextView tv_gross_weight;
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
    private BottonPopupWindowUtils bottonPopupWindowUtils;
    private ScrollView sv_main;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    };
    private CustomProgressDialog customProgressDialog;
    private WareInfo wareInfo;
    private String username;
    private boolean isLast;
    private String markNum;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_scan_result);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra(Constants.TYPE);
        username = SPUtils.getString(this, SPConstants.USER_NAME);
        if (Constants.IN_WARE.equals(type)) {
            initHeader("入库");
            isLast = intent.getBooleanExtra(Constants.IS_LAST, false);
        } else if (Constants.CHANGE_WARE.equals(type)) {
            initHeader("移位");
        } else if (Constants.CHECK.equals(type)) {
            initHeader("盘点");
            btn_commit.setText("盘点");
        } else if (Constants.SCAN_RESULT.equals(type)) {
            initHeader("条码扫描");
            btn_commit.setVisibility(View.INVISIBLE);
            ll_more.setVisibility(View.VISIBLE);
            ll_more.setOnClickListener(this);
            initMorePopupWindow();
        } else if (Constants.OUT_WARE.equals(type)) {
            initHeader("出库");
            isLast = intent.getBooleanExtra(Constants.IS_LAST, false);
        }
        String resultStr = intent.getStringExtra(Constants.SCAN_RESULT);
        if (resultStr != null) {
            String UTF_Str = "";
            String GB_Str = "";
            boolean is_cN = false;
            try {
                UTF_Str = new String(resultStr.getBytes("ISO-8859-1"), "UTF-8");
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
                markNum = result.substring(result.indexOf("标签号") + 5, result.indexOf("订单号") - 2).trim();
                String spec = result.substring(result.indexOf("规格") + 8, result.indexOf("加工性能") - 2).trim();
                String grossW = result.substring(result.indexOf("毛重") + 4, result.indexOf("净重") - 2).trim();
                String netW = result.substring(result.indexOf("净重") + 4, result.indexOf("日期") - 2).trim();
                tv_markNum.setText(markNum);
                tv_type.setText(spec);
                tv_gross_weight.setText(grossW);
                tv_net_weight.setText(netW);
                if (type.equals(Constants.OUT_WARE)) {
                    tv_addressWare.setText("无");
                    tv_addressWare.setOnClickListener(null);
                }
                boolean checkNetWork = CommonUtils.checkNetWork(this);
                if (checkNetWork) {
                    showDialog(getResources().getString(R.string.info_check));
                    WareInfoRequest wareInfoRequest = new WareInfoRequest(markNum);
                    sendRequest(wareInfoRequest, WareInfoResponse.class);
                    return;
                } else {
                    CommonUtils.showToast(this, "请检查网络");
                }
            }
        }
        wareInfo = (WareInfo) intent.getSerializableExtra(Constants.WARE_INFO);
        if (wareInfo != null) {
            markNum = wareInfo.getMarkNum();
            tv_markNum.setText(markNum);
            tv_type.setText(wareInfo.getSpec());
            tv_gross_weight.setText(wareInfo.getGrossWeight() + "kg");
            tv_net_weight.setText(wareInfo.getNetWeight() + "kg");
            String wareInfoAddress = wareInfo.getAddress();
            if (!TextUtils.isEmpty(wareInfoAddress)) {
                ware = wareInfoAddress.substring(0, 2);
                row = wareInfoAddress.substring(2, 4);
                column = wareInfoAddress.substring(4, 6);
                floor = wareInfoAddress.substring(6, 8);
                address = ware + "仓" + row + "排" + column + "垛" + floor + "层";
                CommonUtils.stringInterceptionChangeLarge(tv_addressWare, address, new String[]{ware, row, column, floor}, "仓", "排", "垛", "层");
            }

            String state = wareInfo.getState();
            if (!"0".equals(state)) {
                btn_commit.setVisibility(View.INVISIBLE);
                tv_addressWare.setOnClickListener(null);
                if (TextUtils.isEmpty(wareInfoAddress)) {
                    tv_addressWare.setText("无");
                }
            }
        }
        List<WareInfo> data = (List<WareInfo>) intent.getSerializableExtra(Constants.LIST_DATA);
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

    @Override
    public void onBackPressed() {
        backWarm();
    }

    @Override
    protected void initView() {
        tv_markNum = (TextView) findViewById(R.id.tv_markNum);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_net_weight = (TextView) findViewById(R.id.tv_net_weight);
        tv_gross_weight = (TextView) findViewById(R.id.tv_gross_weight);
        tv_addressWare = (TextView) findViewById(R.id.tv_addressWare);
        tv_addressWare.setOnClickListener(this);
        tv_test = (TextView) findViewById(R.id.tv_test);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        ImageView iv_more = (ImageView) findViewById(R.id.search);
        iv_more.setImageResource(R.mipmap.and);
        ll_more = (LinearLayout) findViewById(R.id.ll_search);
        sv_main = (ScrollView) findViewById(R.id.sv_main);
    }

    final int[] seatRows = new int[]{4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6};
    final int[] seatColumns = new int[]{4, 5, 6, 7, 8, 9, 10, 4, 5, 6, 7, 8, 9, 10, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    final int[] unSeatRows = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4};
    final int[] unSeatColums = new int[]{1, 2, 3, 4, 12, 13, 14, 15, 1, 2, 3, 13, 14, 15, 1, 2, 14, 15, 1, 15};
    final int[] unFullRows = new int[]{7, 7, 7, 8, 8, 8};
    final int[] unFullColums = new int[]{14, 12, 13, 13, 14, 12};

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
        if (request instanceof InWareRequest) {
            InWareResponse inWareResponse = (InWareResponse) response;
            if (inWareResponse != null) {
                if (inWareResponse.getResponseCode().getCode() == 200) {
                    CommonUtils.showToast(ScanResultActivity.this, inWareResponse.getErrorMsg());
                    if ("请求成功".equals(inWareResponse.getErrorMsg())) {
                        setResult(Constants.RESULT_OK);
                        finish();
                    }
                } else {
                    CommonUtils.showToast(ScanResultActivity.this, inWareResponse.getErrorMsg());
                }
            }
        } else if (request instanceof WareInfoRequest) {
            WareInfoResponse wareInfoResponse = (WareInfoResponse) response;
            if (wareInfoResponse.getResponseCode().getCode() == 200) {
                if ("请求成功".equals(wareInfoResponse.getErrorMsg())) {
                    WareInfoResponse.DataEntity entity = wareInfoResponse.getData();
                    if (entity != null) {
                        if (!TextUtils.isEmpty(wareInfoResponse.getData().getPositionCode())) {
                            wareInfo = new WareInfo();
                            wareInfo.setData(entity);
                            markNum = wareInfo.getMarkNum();
                            tv_markNum.setText(markNum);
                            tv_type.setText(wareInfo.getSpec());
                            tv_gross_weight.setText(wareInfo.getGrossWeight() + "kg");
                            tv_net_weight.setText(wareInfo.getNetWeight() + "kg");
                            address = wareInfo.getAddress();
                            String str = CommonUtils.formatAddress(address);
                            tv_addressWare.setText(str);
                            if (type.equals(Constants.IN_WARE)) {
                                tv_addressWare.setOnClickListener(null);
                                btn_commit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CommonUtils.showToast(ScanResultActivity.this, "该货品已入库，无法再次入库");
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
                                        CommonUtils.showToast(ScanResultActivity.this, "该货品已出库，无法再次出库");
                                    }
                                });
                            } else if (type.equals(Constants.CHANGE_WARE)) {
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
                        CommonUtils.showToast(this, "服务器中查询不到该货品");
                    }
                }
            }
        } else if (request instanceof ChangeWareRequest) {
            ChangeWareResponse changeWareResponse = (ChangeWareResponse) response;
            if (changeWareResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(ScanResultActivity.this, changeWareResponse.getErrorMsg());
                if ("请求成功".equals(changeWareResponse.getErrorMsg())) {
                    finish();
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
        bottonPopupWindowUtils.change();
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
        }
    }

    private void outCommit() {
        address = address.replace("仓", "");
        address = address.replace("排", "");
        address = address.replace("垛", "");
        address = address.replace("层", "");
        showDialog(getString(R.string.commit_data));
        InWareRequest inWareRequest = new InWareRequest(wareInfo.getMarkNum(), username, isLast ? wareInfo.getTaskId() : null, "1", address);
        sendRequest(inWareRequest, InWareResponse.class);
    }

    private void checkCommit() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                }
                CommonUtils.showToast(ScanResultActivity.this, "信息正确");
                finish();
            }
        }, 2000);
        showDialog(getString(R.string.info_check));
    }

    private void inCommit() {
//        String currentTime = CommonUtils.getCurrentTime();
        address = address.replace("仓", "");
        address = address.replace("排", "");
        address = address.replace("垛", "");
        address = address.replace("层", "");
        showDialog(getString(R.string.commit_data));
        InWareRequest inWareRequest = new InWareRequest(markNum, username, isLast ? wareInfo.getTaskId() : null, "0", address);
        sendRequest(inWareRequest, InWareResponse.class);
    }

    private void changeCommit() {
        address = address.replace("仓", "");
        address = address.replace("排", "");
        address = address.replace("垛", "");
        address = address.replace("层", "");
        showDialog(getString(R.string.commit_data));
        ChangeWareRequest changeWareRequest = new ChangeWareRequest(markNum, address, username);
        sendRequest(changeWareRequest, ChangeWareResponse.class);
    }

    private void showDialog(String content) {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(this, content);
        }
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        customProgressDialog.setContent(content);
    }

    private void showMore() {
        morePopupWindow.showAsDropDown(ll_more);
        backgroundAlpha(0.8f);
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
            bottonPopupWindowUtils = new BottonPopupWindowUtils(ware, floor, getString(R.string.choose_address));
            bottonPopupWindow = bottonPopupWindowUtils.creat(this, wareNums, rows, columns, floors, this);
            bottonPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
//            bottonPopupWindowUtils.initSeatTable(seatRows, seatColumns, unSeatRows, unSeatColums, unFullRows, unFullColums);
            bottonPopupWindowUtils.show(btn_commit, Gravity.BOTTOM, 0, 0);
            backgroundAlpha(0.8f);
        }
    }

    String[] wareNums = {"01", "02", "03", "04", "05", "06"};
    String[] rows = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};
    String[] columns = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};
    String[] floors = {"01", "02", "03"};

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
