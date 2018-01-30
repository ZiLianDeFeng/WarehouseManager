package com.hgad.warehousemanager.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseFragment;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.bean.request.OutOrderListRequest;
import com.hgad.warehousemanager.bean.response.OutOrderListResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.activity.ProductListActivity;
import com.hgad.warehousemanager.ui.adapter.OrderAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/8/10.
 */
public class ReviewFragment extends BaseFragment {
    private View mView;
    private XListView lv;
    private List<OrderInfo> data = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private SwipeRefreshLayout swipeRefreshView;
    private Animation operatingAnim;
    private RelativeLayout rl_info;
    private ImageView infoOperating;
    private boolean isConnect;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.NOTIFY:
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
                    orderAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private String type = Constants.REVIEW_TYPE;
    private int currentPage = 1;
    private int userId;
    private String state = "'2'";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.ORDER_REFRESH)) {
                callRefresh();
            }
        }
    };

    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ORDER_REFRESH);
        mContext.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void initView() {
        lv = (XListView) mView.findViewById(R.id.lv_order);
        orderAdapter = new OrderAdapter(data, mContext, type);
        orderAdapter.setCallFreshListener(callRefreshListener);
        lv.setAdapter(orderAdapter);
        lv.setOnItemClickListener(itemListener);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(xlistviewListener);
        TextView tv_empty = (TextView) mView.findViewById(R.id.tv_empty);
        lv.setEmptyView(tv_empty);
        initAnimation();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registBroadcastReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void initData() {
        userId = SPUtils.getInt(mContext, SPConstants.USER_ID);
        boolean netWork = CommonUtils.checkNetWork(mContext);
        if (netWork) {
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
            }
            isConnect = false;
            if (Constants.DEBUG) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            data.add(new OrderInfo("F3Q4235" + i, 50, "0", i, "1", "zang", "test", "2017-8-15", "1000"));
                        }
                        orderAdapter.notifyDataSetChanged();
                        rl_info.setVisibility(View.INVISIBLE);
                        infoOperating.clearAnimation();
                    }
                }, 2000);
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
                OutOrderListRequest outOrderListRequest = new OutOrderListRequest(currentPage, state, "");
                sendRequest(outOrderListRequest, OutOrderListResponse.class);
                currentPage--;
            }
        } else {
            CommonUtils.showToast(mContext, getString(R.string.check_net));
        }
    }

    private void initAnimation() {
        rl_info = (RelativeLayout) mView.findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) mView.findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }


    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            boolean isSwipe = orderAdapter.isSwipe();
            if (isSwipe)
                return;
            OrderInfo orderInfo = data.get(position - 1);
            Intent intent = new Intent(mContext, ProductListActivity.class);
            intent.putExtra(Constants.TYPE, Constants.REVIEW_TYPE);
            intent.putExtra(Constants.ORDER_INFO, orderInfo);
            startActivity(intent);
        }
    };

    private boolean isRefresh;
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

    private OrderAdapter.CallFreshListener callRefreshListener = new OrderAdapter.CallFreshListener() {
        @Override
        public void callFresh() {
            callRefresh();
        }
    };

    private void callLoadMore() {
        boolean isNetWork = CommonUtils.checkNetWork(mContext);
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
            OutOrderListRequest outOrderListRequest = new OutOrderListRequest(currentPage, state, "");
            sendRequest(outOrderListRequest, OutOrderListResponse.class);
            currentPage--;
        } else {
            lv.stopLoadMore();
            CommonUtils.showToast(mContext, getString(R.string.check_net));
        }
    }

    private void callRefresh() {
        boolean isNetWork = CommonUtils.checkNetWork(mContext);
        if (isNetWork) {
            isRefresh = true;
            isConnect = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isConnect) {
                        lv.stopLoadMore();
                    }
                }
            }, 5000);
            currentPage = 1;
            OutOrderListRequest outOrderListRequest = new OutOrderListRequest(currentPage, state, "");
            sendRequest(outOrderListRequest, OutOrderListResponse.class);
            currentPage--;
        } else {
            lv.stopRefresh();
            CommonUtils.showToast(mContext, getString(R.string.check_net));
        }
    }


    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_review, null);
        return mView;
    }

    @Override
    public <Res extends BaseResponse> void onSuccessResult(BaseRequest request, Res response) {
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
                if (currentPage == outOrderListResponse.getData().getPage().getPage() + 1) {
                    List<OutOrderListResponse.DataEntity.ListEntity> list = outOrderListResponse.getData().getList();
                    for (OutOrderListResponse.DataEntity.ListEntity listEntity : list) {
                        OrderInfo orderInfo = new OrderInfo();
                        orderInfo.setData(listEntity);
                        data.add(orderInfo);
                    }
                }
                handler.sendEmptyMessageDelayed(Constants.NOTIFY, 200);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void search(String orderNum) {
        CommonUtils.showToast(mContext, "查询审核订单" + orderNum);
    }
}
