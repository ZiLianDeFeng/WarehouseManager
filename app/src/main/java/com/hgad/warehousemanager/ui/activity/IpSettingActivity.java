package com.hgad.warehousemanager.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.IpInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.db.dao.BaseDaoImpl;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.IpAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
public class IpSettingActivity extends BaseActivity {

    private static final int ADD = 202;
    private ListView lv;
    private List<IpInfo> ips = new ArrayList<>();
    private IpAdapter ipAdapter;
    private BaseDaoImpl<IpInfo, Integer> ipInfoDao;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.EDIT_IP)) {
                callRefresh();
            }
        }
    };

    private void callRefresh() {
        ips.clear();
        try {
            List<IpInfo> ipInfos = ipInfoDao.queryAll();
            if (ipInfos != null) {
                ips.addAll(ipInfos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Collections.sort(ips, new Comparator<IpInfo>() {
            @Override
            public int compare(IpInfo lhs, IpInfo rhs) {
                return rhs.getId()-lhs.getId();
            }
        });
        ipAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_ip_setting);
        registBroadcastReceiver(receiver);
    }


    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.EDIT_IP);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void initData() {
        initHeader("IP地址管理");
        ipInfoDao = new BaseDaoImpl<>(this, IpInfo.class);
        callRefresh();
    }

    @Override
    protected void initView() {
        lv = (ListView) findViewById(R.id.lv_ip);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        lv.setEmptyView(tv_empty);
        ipAdapter = new IpAdapter(ips, this);
        ipAdapter.setCallFreshListener(callRefreshListener);
        lv.setAdapter(ipAdapter);
        findViewById(R.id.btn_add).setOnClickListener(this);
    }
    private IpAdapter.CallFreshListener callRefreshListener = new IpAdapter.CallFreshListener() {
        @Override
        public void callFresh() {
            callRefresh();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD:
                if (resultCode == Constants.RESULT_OK) {
                    if (data != null) {
                        IpInfo ipInfo = (IpInfo) data.getSerializableExtra(Constants.IP_INFO);
                        if (ipInfo != null) {
                            ips.add(0, ipInfo);
//                            ipAdapter.notifyDataSetChanged();
                            try {
                                ipInfoDao.saveOrUpdate(ips);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            callRefresh();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Intent intent = new Intent(this, AddIpActivity.class);
                startActivityForResult(intent, ADD);
                break;
        }
    }
}
