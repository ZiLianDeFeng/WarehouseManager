package com.hgad.warehousemanager.ui.activity;

import android.view.View;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

/**
 * Created by Administrator on 2017/7/12.
 */
public class UserSettingActivity extends BaseActivity{
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_settting);
    }

    @Override
    protected void initData() {
        initHeader("个人中心");
        String userName = SPUtils.getString(this, SPConstants.USER_NAME);
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
