package com.hgad.warehousemanager.ui.activity;

import android.view.View;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;

/**
 * Created by Administrator on 2017/7/3.
 */
public class ChangePwdActivity extends BaseActivity{
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_change_pwd);
    }

    @Override
    protected void initData() {
        initHeader("修改密码");
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
