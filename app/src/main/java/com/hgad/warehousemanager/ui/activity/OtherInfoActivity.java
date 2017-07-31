package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.BaseRequest;

/**
 * Created by Administrator on 2017/7/11.
 */
public class OtherInfoActivity extends BaseActivity {

    private TextView tv_text;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_other_info);
    }

    @Override
    protected void initData() {
        initHeader("扫描结果");
        Intent intent = getIntent();
        String result = intent.getStringExtra(Constants.SCAN_RESULT);
        tv_text.setText(result);
    }

    @Override
    protected void initView() {
        tv_text = (TextView) findViewById(R.id.tv_text);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {

    }
}
