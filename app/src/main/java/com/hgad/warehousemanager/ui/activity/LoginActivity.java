package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.databinding.ActivityLoginBinding;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.ui.adapter.models.UserModel;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;

/**
 * Created by Administrator on 2017/6/26.
 */
public class LoginActivity extends BaseActivity {


    private CheckBox checkBox;
    private EditText et_username;
    private EditText et_password;
    private CustomProgressDialog mProgressDialog;
    private boolean notConnect = true;
    private String name;
    private String pwd;

    @Override
    protected void setContentView() {
        ActivityLoginBinding activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        UserModel userModel = new UserModel(activityLoginBinding, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
//        checkBox = (CheckBox) findViewById(R.id.cb_remenber);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
    }


    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
        }
    }

    private void login() {
        name = et_username.getText().toString().trim();
        pwd = et_password.getText().toString().trim();
        boolean checkNetWork = CommonUtils.checkNetWork(this);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            CommonUtils.showToast(this, getString(R.string.not_null));
            return;
        }
        if (checkNetWork) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (notConnect) {
                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                            CommonUtils.showToast(LoginActivity.this, "服务器连接错误！");
                        }
                    }
                }
            }, 5000);
            mProgressDialog = new CustomProgressDialog(LoginActivity.this, "正在登录");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    notConnect = false;
                    SPUtils.put(LoginActivity.this, SPConstants.LOGIN_SUCCESS, true);
                    SPUtils.put(LoginActivity.this, SPConstants.USER_NAME, name);
                    SPUtils.put(LoginActivity.this, SPConstants.PWD, pwd);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }
}
