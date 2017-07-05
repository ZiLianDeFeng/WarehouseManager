package com.hgad.warehousemanager.ui.adapter.models;

import android.app.Activity;

import com.hgad.warehousemanager.bean.UserInfo;
import com.hgad.warehousemanager.databinding.ActivityLoginBinding;

/**
 * Created by Administrator on 2017/6/27.
 */
public class UserModel {

    private ActivityLoginBinding activityLoginBinding;
    private Activity activity;
    private UserInfo userInfo;

    public UserModel(ActivityLoginBinding activityLoginBinding, Activity activity) {
        this.activityLoginBinding = activityLoginBinding;
        this.activity = activity;
        init();
    }

    private void init() {
        userInfo = new UserInfo();
        activityLoginBinding.setUserModel(this);
    }

    public void login() {
        userInfo.setUserName(activityLoginBinding.etUsername.getText().toString());
        userInfo.setPassword(activityLoginBinding.etPassword.getText().toString());
    }
}
