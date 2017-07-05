package com.hgad.warehousemanager.ui.activity;

import android.view.View;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;

/**
 * Created by Administrator on 2017/6/29.
 */
public class ChangeWareActivity extends BaseActivity{
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

    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {

    }
}
