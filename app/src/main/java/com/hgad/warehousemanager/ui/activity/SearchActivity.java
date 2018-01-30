package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.bean.request.GroupStateRequest;
import com.hgad.warehousemanager.bean.request.OutOrderListRequest;
import com.hgad.warehousemanager.bean.response.GroupStateResponse;
import com.hgad.warehousemanager.bean.response.OutOrderListResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.NewOrderAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/11/27.
 */
public class SearchActivity extends BaseActivity {

    private EditText et_search;
    private String state = "'1','2'";
    private List<OrderInfo> data = new ArrayList<>();
    private XListView lv;
    private NewOrderAdapter orderAdapter;
    private boolean isConnect;
    private CustomProgressDialog customProgressDialog;
    private boolean forOutware;
    private boolean forReviewWare;
    private OrderInfo curOrderInfo;
    private int groupId;
    private int userId;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initData() {
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_title).setVisibility(View.GONE);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setVisibility(View.VISIBLE);
        LinearLayout ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ll_search.setVisibility(View.VISIBLE);
        ll_search.setOnClickListener(this);
        lv = (XListView) findViewById(R.id.lv_order);
        orderAdapter = new NewOrderAdapter(data, this);
//        orderAdapter.setGroupStateListener(groupStateListener);
        orderAdapter.setOnActionListener(onActionListener);
        lv.setAdapter(orderAdapter);
//        lv.setOnItemClickListener(itemListener);
        lv.setPullLoadEnable(false);
        lv.setPullRefreshEnable(false);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        lv.setEmptyView(tv_empty);
    }

//    boolean inGroup = false;
//    private NewOrderAdapter.GroupStateListener groupStateListener = new NewOrderAdapter.GroupStateListener() {
//        @Override
//        public boolean inGroupOrNot() {
//            if (!inGroup) {
//                CommonUtils.showToast(SearchActivity.this, "当前不在任何班组中，请先加入一个班组或创建一个班组");
////                showGroup();
//            }
//            return inGroup;
//        }
//    };

    private NewOrderAdapter.OnActionListener onActionListener = new NewOrderAdapter.OnActionListener() {
        @Override
        public void onOutWare(OrderInfo orderInfo) {
            curOrderInfo = orderInfo;
            forOutware = true;
            getGroupState();

        }

        @Override
        public void onReviewWare(OrderInfo orderInfo) {
            curOrderInfo = orderInfo;
            forReviewWare = true;
            getGroupState();

        }
    };

    public void getGroupState() {
        showDialog(getString(R.string.info_check));
        GroupStateRequest groupStateRequest = new GroupStateRequest(userId + "");
        sendRequest(groupStateRequest, GroupStateResponse.class);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        isConnect = true;
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
        if (request instanceof OutOrderListRequest) {
            OutOrderListResponse outOrderListResponse = (OutOrderListResponse) response;
            if (outOrderListResponse.getData() != null) {
                if (data != null) {
                    data.clear();
                }
                List<OutOrderListResponse.DataEntity.ListEntity> list = outOrderListResponse.getData().getList();
                for (OutOrderListResponse.DataEntity.ListEntity listEntity : list) {
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo.setData(listEntity);
                    data.add(orderInfo);
                }
                orderAdapter.notifyDataSetChanged();
            }
        } else if (request instanceof GroupStateRequest) {
            GroupStateResponse groupStateResponse = (GroupStateResponse) response;
            if (groupStateResponse.getResponseCode() != null) {
                if (groupStateResponse.getResponseCode().getCode() == 200) {
                    if (groupStateResponse.getData() != null) {
                        GroupStateResponse.DataEntity data = groupStateResponse.getData();
                        groupId = data.getId();
                        if (forOutware) {
                            forOutware = false;
                            if (curOrderInfo != null) {
                                Intent intent = new Intent(this, ProductListActivity.class);
                                intent.putExtra(Constants.TYPE, Constants.OUT_WARE);
                                intent.putExtra(Constants.ORDER_INFO, curOrderInfo);
                                intent.putExtra(Constants.PRO_TYPE, curOrderInfo.getProductType());
                                startActivity(intent);
                                return;
                            }
                        }
                        if (forReviewWare) {
                            forReviewWare = false;
                            if (curOrderInfo != null) {
                                Intent intent = new Intent(this, ProductListActivity.class);
                                intent.putExtra(Constants.TYPE, Constants.REVIEW_TYPE);
                                intent.putExtra(Constants.ORDER_INFO, curOrderInfo);
                                intent.putExtra(Constants.PRO_TYPE, curOrderInfo.getProductType());
                                startActivity(intent);
                                return;
                            }
                        }
                    } else {
                        if (forOutware || forReviewWare) {
                            forOutware = false;
                            forReviewWare = false;
                            CommonUtils.showToast(this, "当前不在任何群组中，请先加入一个群组或创建一个群组");
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
                search();
                break;
        }
    }

    private void search() {
        String orderNum = et_search.getText().toString().trim();
        int currentPage = 1;
        showDialog("获取数据中");
        OutOrderListRequest outOrderListRequest = new OutOrderListRequest(currentPage, state, orderNum);
        sendRequest(outOrderListRequest, OutOrderListResponse.class);
    }

    private void showDialog(String content) {
        customProgressDialog = new CustomProgressDialog(this, content);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        isConnect = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isConnect) {
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                        CommonUtils.showToast(SearchActivity.this, getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }
}
