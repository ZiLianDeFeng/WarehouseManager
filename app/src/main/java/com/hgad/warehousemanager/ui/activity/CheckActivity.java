package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.zxing.activity.CaptureActivity;

/**
 * Created by Administrator on 2017/6/29.
 */
public class CheckActivity extends BaseActivity {
    private static final int SCAN = 110;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_check);
    }

    @Override
    protected void initData() {
        initHeader("盘点");
    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_in_hand).setOnClickListener(this);
        findViewById(R.id.tv_in_mark).setOnClickListener(this);
        TextView tv_record = (TextView) findViewById(R.id.btn_confirm);
        tv_record.setOnClickListener(this);
        tv_record.setVisibility(View.VISIBLE);
        tv_record.setText("记录");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String resultStr = bundle.getString("result");
                Intent intent = new Intent(this, ScanResultActivity.class);
                intent.putExtra(Constants.SCAN_RESULT, resultStr);
                intent.putExtra(Constants.TYPE, Constants.CHECK);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

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
            case R.id.btn_confirm:
                seeRecord();
                break;
        }
    }

    private void seeRecord() {
        Intent intent = new Intent(this, CheckRecordActivity.class);
        startActivity(intent);
    }


    private void go2Scan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, SCAN);
    }

    private void go2InHand() {
        Intent intent = new Intent(this, InWareByHandActivity.class);
        intent.putExtra(Constants.TYPE, Constants.CHECK);
        startActivity(intent);
    }
}
