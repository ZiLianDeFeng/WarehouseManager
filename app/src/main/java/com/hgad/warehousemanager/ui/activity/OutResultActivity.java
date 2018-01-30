package com.hgad.warehousemanager.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;

/**
 * Created by Administrator on 2017/8/14.
 */
public class OutResultActivity extends BaseActivity {


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_out_result);
    }

    @Override
    protected void initData() {
        initHeader("出库结果");
    }

    @Override
    protected void initView() {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.INVISIBLE);
        TextView btn_confirm = (TextView) findViewById(R.id.btn_confirm);
        btn_confirm.setVisibility(View.VISIBLE);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                finish();
                break;
        }
    }
}
