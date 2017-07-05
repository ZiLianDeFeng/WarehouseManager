package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.databinding.ActivityLoginBinding;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.ui.adapter.models.UserModel;

/**
 * Created by Administrator on 2017/6/26.
 */
public class LoginActivity extends BaseActivity {


    private CheckBox checkBox;

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
        checkBox = (CheckBox) findViewById(R.id.cb_remenber);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
