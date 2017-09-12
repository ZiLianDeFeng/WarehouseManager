package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

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

/**
 * Created by Administrator on 2017/1/4.
 */
public class SplashActivity extends BaseActivity {
    private Handler handler;
    private String userName;
    private String password;
    private boolean notConnect = true;
    private Runnable splashRunnable;
    private boolean loginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initData() {
        loginSuccess = SPUtils.getBoolean(this, SPConstants.LOGIN_SUCCESS);
        if (loginSuccess) {
            userName = SPUtils.getString(this, SPConstants.USER_NAME);
            password = SPUtils.getString(this, SPConstants.PWD);
        }
        initHandler();
    }

    private void initHandler() {
        handler = new Handler();
        splashRunnable = new Runnable() {
            @Override
            public void run() {
                if (Constants.DEBUG) {
                    Intent intent1 = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
                boolean notFirst = SPUtils.getBoolean(SplashActivity.this, SPConstants.NOT_FRIST);
//                notFirst = false;
                if (notFirst) {
                    if (loginSuccess) {
                        boolean checkNetWork = CommonUtils.checkNetWork(SplashActivity.this);
                        if (checkNetWork) {
                            LoginRequest loginRequest = new LoginRequest(userName, password);
                            sendRequest(loginRequest, LoginResponse.class);
                            notConnect = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (notConnect) {
                                        CommonUtils.showToast(SplashActivity.this, "服务器连接错误！");
                                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }, 5000);
                        } else {
                            CommonUtils.showToast(SplashActivity.this, "请检查网络");
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null && splashRunnable != null) {
            handler.removeCallbacks(splashRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (handler != null && splashRunnable != null) {
            handler.postDelayed(splashRunnable, 3000);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (request instanceof LoginRequest) {
            LoginResponse loginResponse = (LoginResponse) response;
            if (loginResponse.getResponseCode() != null) {
                if (loginResponse.getResponseCode().getCode() == 200) {
//                    CommonUtils.showToast(this, "登录成功！");
                    notConnect = false;
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    CommonUtils.showToast(this, "账号或密码错误！");
                    notConnect = false;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}