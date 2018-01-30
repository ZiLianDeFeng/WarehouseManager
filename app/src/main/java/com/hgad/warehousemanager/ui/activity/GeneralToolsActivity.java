package com.hgad.warehousemanager.ui.activity;

import android.view.View;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.BaseRequest;

/**
 * Created by Administrator on 2017/7/20.
 */
public class GeneralToolsActivity extends BaseActivity {
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_general_tools);
    }

    @Override
    protected void initData() {
        initHeader("工具");
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {

    }

//    public void clickCommonNum(View view) {
////        Intent intent = new Intent(this, CommonNumActivity.class);
////        startActivity(intent);
//    }
//
//    public void clickQueryAddress(View view) {
////        Intent intent = new Intent(this, QueryAddressActivity.class);
////        startActivity(intent);
//    }
}
