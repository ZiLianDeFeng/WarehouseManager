package com.hgad.warehousemanager.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.bean.ProSectionBean;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.ForUpRequest;
import com.hgad.warehousemanager.bean.request.ProductListRequest;
import com.hgad.warehousemanager.bean.response.ForUpResponse;
import com.hgad.warehousemanager.bean.response.ProductListResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.ProductAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;
import com.hgad.warehousemanager.zxing.activity.QrScanActivity;
import com.max.pinnedsectionrefreshlistviewdemo.PinnedSectionRefreshListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */
public class ProductListActivity extends BaseActivity {

    private static final int SCAN = 99;
    private static final int RESULT = 300;
    private PinnedSectionRefreshListView lv;
    private List<WareInfo> data = new ArrayList<>();
    private List<WareInfo> allData = new ArrayList<>();
    private List<ProSectionBean> newData = new ArrayList<>();
    private ProductAdapter productAdapter;
    private SwipeRefreshLayout swipeRefreshView;
    private Animation operatingAnim;
    private RelativeLayout rl_info;
    private ImageView infoOperating;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.NOTIFY:
                    rl_info.setVisibility(View.INVISIBLE);
                    infoOperating.clearAnimation();
                    productAdapter.notifyDataSetChanged();
                    swipeRefreshView.setRefreshing(false);
                    break;
            }
        }
    };
    //    private String type = Constants.OUT_WARE;
    private String type;
    private int orderId;
    private PopupWindow morePopupWindow;
    private LinearLayout ll_more;
    private String orderNum;
    private String taskState;
    private boolean isConnet;
    private CustomProgressDialog customProgressDialog;
    private Button btn_commit;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.REFRESH)) {
                callRefresh();
            } else if (action.equals(Constants.OUT_TYPE) || action.equals(Constants.REVIEW_TYPE)) {
                String type = intent.getStringExtra(Constants.TYPE);
                toResult(type);
            }
        }
    };
    private OrderInfo orderInfo;
    private String proType;
    private InputMethodManager imm;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_product_list);
        registBroadcastReceiver(receiver);
    }

    private void toResult(String type) {
        Intent intent = new Intent(this, OperateResultActivity.class);
        intent.putExtra(Constants.TYPE, type);
        intent.putExtra(Constants.ORDER_INFO, orderInfo);
        intent.putExtra(Constants.LIST_DATA, ((Serializable) data));
        startActivityForResult(intent, RESULT);
    }


    private void registBroadcastReceiver(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.REFRESH);
        intentFilter.addAction(Constants.OUT_TYPE);
        intentFilter.addAction(Constants.REVIEW_TYPE);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        orderInfo = (OrderInfo) intent.getSerializableExtra(Constants.ORDER_INFO);
        proType = intent.getStringExtra(Constants.PRO_TYPE);
        if (orderInfo != null) {
            orderNum = orderInfo.getOrderNum();
            orderId = orderInfo.getTaskId();
            taskState = orderInfo.getState();
        }
        initHeader("订单" + orderNum);
        type = intent.getStringExtra(Constants.TYPE);
        productAdapter.setType(type);
        productAdapter.setProType(proType);
        data.clear();
        if (Constants.REVIEW_TYPE.equals(type)) {
            btn_commit.setVisibility(View.VISIBLE);
        } else {
            btn_commit.setVisibility(View.GONE);
        }
        if (operatingAnim != null) {
            rl_info.setVisibility(View.VISIBLE);
            infoOperating.startAnimation(operatingAnim);
            isConnet = false;
            ProductListRequest productListRequest = new ProductListRequest(orderNum);
            sendRequest(productListRequest, ProductListResponse.class);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isConnet) {
                        rl_info.setVisibility(View.INVISIBLE);
                        infoOperating.clearAnimation();
                    }
                }
            }, 5000);
        }
    }

    @Override
    protected void initView() {
        lv = (PinnedSectionRefreshListView) findViewById(R.id.lv_product);
        productAdapter = new ProductAdapter(data, this, type);
        lv.setPullRefreshEnable(false);
        lv.setPullRefreshEnable(false);
        lv.setAdapter(productAdapter);
        lv.setOnItemClickListener(itemListener);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        lv.setEmptyView(tv_empty);
        swipeRefreshView = (SwipeRefreshLayout) findViewById(R.id.srl);
        swipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshView.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 开始刷新，设置当前为刷新状态
                // swipeRefreshView.setRefreshing(true);
                // 这里是主线程
                // 一些比较耗时的操作，比如联网获取数据，需要放到子线程去执行
                callRefresh();
            }
        });
        initAnimation();
        ImageView iv_more = (ImageView) findViewById(R.id.search);
        iv_more.setImageResource(R.mipmap.scan);
        ll_more = (LinearLayout) findViewById(R.id.ll_search);
        ll_more.setVisibility(View.VISIBLE);
        ll_more.setOnClickListener(this);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
    }

    private void callRefresh() {
        ProductListRequest productListRequest = new ProductListRequest(orderNum);
        sendRequest(productListRequest, ProductListResponse.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            WareInfo wareInfo = data.get(position - 1);
            Intent intent = new Intent(ProductListActivity.this, ScanResultActivity.class);
            intent.putExtra(Constants.TYPE, type);
            intent.putExtra(Constants.ORDER_INFO, orderInfo);
            intent.putExtra(Constants.WARE_INFO, wareInfo);
            boolean isLast = false;
            boolean hava = false;
            for (WareInfo info : data) {
                if (info.getMarkNum().equals(wareInfo.getMarkNum())) {
                    wareInfo = info;
                    hava = true;
                }
            }
            if (hava) {
                int i = 0;
                for (WareInfo info : data) {
                    if ("0".equals(info.getState())) {
                        i++;
                    }
                }
                if (i == 1) {
                    isLast = true;
                }
            }
            intent.putExtra(Constants.IS_LAST, isLast);
            startActivityForResult(intent, Constants.NOTIFY);
//            }
//            } else if ("1".equals(taskState)) {
//                CommonUtils.showToast(ProductListActivity.this, getString(R.string.not_accept));
//            } else {
//                CommonUtils.showToast(ProductListActivity.this, getString(R.string.done_task));
//            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.NOTIFY:
                if (resultCode == Constants.RESULT_OK) {
                    callRefresh();
                }
                break;
            case SCAN:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String resultStr = bundle.getString("result");
                        String scanState = bundle.getString(Constants.SCAN_STATE);
                        Intent intent = new Intent(this, ScanResultActivity.class);
                        String codeType = bundle.getString("codeType");
                        intent.putExtra(Constants.CODE_TYPE, codeType);
                        intent.putExtra(Constants.SCAN_RESULT, resultStr);
                        intent.putExtra(Constants.TYPE, type);
                        intent.putExtra(Constants.ORDER_INFO, orderInfo);
                        intent.putExtra(Constants.SCAN_STATE, scanState);
                        intent.putExtra(Constants.LIST_DATA, ((Serializable) this.data));
                        startActivityForResult(intent, Constants.NOTIFY);
                    }
                }
                break;
            case RESULT:
                if (resultCode == Constants.RESULT_OK) {
                    go2Scan();
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
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
        }
        isConnet = true;
        if (request instanceof ProductListRequest) {
            ProductListResponse productListResponse = (ProductListResponse) response;
            if (productListResponse.getData() != null) {
                data.clear();
                allData.clear();
                boolean haveNeedScan = false;
                boolean haveNeedCheck = false;
                boolean haveDoneCheck = false;
                List<ProductListResponse.DataEntity.ListEntity> list = productListResponse.getData().getList();
                for (ProductListResponse.DataEntity.ListEntity listEntity : list) {
                    WareInfo wareInfo = new WareInfo();
                    wareInfo.setData(listEntity);
                    allData.add(wareInfo);
                    if (Constants.OUT_TYPE.equals(type) && !wareInfo.getState().equals("2")) {
                        data.add(wareInfo);
                    } else if (Constants.REVIEW_TYPE.equals(type) && !wareInfo.getState().equals("0")) {
                        data.add(wareInfo);
                    }
                    if (listEntity.getStatus().equals("1")) {
                        haveNeedCheck = true;
                    }
                    if (listEntity.getStatus().equals("2")) {
                        haveDoneCheck = true;
                    }
                    if (listEntity.getStatus().equals("0")) {
                        haveNeedScan = true;
                    }
                }
                if ("1".equals(proType) && Constants.REVIEW_TYPE.equals(type)) {
                    ll_more.setVisibility(View.GONE);
                    if (!haveNeedScan) {
                        btn_commit.setBackgroundColor(getResources().getColor(R.color.btn_color));
                        btn_commit.setOnClickListener(this);
                    }
                }
                if ("0".equals(proType)) {
                    if (!haveNeedCheck && haveDoneCheck) {
                        btn_commit.setBackgroundColor(getResources().getColor(R.color.btn_color));
                        btn_commit.setOnClickListener(this);
                    } else {
                        btn_commit.setBackgroundColor(getResources().getColor(R.color.gray));
                        btn_commit.setOnClickListener(null);
                    }
                }
                Collections.sort(data, new Comparator<WareInfo>() {
                    @Override
                    public int compare(WareInfo lhs, WareInfo rhs) {
                        return lhs.getState().compareTo(rhs.getState());
                    }
                });
//                ArrayList<ProSectionBean> beans = ProSectionBean.getData(this.data);
//                newData.addAll(beans);
                handler.sendEmptyMessageDelayed(Constants.NOTIFY, 200);
            }
        } else if (request instanceof ForUpRequest) {
            ForUpResponse forUpResponse = (ForUpResponse) response;
            if (forUpResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(this, "当前出库货品已复核完毕");
                Intent intent = new Intent(Constants.ORDER_REFRESH);
                sendBroadcast(intent);
//                setResult(Constants.NOTIFY);
                finish();
//                new AlertView("提示", "当前出库货品已复核完毕", null, new String[]{"确认"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Object o, int position) {
//
//                        switch (position) {
//                            case 0:
//                                break;
//                        }
//                    }
//                }).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
                go2Scan();
                break;
            case R.id.ll_in_hand:
                if ("2".equals(taskState)) {
                    go2InHand();
                } else if ("1".equals(taskState)) {
                    CommonUtils.showToast(ProductListActivity.this, getString(R.string.not_accept));
                } else {
                    CommonUtils.showToast(ProductListActivity.this, getString(R.string.done_task));
                }
                break;
            case R.id.ll_in_scan:
                if ("2".equals(taskState)) {
                    go2Scan();
                } else if ("1".equals(taskState)) {
                    CommonUtils.showToast(ProductListActivity.this, getString(R.string.not_accept));
                } else {
                    CommonUtils.showToast(ProductListActivity.this, getString(R.string.done_task));
                }
                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void commit() {
        if ("1".equals(proType)) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alertext_form, null);
            final EditText et_pieces = (EditText) extView.findViewById(R.id.et_pieces);
            final AlertView mAlertViewExt = new AlertView("请填写复核的钢板张数", null, "取消", new String[]{"完成"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    switch (position) {
                        case 0:
                            CommonUtils.closeKeybord(et_pieces, ProductListActivity.this);
                            String pieces = et_pieces.getText().toString().trim();
                            if (TextUtils.isEmpty(pieces)) {
                                CommonUtils.showToast(ProductListActivity.this, "未填写张数");
                                return;
                            }
                            int total = 0;
                            for (WareInfo wareInfo : allData) {
                                total += Integer.parseInt(wareInfo.getCount().trim());
                            }
                            if (pieces.equals(total + "")) {
                                customProgressDialog = new CustomProgressDialog(ProductListActivity.this, "信息提交中");
                                customProgressDialog.setCancelable(false);
                                customProgressDialog.setCanceledOnTouchOutside(false);
                                customProgressDialog.show();
                                ForUpRequest forUpRequest = new ForUpRequest(orderId, "2", null);
                                sendRequest(forUpRequest, ForUpResponse.class);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!isConnet) {
                                            if (customProgressDialog != null) {
                                                customProgressDialog.dismiss();
                                                CommonUtils.showToast(ProductListActivity.this, getString(R.string.poor_signal));
                                            }
                                        }
                                    }
                                }, 5000);
                            } else {
                                CommonUtils.showToast(ProductListActivity.this, "张数不一致，请重新确认！");
                            }
                            break;
                        case -1:
                            CommonUtils.closeKeybord(et_pieces, ProductListActivity.this);
                            break;
                    }
                }
            });
            et_pieces.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean focus) {
                    //输入框出来则往上移动
                    boolean isOpen = imm.isActive();
                    mAlertViewExt.setMarginBottom(isOpen && focus ? 150 : 0);
                }
            });
            mAlertViewExt.addExtView(extView);
            mAlertViewExt.show();
        } else {
            customProgressDialog = new CustomProgressDialog(this, "信息提交中");
            customProgressDialog.setCancelable(false);
            customProgressDialog.setCanceledOnTouchOutside(false);
            customProgressDialog.show();
            isConnet = false;
            boolean notAll = false;
            for (WareInfo wareInfo : allData) {
                if (!wareInfo.getState().equals("2")) {
                    notAll = true;
                    break;
                }
            }
            if (notAll) {
                ForUpRequest forUpRequest = new ForUpRequest(orderId, null, "1");
                sendRequest(forUpRequest, ForUpResponse.class);
            } else {
                ForUpRequest forUpRequest = new ForUpRequest(orderId, "2", null);
                sendRequest(forUpRequest, ForUpResponse.class);
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isConnet) {
                        if (customProgressDialog != null) {
                            customProgressDialog.dismiss();
                            CommonUtils.showToast(ProductListActivity.this, getString(R.string.poor_signal));
                        }
                    }

                }
            }, 5000);
        }
    }

    private void go2Scan() {
//        morePopupWindow.dismiss();
        Intent intent = new Intent(this, QrScanActivity.class);
        intent.putExtra(Constants.TYPE, type);
        intent.putExtra(Constants.ORDER_INFO, orderInfo);
        intent.putExtra(Constants.LIST_DATA, ((Serializable) data));
        startActivityForResult(intent, SCAN);
    }

    private void go2InHand() {
        morePopupWindow.dismiss();
        Intent intent = new Intent(this, InWareByHandActivity.class);
        intent.putExtra(Constants.TYPE, type);
        intent.putExtra(Constants.LIST_DATA, ((Serializable) data));
        startActivityForResult(intent, Constants.NOTIFY);
    }

    private void showMore() {
        morePopupWindow.showAsDropDown(ll_more);
        CommonUtils.backgroundAlpha(0.8f, this);
    }
}
