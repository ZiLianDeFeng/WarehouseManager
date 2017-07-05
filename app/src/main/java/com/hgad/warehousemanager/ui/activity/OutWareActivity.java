package com.hgad.warehousemanager.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.ui.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
public class OutWareActivity extends BaseActivity {

    private ListView lv;
    private List<WareInfo> data = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private EditText et_order_num;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_outware);
    }

    @Override
    protected void initData() {
        initHeader("出库");
    }

    @Override
    protected void initView() {
        lv = (ListView) findViewById(R.id.lv_order);
        orderAdapter = new OrderAdapter(data, this);
        lv.setAdapter(orderAdapter);
        et_order_num = (EditText) findViewById(R.id.et_order_num);
        ((Button) findViewById(R.id.btn_find)).setOnClickListener(this);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find:
                data.clear();
                String orderNum = et_order_num.getText().toString().trim();
                for (int i = 0; i < 10; i++) {
                    float weight = (float) (Math.random() * 1000);
                    int w = (int) weight;
                    data.add(new WareInfo(i, "HIC7039098" + i, "1仓" + i + "排3垛2层", w, w + i, "待出库"));
                }
                orderAdapter.notifyDataSetChanged();
                break;
        }
    }
}
