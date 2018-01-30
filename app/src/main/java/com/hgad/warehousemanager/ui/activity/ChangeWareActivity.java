package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.zxing.activity.QrScanActivity;

/**
 * Created by Administrator on 2017/6/29.
 */
public class ChangeWareActivity extends BaseActivity{

    private static final int SCAN = 101;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_change_ware);
    }

    @Override
    protected void initData() {
        initHeader("移位");
    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_in_hand).setOnClickListener(this);
        findViewById(R.id.tv_in_mark).setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String resultStr = bundle.getString("result");
                Intent intent = new Intent(this, ScanResultActivity.class);
                String codeType = bundle.getString("codeType");
                intent.putExtra(Constants.CODE_TYPE, codeType);
                intent.putExtra(Constants.SCAN_RESULT, resultStr);
                intent.putExtra(Constants.TYPE, Constants.CHANGE_WARE);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_in_hand:
                go2InHand();
                break;
            case R.id.tv_in_mark:
                go2Scan();
                break;
        }
    }

    private void go2Scan() {
        Intent intent = new Intent(this, QrScanActivity.class);
        intent.putExtra(Constants.TYPE,Constants.CHANGE_WARE);
        startActivityForResult(intent, SCAN);
    }

    private void go2InHand() {
        Intent intent = new Intent(this, InWareByHandActivity.class);
        intent.putExtra(Constants.TYPE,Constants.CHANGE_WARE);
        startActivity(intent);
    }
}
