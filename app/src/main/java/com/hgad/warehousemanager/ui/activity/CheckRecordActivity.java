package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.CheckTaskInfo;
import com.hgad.warehousemanager.bean.SectionBean;
import com.hgad.warehousemanager.bean.request.CheckTaskListRequest;
import com.hgad.warehousemanager.bean.response.CheckTaskListResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.adapter.CheckRecordAdapter;
import com.hgad.warehousemanager.util.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.maxwin.view.XListView;

/**
 * Created by Administrator on 2017/7/13.
 */
public class CheckRecordActivity extends BaseActivity {

    private static final int REFRESH = 220;
    private static final String TAG = "test";
    private XListView lv;
    private List<CheckTaskInfo> data = new ArrayList<>();
    private List<SectionBean> newList = new ArrayList<>();
    private CheckRecordAdapter checkRecordAdapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_check_record);
    }

    @Override
    protected void initData() {
        initHeader("盘点");
        if (Constants.DEBUG) {
            for (int i = 0; i < 10; i++) {
                data.add(new CheckTaskInfo(i, "2017-9-13", i + 1 + "号仓库盘点", "0" + (i + 1), "0"));
            }
            checkRecordAdapter.notifyDataSetChanged();
        } else {
            callRefresh();
        }
    }

    @Override
    protected void initView() {
        lv = (XListView) findViewById(R.id.lv_check);
        checkRecordAdapter = new CheckRecordAdapter(data, this);
        TextView tv_empty = (TextView) findViewById(R.id.tv_empty);
        lv.setAdapter(checkRecordAdapter);
        lv.setEmptyView(tv_empty);
        lv.setPullLoadEnable(false);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(listListener);
        lv.setOnItemClickListener(onItemListener);
        TextView tv_confirm = (TextView) findViewById(R.id.btn_confirm);
        tv_confirm.setVisibility(View.INVISIBLE);
        tv_confirm.setText("异常");
        tv_confirm.setOnClickListener(this);
    }

    private AdapterView.OnItemClickListener onItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Intent intent = new Intent(CheckRecordActivity.this, CheckDetailActivity.class);
            Intent intent = new Intent(CheckRecordActivity.this, CheckHouseListActivity.class);
            CheckTaskInfo checkTaskInfo = data.get(position - 1);
            if (!"0".equals(checkTaskInfo.getState())) {
                CommonUtils.showToast(CheckRecordActivity.this, "任务已关闭");
                return;
            }
            intent.putExtra(Constants.CHECK_INFO, ((Serializable) checkTaskInfo));
            startActivityForResult(intent, REFRESH);
        }
    };

    private XListView.IXListViewListener listListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            callRefresh();
            lv.setRefreshTime(CommonUtils.getCurrentTime());
        }

        @Override
        public void onLoadMore() {

        }
    };

    private void callRefresh() {
        CheckTaskListRequest checkTaskListRequest = new CheckTaskListRequest();
        sendRequest(checkTaskListRequest, CheckTaskListResponse.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REFRESH && resultCode == CheckActivity.FOR_REFRESH) {
            callRefresh();
        }
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {
        if (request instanceof CheckTaskListRequest) {
            CheckTaskListResponse checkTaskListResponse = (CheckTaskListResponse) response;
            if (checkTaskListResponse.getResponseCode().getCode() == 200) {
                data.clear();
                List<CheckTaskListResponse.DataEntity> list = checkTaskListResponse.getData();
                for (CheckTaskListResponse.DataEntity dataEntity : list) {
                    CheckTaskInfo checkTaskInfo = new CheckTaskInfo();
                    checkTaskInfo.setData(dataEntity);
                    data.add(checkTaskInfo);
                }
                Collections.sort(data, new Comparator<CheckTaskInfo>() {
                    @Override
                    public int compare(CheckTaskInfo lhs, CheckTaskInfo rhs) {
                        return lhs.getState().compareTo(rhs.getState());
                    }
                });
//                ArrayList<SectionBean> beans = SectionBean.getData(data);
//                newList.addAll(beans);
                checkRecordAdapter.notifyDataSetChanged();
            }
            lv.stopRefresh();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                go2Exception();
                break;
        }
    }

    private void go2Exception() {
        Intent intent = new Intent(this, CheckExceptionActivity.class);
        startActivity(intent);
    }
}
