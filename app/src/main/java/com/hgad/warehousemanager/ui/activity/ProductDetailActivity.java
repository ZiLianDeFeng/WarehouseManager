package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.HistoryInfo;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.ProductRecordRequest;
import com.hgad.warehousemanager.bean.response.ProductRecordResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.ProductHistoryAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */
public class ProductDetailActivity extends BaseActivity {

    private TextView tv_markNum;
    private TextView tv_type;
    private TextView tv_pro_name;
    private TextView tv_steel_grade;
    private TextView tv_order_num;
    private TextView tv_net_weight;
    private TextView tv_addressWare;
    private String type;
    private ListView lv_history;
    private List<HistoryInfo> data = new ArrayList<>();
    private ProductHistoryAdapter productHistoryAdapter;
    private LinearLayout ll_history;
    private CustomProgressDialog customProgressDialog;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (customProgressDialog != null) {
                customProgressDialog.dismiss();
                CommonUtils.showToast(ProductDetailActivity.this, getString(R.string.poor_signal));
            }
        }
    };

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_product_detail);
    }

    @Override
    protected void initData() {
        initHeader("货品详情");
        Intent intent = getIntent();
        WareInfo wareInfo = (WareInfo) intent.getSerializableExtra(Constants.WARE_INFO);
        type = intent.getStringExtra(Constants.TYPE);
        String markNum = wareInfo.getMarkNum();
        if (Constants.HISTORY_TYPE.equals(type)) {
            ll_history.setVisibility(View.VISIBLE);
//            for (int i = 0; i < 10; i++) {
//                data.add(new HistoryInfo("入库", "01010101", "01020304", "2017-8-21", "admin"));
//            }
//            productHistoryAdapter.notifyDataSetChanged();
            showDialog(getString(R.string.info_check));
            ProductRecordRequest productRecordRequest = new ProductRecordRequest(markNum);
            sendRequest(productRecordRequest, ProductRecordResponse.class);
        }
        tv_markNum.setText(markNum);
        tv_type.setText(wareInfo.getSpec());
        tv_pro_name.setText(wareInfo.getProName());
        tv_steel_grade.setText(wareInfo.getSteelGrade());
        tv_order_num.setText(wareInfo.getOrderItem());
        tv_net_weight.setText(wareInfo.getNetWeight());
        String address = wareInfo.getAddress();
        if (!TextUtils.isEmpty(address.trim())) {
            address = CommonUtils.formatAddress(address);
            CommonUtils.stringInterceptionChangeLarge(tv_addressWare, address, "仓", "排", "垛", "号");
        } else {
            tv_addressWare.setText("无");
        }
    }

    @Override
    protected void initView() {
        tv_markNum = (TextView) findViewById(R.id.tv_markNum);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_pro_name = (TextView) findViewById(R.id.tv_pro_name);
        tv_steel_grade = (TextView) findViewById(R.id.tv_steel_grade);
        tv_order_num = (TextView) findViewById(R.id.tv_order_num);
        tv_net_weight = (TextView) findViewById(R.id.tv_net_weight);
        tv_addressWare = (TextView) findViewById(R.id.tv_addressWare);
        ll_history = (LinearLayout) findViewById(R.id.ll_history);
        lv_history = (ListView) findViewById(R.id.lv_history);
        productHistoryAdapter = new ProductHistoryAdapter(data, this);
        lv_history.setAdapter(productHistoryAdapter);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
            handler.removeCallbacks(runnable);
        }
        if (request instanceof ProductRecordRequest) {
            ProductRecordResponse productRecordResponse = (ProductRecordResponse) response;
            if (productRecordResponse.getResponseCode() != null && productRecordResponse.getResponseCode().getCode() == 200) {
                List<ProductRecordResponse.DataEntity> data = productRecordResponse.getData();
                if (data != null) {
                    for (ProductRecordResponse.DataEntity dataEntity : data) {
                        String operType = dataEntity.getOperType();
                        String curPosition = dataEntity.getCurPosition();
                        String origPosition = dataEntity.getOrigPosition();
                        String operDate = dataEntity.getOperDate();
                        String operator = dataEntity.getOperator().trim();
                        HistoryInfo historyInfo = new HistoryInfo(operType, origPosition, curPosition, operDate, operator);
                        this.data.add(historyInfo);
                    }
                    productHistoryAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void showDialog(String content) {
        customProgressDialog = new CustomProgressDialog(this, content);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        handler.postDelayed(runnable, 5000);
    }
}
