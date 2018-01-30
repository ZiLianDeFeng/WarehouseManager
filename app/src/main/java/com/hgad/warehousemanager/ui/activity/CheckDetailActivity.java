package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/7/27.
 */
public class CheckDetailActivity extends BaseActivity {

    private static final int SCAN = 100;
    private XListView lv;
    private List<WareInfo> data = new ArrayList<>();
    private ProductAdapter productAdapter;
    private Animation operatingAnim;
    private RelativeLayout rl_info;
    private ImageView infoOperating;
    private Handler handler = new Handler();

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_check_detail);
    }

    @Override
    protected void initData() {
        initHeader("盘点记录");
        if (Constants.DEBUG) {
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
//            TaskRequest taskRequest = new TaskRequest(type, currentPage, userId);
//            sendRequest(taskRequest, TaskResponse.class);
//            currentPage--;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rl_info.setVisibility(View.INVISIBLE);
                        infoOperating.clearAnimation();
                        for (int i = 0; i < 10; i++) {
                            data.add(new WareInfo(i, "HIC7508653", "01010101", "3*1600", "2", "", "", ""));
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        }
    }

    @Override
    protected void initView() {
        lv = (XListView) findViewById(R.id.lv_check_detail);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        productAdapter = new ProductAdapter(data, this, "");
        lv.setAdapter(productAdapter);
        lv.setEmptyView(tv_empty);
        lv.setOnItemClickListener(onItemListener);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(xlistviewListener);
        initAnimation();
    }

    private void initAnimation() {
        rl_info = (RelativeLayout) findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }


    private AdapterView.OnItemClickListener onItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            WareInfo wareInfo = data.get(position);
            Intent intent = new Intent(CheckDetailActivity.this, CheckResultActivity.class);
            intent.putExtra(Constants.WARE_INFO, wareInfo);
            startActivity(intent);
        }
    };

    private XListView.IXListViewListener xlistviewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            callRefresh();
        }

        @Override
        public void onLoadMore() {
            callLoadMore();
        }
    };

    private void callLoadMore() {
        lv.stopLoadMore();
    }

    private void callRefresh() {
        lv.stopRefresh();
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                Intent intent = new Intent(this, CheckResultActivity.class);
                intent.putExtra(Constants.SCAN_RESULT, result);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}
