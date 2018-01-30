package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.zxing.activity.QrScanActivity;

/**
 * Created by Administrator on 2017/8/16.
 */
public class HistoryActivity extends BaseActivity {

    private EditText et_markNum;
    private EditText et_order_item;
    private EditText et_pro_name;
    private EditText tv_address;
    private EditText et_customer;
    private static final int SCAN = 99;

    // TODO: 2017/10/19 增加客户查询条件
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_history);
    }

    @Override
    protected void initData() {
        initHeader("历史查询");
    }

    @Override
    protected void initView() {
        et_markNum = (EditText) findViewById(R.id.et_markNum);
        et_order_item = (EditText) findViewById(R.id.et_order_item);
        et_pro_name = (EditText) findViewById(R.id.et_pro_name);
        tv_address = (EditText) findViewById(R.id.tv_address);
        et_customer = (EditText) findViewById(R.id.et_customer);
//        tv_address.setOnClickListener(this);
        findViewById(R.id.btn_search).setOnClickListener(this);
        ImageView iv_more = (ImageView) findViewById(R.id.search);
        iv_more.setImageResource(R.mipmap.scan);
        LinearLayout ll_more = (LinearLayout) findViewById(R.id.ll_search);
        ll_more.setVisibility(View.VISIBLE);
        ll_more.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCAN:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String resultStr = bundle.getString("result");
                        String scanState = bundle.getString(Constants.SCAN_STATE);
                        String codeType = bundle.getString("codeType");
                        Intent intent = new Intent(this, HistoryListActivity.class);
                        intent.putExtra(Constants.SCAN_RESULT, resultStr);
                        intent.putExtra(Constants.CODE_TYPE, codeType);
                        intent.putExtra(Constants.SCAN_STATE, scanState);
                        startActivity(intent);
                    }
                }
                break;
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                search();
                break;
            case R.id.ll_search:
                go2Scan();
                break;
        }
    }

    private void go2Scan() {
        Intent intent = new Intent(this, QrScanActivity.class);
        startActivityForResult(intent, SCAN);
    }

    private void search() {
        String markNum = et_markNum.getText().toString().trim();
        String orderItem = et_order_item.getText().toString().trim();
        String proName = et_pro_name.getText().toString().trim();
        String address = tv_address.getText().toString().trim();
//        address = CommonUtils.formatAddress(address);
        Intent intent = new Intent(this, HistoryListActivity.class);
        intent.putExtra(Constants.MARK_NUM, markNum);
        intent.putExtra(Constants.ORDER_ITEM, orderItem);
        intent.putExtra(Constants.PRO_NAME, proName);
        intent.putExtra(Constants.ADDRESS, address);
        startActivity(intent);
    }
}
