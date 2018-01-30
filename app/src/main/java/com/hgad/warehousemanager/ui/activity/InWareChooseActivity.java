package com.hgad.warehousemanager.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.InWareListRequest;
import com.hgad.warehousemanager.bean.request.WareInfoRequest;
import com.hgad.warehousemanager.bean.response.InWareListResponse;
import com.hgad.warehousemanager.bean.response.WareInfoResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.OrderAdapter;
import com.hgad.warehousemanager.ui.adapter.ProductAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;
import com.hgad.warehousemanager.zxing.activity.QrScanActivity;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/6/29.
 */
public class InWareChooseActivity extends BaseActivity {

    private static final int SCAN = 99;
    private static final int HAND = 100;
    private LinearLayout ll_more;
    private PopupWindow morePopupWindow;
    private Animation operatingAnim;
    private RelativeLayout rl_info;
    private ImageView infoOperating;
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
                    productAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private XListView lv;
    private List<WareInfo> data = new ArrayList<>();
    private ProductAdapter productAdapter;
    private EditText et_markNum;
    private int currentPage = 1;
    private String type = Constants.IN_TYPE;
    private int userId;
    private boolean isConnect = false;
    private CustomProgressDialog customProgressDialog;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.REFRESH)) {
                callRefresh();
            }
        }
    };


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_inware_choose);
        registBroadcastReceiver(receiver);
    }

    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.REFRESH);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void initData() {
        initHeader("入库货品");
        userId = SPUtils.getInt(this, SPConstants.USER_ID);
        boolean netWork = CommonUtils.checkNetWork(this);
        if (netWork) {
            if (operatingAnim != null) {
                rl_info.setVisibility(View.VISIBLE);
                infoOperating.startAnimation(operatingAnim);
                InWareListRequest inWareListRequest = new InWareListRequest(currentPage);
                sendRequest(inWareListRequest, InWareListResponse.class);
                currentPage--;
                isConnect = false;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isConnect) {
                            rl_info.setVisibility(View.INVISIBLE);
                            infoOperating.clearAnimation();
                        }
                    }
                }, 5000);
                if (Constants.DEBUG) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 10; i++) {
                                data.add(new WareInfo(i, "HIC000" + i, "01010101", "3*1600*1250", "0.5", "1", i + "", ""));
                            }
                            productAdapter.notifyDataSetChanged();
                            rl_info.setVisibility(View.INVISIBLE);
                            infoOperating.clearAnimation();
                        }
                    }, 2000);
                }
            }
        } else {
            CommonUtils.showToast(this, getString(R.string.check_net));
        }
    }

    @Override
    protected void initView() {
        lv = (XListView) findViewById(R.id.lv_in_ware);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        lv.setEmptyView(tv_empty);
        productAdapter = new ProductAdapter(data, this, type);
//        productAdapter.setCallFreshListener(callRefreshListener);
        lv.setAdapter(productAdapter);
        lv.setOnItemClickListener(onItemListener);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(xlistviewListener);
        ImageView iv_more = (ImageView) findViewById(R.id.search);
        iv_more.setImageResource(R.mipmap.scan);
        ll_more = (LinearLayout) findViewById(R.id.ll_search);
        ll_more.setVisibility(View.VISIBLE);
        ll_more.setOnClickListener(this);
        et_markNum = (EditText) findViewById(R.id.et_markNum);
        findViewById(R.id.btn_find).setOnClickListener(this);
//        initMorePopupWindow();
        initAnimation();
    }

    private AdapterView.OnItemClickListener onItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            OrderInfo orderInfo = data.get(position - 1);
