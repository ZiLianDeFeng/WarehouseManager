package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.base.BaseApplication;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;

/**
 * Created by Administrator on 2017/7/3.
 */
public class ChangePwdActivity extends BaseActivity {

    private EditText et_old_pwd;
    private EditText et_new_pwd;
    private EditText et_confirm_pwd;

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
        et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
        et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
        findViewById(R.id.btn_commit).setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commitChangePwd();
                break;
        }
    }

    private void commitChangePwd() {
        if (TextUtils.isEmpty(et_old_pwd.getText().toString().trim())) {
            CommonUtils.showToast(this, "原密码不能为空");
            return;
        }
        String newPwd = et_new_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(newPwd)) {
            CommonUtils.showToast(this, "新密码不能为空");
            return;
        }
        String confirmPwd = et_confirm_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(confirmPwd)) {
            CommonUtils.showToast(this, "确认密码不能为空");
            return;
        }
        if (!newPwd.equals(confirmPwd)){
            CommonUtils.showToast(this,"确认密码与新密码不一致");
            return;
        }
        CommonUtils.showToast(this, "修改成功，请重新登录");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        SPUtils.put(this, SPConstants.LOGIN_SUCCESS, false);
        BaseApplication.getApplication().exit();
    }
}
