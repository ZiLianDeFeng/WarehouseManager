package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.SPUtils;

/**
 * Created by Administrator on 2017/6/26.
 */
public class SettingActivity extends BaseActivity {
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initData() {
        initHeader("个人设置");
    }

    @Override
    protected void initView() {
        TextView tv_loginout = (TextView) findViewById(R.id.tv_loginout);
        tv_loginout.setOnClickListener(this);
        findViewById(R.id.rl_change_pwd).setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_loginout:
                loginout();
                break;
            case R.id.rl_change_pwd:
                changePwd();
                break;
        }
    }

    private void changePwd() {
        Intent intent = new Intent(this, ChangePwdActivity.class);
        startActivity(intent);
    }

    private void loginout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        SPUtils.put(this, SPConstants.LOGIN_SUCCESS, false);
        BaseApplication.getApplication().exit();
    }
}
