package com.hgad.warehousemanager.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseFragment;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.HomeDataRequest;
import com.hgad.warehousemanager.bean.response.HomeDataResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.db.dao.BaseDaoImpl;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.activity.CheckRecordActivity;
import com.hgad.warehousemanager.ui.activity.DataSynActivity;
import com.hgad.warehousemanager.ui.activity.HistoryActivity;
import com.hgad.warehousemanager.ui.activity.NotificationActivity;
import com.hgad.warehousemanager.ui.activity.OperateResultActivity;
import com.hgad.warehousemanager.ui.activity.OutWareActivity;
import com.hgad.warehousemanager.ui.activity.ReviewActivity;
import com.hgad.warehousemanager.ui.activity.ReviewResultActivity;
import com.hgad.warehousemanager.ui.activity.ScanResultActivity;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.zxing.activity.CaptureActivity;
import com.hgad.warehousemanager.zxing.activity.ScannerActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class HomeFragment extends BaseFragment {
    private static final int IN_SCAN = 199;
    private static final int CHANGE_SCAN = 220;
    private static final int RESULT = 330;

    private View mView;
    //    private ConvenientBanner<String> convenientBanner;
    private List<String> mImageList = new ArrayList<>();
    private TextView tv_out_review;
    private TextView tv_review_result;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.IN_WARE) || action.equals(Constants.CHANGE_WARE)) {
                type = intent.getStringExtra(Constants.TYPE);
                toResult(type);
            }
        }
    };
    private IntenterBoradCastReceiver netReceiver;
    private BaseDaoImpl<WareInfo, Integer> wareDao;
    private TextView tv_check_count;
    private TextView tv_in_count;
    private TextView tv_out_count;
    private TextView tv_today_in_count;
    private String userName;
    private String type;

    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.IN_WARE);
        intentFilter.addAction(Constants.CHANGE_WARE);
        mContext.registerReceiver(receiver, intentFilter);
    }

    //监听网络状态变化的广播接收器
    public class IntenterBoradCastReceiver extends BroadcastReceiver {

        private ConnectivityManager mConnectivityManager;
        private NetworkInfo netInfo;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    //网络连接
                    String name = netInfo.getTypeName();
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        //WiFi网络
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        //有线网络
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        //3g网络
                    }
                    try {
                        commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    //网络断开
                }
            }

        }
    }


    private void registerNetBroadrecevicer() {
        //获取广播对象
        netReceiver = new IntenterBoradCastReceiver();
        //创建意图过滤器
        IntentFilter filter = new IntentFilter();
        //添加动作，监听网络
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        mContext.registerReceiver(netReceiver, filter);
    }

    private void commit() throws SQLException {
        List<WareInfo> wareInfos = wareDao.queryAll();
        if (wareInfos != null && wareInfos.size() != 0) {
            new AlertView("提示", "有未提交的入库货品，是否现在去提交", "等会再说", new String[]{"现在就去"}, null, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    switch (position) {
                        case 0:
                            go2Syn();
                            break;
                    }
                }
            }).setCancelable(true).show();
        }
    }

    @Override
    protected void initData() {
//        if (Constants.NORMAL) {
//            tv_out_review.setVisibility(View.GONE);
//            tv_review_result.setVisibility(View.VISIBLE);
//        } else {
//            tv_out_review.setVisibility(View.VISIBLE);
//            tv_review_result.setVisibility(View.GONE);
//        }
        userName = SPUtils.getString(mContext, SPConstants.USER_NAME);
        wareDao = new BaseDaoImpl<>(mContext, WareInfo.class);
        HomeDataRequest homeDataRequest = new HomeDataRequest(userName);
        sendRequest(homeDataRequest, HomeDataResponse.class);
    }

    @Override
    protected void initView() {
        mView.findViewById(R.id.tv_in_ware).setOnClickListener(this);
        mView.findViewById(R.id.tv_change_ware).setOnClickListener(this);
        mView.findViewById(R.id.tv_out_ware).setOnClickListener(this);
        mView.findViewById(R.id.tv_history).setOnClickListener(this);
        mView.findViewById(R.id.tv_check).setOnClickListener(this);
//        mView.findViewById(R.id.tv_task_notification).setOnClickListener(this);
        mView.findViewById(R.id.iv_scan).setOnClickListener(this);
        mView.findViewById(R.id.tv_data_syn).setOnClickListener(this);
        tv_out_review = (TextView) mView.findViewById(R.id.tv_out_review);
        tv_out_review.setOnClickListener(this);
        tv_review_result = (TextView) mView.findViewById(R.id.tv_review_result);
        tv_review_result.setOnClickListener(this);
        tv_check_count = (TextView) mView.findViewById(R.id.tv_check_count);
        tv_in_count = (TextView) mView.findViewById(R.id.tv_in_count);
        tv_out_count = (TextView) mView.findViewById(R.id.tv_out_count);
        tv_today_in_count = (TextView) mView.findViewById(R.id.tv_today_in_count);
    }

    private void toResult(String type) {
        Intent intent = new Intent(mContext, OperateResultActivity.class);
        intent.putExtra(Constants.TYPE, type);
        startActivityForResult(intent, RESULT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        registBroadcastReceiver(receiver);
        registerNetBroadrecevicer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
            receiver = null;
        }
//        if (netReceiver != null) {
//            mContext.unregisterReceiver(netReceiver);
//            netReceiver = null;
//        }
    }


    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_home, null);
        return mView;
    }

    @Override
    public <Res extends BaseResponse> void onSuccessResult(BaseRequest request, Res response) {
        if (request instanceof HomeDataRequest) {
            HomeDataResponse homeDataResponse = (HomeDataResponse) response;
            if (homeDataResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(homeDataResponse.getErrorMsg())) {
                    HomeDataResponse.DataEntity dataEntity = homeDataResponse.getData();
                    tv_today_in_count.setText(dataEntity.getDayIn() + "");
                    tv_in_count.setText(dataEntity.getIn() + "");
                    tv_out_count.setText(dataEntity.getOut() + "");
                    tv_check_count.setText(dataEntity.getChk() + "");
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == IN_SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                Intent intent = new Intent(mContext, ScanResultActivity.class);
                intent.putExtra(Constants.SCAN_RESULT, result);
                intent.putExtra(Constants.TYPE, Constants.IN_WARE);
                startActivity(intent);
            }
        } else if (resultCode == getActivity().RESULT_OK && requestCode == CHANGE_SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                Intent intent = new Intent(mContext, ScanResultActivity.class);
                intent.putExtra(Constants.SCAN_RESULT, result);
                intent.putExtra(Constants.TYPE, Constants.CHANGE_WARE);
                startActivity(intent);
            }
        } else if (resultCode == Constants.RESULT_OK && requestCode == RESULT) {
            String type = data.getStringExtra(Constants.TYPE);
            Intent intent = new Intent(mContext, ScannerActivity.class);
            intent.putExtra(Constants.TYPE, type);
            startActivityForResult(intent, type.equals(Constants.IN_WARE) ? IN_SCAN : CHANGE_SCAN);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_in_ware:
                go2InWare();
                break;
            case R.id.tv_change_ware:
                go2ChangeWare();
                break;
            case R.id.tv_out_ware:
                go2OutWare();
                break;
            case R.id.tv_history:
                go2History();
                break;
            case R.id.tv_check:
                go2Check();
                break;
//            case R.id.tv_task_notification:
//                go2Notification();
//                break;
            case R.id.iv_scan:
                go2Scan();
                break;
            case R.id.tv_out_review:
                go2Review();
                break;
            case R.id.tv_review_result:
                go2ReviewResult();
                break;
            case R.id.tv_data_syn:
                go2Syn();
                break;
        }
    }

    private void go2Syn() {
        Intent intent = new Intent(mContext, DataSynActivity.class);
        startActivity(intent);
    }

    private void go2History() {
        Intent intent = new Intent(mContext, HistoryActivity.class);
        startActivity(intent);
    }

    private void go2ReviewResult() {
        Intent intent = new Intent(mContext, ReviewResultActivity.class);
        startActivity(intent);
    }

    private void go2Review() {
        Intent intent = new Intent(mContext, ReviewActivity.class);
        startActivity(intent);
    }

    private void go2Notification() {
        Intent intent = new Intent(mContext, NotificationActivity.class);
        startActivity(intent);
    }

    private void go2Scan() {
        Intent intent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(intent, IN_SCAN);
    }

    private void go2ChangeWare() {
//        Intent intent = new Intent(mContext, ChangeWareActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(mContext, ScannerActivity.class);
        intent.putExtra(Constants.TYPE, Constants.CHANGE_WARE);
        startActivityForResult(intent, CHANGE_SCAN);
    }

    private void go2Check() {
        Intent intent = new Intent(mContext, CheckRecordActivity.class);
        startActivity(intent);
    }

    private void go2OutWare() {
        Intent intent = new Intent(mContext, OutWareActivity.class);
        startActivity(intent);
    }

    private void go2InWare() {
//        Intent intent = new Intent(mContext, InWareChooseActivity.class);
//        startActivity(intent);

        Intent intent = new Intent(mContext, ScannerActivity.class);
        intent.putExtra(Constants.TYPE, Constants.IN_WARE);
        startActivityForResult(intent, IN_SCAN);
    }
}
