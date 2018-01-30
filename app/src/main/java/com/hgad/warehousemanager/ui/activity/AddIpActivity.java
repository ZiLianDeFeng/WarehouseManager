package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.IpInfo;
import com.hgad.warehousemanager.bean.request.CheckFloorDetailRequest;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.db.dao.BaseDaoImpl;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.util.CommonUtils;

import java.sql.SQLException;

/**
 * Created by Administrator on 2017/7/13.
 */
public class AddIpActivity extends BaseActivity {

    private EditText et_ip0;
    private EditText et_ip1;
    private EditText et_ip2;
    private EditText et_ip3;
    private EditText et_ip4;
    private EditText et_name;
    private IpInfo ipInfo;
    private BaseDaoImpl<IpInfo, Integer> ipInfoDao;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_add_ip);
    }

    @Override
    protected void initData() {
        ipInfoDao = new BaseDaoImpl<>(this, IpInfo.class);
        initHeader("编辑IP信息");
        Intent intent = getIntent();
        ipInfo = (IpInfo) intent.getSerializableExtra(Constants.IP_INFO);
        if (ipInfo != null) {
            String ip = ipInfo.getIp();
            String name = ipInfo.getName();
            et_name.setText(name);
            String[] ips = ip.split("\\.");
            String ip0 = ips[0];
            String ip1 = ips[1];
            String ip2 = ips[2];
            String[] split = ips[3].split(":");
            String ip3 = split[0];
            String ip4 = split[1];
            et_ip0.setText(ip0);
            et_ip1.setText(ip1);
            et_ip2.setText(ip2);
            et_ip3.setText(ip3);
            et_ip4.setText(ip4);
        }
    }

    @Override
    protected void initView() {
        et_ip0 = (EditText) findViewById(R.id.et_ip0);
        et_ip1 = (EditText) findViewById(R.id.et_ip1);
        et_ip2 = (EditText) findViewById(R.id.et_ip2);
        et_ip3 = (EditText) findViewById(R.id.et_ip3);
        et_ip4 = (EditText) findViewById(R.id.et_ip4);
        et_name = (EditText) findViewById(R.id.et_name);
        findViewById(R.id.btn_add).setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (request instanceof CheckFloorDetailRequest) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                add();
                break;
        }
    }

    private void add() {
        String ip0 = et_ip0.getText().toString().trim();
        String ip1 = et_ip1.getText().toString().trim();
        String ip2 = et_ip2.getText().toString().trim();
        String ip3 = et_ip3.getText().toString().trim();
        String ip4 = et_ip4.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(ip0) || TextUtils.isEmpty(ip1) || TextUtils.isEmpty(ip2) || TextUtils.isEmpty(ip3) || TextUtils.isEmpty(ip4)) {
            CommonUtils.showToast(this, "服务器地址不能为空");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            CommonUtils.showToast(this, "名称不能为空");
            return;
        }
        String ip = ip0 + "." + ip1 + "." + ip2 + "." + ip3 + ":" + ip4;
        if (ipInfo != null) {
            ipInfo.setIp(ip);
            ipInfo.setName(name);
            ipInfo.setDefault(false);
            try {
                ipInfoDao.update(ipInfo);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Constants.EDIT_IP);
            sendBroadcast(intent);
            finish();
        } else {
            ipInfo = new IpInfo(name, ip, false);
            Intent intent = new Intent();
            intent.putExtra(Constants.IP_INFO, ipInfo);
            setResult(Constants.RESULT_OK, intent);
            finish();
        }
    }
}
