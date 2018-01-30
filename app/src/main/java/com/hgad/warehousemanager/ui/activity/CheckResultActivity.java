package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.WareInfoRequest;
import com.hgad.warehousemanager.bean.response.WareInfoResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;
import com.hgad.warehousemanager.view.WritePadDialog;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/7/28.
 */
public class CheckResultActivity extends BaseActivity {

    private TextView tv_markNum;
    private TextView tv_type;
    private TextView tv_net_weight;
    //    private TextView tv_gross_weight;
    private TextView tv_addressWare;
    private RadioGroup rg;
    private RadioButton rb_same;
    private RadioButton rb_different;
    private String result;
    private CustomProgressDialog customProgressDialog;
    private String markNum;
    private WareInfo wareInfo;
    private Handler handler = new Handler();
    private LinearLayout ll_real_address;
    private EditText et_real_address;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_check_result);
    }

    @Override
    protected void initData() {
        initHeader("盘点结果");
        Intent intent = getIntent();
        wareInfo = (WareInfo) intent.getSerializableExtra(Constants.WARE_INFO);
        if (wareInfo != null) {
            markNum = wareInfo.getMarkNum();
            tv_markNum.setText(markNum);
            tv_type.setText(wareInfo.getSpec());
//            tv_gross_weight.setText(wareInfo.getGrossWeight() + "kg");
            tv_net_weight.setText(wareInfo.getNetWeight() + "kg");
            String address = wareInfo.getAddress();
            address = CommonUtils.formatAddress(address);
            CommonUtils.stringInterceptionChangeLarge(tv_addressWare, address, "仓", "排", "垛", "号");
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
                CommonUtils.showToast(this, "扫描的信息内容不匹配");
                finish();
            } else {
                String markNumStr = result.substring(result.indexOf("标签号"));
                markNum = subStringInfo(markNumStr);
                String specStr = result.substring(result.indexOf("规格"));
                String spec = subStringInfo(specStr);
                String netWStr = result.substring(result.indexOf("净重"));
                String netW = subStringInfo(netWStr);
                String orderNumStr = result.substring(result.indexOf("订单号"));
                String orderNum = subStringInfo(orderNumStr);
                String orderNameStr = result.substring(result.indexOf("品名"));
                String orderName = subStringInfo(orderNameStr);
                tv_markNum.setText(markNum);
                tv_type.setText(spec);
                tv_net_weight.setText(netW);
                boolean checkNetWork = CommonUtils.checkNetWork(this);
                if (checkNetWork) {
                    showDialog(getString(R.string.info_check));
                    WareInfoRequest wareInfoRequest = new WareInfoRequest(markNum);
                    sendRequest(wareInfoRequest, WareInfoResponse.class);
                    return;
                } else {
                    CommonUtils.showToast(this, "请检查网络");
                }
            }
        }
        chooseResult();
    }

    private String subStringInfo(String str) {
        return str.substring(str.indexOf(":") + 1, str.indexOf("|")).trim();
    }

    private void showEditDialog(final int orderId) {
        WritePadDialog writeTabletDialog = new WritePadDialog(
                this, new HandwritingActivity.DialogListener() {
            @Override
            public void refreshActivity(Object object) {

//                mSignBitmap = (Bitmap) object;
////                signPath = createFile();
//                final File handWriteFile = createFile();
//                            /*BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inSampleSize = 15;
//                            options.inTempStorage = new byte[5 * 1024];
//                            Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);*/
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        commit(handWriteFile, orderId);
//                    }
//                }).start();
            }
        });
        writeTabletDialog.setCanceledOnTouchOutside(false);
        writeTabletDialog.show();
    }

    @Override
    protected void initView() {
        tv_markNum = (TextView) findViewById(R.id.tv_markNum);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_net_weight = (TextView) findViewById(R.id.tv_net_weight);
//        tv_gross_weight = (TextView) findViewById(R.id.tv_gross_weight);
        tv_addressWare = (TextView) findViewById(R.id.tv_addressWare);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        ll_real_address = (LinearLayout) findViewById(R.id.ll_real_address);
        et_real_address = (EditText) findViewById(R.id.et_real_addressWare);
//        ImageView iv_more = (ImageView) findViewById(R.id.search);
//        iv_more.setImageResource(R.mipmap.and);
//        findViewById(R.id.ll_search).setOnClickListener(this);
        rg = (RadioGroup) findViewById(R.id.rg_result);
        rb_same = (RadioButton) findViewById(R.id.rb_same);
        rb_different = (RadioButton) findViewById(R.id.rb_different);
    }

    private void chooseResult() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb_same.getId() == checkedId) {
                    result = rb_same.getText().toString().trim();
                    ll_real_address.setVisibility(View.GONE);
                } else if (rb_different.getId() == checkedId) {
                    result = rb_different.getText().toString().trim();
                    ll_real_address.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showDialog(String content) {
        customProgressDialog = new CustomProgressDialog(this, content);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
//        customProgressDialog.setContent(content);
//        isConnect = false;
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!isConnect) {
//                    if (customProgressDialog != null) {
//                        customProgressDialog.dismiss();
//                        CommonUtils.showToast(ScanResultActivity.this,getString(R.string.poor_signal));
//                    }
//                }
//            }
//        }, 5000);
    }


    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
        if (request instanceof WareInfoRequest) {
            WareInfoResponse wareInfoResponse = (WareInfoResponse) response;
            if (wareInfoResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(wareInfoResponse.getErrorMsg())) {
                    WareInfoResponse.DataEntity entity = wareInfoResponse.getData();
                    if (entity != null) {
                        if (!TextUtils.isEmpty(wareInfoResponse.getData().getPositionCode())) {
                            wareInfo = new WareInfo();
                            wareInfo.setData(entity);
                            markNum = wareInfo.getMarkNum();
                            tv_markNum.setText(markNum);
                            tv_type.setText(wareInfo.getSpec());
//                            tv_gross_weight.setText(wareInfo.getGrossWeight() + "kg");
                            tv_net_weight.setText(wareInfo.getNetWeight() + "吨");
                            String address = wareInfo.getAddress();
                            address = CommonUtils.formatAddress(address);
                            tv_addressWare.setText(address);
                        } else {
                            CommonUtils.showToast(this, "该货品不是盘点状态，请重新扫描");
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
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void commit() {
        showEditDialog(1);
//        if (TextUtils.isEmpty(result)) {
//            CommonUtils.showToast(this, "未选择结果");
//            return;
//        }
//        String realAddress = null;
//        if (result.equals("不一致")) {
//            realAddress = et_real_address.getText().toString().trim();
//            if (TextUtils.isEmpty(realAddress)) {
//                CommonUtils.showToast(this, "未填写实际地址");
//                return;
//            }
//        }
//        showDialog(getString(R.string.commit_data));
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (customProgressDialog != null) {
//                    customProgressDialog.dismiss();
//                }
//                CommonUtils.showToast(CheckResultActivity.this, "信息正确");
//                finish();
//            }
//        }, 2000);
    }
}
