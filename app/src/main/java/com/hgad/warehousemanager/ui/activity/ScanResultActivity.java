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
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.view.BottonPopupWindowUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.io.UnsupportedEncodingException;

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
                case FINISH:
                    finish();
                    break;
            }
        }
    };
    private CustomProgressDialog customProgressDialog;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_scan_result);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra(Constants.TYPE);
        if (Constants.IN_WARE.equals(type)) {
            initHeader("入库");
        } else if (Constants.CHANGE_WARE.equals(type)) {
            initHeader("移位");
        } else if (Constants.CHECK.equals(type)) {
            initHeader("盘点");
            btn_commit.setText("盘点");
//            btn_commit.setVisibility(View.GONE);
            address = "2 仓  3 排  4 垛  2 层";
            CommonUtils.stringInterceptionChangeLarge(tv_addressWare, address, new String[]{ware, row, column, floor}, "仓", "排", "垛", "层");
        } else if (Constants.SCAN_RESULT.equals(type)) {
            initHeader("条码扫描");
            btn_commit.setVisibility(View.GONE);
            address = "2 仓  3 排  4 垛  2 层";
            CommonUtils.stringInterceptionChangeLarge(tv_addressWare, address, new String[]{ware, row, column, floor}, "仓", "排", "垛", "层");
            ll_more.setVisibility(View.VISIBLE);
            ll_more.setOnClickListener(this);
            initMorePopupWindow();
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
            } else {
                String mark = result.substring(result.indexOf("标签号") + 5, result.indexOf("订单号") - 2).trim();
                String spec = result.substring(result.indexOf("规格") + 8, result.indexOf("加工性能") - 2).trim();
                String grossW = result.substring(result.indexOf("毛重") + 4, result.indexOf("净重") - 2).trim();
                String netW = result.substring(result.indexOf("净重") + 4, result.indexOf("日期") - 2).trim();
                tv_markNum.setText(mark);
                tv_type.setText(spec);
                tv_gross_weight.setText(grossW);
                tv_net_weight.setText(netW);
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
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

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

                break;
            case R.id.ll_out_ware:

                break;
            case R.id.ll_change_ware:

                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void changeChoose() {
        bottonPopupWindowUtils.change();
    }

    private void commit() {
        if (TextUtils.isEmpty(tv_addressWare.getText().toString().trim())) {
            CommonUtils.showToast(this, "未选择仓库位置");
            return;
        }
        if (Constants.CHANGE_WARE.equals(type)) {
            changeCommit();
        } else if (Constants.IN_WARE.equals(type)) {
            inCommit();
        } else if (Constants.CHECK.equals(type)) {
            checkCommit();
        }
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
        customProgressDialog = new CustomProgressDialog(this, "信息校验中");
//        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
    }

    private void inCommit() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                }
                CommonUtils.showToast(ScanResultActivity.this, "入库成功");
                finish();
            }
        }, 2000);
        customProgressDialog = new CustomProgressDialog(this, "数据提交中");
//        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
    }

    private void changeCommit() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                }
                CommonUtils.showToast(ScanResultActivity.this, "移位成功");
                finish();
            }
        }, 2000);
        customProgressDialog = new CustomProgressDialog(this, "数据提交中");
//        customProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
    }

    private void showMore() {
        morePopupWindow.showAsDropDown(ll_more);
        backgroundAlpha(0.8f);
    }

    private void confirmAddress() {
        if (bottonPopupWindowUtils.getRow() == null
                || bottonPopupWindowUtils.getColumn() == null) {
            CommonUtils.showToast(this, "还未选择地址");
            return;
        }
        ware = bottonPopupWindowUtils.getWare();
        row = bottonPopupWindowUtils.getRow();
        column = bottonPopupWindowUtils.getColumn();
        floor = bottonPopupWindowUtils.getFloor();
        address = ware + " 仓  " + row + " 排  " + column + " 垛  " + floor + " 层  ";
        CommonUtils.stringInterceptionChangeLarge(tv_addressWare, address, new String[]{ware, row, column, floor}, "仓", "排", "垛", "层");
        bottonPopupWindow.dismiss();
    }

    private void chooseAddress() {
        if (Constants.CHECK.equals(type) || Constants.SCAN_RESULT.equals(type)) {
            Intent intent = new Intent(this, WareHouseActivity.class);
            intent.putExtra(Constants.TYPE, Constants.CHECK);
            intent.putExtra(Constants.ADDRESS, address);
            startActivity(intent);
        } else {
            bottonPopupWindowUtils = new BottonPopupWindowUtils(ware, floor, getResources().getString(R.string.choose_address));
            bottonPopupWindow = bottonPopupWindowUtils.creat(this, wareNums, rows, columns, floors, this);
            bottonPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
            bottonPopupWindowUtils.initSeatTable(seatRows, seatColumns, unSeatRows, unSeatColums, unFullRows, unFullColums);
            bottonPopupWindowUtils.show(btn_commit, Gravity.BOTTOM, 0, 0);
            backgroundAlpha(0.8f);
        }
    }

    String[] wareNums = {"1", "2", "3", "4", "5", "6"};
    String[] rows = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] columns = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] floors = {"1", "2", "3"};

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
