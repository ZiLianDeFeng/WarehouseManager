package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.request.LoginRequest;
import com.hgad.warehousemanager.bean.response.LoginResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2017/6/26.
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "JPUSH";
    private EditText et_username;
    private EditText et_password;
    private CustomProgressDialog mProgressDialog;
    private boolean notConnect = true;
    private String name;
    private String pwd;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        findViewById(R.id.tv_ip_setting).setOnClickListener(this);
        findViewById(R.id.tv_test).setOnClickListener(this);
    }


    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (request instanceof LoginRequest) {
            LoginResponse loginResponse = (LoginResponse) response;
            if (loginResponse.getResponseCode() != null) {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                if (loginResponse.getResponseCode().getCode() == 200) {
                    CommonUtils.showToast(LoginActivity.this, "登录成功！");
                    notConnect = false;
                    int userId = loginResponse.getData().getId();
                    String username = loginResponse.getData().getUsername();
                    String password = loginResponse.getData().getPassword();
                    SPUtils.put(LoginActivity.this, SPConstants.USER_ID, userId);
                    SPUtils.put(LoginActivity.this, SPConstants.LOGIN_SUCCESS, true);
                    SPUtils.put(LoginActivity.this, SPConstants.USER_NAME, username);
                    SPUtils.put(LoginActivity.this, SPConstants.PWD, pwd);
                    boolean success = SPUtils.getBoolean(LoginActivity.this, SPConstants.SET_ALIAS_SUCCESS);
                    if (!success) {
                        setAlias(userId + "");
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    CommonUtils.showToast(LoginActivity.this, "账号或密码错误！");
                    notConnect = false;
                }
            }
        }
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias(String alias) {
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    SPUtils.put(LoginActivity.this, SPConstants.SET_ALIAS_SUCCESS, true);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
//            CommonUtils.showToast(getApplicationContext(), logs);
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_ip_setting:
                ipSetting();
                break;
            case R.id.tv_test:
                outline();
                break;
        }
    }

    private void outline() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void ipSetting() {
        Intent intent = new Intent(this, IpSettingActivity.class);
        startActivity(intent);
    }

    private void login() {
        if (Constants.DEBUG) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
//        } else {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }, 3000);
//            return;
        }
        name = et_username.getText().toString().trim();
        pwd = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            CommonUtils.showToast(this, getString(R.string.not_null));
            return;
        }
        boolean checkNetWork = CommonUtils.checkNetWork(this);
        if (checkNetWork) {
            LoginRequest loginRequest = new LoginRequest(name, pwd);
            sendRequest(loginRequest, LoginResponse.class);
            mProgressDialog = new CustomProgressDialog(LoginActivity.this, "正在登录");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (notConnect) {
                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                            CommonUtils.showToast(LoginActivity.this, "连接不上服务器！");
                        }
                    }
                }
            }, 5000);
        } else {
            CommonUtils.showToast(this, "请检查网络");
        }
    }
}
