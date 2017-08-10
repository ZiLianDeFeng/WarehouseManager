package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.view.View;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

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
        findViewById(R.id.tv_loginout).setOnClickListener(this);
        findViewById(R.id.rl_change_pwd).setOnClickListener(this);
        findViewById(R.id.rl_version).setOnClickListener(this);
        findViewById(R.id.rl_system_setting).setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

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
            case R.id.rl_version:
                toVersion();
                break;
            case R.id.rl_system_setting:
                toSystemSetting();
                break;
        }
    }

    private void toSystemSetting() {
        Intent intent = new Intent(this, SystemSettingActivity.class);
        startActivity(intent);
    }

    private void changePwd() {
        Intent intent = new Intent(this, ChangePwdActivity.class);
        startActivity(intent);
    }

    private void loginout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        JPushInterface.setAlias(SettingActivity.this, "", new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                SPUtils.put(SettingActivity.this, SPConstants.SET_ALIAS_SUCCESS, false);
            }
        });
        SPUtils.put(this, SPConstants.LOGIN_SUCCESS, false);
        BaseApplication.getApplication().exit();
    }

    private void toVersion() {
        Intent intent = new Intent(this, VersionActivity.class);
        startActivity(intent);
    }
}
