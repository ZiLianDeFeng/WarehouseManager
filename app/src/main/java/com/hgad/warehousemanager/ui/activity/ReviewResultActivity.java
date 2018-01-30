package com.hgad.warehousemanager.ui.activity;

import android.os.Handler;
import android.os.Message;
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
import com.hgad.warehousemanager.bean.ReviewInfo;
import com.hgad.warehousemanager.bean.request.OutOrderListRequest;
import com.hgad.warehousemanager.bean.response.OutOrderListResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.ReviewResultAdapter;
import com.hgad.warehousemanager.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/8/15.
 */
public class ReviewResultActivity extends BaseActivity {

    private ReviewResultAdapter reviewResultAdapter;
    private List<ReviewInfo> data = new ArrayList<>();
    private RelativeLayout rl_info;
    private ImageView infoOperating;
    private Animation operatingAnim;
    private LinearInterpolator lin;
    private boolean isConnect;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.NOTIFY:
                    isConnect = true;
                    if (isRefresh) {
                        lv.stopRefresh();
                        lv.setRefreshTime(CommonUtils.getCurrentTime());
                    } else {
                        lv.stopLoadMore();
                    }
                    rl_info.setVisibility(View.INVISIBLE);
                    infoOperating.clearAnimation();
                    if (data.size() < 10) {
                        lv.setPullLoadEnable(false);
                    } else {
                        lv.setPullLoadEnable(true);
                    }
                    reviewResultAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private int currentPage = 1;
    private String state = "'3','4'";
    private XListView lv;
    private boolean isRefresh;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_review_result);
    }

    @Override
    protected void initData() {
        initHeader("审核记录");
        boolean netWork = CommonUtils.checkNetWork(this);
        if (netWork) {
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
            }
            isConnect = false;
            if (Constants.DEBUG) {
                for (int i = 0; i < 10; i++) {
                    data.add(new ReviewInfo("F3Q84276" + i, "因为货品量过多，时间紧急，无法将全部货品审核完，遂申请出库", "2017-8-15", "admin", i % 3 == 0 ? "非常紧急" : i % 3 == 1 ? "一般" : "不紧急", "admin", "1000kg", i % 3 == 0 ? "0" : i % 3 == 1 ? "1" : "2"));
                }
                reviewResultAdapter.notifyDataSetChanged();
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isConnect) {
                            rl_info.setVisibility(View.INVISIBLE);
                            infoOperating.clearAnimation();
                        }
                    }
                }, 5000);
                OutOrderListRequest outOrderListRequest = new OutOrderListRequest(currentPage, state, null);
                sendRequest(outOrderListRequest, OutOrderListResponse.class);
                currentPage--;
            }
        } else {
            CommonUtils.showToast(this, getString(R.string.check_net));
        }
    }

    @Override
    protected void initView() {
        lv = (XListView) findViewById(R.id.lv_review_result);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        lv.setEmptyView(tv_empty);
        reviewResultAdapter = new ReviewResultAdapter(data, this);
        lv.setAdapter(reviewResultAdapter);
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
        lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    private AdapterView.OnItemClickListener onItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

    private void callRefresh() {
        boolean isNetWork = CommonUtils.checkNetWork(this);
        if (isNetWork) {
            isConnect = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isConnect) {
                        lv.stopRefresh();
                    }
                }
            }, 5000);
            isRefresh = true;
            currentPage = 1;
            OutOrderListRequest outOrderListRequest = new OutOrderListRequest(currentPage, state, null);
            sendRequest(outOrderListRequest, OutOrderListResponse.class);
            currentPage--;
        } else {
            lv.stopRefresh();
            CommonUtils.showToast(this, getString(R.string.check_net));
        }
    }

    private void callLoadMore() {
        boolean isNetWork = CommonUtils.checkNetWork(this);
        if (isNetWork) {
            isRefresh = false;
            isConnect = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isConnect) {
                        lv.stopLoadMore();
                    }
                }
            }, 5000);
            currentPage++;
            OutOrderListRequest outOrderListRequest = new OutOrderListRequest(currentPage, state, null);
            sendRequest(outOrderListRequest, OutOrderListResponse.class);
            currentPage--;
        } else {
            lv.stopLoadMore();
            CommonUtils.showToast(this, getString(R.string.check_net));
        }
    }


    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        isConnect = true;
        if (request instanceof OutOrderListRequest) {
            OutOrderListResponse outOrderListResponse = (OutOrderListResponse) response;
            if (outOrderListResponse.getData() != null) {
                currentPage++;
                if (currentPage == 1) {
                    if (data != null) {
                        data.clear();
                    }
                }
                if (currentPage == outOrderListResponse.getData().getPage().getPage() ) {
                    List<OutOrderListResponse.DataEntity.ListEntity> list = outOrderListResponse.getData().getList();
                    for (OutOrderListResponse.DataEntity.ListEntity listEntity : list) {
                        ReviewInfo reviewInfo = new ReviewInfo();
                        reviewInfo.setData(listEntity);
                        data.add(reviewInfo);
                    }
                }
                handler.sendEmptyMessage(Constants.NOTIFY);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
