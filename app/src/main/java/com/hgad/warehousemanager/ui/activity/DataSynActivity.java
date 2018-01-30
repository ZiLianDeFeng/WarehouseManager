package com.hgad.warehousemanager.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.bean.request.InWareRequest;
import com.hgad.warehousemanager.bean.response.InWareResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.db.dao.BaseDaoImpl;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.ProDataAdapter;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */
public class DataSynActivity extends BaseActivity {

    private BaseDaoImpl<WareInfo, Integer> wareDao;
    private List<WareInfo> data = new ArrayList<>();
    private ListView lv;
    private SwipeRefreshLayout swipeRefreshView;
    private ProDataAdapter prodataAdapter;
    private String userName;
    private String model;
    private TextView tv_count;
    private CheckBox cb;
    private TextView tv_choose_count;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_data_syn);
    }

    @Override
    protected void initData() {
        initHeader("数据同步");
        model = Constants.MODEL;
        userName = SPUtils.getString(this, SPConstants.USER_NAME);
        wareDao = new BaseDaoImpl<>(this, WareInfo.class);
        callRefresh();
    }

    @Override
    protected void initView() {
        lv = (ListView) findViewById(R.id.lv_cache);
        prodataAdapter = new ProDataAdapter(data, this);
        lv.setAdapter(prodataAdapter);
        prodataAdapter.setListener(countListener);
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
                callRefresh();
            }
        });
        findViewById(R.id.btn_commit).setOnClickListener(this);
        cb = (CheckBox) findViewById(R.id.cb_all);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (WareInfo wareInfo : data) {
                    wareInfo.setCheck(isChecked);
                }
                prodataAdapter.notifyDataSetChanged();
            }
        });
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_choose_count = (TextView) findViewById(R.id.tv_choose_count);
    }

    private ProDataAdapter.DataChooseListener countListener = new ProDataAdapter.DataChooseListener() {
        @Override
        public void notifyDataChooseCount() {
            List<WareInfo> list = new ArrayList<>();
            for (WareInfo wareInfo : data) {
                if (wareInfo.isCheck()) {
                    list.add(wareInfo);
                }
            }
            tv_choose_count.setText(list.size() + "");
        }
    };

    private void callRefresh() {
        data.clear();
        try {
            List<WareInfo> wareInfos = wareDao.query("haveCommit", false);
            for (WareInfo wareInfo : wareInfos) {
                wareInfo.setCheck(false);
            }
            data.addAll(wareInfos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tv_count.setText(data.size() + "");
        prodataAdapter.notifyDataSetChanged();
        swipeRefreshView.setRefreshing(false);
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (request instanceof InWareRequest) {
            InWareResponse inWareResponse = (InWareResponse) response;
            if (inWareResponse.getResponseCode().getCode() == 200) {
                if (Constants.REQUEST_SUCCESS.equals(inWareResponse.getErrorMsg())) {
                    InWareResponse.DataEntity entity = inWareResponse.getData();
                    try {
                        List<WareInfo> list = wareDao.query("markNum", entity.getIdentification());
                        if (list.size() != 0) {
                            WareInfo wareInfo = list.get(0);
                            wareInfo.setHaveCommit(true);
                            wareDao.update(wareInfo);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    callRefresh();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                final List<WareInfo> list = new ArrayList<>();
                for (WareInfo wareInfo : data) {
                    if (wareInfo.isCheck()) {
                        list.add(wareInfo);
                    }
                }
                if (list.size() != 0) {
                    new AlertView("提示", "数据正在后台进行提交\n可进行其它操作", null, new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {

                        }
                    }).setCancelable(true).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (WareInfo wareInfo : list) {
//                                InWareRequest inWareRequest = new InWareRequest(wareInfo.getMarkNum(), userName, wareInfo.getOrderItem(), wareInfo.getAddress(), "1", wareInfo.getNetWeight(), wareInfo.getSpec(), wareInfo.getProName(), "0", wareInfo.getSteelGrade(), model, null, "0");
//                                sendRequest(inWareRequest, InWareResponse.class);
                            }
                        }
                    }).start();
                } else {
                    CommonUtils.showToast(this, "未选择货品数据！");
                }
                if (cb.isChecked()) {
                    cb.setChecked(false);
                }
                break;
        }
    }
}