//            Intent intent = new Intent(InWareChooseActivity.this, ProductListActivity.class);
//            intent.putExtra(Constants.TYPE, Constants.IN_WARE);
//            intent.putExtra(Constants.ORDER_INFO, orderInfo);
//            startActivity(intent);
            WareInfo wareInfo = data.get(position - 1);
            Intent intent = new Intent(InWareChooseActivity.this, ProductDetailActivity.class);
            intent.putExtra(Constants.TYPE, Constants.IN_WARE);
            intent.putExtra(Constants.WARE_INFO, wareInfo);
            startActivity(intent);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private OrderAdapter.CallFreshListener callRefreshListener = new OrderAdapter.CallFreshListener() {
        @Override
        public void callFresh() {
            callRefresh();
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
        boolean isNetWork = CommonUtils.checkNetWork(this);
        if (isNetWork) {
            isRefresh = false;
            isConnect = false;
            currentPage++;
            InWareListRequest inWareListRequest = new InWareListRequest(currentPage);
            sendRequest(inWareListRequest, InWareListResponse.class);
            currentPage--;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isConnect) {
                        lv.stopLoadMore();
                        CommonUtils.showToast(InWareChooseActivity.this, getString(R.string.poor_signal));
                    }
                }
            }, 5000);
        } else {
            lv.stopLoadMore();
        }
    }

    private boolean isRefresh;

    private void callRefresh() {
        boolean isNetWork = CommonUtils.checkNetWork(this);
        if (isNetWork) {
            isRefresh = true;
            isConnect = false;
            currentPage = 1;
            InWareListRequest inWareListRequest = new InWareListRequest(currentPage);
            sendRequest(inWareListRequest, InWareListResponse.class);
            currentPage--;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isConnect) {
                        lv.stopRefresh();
                        CommonUtils.showToast(InWareChooseActivity.this, getString(R.string.poor_signal));
                    }
                }
            }, 5000);
        } else {
            lv.stopRefresh();
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        isConnect = true;
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
        if (request instanceof InWareListRequest) {
            InWareListResponse inWareListResponse = (InWareListResponse) response;
            if (inWareListResponse.getData() != null) {
                currentPage++;
                if (currentPage == 1) {
                    if (data != null) {
                        data.clear();
                    }
                }
                if (currentPage == inWareListResponse.getData().getPage().getPage()) {
                    List<InWareListResponse.DataEntity.ListEntity> list = inWareListResponse.getData().getList();
                    for (InWareListResponse.DataEntity.ListEntity listEntity : list) {
                        WareInfo wareInfo = new WareInfo();
                        wareInfo.setData(listEntity);
                        data.add(wareInfo);
                    }
                } else {
                    currentPage--;
                }
                handler.sendEmptyMessageDelayed(Constants.NOTIFY, 200);
            }
        } else if (request instanceof WareInfoRequest) {
            data.clear();
            WareInfoResponse wareInfoResponse = (WareInfoResponse) response;
            if (wareInfoResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(wareInfoResponse.getErrorMsg())) {
                    WareInfoResponse.DataEntity entity = wareInfoResponse.getData();
                    if (entity != null) {
                        WareInfo wareInfo = new WareInfo();
                        wareInfo.setData(entity);
                        data.add(wareInfo);
                    }
                }
                handler.sendEmptyMessageDelayed(Constants.NOTIFY, 200);
            }
        }
    }

    private void initAnimation() {
        rl_info = (RelativeLayout) findViewById(R.id.rl_infoOperating);
        infoOperating = (ImageView) findViewById(R.id.infoOperating);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String resultStr = bundle.getString("result");
                Intent intent = new Intent(this, ScanResultActivity.class);
                String codeType = bundle.getString("codeType");
                intent.putExtra(Constants.CODE_TYPE, codeType);
                intent.putExtra(Constants.SCAN_RESULT, resultStr);
                intent.putExtra(Constants.TYPE, Constants.IN_WARE);
                startActivity(intent);
            }
        } else if (resultCode == Constants.RESULT_OK && requestCode == HAND) {
            callRefresh();
        }
    }

//    private void initMorePopupWindow() {
//        View contentView = View.inflate(this, R.layout.popupwindow_inware, null);
//        morePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        morePopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000"))); //设置背景
//        morePopupWindow.setFocusable(true); //设置获取焦点
////        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
//        morePopupWindow.setOutsideTouchable(true);
//        morePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                CommonUtils.backgroundAlpha(1f, InWareChooseActivity.this);
//            }
//        });
//        contentView.findViewById(R.id.ll_in_hand).setOnClickListener(this);
//        contentView.findViewById(R.id.ll_in_scan).setOnClickListener(this);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
//                showMore();
                go2Scan();
                break;
//            case R.id.ll_in_hand:
//                go2InHand();
//                break;
//            case R.id.ll_in_scan:
//                go2Scan();
//                break;
            case R.id.btn_find:
                search();
                break;
        }
    }

    private void showMore() {
        morePopupWindow.showAsDropDown(ll_more);
        CommonUtils.backgroundAlpha(0.8f, this);
    }

    private void go2Scan() {
//        morePopupWindow.dismiss();
        Intent intent = new Intent(this, QrScanActivity.class);
        intent.putExtra(Constants.TYPE, Constants.IN_WARE);
        startActivityForResult(intent, SCAN);
    }

    private void go2InHand() {
        morePopupWindow.dismiss();
        Intent intent = new Intent(this, HandOperateActivity.class);
        intent.putExtra(Constants.TYPE, Constants.IN_WARE);
        startActivityForResult(intent, HAND);
    }

    private void search() {
        String markNum = et_markNum.getText().toString().trim();
        if (TextUtils.isEmpty(markNum)) {
            callRefresh();
            return;
        }
        showDialog(getString(R.string.info_check));
        WareInfoRequest wareInfoRequest = new WareInfoRequest(markNum);
        sendRequest(wareInfoRequest, WareInfoResponse.class);
    }

    private void showDialog(String content) {
        customProgressDialog = new CustomProgressDialog(this, content);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        isConnect = false;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isConnect) {
                    if (customProgressDialog != null) {
                        customProgressDialog.dismiss();
                        CommonUtils.showToast(InWareChooseActivity.this, getString(R.string.poor_signal));
                    }
                }
            }
        }, 5000);
    }
}
