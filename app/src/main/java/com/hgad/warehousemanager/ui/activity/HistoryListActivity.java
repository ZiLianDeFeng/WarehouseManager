package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
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
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.SearchRequest;
import com.hgad.warehousemanager.bean.request.WareInfoRequest;
import com.hgad.warehousemanager.bean.response.SearchResponse;
import com.hgad.warehousemanager.bean.response.WareInfoResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.HistoryListAdapter;
import com.hgad.warehousemanager.util.CommonUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/8/21.
 */
public class HistoryListActivity extends BaseActivity {
    private List<WareInfo> data = new ArrayList<>();
    private HistoryListAdapter historyListAdapter;
    private int currentPage = 1;
    private String markNum;
    private String orderItem;
    private String proName;
    private String address;
    private XListView lv;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
                    historyListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private RelativeLayout rl_info;
    private ImageView infoOperating;
    private Animation operatingAnim;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_history_list);
    }

    @Override
    protected void initData() {
        initHeader("查询结果");
        Intent intent = getIntent();
        markNum = intent.getStringExtra(Constants.MARK_NUM);
        orderItem = intent.getStringExtra(Constants.ORDER_ITEM);
        proName = intent.getStringExtra(Constants.PRO_NAME);
        address = intent.getStringExtra(Constants.ADDRESS);
        String resultStr = intent.getStringExtra(Constants.SCAN_RESULT);
        String codeType = intent.getStringExtra(Constants.CODE_TYPE);
        String scanState = intent.getStringExtra(Constants.SCAN_STATE);
        getResultIfnotNull(resultStr, codeType, scanState);
        if (operatingAnim != null) {
            rl_info.setVisibility(View.VISIBLE);
            infoOperating.startAnimation(operatingAnim);
        }
        isConnect = false;
        if (Constants.DEBUG) {
            rl_info.setVisibility(View.INVISIBLE);
            infoOperating.clearAnimation();
            for (int i = 0; i < 10; i++) {
                data.add(new WareInfo(i, "HIC78456684" + i, "01010101", "3*1600", "1.8", "0", "F3Q786542", "热镀锌卷"));
            }
            historyListAdapter.notifyDataSetChanged();
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
            SearchRequest searchRequest = new SearchRequest(markNum, orderItem, proName, address, currentPage);
            sendRequest(searchRequest, SearchResponse.class);
            currentPage--;
        }
    }

    private void getResultIfnotNull(String resultStr, String codeType, String scanState) {
        if (resultStr != null) {
            String encoding = CommonUtils.getEncoding(resultStr);
            String UTF_Str = "";
            String GB_Str = "";
            boolean is_cN = false;
            try {
                if (encoding.equals("GB2312")) {
                    UTF_Str = new String(resultStr.getBytes("GB2312"), "GB2312");
                } else if (encoding.equals("ISO-8859-1")) {
                    UTF_Str = new String(resultStr.getBytes("ISO-8859-1"), "UTF-8");
                }
                is_cN = CommonUtils.isChineseCharacter(UTF_Str);
                //防止有人特意使用乱码来生成二维码来判断的情况
                boolean b = CommonUtils.isSpecialCharacter(resultStr);
                if (b) {
                    is_cN = true;
                }
                if (!is_cN) {
                    GB_Str = new String(resultStr.getBytes("ISO-8859-1"), "GB2312");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String result;
            if (is_cN) {
                result = UTF_Str;
            } else {
                result = GB_Str;
            }
            if (Constants.NO_PLATES.equals(scanState)) {
                if (Constants.QR.equals(codeType)) {
                    if (result.contains("|") && result.contains(":")) {
                        if (!result.contains("标签号")) {
                            String markNumStr = result;
                            for (int i = 0; i < 3; i++) {
                                markNumStr = markNumStr.substring(markNumStr.indexOf(":") + 1);
                            }
                            markNum = subStringUnCh(markNumStr);
                        } else {
                            String markNumStr = result.substring(result.indexOf("标签号"));
                            markNum = subStringInfo(markNumStr);
                            String numberStr = result.substring(result.indexOf("张数"));
                            String pieces = subStringInfo(numberStr);
                            boolean matches = pieces.matches("[0-9]+");
                            if (matches) {
                                CommonUtils.showToast(this, "请选择'钢板/板材'进行扫描");
                            }
                        }
                    } else {
                        CommonUtils.showToast(this, "内容不匹配，请重新扫描");
                    }
                } else if (Constants.CODE.equals(codeType)) {
                    markNum = resultStr;
                    WareInfoRequest wareInfoRequest = new WareInfoRequest(markNum);
                    sendRequest(wareInfoRequest, WareInfoResponse.class);
                }
            } else if (Constants.PLATES.equals(scanState)) {
                if (Constants.CODE.equals(codeType)) {
                    CommonUtils.showToast(this, "钢板需扫二维码,请重新扫描二维码");
                } else if (Constants.QR.equals(codeType)) {
                    if (!result.contains("标签号")) {
                        CommonUtils.showToast(this, "内容不匹配，请重新扫描");
                    } else {
                        String markNumStr = result.substring(result.indexOf("标签号"));
                        markNum = subStringInfo(markNumStr);
                        String numberStr = result.substring(result.indexOf("张数"));
                        String count = subStringInfo(numberStr);
                        boolean matches = count.matches("[0-9]+");
                        if (!matches) {
                            CommonUtils.showToast(this, "请选择'钢卷/钢轧'进行扫描");
                        }
                    }
                }
            }
        }
    }

    private String subStringUnCh(String str) {
        if (!str.contains("|")) {
            return str.substring(0).trim();
        }
        return str.substring(0, str.indexOf("|")).trim();
    }

    private String subStringInfo(String str) {
        if (!str.contains("|")) {
            return str.substring(str.indexOf(":") + 1).trim();
        }
        return str.substring(str.indexOf(":") + 1, str.indexOf("|")).trim();
    }

    @Override
    protected void initView() {
        lv = (XListView) findViewById(R.id.lv_history_list);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        lv.setEmptyView(tv_empty);
        historyListAdapter = new HistoryListAdapter(data, this);
        lv.setAdapter(historyListAdapter);
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
            Intent intent = new Intent(HistoryListActivity.this, ProductDetailActivity.class);
            intent.putExtra(Constants.WARE_INFO, data.get(position - 1));
            intent.putExtra(Constants.TYPE, Constants.HISTORY_TYPE);
            startActivity(intent);
        }
    };

    private boolean isConnect;
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
            SearchRequest searchRequest = new SearchRequest(markNum, orderItem, proName, address, currentPage);
            sendRequest(searchRequest, SearchResponse.class);
            currentPage--;
        } else {
            lv.stopLoadMore();
            CommonUtils.showToast(this, getString(R.string.check_net));
        }
    }

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
            SearchRequest searchRequest = new SearchRequest(markNum, orderItem, proName, address, currentPage);
            sendRequest(searchRequest, SearchResponse.class);
            currentPage--;
        } else {
            lv.stopRefresh();
            CommonUtils.showToast(this, getString(R.string.check_net));
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        isConnect = true;
        if (request instanceof SearchRequest) {
            SearchResponse searchResponse = (SearchResponse) response;
            if (searchResponse.getResponseCode().getCode() == 200) {
                currentPage++;
                initHeader("查询结果(" + searchResponse.getData().getPage().getCountTotal() + "条)");
                if (currentPage == 1) {
                    if (data != null) {
                        data.clear();
                    }
                }
                if (currentPage == searchResponse.getData().getPage().getPage()) {
                    List<SearchResponse.DataEntity.ListEntity> list = searchResponse.getData().getList();
                    for (SearchResponse.DataEntity.ListEntity listEntity : list) {
                        WareInfo wareInfo = new WareInfo();
                        wareInfo.setData(listEntity);
                        data.add(wareInfo);
                    }
                }
                handler.sendEmptyMessageDelayed(Constants.NOTIFY, 200);
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
