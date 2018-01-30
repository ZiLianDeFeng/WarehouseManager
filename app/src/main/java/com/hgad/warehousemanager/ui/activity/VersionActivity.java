package com.hgad.warehousemanager.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.BaseRequest;


/**
 * Created by Administrator on 2017/2/17.
 */
public class VersionActivity extends BaseActivity {

    private TextView tv_version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_version);
    }

    protected void initData() {
        initHeader("应用信息");
        String versionName = getVersionName();
        tv_version.setText("当前版本号：v" + versionName);
    }

    protected void initView() {
        tv_version = (TextView) findViewById(R.id.tv_version);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 获取版本号
     *
     * @return
     */
    private String getVersionName() {
        // PackageManager
        PackageManager pm = getPackageManager();
        // PackageInfo
        try {
            // flags 代表可以获取的包信息的内容 传0即可 因为任何Flag都可以获取版本号
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
        }
        return null;
    }
}
