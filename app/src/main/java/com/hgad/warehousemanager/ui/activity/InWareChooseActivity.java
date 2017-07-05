package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.zxing.activity.CaptureActivity;

/**
 * Created by Administrator on 2017/6/29.
 */
public class InWareChooseActivity extends BaseActivity {


    private static final int SCAN = 99;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_inware_choose);
    }

    @Override
    protected void initData() {
        initHeader("入库");
    }

    @Override
    protected void initView() {
        ((TextView) findViewById(R.id.tv_in_hand)).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_in_mark)).setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String resultStr = bundle.getString("result");
                Intent intent = new Intent(this, ScanResultActivity.class);
                intent.putExtra(Constants.SCAN_RESULT, resultStr);
                intent.putExtra(Constants.TYPE,Constants.IN_WARE);
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
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, SCAN);
    }

    private void go2InHand() {
        Intent intent = new Intent(this, InWareByHandActivity.class);
        startActivity(intent);
    }
}
