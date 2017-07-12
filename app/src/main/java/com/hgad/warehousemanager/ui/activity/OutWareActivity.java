package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseReponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.ui.adapter.OrderAdapter;
import com.hgad.warehousemanager.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/29.
 */
public class OutWareActivity extends BaseActivity {

    private ListView lv;
    private List<WareInfo> data = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private EditText et_order_num;
    private SwipeRefreshLayout swipeRefreshView;
    private Animation operatingAnim;
    private RelativeLayout rl_info;
    private ImageView infoOperating;
    private Handler handler = new Handler();

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
        lv.setOnItemClickListener(itemListener);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        lv.setEmptyView(tv_empty);
        et_order_num = (EditText) findViewById(R.id.et_order_num);
        findViewById(R.id.btn_find).setOnClickListener(this);
        swipeRefreshView = (SwipeRefreshLayout) findViewById(R.id.srl);
        swipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshView.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // 开始刷新，设置当前为刷新状态
                //swipeRefreshLayout.setRefreshing(true);

                // 这里是主线程
                // 一些比较耗时的操作，比如联网获取数据，需要放到子线程去执行
                final Random random = new Random();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data.add(0, new WareInfo(9, "HIC7039098" + 9, "1仓" + (9 + 1) + "排3垛2层", 500, 510, "待出库"));
                        orderAdapter.notifyDataSetChanged();
                        CommonUtils.showToast(OutWareActivity.this, "刷新成功");
                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        swipeRefreshView.setRefreshing(false);
                    }
                }, 2000);

                // System.out.println(Thread.currentThread().getName());

                // 这个不能写在外边，不然会直接收起来
                //swipeRefreshLayout.setRefreshing(false);
            }
        });
        initAnimation();
    }

    private void initAnimation() {
        rl_info = (RelativeLayout) findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            WareInfo wareInfo = data.get(position);
            Intent intent = new Intent(OutWareActivity.this, WareHouseActivity.class);
            intent.putExtra(Constants.ADDRESS, wareInfo.getAddress());
            intent.putExtra(Constants.TYPE, Constants.OUT_WARE);
            startActivity(intent);
        }
    };

    @Override
    public void onSuccessResult(BaseRequest request, BaseReponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find:
                data.clear();
                if (operatingAnim != null) {
                    rl_info.setVisibility(View.VISIBLE);
                    infoOperating.startAnimation(operatingAnim);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rl_info.setVisibility(View.INVISIBLE);
                            infoOperating.clearAnimation();
                            String orderNum = et_order_num.getText().toString().trim();
                            for (int i = 0; i < 10; i++) {
                                float weight = (float) (Math.random() * 1000);
                                int w = (int) weight;
                                data.add(new WareInfo(i, "HIC7039098" + i, "1仓" + (i + 1) + "排3垛2层", w, w + i, "待出库"));
                            }
                            orderAdapter.notifyDataSetChanged();
                        }
                    }, 2000);
                }

                break;
        }
    }
}
